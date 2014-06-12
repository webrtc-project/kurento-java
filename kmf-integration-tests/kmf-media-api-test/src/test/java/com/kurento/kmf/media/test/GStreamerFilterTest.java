/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.kurento.kmf.media.test;

import static com.kurento.kmf.media.test.RtpEndpoint2Test.URL_SMALL;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kurento.kmf.media.GStreamerFilter;
import com.kurento.kmf.media.PlayerEndpoint;
import com.kurento.kmf.media.events.EndOfStreamEvent;
import com.kurento.kmf.media.events.MediaEventListener;
import com.kurento.kmf.media.test.base.MediaPipelineBaseTest;

/**
 * {@link GStreamerFilter} test suite.
 *
 *
 * @author Ivan Gracia (igracia@gsyc.es)
 * @since 3.0.1
 *
 */
public class GStreamerFilterTest extends MediaPipelineBaseTest {

	private GStreamerFilter filter;
	private PlayerEndpoint player;

	@Before
	public void setupMediaElements() {
		player = pipeline.newPlayerEndpoint(URL_SMALL).build();
	}

	@After
	public void teardownMediaElements() {
		player.release();
	}

	@Test
	public void testInstantiation() throws InterruptedException {

		filter = pipeline
				.newGStreamerFilter("videoflip method=horizontal-flip").build();

		Assert.assertNotNull(filter);

		player.connect(filter);

		final BlockingQueue<EndOfStreamEvent> eosEvents = new ArrayBlockingQueue<EndOfStreamEvent>(
				1);
		player.addEndOfStreamListener(new MediaEventListener<EndOfStreamEvent>() {

			@Override
			public void onEvent(EndOfStreamEvent event) {
				eosEvents.add(event);
			}
		});

		player.play();
		Assert.assertNotNull("EndOfStream event not received in 7s",
				eosEvents.poll(7, SECONDS));
		filter.release();
	}

}
