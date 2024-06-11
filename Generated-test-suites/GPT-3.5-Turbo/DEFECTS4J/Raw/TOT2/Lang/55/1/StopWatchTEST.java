import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertEquals(StopWatch.STATE_RUNNING, watch.getRunningState());
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(StopWatch.STATE_STOPPED, watch.getRunningState());
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, watch.getRunningState());
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertEquals(StopWatch.STATE_SPLIT, watch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, watch.getSplitState());
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.resume();
        assertEquals(StopWatch.STATE_RUNNING, watch.getRunningState());
    }

    @Test
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        // Simple test for millisecond difference
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        // Simple test for millisecond difference
        assertTrue(watch.getSplitTime() >= 0);
    }

    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        watch.start();
        // Not assert the exact output, just check if it returns a string
        assertNotNull(watch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        // Not assert the exact output, just check if it returns a string
        assertNotNull(watch.toSplitString());
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}