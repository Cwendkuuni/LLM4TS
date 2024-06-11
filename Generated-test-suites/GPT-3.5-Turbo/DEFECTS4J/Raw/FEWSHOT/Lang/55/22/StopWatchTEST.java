import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.time.DurationFormatUtils;
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
        
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
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
    
    @Test(expected = IllegalStateException.class)
    public void testStartWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.start();
    }
    
    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunningOrSuspended() {
        stopWatch.stop();
    }
    
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
    }
    
    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
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
        
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
    }
    
    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertNotEquals(-1, stopWatch.getStopTime());
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
        
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertNotEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
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
        
        assertNotEquals(0, time);
        String duration = DurationFormatUtils.formatDurationHMS(time);
        assertNotNull(duration);
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetTimeWhenNotStarted() {
        stopWatch.getTime();
    }
    
    @Test
    public void testGetTimeWhenStopped() {
        stopWatch.start();
        stopWatch.stop();
        
        long time = stopWatch.getTime();
        
        assertNotEquals(0, time);
        String duration = DurationFormatUtils.formatDurationHMS(time);
        assertNotNull(duration);
    }
    
    @Test
    public void testGetTimeWhenSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        
        long time = stopWatch.getTime();
        
        assertNotEquals(0, time);
        String duration = DurationFormatUtils.formatDurationHMS(time);
        assertNotNull(duration);
    }
    
    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        
        long splitTime = stopWatch.getSplitTime();
        
        assertNotEquals(0, splitTime);
        String duration = DurationFormatUtils.formatDurationHMS(splitTime);
        assertNotNull(duration);
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
        
        assertNotNull(timeString);
    }
    
    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        
        String splitTimeString = stopWatch.toSplitString();
        
        assertNotNull(splitTimeString);
    }
    
}