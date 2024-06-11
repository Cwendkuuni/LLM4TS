package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
    private StopWatch stopwatch;

    @Before
    public void setup() {
        stopwatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopwatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
        assertTrue(stopwatch.getStartTime() > 0);
        assertEquals(-1, stopwatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenAlreadyStarted() {
        stopwatch.start();
        stopwatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        stopwatch.start();
        stopwatch.stop();
        stopwatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterResetNoStopwatch() {
        stopwatch.start();
        stopwatch.reset();
        stopwatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAfterResetStopped() {
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        stopwatch.start();
    }

    @Test
    public void testStop() {
        stopwatch.start();
        stopwatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
        assertTrue(stopwatch.getStartTime() > 0);
        assertTrue(stopwatch.getStopTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        stopwatch.stop();
    }

    @Test
    public void testResume() {
        stopwatch.start();
        stopwatch.stop();
        stopwatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
        assertTrue(stopwatch.getStartTime() > 0);
        assertEquals(-1, stopwatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopwatch.resume();
    }

    @Test
    public void testSuspend() {
        stopwatch.start();
        stopwatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
        assertTrue(stopwatch.getStartTime() > 0);
        assertTrue(stopwatch.getStopTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        stopwatch.suspend();
    }

    @Test
    public void testUnsplit() {
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
        assertTrue(stopwatch.getStartTime() > 0);
        assertEquals(-1, stopwatch.getStopTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopwatch.unsplit();
    }

    @Test
    public void testReset() {
        stopwatch.start();
        stopwatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.getSplitState());
        assertEquals(-1, stopwatch.getStartTime());
        assertEquals(-1, stopwatch.getStopTime());
    }

    @Test
    public void testGetTime() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.pause();
        Thread.sleep(1000);
        stopwatch.resume();
        Thread.sleep(1000);
        stopwatch.stop();
        assertTrue(stopwatch.getTime() >= 3000 && stopwatch.getTime() < 4000);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeWhenIllegalRunningState() {
        stopwatch.getTime();
    }

    @Test
    public void testGetSplitTime() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.split();
        Thread.sleep(1000);
        stopwatch.unsplit();
        Thread.sleep(1000);
        stopwatch.stop();
        assertTrue(stopwatch.getSplitTime() >= 2000 && stopwatch.getSplitTime() < 3000);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        stopwatch.getSplitTime();
    }

    @Test
    public void testToString() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.pause();
        Thread.sleep(1000);
        stopwatch.resume();
        Thread.sleep(1000);
        stopwatch.stop();
        assertNotNull(stopwatch.toString());
    }

    @Test
    public void testToSplitString() throws InterruptedException {
        stopwatch.start();
        Thread.sleep(1000);
        stopwatch.split();
        Thread.sleep(1000);
        stopwatch.unsplit();
        Thread.sleep(1000);
        stopwatch.stop();
        assertNotNull(stopwatch.toSplitString());
    }
}