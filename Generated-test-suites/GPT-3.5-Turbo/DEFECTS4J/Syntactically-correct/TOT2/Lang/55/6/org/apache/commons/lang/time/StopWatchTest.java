import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 1000000; i++) {
            // Do some dummy operations
        }
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
    }
    
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        for (int i = 0; i < 1000000; i++) {
            // Do some dummy operations
        }
        stopWatch.unsplit();
        assertTrue(stopWatch.getSplitTime() > 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }
    
    @Test(expected = RuntimeException.class)
    public void testIllegalRunningState() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.toString();
    }
}