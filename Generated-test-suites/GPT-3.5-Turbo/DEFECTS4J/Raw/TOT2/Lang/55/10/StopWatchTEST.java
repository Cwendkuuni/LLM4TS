import static org.junit.Assert.*;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertEquals(StopWatch.STATE_RUNNING, watch.runningState);
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(StopWatch.STATE_STOPPED, watch.runningState);
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, watch.runningState);
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(StopWatch.STATE_SPLIT, watch.splitState);
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, watch.splitState);
    }

    @Test
    public void testSuspend() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, watch.runningState);
    }

    @Test
    public void testResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.resume();
        assertEquals(StopWatch.STATE_RUNNING, watch.runningState);
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
        assertTrue(watch.getTime() > 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
            watch.split();
            Thread.sleep(1000);
            watch.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(watch.getSplitTime() > 0);
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
            watch.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(watch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        watch.start();
        
        try {
            Thread.sleep(1000);
            watch.split();
            Thread.sleep(1000);
            watch.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(watch.toSplitString());
    }
}