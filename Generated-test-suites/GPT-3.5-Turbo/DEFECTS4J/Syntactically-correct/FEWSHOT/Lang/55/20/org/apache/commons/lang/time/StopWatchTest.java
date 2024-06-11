package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals("Stopwatch must be running.", StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals("Stopwatch must be stopped.", StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals("Stopwatch must be unstarted.", StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals("Stopwatch must be split.", StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals("Stopwatch must be unsplit.", StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals("Stopwatch must be suspended.", StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals("Stopwatch must be running.", StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() throws InterruptedException {
        stopWatch.start();
        // Wait for 1 second
        Thread.sleep(1000);
        stopWatch.stop();

        long time = stopWatch.getTime();
        assertEquals("Stopwatch must measure 1 second.", 1000, time, 10);
    }

    @Test
    public void testGetTimeWhenUnstarted() {
        long time = stopWatch.getTime();
        assertEquals("Stopwatch must measure 0 milliseconds.", 0, time);
    }

    @Test
    public void testGetTimeWhenSuspended() throws InterruptedException {
        stopWatch.start();
        // Wait for 1 second
        Thread.sleep(1000);
        stopWatch.suspend();
        // Wait for another 1 second
        Thread.sleep(1000);
        stopWatch.resume();
        stopWatch.stop();

        long time = stopWatch.getTime();
        assertEquals("Stopwatch must measure 1 second.", 1000, time, 10);
    }

    @Test
    public void testGetSplitTime() throws InterruptedException {
        stopWatch.start();
        // Wait for 1 second
        Thread.sleep(1000);
        stopWatch.split();

        long splitTime = stopWatch.getSplitTime();
        assertEquals("Stopwatch must measure 1 second for split time.", 1000, splitTime, 10);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }
}