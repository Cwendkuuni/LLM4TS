import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
        Assert.assertEquals(0, stopWatch.getSplitTime());
        Assert.assertEquals("0.0", stopWatch.toString());
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
        long splitTime = stopWatch.getSplitTime();
        Assert.assertNotEquals(-1, splitTime);
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertEquals(splitTime, time);
        Assert.assertEquals("0.0", stopWatch.toString());
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
        long pausedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        stopWatch.resume();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertEquals(time - pausedTime, stopWatch.getSplitTime());
        Assert.assertEquals("0.0", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRun() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testMultipleSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long firstSplitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        stopWatch.split();
        long secondSplitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertEquals(time - firstSplitTime + secondSplitTime, getTotalTime());
        Assert.assertEquals("0.0", stopWatch.toString());
    }

    private long getTotalTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long firstSplitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        stopWatch.split();
        long secondSplitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        stopWatch.stop();
        return stopWatch.getTime();
    }
}