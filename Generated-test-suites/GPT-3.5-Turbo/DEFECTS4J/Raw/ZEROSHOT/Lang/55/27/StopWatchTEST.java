import static org.junit.Assert.*;
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
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        long start = System.currentTimeMillis();
        // Busy wait for a short while to simulate some work being done
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        long end = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(end - start, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        long start = System.currentTimeMillis();
        // Busy wait for a short while to simulate some work being done
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        long mid = System.currentTimeMillis();
        stopWatch.split();
        // Busy wait for a short while to simulate some more work being done
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        long end = System.currentTimeMillis();
        assertEquals(mid - start, stopWatch.getSplitTime());
    }

    @Test
    public void testToString() {
        stopWatch.start();
        // Busy wait for a short while to simulate some work being done
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        stopWatch.stop();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        // Busy wait for a short while to simulate some work being done
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        stopWatch.split();
        // Busy wait for a short while to simulate some more work being done
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        stopWatch.stop();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }
}