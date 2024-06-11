import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for StopWatch class.
 */
public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test runs instantly
        Assert.assertEquals("The time should be greater than or equal to zero", expectedTime, actualTime);
        Assert.assertEquals("toString should return a formatted string representation of time",
                DurationFormatUtils.formatDurationHMS(expectedTime), stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test runs instantly
        Assert.assertEquals("The time should be greater than or equal to zero", expectedTime, actualTime);
        Assert.assertEquals("toString should return a formatted string representation of time",
                DurationFormatUtils.formatDurationHMS(expectedTime), stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test runs instantly
        Assert.assertEquals("The time should be greater than or equal to zero", expectedTime, actualTime);
        Assert.assertEquals("toString should return a formatted string representation of time",
                DurationFormatUtils.formatDurationHMS(expectedTime), stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testMultipleOperations() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.resume();
        stopWatch.suspend();
        stopWatch.unsplit();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test runs instantly
        Assert.assertEquals("The time should be greater than or equal to zero", expectedTime, actualTime);
        Assert.assertEquals("toString should return a formatted string representation of time",
                DurationFormatUtils.formatDurationHMS(expectedTime), stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long actualSplitTime = stopWatch.getSplitTime();
        long expectedSplitTime = 0; // assuming the test runs instantly
        Assert.assertEquals("The split time should be greater than or equal to zero",
                expectedSplitTime, actualSplitTime);
        Assert.assertEquals("toSplitString should return a formatted string representation of split time",
                DurationFormatUtils.formatDurationHMS(expectedSplitTime), stopWatch.toSplitString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        long actualSplitTime = stopWatch.getSplitTime();
    }
}