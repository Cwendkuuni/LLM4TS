import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    // Test start()
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    // Test stop()
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    // Test reset()
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertEquals(-1, stopWatch.getStopTime());
    }

    // Test split()
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    // Test unsplit()
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    // Test suspend()
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    // Test resume()
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    // Test getTime()
    @Test
    public void testGetTime() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertTrue(time >= 100);
    }

    // Test getSplitTime()
    @Test
    public void testGetSplitTime() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.split();
        Thread.sleep(100);
        stopWatch.stop();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertTrue(splitTime >= 100 && splitTime <= 200);
    }

    // Test toString()
    @Test
    public void testToString() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.stop();
        String timeString = stopWatch.toString();
        Assert.assertNotNull(timeString);
    }

    // Test toSplitString()
    @Test
    public void testToSplitString() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.split();
        Thread.sleep(100);
        stopWatch.stop();
        String splitTimeString = stopWatch.toSplitString();
        Assert.assertNotNull(splitTimeString);
    }

}