import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testUnstartedStopWatch() {
        assertNull(stopWatch.getTime());
        assertTrue(stopWatch.isUnstarted());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStartStop() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(time, stopWatch.getTime());
        assertTrue(stopWatch.isStopped());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        stopWatch.stop();
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        long totalTime = stopWatch.getTime();
        assertEquals(splitTime, totalTime);
        assertTrue(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        assertEquals(time, stopWatch.getTime());
        assertFalse(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspendResume() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        long endTime = System.currentTimeMillis();
        long time = stopWatch.getTime();
        assertEquals(time, endTime - startTime - pauseTime);
        assertSame(startTime, stopWatch.getStartTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        stopWatch.split();
        String toString = stopWatch.toString();
        String expected = "0m:0s:0.001ms";
        assertEquals(expected, toString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String toSplitString = stopWatch.toSplitString();
        String expected = "0m:0s:0.001ms";
        assertEquals(expected, toSplitString);
    }
}