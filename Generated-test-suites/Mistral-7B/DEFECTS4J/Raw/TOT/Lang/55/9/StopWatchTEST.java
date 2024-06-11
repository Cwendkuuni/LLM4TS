package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Test cases for {@link StopWatch} class.
 */
public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some processing here
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = TimeUnit.SECONDS.toMillis(1); // replace with actual processing time
        Assert.assertEquals("Time should match expected time.", expectedTime, actualTime);
    }

    @Test
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
        Assert.throwsException(IllegalStateException.class, "Stopwatch must be reset before being restarted.");
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some processing here
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        // some processing here
        stopWatch.unsplit();
        long actualTime = stopWatch.getTime();
        long expectedTime = TimeUnit.SECONDS.toMillis(2); // replace with actual processing time before and after split
        Assert.assertEquals("Total time should match expected time.", expectedTime, actualTime);
        Assert.assertEquals("Split time should match expected split time.", splitTime, actualTime / 2);
    }

    @Test
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some processing here
        stopWatch.split();
        stopWatch.split();
        Assert.throwsException(IllegalStateException.class, "Stopwatch has already been split.");
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some processing here
        stopWatch.suspend();
        // some additional processing here
        stopWatch.resume();
        long actualTime = stopWatch.getTime();
        long expectedTime = TimeUnit.SECONDS.toMillis(3); // replace with actual processing time before suspend and resume
        Assert.assertEquals("Time should match expected time.", expectedTime, actualTime);
    }

    @Test
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some processing here
        stopWatch.suspend();
        stopWatch.suspend();
        Assert.throwsException(IllegalStateException.class, "Stopwatch must be resumed before being suspended again.");
    }
}