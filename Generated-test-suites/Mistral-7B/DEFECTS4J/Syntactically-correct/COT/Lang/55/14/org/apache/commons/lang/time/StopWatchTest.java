package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;

/**
 * Test cases for {@link StopWatch} class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    // Test case for start() method
    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
    }

    // Test case for start() method with previous running state
    @Test(expected = IllegalStateException.class)
    public void testStart_AlreadyRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    // Test case for stop() method
    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(STATE_STOPPED, stopWatch.getRunningState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(System.currentTimeMillis(), stopWatch.getStopTime());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    // Test case for stop() method with previous stopped state
    @Test
    public void testStop_AlreadyStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    // Test case for reset() method
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(STATE_UNSTARTED, stopWatch.getRunningState());
        assertNull(stopWatch.getStartTime());
        assertNull(stopWatch.getStopTime());
    }

    // Test case for split() method
    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(STATE_SPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
    }

    // Test case for split() method with previous split state
    @Test(expected = IllegalStateException.class)
    public void testSplit_AlreadySplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    // Test case for unsplit() method
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(STATE_UNSPLIT, stopWatch.getSplitState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(System.currentTimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
    }

    // Test case for unsplit() method with previous unsplit state
    @Test(expected = IllegalStateException.class)
    public void testUnsplit_NotSplit() {
        stopWatch.start();
        stopWatch.unsplit();
    }

    // Test case for suspend() method
    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(STATE_SUSPENDED, stopWatch.getRunningState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(System.currentTimeMillis(), stopWatch.getSuspendTime());
    }

    // Test case for resume() method
    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(STATE_RUNNING, stopWatch.getRunningState());
        assertEquals(-1, stopWatch.getStartTime());
        assertEquals(-1, stopWatch.getStopTime());
        assertEquals(System.currenttimeMillis() - stopWatch.getStartTime(), stopWatch.getTime());
    }

    // Test case for resume() method with previous running state
    @Test(expected = IllegalStateException.class)
    public void testResume_NotSuspended() {
        stopWatch.start();
        stopWatch.resume();
    }

    // Test case for resume() method with previous stopped state
    @Test(expected = IllegalStateException.class)
    public void testResume_NotStopped() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.resume();
    }

    // Test case for getTime() method with running state
    @Test
    public void testGetTime_Running() {
        stopWatch.start();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    // Test case for getTime() method with stopped state
    @Test
    public void testGetTime_Stopped() {
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    // Test case for getTime() method with split state
    @Test
    public void testGetTime_Split() {
        stopWatch.start();
        stopWatch.split();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    // Test case for getSplitTime() method with split state
    @Test
    public void testGetSplitTime_Split() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 0);
    }

    // Test case for getSplitTime() method with unsplit state
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTime_Unsplit() {
        stopWatch.start();
        stopWatch.getSplitTime();
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        stopWatch.start();
        long time = stopWatch.getTime();
        String result = stopWatch.toString();
        String expected = String.format("%d:%02d:%02d.%03d", time / 1000 / 60, time / 1000 % 60, time % 60, time % 1000);
        assertEquals(expected, result);
    }

    // Test case for toSplitString() method
    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        String result = stopWatch.toSplitString();
        String expected = String.format("%d:%02d:%02d.%03d", splitTime / 1000 / 60, splitTime / 1000 % 60, splitTime % 60, splitTime % 1000);
        assertEquals(expected, result);
    }

    // Test case for IllegalStateException with inappropriate calls
    @Test(expected = IllegalStateException.class)
    public void testIllegalCalls() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    // Test case for IllegalArgumentException with negative split time
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSplitTime() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.setSplitTime(-1);
    }

    // Test case for IllegalArgumentException with negative time
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTime() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.setTime(-1);
    }

    // Test case for ParseException when formatting incorrect duration
    @Test(expected = ParseException.class)
    public void testFormatException() {
        String duration = "99:60:60:60:60";
        DurationFormatUtils.formatDurationHMS(Long.parseLong(duration));
    }

}