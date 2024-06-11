
package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

	private StopWatch stopwatch;
	
	@Before
	public void setUp() {
		stopwatch = new StopWatch();
	}
	
	@Test
	public void testStart() {
		stopwatch.start();
		assertTrue(stopwatch.getTime() > 0);
		assertEquals(StopWatch.STATE_RUNNING, stopwatch.getRunningState());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testStartTwice() {
		stopwatch.start();
		stopwatch.start();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testStopWithoutStart() {
		stopwatch.stop();
	}
	
	@Test
	public void testStop() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.stop();
		assertTrue(stopwatch.getTime() >= 1000);
		assertEquals(StopWatch.STATE_STOPPED, stopwatch.getRunningState());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testStopTwice() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.stop();
		Thread.sleep(1000);
		stopwatch.stop();
	}
	
	@Test
	public void testReset() {
		stopwatch.start();
		stopwatch.stop();
		stopwatch.reset();
		assertEquals(0, stopwatch.getTime());
		assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.getRunningState());
		assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
	}
	
	@Test
	public void testSplit() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.split();
		assertTrue(stopwatch.getSplitTime() >= 1000);
		assertEquals(StopWatch.STATE_SPLIT, stopwatch.getSplitState());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSplitWithoutStart() {
		stopwatch.split();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSplitTwice() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.split();
		Thread.sleep(1000);
		stopwatch.split();
	}
	
	@Test
	public void testUnsplit() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.split();
		stopwatch.unsplit();
		assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
		assertEquals(stopwatch.getTime(), stopwatch.getSplitTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testUnsplitWithoutSplit() {
		stopwatch.unsplit();
	}
	
	@Test
	public void testSuspendAndResume() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		stopwatch.suspend();
		Thread.sleep(1000);
		stopwatch.resume();
		long elapsedTime = stopwatch.getTime();
		assertTrue(elapsedTime >= 1000 && elapsedTime <= 2000);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSuspendWithoutStart() {
		stopwatch.suspend();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testResumeWithoutSuspend() {
		stopwatch.resume();
	}
	
	@Test
	public void testGetTime() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		long elapsed1 = stopwatch.getTime();
		Thread.sleep(1000);
		long elapsed2 = stopwatch.getTime();
		assertTrue(elapsed2 > elapsed1);
	}
	
	@Test
	public void testToString() throws InterruptedException {
		stopwatch.start();
		Thread.sleep(1000);
		String str = stopwatch.toString();
		assertFalse(str.isEmpty());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetSplitTimeWithoutSplit() {
		stopwatch.getSplitTime();
	}

	
}
