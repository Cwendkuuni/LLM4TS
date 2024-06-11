import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {
    
    private StopWatch stopWatch;
    
    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }
    
    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() > 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartWhileRunning() {
        stopWatch.start();
        stopWatch.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }
    
    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        assertTrue(stopWatch.getStopTime() > 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWhileSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.stop();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWhileUnstarted() {
        stopWatch.stop();
    }
    
    @Test
    public void testReset() {
        stopWatch.start();
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
        assertTrue(stopWatch.getStopTime() > stopWatch.getStartTime());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSplitWhileStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.split();
    }
    
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStopTime());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhileUnsplit() {
        stopWatch.start();
        stopWatch.unsplit();
    }
    
    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertTrue(stopWatch.getStopTime() > stopWatch.getStartTime());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSuspendWhileUnstarted() {
        stopWatch.suspend();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSuspendWhileStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.suspend();
    }
    
    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() > 0);
        assertEquals(-1, stopWatch.getStopTime());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testResumeWhileStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.resume();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testResumeWhileNotSuspended() {
        stopWatch.start();
        stopWatch.resume();
    }
    
    @Test
    public void testGetTime() {
        stopWatch.start();
        long time1 = stopWatch.getTime();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time2 = stopWatch.getTime();
        assertTrue(time2 > time1);
    }
    
    @Test
    public void testGetTimeWhileUnstarted() {
        assertEquals(0, stopWatch.getTime());
    }
    
    @Test
    public void testGetTimeWhileStopped() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
    }
    
    @Test(expected = RuntimeException.class)
    public void testIllegalRunningState() {
        stopWatch.getRunningState();
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
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhileNotSplit() {
        stopWatch.start();
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
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
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
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }
    
}