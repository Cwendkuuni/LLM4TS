package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

    @Test
    public void testSimpleUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0);
        System.out.println("Time elapsed: " + stopWatch.toString() + ".");
    }

    @Test
    public void testSplitUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertGreaterThan(splitTime, 0);
        System.out.println("Split time: " + stopWatch.toSplitString() + ".");
        stopWatch.unsplit();
        long totalTime = stopWatch.getTime();
        Assert.assertGreaterThan(totalTime, splitTime);
        System.out.println("Total time: " + stopWatch.toString() + ".");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException_startTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException_splitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(-1, stopWatch.getTime());
        Assert.assertEquals(-1, stopWatch.getSplitTime());
    }
}