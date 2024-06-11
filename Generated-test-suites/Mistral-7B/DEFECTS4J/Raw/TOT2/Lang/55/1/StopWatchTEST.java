import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the StopWatch class.
 */
public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStartSplitStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0);
        long splitTime2 = stopWatch.split();
        Assert.assertEquals(splitTime, splitTime2);
        Assert.assertFalse(stopWatch.isRunning());
        stopWatch.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long suspendTime = System.currentTimeMillis();
        stopWatch.suspend();
        long resumeTime = System.currentTimeMillis();
        stopWatch.resume();
        long time = stopWatch.getTime();
        long expectedTime = System.currentTimeMillis() - suspendTime + resumeTime - stopWatch.getStartTime();
        Assert.assertEquals(expectedTime, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }
}