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
    public void testStartWhenAlreadyRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        stopWatch.start();
        stopWatch.stop();
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
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertEquals(-1, stopWatch.getStopTime());
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

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
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
        Assert.assertTrue(stopWatch.getStopTime() > 0);
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
        Assert.assertTrue(stopWatch.getStartTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }
    
    @Test
    public void testGetTimeWhenStartedAndStopped() {
        stopWatch.start();
        // Wait for some time to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        
        long time = stopWatch.getTime();
        
        Assert.assertTrue(time > 0);
    }
    
    @Test
    public void testGetTimeWhenNotStarted() {
        long time = stopWatch.getTime();
        
        Assert.assertEquals(0, time);
    }
    
    @Test
    public void testGetTimeWhenRunning() {
        stopWatch.start();
        
        long time = stopWatch.getTime();
        
        Assert.assertTrue(time > 0);
    }
    
    @Test
    public void testGetSplitTimeWhenSplit() {
        stopWatch.start();
        // Wait for some time to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        
        long splitTime = stopWatch.getSplitTime();
        
        Assert.assertTrue(splitTime > 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        stopWatch.getSplitTime();
    }
    
    @Test
    public void testToString() {
        stopWatch.start();
        // Wait for some time to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        
        String timeString = stopWatch.toString();
        
        Assert.assertNotNull(timeString);
    }
    
    @Test
    public void testToSplitString() {
        stopWatch.start();
        // Wait for some time to simulate the stopwatch running
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        
        String splitTimeString = stopWatch.toSplitString();
        
        Assert.assertNotNull(splitTimeString);
    }
}