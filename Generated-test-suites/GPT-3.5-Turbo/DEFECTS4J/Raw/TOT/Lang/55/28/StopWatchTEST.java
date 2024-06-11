import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertTrue(time >= 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertTrue(splitTime >= 0);
    }
    
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        Assert.assertNotNull(timeString);
        Assert.assertFalse(timeString.isEmpty());
    }
    
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        Assert.assertNotNull(splitTimeString);
        Assert.assertFalse(splitTimeString.isEmpty());
    }
}