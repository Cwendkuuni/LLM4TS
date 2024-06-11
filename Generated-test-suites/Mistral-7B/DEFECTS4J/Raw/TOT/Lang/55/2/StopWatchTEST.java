package org.apache.commons.lang.time;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the StopWatch class.
 */
public class StopWatchTest {

    @Test
    public void testBasicUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some long running operation here
        stopWatch.stop();
        long timeElapsed = stopWatch.getTime();
        Assert.assertNotNull(timeElapsed);
        Assert.assertTrue(timeElapsed > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some operation here
        long splitTime = stopWatch.split();
        Assert.assertNotNull(splitTime);
        Assert.assertTrue(splitTime > 0);
        long totalTime = stopWatch.getTime();
        Assert.assertTrue(totalTime > splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some operation here
        long suspendTime = System.currentTimeMillis();
        stopWatch.suspend();
        long resumeTime = System.currentTimeMillis();
        stopWatch.resume();
        long totalTime = stopWatch.getTime();
        long suspendedTime = resumeTime - suspendTime;
        Assert.assertTrue(totalTime > suspendedTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRun() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some operation here
        stopWatch.stop();
        stopWatch.reset();
        long timeElapsed = stopWatch.getTime();
        Assert.assertEquals(-1, timeElapsed);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some operation here
        stopWatch.stop();
        String timeString = stopWatch.toString();
        Assert.assertNotNull(timeString);
        // check that the format is correct (hours:minutes:seconds.milliseconds)
        String[] parts = timeString.split(":");
        Assert.assertEquals(3, parts.length);
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2].split("\\.")[0]);
        int milliseconds = Integer.parseInt(parts[2].split("\\.")[1]);
        long totalTime = Long.parseLong(hours * 3600000L + minutes * 60000L + seconds * 1000L + milliseconds);
        long timeElapsed = stopWatch.getTime();
        Assert.assertEquals(totalTime, timeElapsed);
    }
}