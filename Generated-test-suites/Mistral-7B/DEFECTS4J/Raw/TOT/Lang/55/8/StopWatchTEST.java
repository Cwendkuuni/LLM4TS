package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.StopWatch;

import org.junit.Test;

/**
 * Test cases for the StopWatch class.
 */
public class StopWatchTest {

    /**
     * Test start and stop method.
     */
    @Test
    public void testStartAndStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        long time = stopwatch.getTime();
        assertTrue(time > 0);
    }

    /**
     * Test start and split method.
     */
    @Test
    public void testStartAndSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long time = stopwatch.getTime();
        long splitTime = stopwatch.getSplitTime();
        assertTrue(time > 0);
        assertTrue(splitTime > 0);
        assertEquals(time, splitTime + stopwatch.getTime());
    }

    /**
     * Test split and unsplit method.
     */
    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        long time = stopwatch.getTime();
        assertTrue(time > 0);
    }

    /**
     * Test start, suspend, resume, and stop method.
     */
    @Test
    public void testStartSuspendResumeAndStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        Thread.sleep(100);
        stopwatch.resume();
        stopwatch.stop();
        long time = stopwatch.getTime();
        long suspendedTime = System.currentTimeMillis() - stopwatch.getStartTime();
        assertTrue(time > 0);
        assertTrue(suspendedTime > 0);
        assertEquals(time, stopwatch.getTime() - suspendedTime);
    }

    /**
     * Test illegal state exceptions.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptions() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.stop();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.unsplit();
        stopwatch.resume();
        stopwatch.resume();
        stopwatch.reset();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.stop();
    }
}