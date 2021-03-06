/*
 * Copyright (c) 2011-2016 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.core.publisher;

import org.junit.Test;
import reactor.test.subscriber.AssertSubscriber;

public class FluxMergeTest {

	@Test
	public void normal() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		Flux.merge(Flux.just(1),
				Flux.range(2, 2),
				Flux.just(4, 5, 6)
				    .hide())
		    .subscribe(ts);

		ts.assertValues(1, 2, 3, 4, 5, 6)
		  .assertNoError()
		  .assertComplete();
	}

	@Test
	public void mergeWithNoStackoverflow() {
		int n = 5000;

		Flux<Integer> source = Flux.just(1);

		Flux<Integer> result = source;
		for (int i = 0; i < n; i++) {
			result = result.mergeWith(source);
		}

		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		result.subscribe(ts);

		ts.assertValueCount(n + 1)
		  .assertNoError()
		  .assertComplete();
	}
}
