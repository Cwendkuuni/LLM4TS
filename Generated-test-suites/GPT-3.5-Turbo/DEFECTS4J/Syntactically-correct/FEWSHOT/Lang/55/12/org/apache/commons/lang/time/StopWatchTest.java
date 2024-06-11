import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class StopWatchTest {
    
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        assertNotNull(stopWatch.startTime);
        assertNull(stopWatch.stopTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartWhenRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }
    
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
        assertNotNull(stopWatch.stopTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }
    
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertNull(stopWatch.startTime);
        assertNull(stopWatch.stopTime);
    }
    
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
        assertNotNull(stopWatch.stopTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }
    
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertNull(stopWatch.stopTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }
    
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
        assertNotNull(stopWatch.stopTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }
    
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertNotNull(stopWatch.startTime);
        assertNull(stopWatch.stopTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }
    
    @Test
    public void testGetTimeWhenStopped() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(stopWatch.stopTime - stopWatch.startTime, time);
    }
    
    @Test
    public void testGetTimeWhenSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(stopWatch.stopTime - stopWatch.startTime, time);
    }
    
    @Test
    public void testGetTimeWhenUnstarted() {
        StopWatch stopWatch = new StopWatch();
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(0, time);
    }
    
    @Test
    public void testGetTimeWhenRunning() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        long time = stopWatch.getTime();
        assertNotNull(time);
        assertEquals(System.currentTimeMillis() - stopWatch.startTime, time, 50);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }
    
    @Test
    public void testGetSplitTimeWhenSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertNotNull(splitTime);
        assertEquals(stopWatch.stopTime - stopWatch.startTime, splitTime);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testToStringWhenNotStopped() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.toString();
    }
    
    @Test
    public void testToStringWhenStopped() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        // TODO - verify format of time string
    }
    
    @Test(expected = IllegalStateException.class)
    public void testToSplitStringWhenNotSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.toSplitString();
    }
    
    @Test
    public void testToSplitStringWhenSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        // TODO - verify format of split time string
    }
}