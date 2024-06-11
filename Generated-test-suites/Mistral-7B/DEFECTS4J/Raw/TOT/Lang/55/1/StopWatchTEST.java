package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some task here, e.g. long computation
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = (long) (5 * TimeUnit.SECONDS.toMillis(1)); // replace with the actual time taken
        Assert.assertEquals("Incorrect time taken for task", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
        Assert.fail("Should have thrown an IllegalStateException for starting the stopwatch twice");
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some task here, e.g. long computation
        long splitTime1 = stopWatch.split();
        stopWatch.stop();
        long actualTime1 = stopWatch.getTime();
        long expectedTime1 = splitTime1;
        Assert.assertEquals("Incorrect time taken for the first split", expectedTime1, actualTime1);

        stopWatch = new StopWatch();
        stopWatch.start();
        // perform some task here, e.g. long computation
        stopWatch.split();
        stopWatch.split();
        long splitTime2 = stopWatch.split();
        stopWatch.stop();
        long actualTime2 = stopWatch.getTime();
        long expectedTime2 = actualTime1 + (2 * splitTime2);
        Assert.assertEquals("Incorrect total time taken for the two splits", expectedTime2, actualTime2);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        Assert.fail("Should have thrown an IllegalStateException for splitting the stopwatch before starting it");
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
        Assert.fail("Should have thrown an IllegalStateException for unspliting the stopwatch before splitting it");
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some task here, e.g. long computation
        long suspendTime = System.currentTimeMillis();
        stopWatch.suspend();
        long resumeTime = System.currentTimeMillis();
        stopWatch.resume();
        long actualTime = stopWatch.getTime();
        long expectedTime = stopWatch.getTime() - (suspendTime - resumeTime);
        Assert.assertEquals("Incorrect time taken for the suspended part of the stopwatch", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
        Assert.fail("Should have thrown an IllegalStateException for suspending the stopwatch before starting it");
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
        Assert.fail("Should have thrown an IllegalStateException for resuming the stopwatch before suspending it");
    }
}