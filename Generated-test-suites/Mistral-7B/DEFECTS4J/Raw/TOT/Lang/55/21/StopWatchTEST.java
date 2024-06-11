import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testBasicUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some operations here
        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        Assert.assertNotEquals(-1, totalTime);
        Assert.assertTrue(totalTime > 0);
        System.out.println("Total time: " + DurationFormatUtils.formatDurationHMS(totalTime));
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotStartTwiceWithoutReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.unsplit();
        long totalTime = stopWatch.getTime();
        Assert.assertEquals(splitTime - startTime, stopWatch.getSplitTime());
        Assert.assertEquals(totalTime, (System.currentTimeMillis() - startTime));
        System.out.println("Total time: " + DurationFormatUtils.formatDurationHMS(totalTime));
        System.out.println("Split time: " + DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()));
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        long timeBetweenSuspendAndResume = suspendTime - startTime;
        Assert.assertEquals(totalTime - timeBetweenSuspendAndResume, (System.currentTimeMillis() - startTime));
        System.out.println("Total time: " + DurationFormatUtils.formatDurationHMS(totalTime));
        System.out.println("Time between suspend and resume: " + DurationFormatUtils.formatDurationHMS(timeBetweenSuspendAndResume));
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }
}