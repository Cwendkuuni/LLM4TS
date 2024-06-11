package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;

/**
 * Test class for StopWatch class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @SuppressWarnings("static-access")
    @Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    // Test for start() method
    @Test
    public void testStart() {
        stopWatch.start();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertEquals(-1, stopWatch.getTime());
    }

    // Test for stop() method
    @Test
    public void testStop() {
        stopWatch.start();
        long initialTime = stopWatch.getTime();
        stopWatch.stop();

        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertNotEquals(-1, stopWatch.getTime());
        assertEquals(initialTime, stopWatch.getTime());
    }

    // Test for reset() method
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();

        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStarted());
        assertEquals(-1, stopWatch.getTime());
    }

    // Test for split() method
    @Test
    public void testSplit() {
        stopWatch.start();
        long initialTime = stopWatch.getTime();
        stopWatch.split();

        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertFalse(stopWatch.isSplit());
        assertEquals(initialTime, stopWatch.getSplitTime());
        assertEquals(-1, stopWatch.getTime());
    }

    // Test for unsplit() method
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        long initialTime = stopWatch.getTime();
        stopWatch.unsplit();

        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertFalse(stopWatch.isSplit());
        assertEquals(initialTime, stopWatch.getTime());
    }

    // Test for suspend() method
    @Test
    public void testSuspend() {
        stopWatch.start();
        long initialTime = stopWatch.getTime();
        stopWatch.suspend();

        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());
        assertEquals(initialTime, stopWatch.getTime());
    }

    // Test for resume() method
    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        long initialTime = stopWatch.getTime();
        stopWatch.resume();

        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertEquals(initialTime, stopWatch.getTime());
    }

    // Test for getTime() method
    @Test
    public void testGetTime() {
        stopWatch.start();
        long initialTime = stopWatch.getTime();
        stopWatch.stop();

        assertEquals(initialTime, stopWatch.getTime());
    }

    // Test for getSplitTime() method
    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long initialSplitTime = stopWatch.getSplitTime();
        stopWatch.stop();

        assertEquals(initialSplitTime, stopWatch.getSplitTime());
    }

    // Test for toString() method
    @Test
    public void testToString() {
        stopWatch.start();
        stopWatch.split();
        String initialToString = stopWatch.toString();
        stopWatch.stop();

        assertEquals(initialToString, stopWatch.toString());
    }

    // Test for toSplitString() method
    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String initialToSplitString = stopWatch.toSplitString();
        stopWatch.stop();

        assertEquals(initialToSplitString, stopWatch.toSplitString());
    }

    // Test for IllegalStateException when calling stop() before start()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenStoppingBeforeStarting() {
        stopWatch.stop();
    }

    // Test for IllegalStateException when calling split() before start()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenSplittingBeforeStarting() {
        stopWatch.split();
    }

    // Test for IllegalStateException when calling stop() after stop()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenStoppingAfterStopping() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    // Test for IllegalStateException when calling split() after split()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenSplittingAfterSplitting() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    // Test for IllegalStateException when calling split() after stop()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenSplittingAfterStopping() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.split();
    }

    // Test for IllegalStateException when calling resume() before suspend()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenResumingBeforeSuspending() {
        stopWatch.start();
        stopWatch.resume();
        stopWatch.suspend();
    }

    // Test for IllegalStateException when calling resume() after unsplit()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenResumingAfterUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.resume();
    }

    // Test for IllegalStateException when calling unsplit() before split()
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenUnsplittingBeforeSplitting() {
        stopWatch.start();
        stopWatch.unsplit();
        stopWatch.split();
    }

    // Test for IllegalArgumentException when parsing invalid duration string
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenParsingInvalidDurationString() {
        String invalidDuration = "x:y:z";
        try {
            DurationFormatUtils.parseDuration(invalidDuration);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid duration format: " + invalidDuration);
        }
    }

    // Test for IllegalArgumentException when parsing duration with negative value
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenParsingDurationWithNegativeValue() {
        String duration = "-1:2:3";
        try {
            DurationFormatUtils.parseDuration(duration);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid duration value: " + duration);
        }
    }
}