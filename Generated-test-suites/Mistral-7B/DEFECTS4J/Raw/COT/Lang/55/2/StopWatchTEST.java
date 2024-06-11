package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * Test class for {@link StopWatch}.
 */
public class StopWatchTest {

    /**
     * Test for {@link StopWatch#start()}, {@link StopWatch#stop()}, and {@link StopWatch#getTime()}.
     */
    @org.junit.Test
    public void testStartStopGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(0, time);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        time = stopWatch.getTime();
        assertFalse(time < 0);

        StopWatch stopWatch2 = new StopWatch();
        stopWatch2.start();
        TimeUnit.SECONDS.sleep(1);
        stopWatch2.stop();
        time = stopWatch2.getTime();
        assertTrue(time > 0);
    }

    /**
     * Test for {@link StopWatch#split()} and {@link StopWatch#unsplit()}, and {@link StopWatch#getSplitTime()}.
     */
    @org.junit.Test
    public void testSplitUnsplitGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals(0, splitTime);

        stopWatch.stop();
        splitTime = stopWatch.getSplitTime();
        assertFalse(splitTime < 0);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        splitTime = stopWatch.getSplitTime();
        assertFalse(splitTime < 0);
    }

    /**
     * Test for {@link StopWatch#start()} when the StopWatch is already running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test for {@link StopWatch#split()} when the StopWatch is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    /**
     * Test for {@link StopWatch#unsplit()} when the StopWatch has not been split.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    /**
     * Test for {@link StopWatch#resume()} when the StopWatch is not suspended.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    /**
     * Test for {@link StopWatch#suspend()} and {@link StopWatch#resume()} when the StopWatch is running.
     */
    @org.junit.Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long startTime = System.currentTimeMillis();
        stopWatch.resume();
        long time = stopWatch.getTime();
        long expectedTime = System.currentTimeMillis() - startTime;
        assertEquals(expectedTime, time);
    }

    /**
     * Test for {@link StopWatch#suspend()} and {@link StopWatch#resume()} multiple times.
     */
    @org.junit.Test
    public void testSuspendAndResumeMultipleTimes() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long startTime = System.currentTimeMillis();
        stopWatch.resume();
        stopWatch.suspend();
        stopWatch.resume();
        long time = stopWatch.getTime();
        long expectedTime = 2 * (System.currentTimeMillis() - startTime);
        assertEquals(expectedTime, time);
    }

    /**
     * Test for {@link StopWatch#stop()} when the StopWatch is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

}