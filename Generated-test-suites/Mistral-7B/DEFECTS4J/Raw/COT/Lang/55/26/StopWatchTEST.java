package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.format.DurationFormatUtils;

public class StopWatchTest {

	StopWatch stopWatch;

	@Before
	public void setUp() {
		stopWatch = new StopWatch();
	}

	// Test for start() method
	@Test
	public void testStart() {
		stopWatch.start();
		assertTrue(stopWatch.isRunning());
		assertFalse(stopWatch.isSplit());
		assertFalse(stopWatch.isSuspended());
	}

	// Test for stop() method
	@Test
	public void testStop() {
		stopWatch.start();
		stopWatch.stop();
		assertEquals(0, stopWatch.getTime(), 0L);
		assertTrue(stopWatch.isStopped());
		assertFalse(stopWatch.isRunning());
		assertFalse(stopWatch.isSuspended());
	}

	// Test for reset() method
	@Test
	public void testReset() {
		stopWatch.start();
		stopWatch.stop();
		stopWatch.reset();
		assertEquals(0, stopWatch.getTime(), 0L);
		assertEquals(0, stopWatch.getSplitTime(), 0L);
		assertFalse(stopWatch.isRunning());
		assertFalse(stopWatch.isStopped());
		assertFalse(stopWatch.isSuspended());
	}

	// Test for split() method
	@Test
	public void testSplit() {
		stopWatch.start();
		stopWatch.split();
		assertTrue(stopWatch.isRunning());
		assertTrue(stopWatch.isSplit());
		assertFalse(stopWatch.isStopped());
		assertFalse(stopWatch.isSuspended());
		assertEquals(0, stopWatch.getTime(), 0L);
		assertNotNull(stopWatch.getSplitTime());
	}

	// Test for unsplit() method
	@Test
	public void testUnsplit() {
		stopWatch.start();
		stopWatch.split();
		stopWatch.unsplit();
		assertTrue(stopWatch.isRunning());
		assertFalse(stopWatch.isSplit());
		assertFalse(stopWatch.isStopped());
		assertFalse(stopWatch.isSuspended());
		assertEquals(0, stopWatch.getTime(), 0L);
		assertNull(stopWatch.getSplitTime());
	}

	// Test for suspend() method
	@Test
	public void testSuspend() {
		stopWatch.start();
		stopWatch.suspend();
		assertTrue(stopWatch.isRunning());
		assertTrue(stopWatch.isSuspended());
		assertEquals(0, stopWatch.getTime(), 0L);
	}

	// Test for resume() method
	@Test
	public void testResume() {
		stopWatch.start();
		stopWatch.suspend();
		stopWatch.resume();
		assertTrue(stopWatch.isRunning());
		assertFalse(stopWatch.isSuspended());
		long startTime = System.currentTimeMillis();
		stopWatch.stop();
		long timeElapsed = stopWatch.getTime();
		stopWatch.reset();
		stopWatch.start();
		stopWatch.suspend();
		Date resumeTime = new Date(System.currentTimeMillis());
		stopWatch.resume();
		long timeSinceResume = System.currentTimeMillis() - resumeTime.getTime();
		assertEquals(timeElapsed + timeSinceResume, stopWatch.getTime(), 0L);
	}

	// Test for getTime() method
	@Test
	public void testGetTime() {
		stopWatch.start();
		long time = System.currentTimeMillis();
		stopWatch.stop();
		assertEquals(time - stopWatch.getStartTime(), stopWatch.getTime(), 0L);
	}

	// Test for getSplitTime() method
	@Test
	public void testGetSplitTime() {
		stopWatch.start();
		stopWatch.split();
		long time = System.currentTimeMillis();
		stopWatch.stop();
		assertEquals(time - stopWatch.getStartTime(), stopWatch.getSplitTime(), 0L);
	}

	// Test for toString() method
	@Test
	public void testToString() {
		stopWatch.start();
		String startTimeString = stopWatch.toString();
		stopWatch.split();
		String splitTimeString = stopWatch.toString();
		stopWatch.stop();
		String stopTimeString = stopWatch.toString();
		assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopTimeString);
		assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), splitTimeString);
		assertEquals(DurationFormatUtils.formatDurationHMS(0), startTimeString);
	}

	// Test for toSplitString() method
	@Test
	public void testToSplitString() {
		stopWatch.start();
		stopWatch.split();
		String splitTimeString = stopWatch.toSplitString();
		stopWatch.stop();
		String stopTimeString = stopWatch.toString();
		assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), splitTimeString);
		assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopTimeString);
	}

	// Test for IllegalStateException when calling illegal methods
	@Test
	public void testIllegalStateExceptions() {
		stopWatch.start();
		assertThrows(IllegalStateException.class, () -> stopWatch.stop());
		stopWatch.split();
		assertThrows(IllegalStateException.class, () -> stopWatch.unsplit());
		assertThrows(IllegalStateException.class, () -> stopWatch.suspend());
		assertThrows(IllegalStateException.class, () -> stopWatch.resume());
		stopWatch.stop();
		assertThrows(IllegalStateException.class, () -> stopWatch.start());
		stopWatch.split();
		assertThrows(IllegalStateException.class, () -> stopWatch.split());
		stopWatch.unsplit();
		assertThrows(IllegalStateException.class, () -> stopWatch.unsplit());
		stopWatch.suspend();
		assertThrows(IllegalStateException.class, () -> stopWatch.suspend());
		stopWatch.resume();
		assertThrows(IllegalStateException.class, () -> stopWatch.resume());
	}
}