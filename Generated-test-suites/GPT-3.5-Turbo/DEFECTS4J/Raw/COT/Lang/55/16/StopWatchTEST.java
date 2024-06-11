import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testReset() {
        stopWatch.reset();
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTimeWhenRunning() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long elapsedTime = stopWatch.getTime();
        Assert.assertTrue(elapsedTime >= 1000);
    }

    @Test
    public void testGetTimeWhenStopped() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long elapsedTime = stopWatch.getTime();
        Assert.assertTrue(elapsedTime >= 1000);
    }

    @Test
    public void testGetTimeWhenSuspended() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long elapsedTime = stopWatch.getTime();
        Assert.assertTrue(elapsedTime >= 1000);
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

    @Test
    public void testToString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String timeString = stopWatch.toString();
        Assert.assertNotNull(timeString);
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
        Assert.assertNotNull(splitTimeString);
    }
}