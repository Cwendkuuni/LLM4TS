package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.StopWatch;

import java.text.ParseException;

/**
 * Tests for {@link StopWatch} class.
 *
 * @author JohnDo
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    // Test for start() method
    @Test
    public void testStartMethod() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isUnsplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethod_AlreadyRunning() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testResetMethod() {
        stopWatch.start();
        stopWatch.reset();
        assertEquals(-1, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStarted());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isUnsplit());
    }

    // Test for split() method
    @Test
    public void testSplitMethod() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertEquals(-1, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitMethod_NotRunning() {
        stopWatch.reset();
        stopWatch.split();
    }

    @Test
    public void testUnsplitMethod() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isUnsplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitMethod_NotSplit() {
        stopWatch.reset();
        stopWatch.unsplit();
    }

    // Test for stop() method
    @Test
    public void testStopMethod() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isStarted());
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isUnsplit());
        long time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    // Test for resume() method
    @Test
    public void testResumeMethod() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isStarted());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isUnsplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeMethod_NotSuspended() {
        stopWatch.start();
        stopWatch.resume();
    }

    @Test
    public void testGetTimeMethod() {
        stopWatch.start();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
        stopWatch.stop();
        time = stopWatch.getTime();
        assertTrue(time > 0);
    }

    @Test
    public void testGetSplitTimeMethod() {
        stopWatch.start();
        stopWatch.split();
        long time = stopWatch.getSplitTime();
        assertTrue(time > 0);
        stopWatch.unsplit();
        time = stopWatch.getSplitTime();
        assertEquals(-1, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeMethod_NotSplit() {
        stopWatch.start();
        stopWatch
            .getSplitTime();
    }

    // Test for toString() method
    @Test
    public void testToStringMethod() {
        stopWatch.start();
        String time = stopWatch.toString();
        assertTrue(time.matches("^\\d{2}:\\d{2}:\\d{2}\\.[0-9]{3}$"));
        stopWatch.stop();
        time = stopWatch.toString();
        assertTrue(time.matches("^\\d{2}:\\d{2}:\\d{2}\\.[0-9]{3}$"));
    }

    // Test for toSplitString() method
    @Test
    public void testToSplitStringMethod() {
        stopWatch.start();
        stopWatch.split();
        String time = stopWatch.toSplitString();
        assertTrue(time.matches("^\\d{2}:\\d{2}:\\d{2}\\.[0-9]{3}$"));
        stopWatch.unsplit();
        time = stopWatch.toSplitString();
        assertEquals("", time);
    }

    @After
    public void tearDown() {
        stopWatch = null;
    }
}