import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {
    // Test start() method
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Assert.assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    // Test start() with already running stopwatch
    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    // Test stop() method
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        Assert.assertEquals(STATE_STOPPED, stopWatch.getRunningState());
    }

    // Test stop() with not running stopwatch
    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    // Test reset() method
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(STATE_UNSTARTED, stopWatch.getRunningState());
    }

    // Test split() method
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Assert.assertEquals(STATE_SPLIT, stopWatch.getSplitState());
    }

    // Test split() with not running stopwatch
    @Test(expected = IllegalStateException.class)
    public void testSplitNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    // Test unsplit() method
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(STATE_UNSPLIT, stopWatch.getSplitState());
    }

    // Test unsplit() with not split stopwatch
    @Test(expected = IllegalStateException.class)
    public void testUnsplitNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    // Test suspend() method
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(STATE_SUSPENDED, stopWatch.getRunningState());
    }

    // Test suspend() with not running stopwatch
    @Test(expected = IllegalStateException.class)
    public void testSuspendNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    // Test resume() method
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.assertEquals(STATE_RUNNING, stopWatch.getRunningState());
    }

    // Test resume() with not suspended stopwatch
    @Test(expected = IllegalStateException.class)
    public void testResumeNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    // Test getTime() method
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        Assert.assertTrue(time >= 0);
    }

    // Test getSplitTime() method
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long time = stopWatch.getSplitTime();
        Assert.assertTrue(time >= 0);
    }

    // Test toString() method
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String result = stopWatch.toString();
        Assert.assertNotNull(result);
    }

    // Test toSplitString() method
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String result = stopWatch.toSplitString();
        Assert.assertNotNull(result);
    }

    // 

    @Test
    public void testStartStopStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
        long time = stopWatch.getTime();
        Assert.assertTrue(time > 0);
    }

    @Test
    public void testSplitUnsplitSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.split();
        long split1 = stopWatch.getSplitTime();
        long split2 = stopWatch.getSplitTime();
        Assert.assertTrue(split1 > 0 && split2 > 0 && split1 != split2);
    }

    @Test
    public void testSuspendResumeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.suspend();
        long time = stopWatch.getTime();
        Assert.assertTrue(time > 0);
    }

    // 
}