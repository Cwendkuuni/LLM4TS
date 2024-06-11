import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.runningState);
    }

    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopwatch.runningState);
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopwatch.runningState);
    }

    @Test
    public void testSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopwatch.splitState);
    }

    @Test
    public void testUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopwatch.splitState);
    }

    @Test
    public void testSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopwatch.runningState);
    }

    @Test
    public void testResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopwatch.runningState);
    }

    @Test
    public void testGetTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        long expectedTime = 1000; // Time in milliseconds
        assertEquals(expectedTime, stopwatch.getTime(), 100); // Delta of 100 ms for uncertainty
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long expectedSplitTime = 500; // Time in milliseconds
        assertEquals(expectedSplitTime, stopwatch.getSplitTime(), 100); // Delta of 100 ms for uncertainty
    }

    @Test
    public void testToString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        String expectedString = "00:00:01.000"; // Expected time format
        assertEquals(expectedString, stopwatch.toString());
    }

    @Test
    public void testToSplitString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        String expectedSplitString = "00:00:00.500"; // Expected split time format
        assertEquals(expectedSplitString, stopwatch.toSplitString());
    }

}