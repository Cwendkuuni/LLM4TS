package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

    private StopWatch stopWatch = new StopWatch();

    /**
     * Test start and stop method.
     */
    @Test
    public void testStartAndStop() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0);
    }

    /**
     * Test split and unsplit method.
     */
    @Test
    public void testSplitAndUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0);
    }

    /**
     * Test suspend and resume method.
     */
    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0);
    }

    /**
     * Test getTime method when not stopped.
     */
    @Test(expected = RuntimeException.class)
    public void testGetTimeBeforeStop() {
        stopWatch.start();
        long time = stopWatch.getTime();
    }

    /**
     * Test getSplitTime method when not split.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        stopWatch.start();
        long time = stopWatch.getSplitTime();
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        long time = stopWatch.getTime();
        Assert.assertEquals(0, time);
    }

    /**
     * Test illegal state exceptions.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalStates() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
        stopWatch.stop();
        stopWatch.stop();
        stopWatch.resume();
        stopWatch.resume();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test getTime method when suspended.
     */
    @Test
    public void testGetTimeWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        long timeBeforeResume = stopWatch.getTime();
        stopWatch.resume();
        long timeAfterResume = stopWatch.getTime();
        long expectedTime = timeBeforeResume + (System.currentTimeMillis() - stopWatch.getStartTime());
        Assert.assertEquals(expectedTime, timeAfterResume);
    }

    /**
     * Test getSplitTime method when suspended.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.split();
    }
}