
import org.apache.commons.lang.time.DurationFormatUtils;
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
    public void testStartTwiceWithoutReset() {
        stopWatch.start();
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
    public void testStopWithoutStart() {
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
        assertTrue(stopWatch.getStopTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
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
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        assertTrue(stopWatch.getStopTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        assertTrue(stopWatch.getStartTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        // Assuming 100 ms time lapse
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        // Assuming 500 ms time lapse
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        String stringTime = stopWatch.toString();
        assertNotNull(stringTime);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        // Assuming 1000 ms time lapse
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.unsplit();
        String stringSplitTime = stopWatch.toSplitString();
        assertNotNull(stringSplitTime);
    }
}
