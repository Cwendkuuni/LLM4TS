
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void testStartWhenRunning() {
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
    public void testGetTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertTrue(time >= 1000);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertTrue(splitTime >= 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        String timeString = stopWatch.toString();
        Assert.assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        Assert.assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }

}
