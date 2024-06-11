import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.Duration;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenAlreadyStarted() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(1000); // Wait for 1 second
        stopWatch.stop();
        long expected = Duration.ofSeconds(1).toMillis();
        Assert.assertEquals(expected, stopWatch.getTime());
    }

    @Test
    public void testGetTimeWhenNotRunning() {
        long expected = 0;
        Assert.assertEquals(expected, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long expected = stopWatch.getSplitTime();
        Assert.assertEquals(expected, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(1000); // Wait for 1 second
        stopWatch.stop();
        String expected = "0:00:01.000";
        Assert.assertEquals(expected, stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String expected = "0:00:00.000";
        Assert.assertEquals(expected, stopWatch.toSplitString());
    }

}