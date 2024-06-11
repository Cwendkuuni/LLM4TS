package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

/**
 * Test class for the StopWatch class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Test
    public void testStartStop() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
        Assert.assertEquals(DurationFormatUtils.parseDuration("0s"), DurationFormatUtils.formatDuration(time));
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitUnsplit() {
        stopWatch = new StopWatch();
        stopWatch.start();
        long start = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertEquals(DurationFormatUtils.formatDuration(splitTime - start), stopWatch.toSplitString());
        Assert.assertEquals(DurationFormatUtils.parseDuration(DurationFormatUtils.formatDuration(time)), DurationFormatUtils.formatDuration(System.currentTimeMillis() - start));
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendResume() {
        stopWatch = new StopWatch();
        stopWatch.start();
        long start = System.currentTimeMillis();
        stopWatch.suspend();
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        long time = stopWatch.getTime();
        Assert.assertEquals(DurationFormatUtils.formatDuration(suspendTime - start), stopWatch.toSplitString());
        Assert.assertEquals(DurationFormatUtils.parseDuration(DurationFormatUtils.formatDuration(time)), DurationFormatUtils.formatDuration(System.currentTimeMillis() - start));
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(DurationFormatUtils.formatDuration("0s"), stopWatch.toString());
    }
}