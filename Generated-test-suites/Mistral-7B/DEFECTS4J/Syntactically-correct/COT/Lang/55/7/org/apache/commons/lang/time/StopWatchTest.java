package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Test class for StopWatch class.
 *
 * @author John Doe
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
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
    }

    // Test case for stop() method
    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
    }

    // Test case for reset() method
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
    }

    // Test case for split() method
    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
    }

    // Test case for unsplit() method
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
    }

    // Test case for suspend() method
    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
    }

    // Test case for resume() method
    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
    }

    // Test case for getTime() method
    @Test
    public void testGetTime() {
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.stop();
        assertTrue(time > 0);
    }

    // Test case for getSplitTime() method
    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.stop();
        assertTrue(splitTime > 0);
    }

    // Test case for isRunning() method
    @Test
    public void testIsRunning() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
    }

    // Test case for isStopped() method
    @Test
    public void testIsStopped() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
    }

    // Test case for isSuspended() method
    @Test
    public void testIsSuspended() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        stopWatch.resume();
        assertFalse(stopWatch.isSuspended());
    }

    // Test case for isSplit() method
    @Test
    public void testIsSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplit());
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplit());
    }

    // Test case for toString() method
    @Test
    public void testToString() throws ParseException {
        stopWatch.start();
        stopWatch.split();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String expected = "00:00:01.000";
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    // Test case for toSplitString() method
    @Test
    public void testToSplitString() throws ParseException {
        stopWatch.start();
        stopWatch.split();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String expected = "00:00:01.000";
        String actual = stopWatch.toSplitString();
        assertEquals(expected, actual);
    }

    // Test case for illegal state exceptions
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptions() {
        stopWatch.start();
        stopWatch.start();
    }

    // Test case for illegal state exceptions - split
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionsSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    // Test case for illegal state exceptions - suspend
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionsSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    // Test case for illegal state exceptions - unsplit
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionsUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
    }

    // Test case for illegal state exceptions - resume
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionsResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.resume();
    }

    // Test case for illegal state exceptions - stop after start
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionsStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }
}