import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertEquals(StopWatch.STATE_RUNNING, watch.getRunningState());
        assertEquals(-1, watch.getStopTime());
        assertTrue(watch.getStartTime() > 0);
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(StopWatch.STATE_STOPPED, watch.getRunningState());
        assertTrue(watch.getStopTime() > 0);
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, watch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, watch.getSplitState());
        assertEquals(-1, watch.getStartTime());
        assertEquals(-1, watch.getStopTime());
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(StopWatch.STATE_SPLIT, watch.getSplitState());
        assertTrue(watch.getStopTime() > 0);
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, watch.getSplitState());
        assertEquals(-1, watch.getStopTime());
    }

    @Test
    public void testSuspend() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, watch.getRunningState());
        assertTrue(watch.getStopTime() > 0);
    }

    @Test
    public void testResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.resume();
        assertEquals(StopWatch.STATE_RUNNING, watch.getRunningState());
        assertEquals(-1, watch.getStopTime());
    }

    @Test
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        assertTrue(watch.getTime() >= 1000);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        watch.split();
        assertTrue(watch.getSplitTime() >= 1000);
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        String timeString = watch.toString();
        assertNotNull(timeString);
        assertTrue(timeString.contains("00:00:01."));
    }

    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        watch.split();
        String splitTimeString = watch.toSplitString();
        assertNotNull(splitTimeString);
        assertTrue(splitTimeString.contains("00:00:01."));
    }
}