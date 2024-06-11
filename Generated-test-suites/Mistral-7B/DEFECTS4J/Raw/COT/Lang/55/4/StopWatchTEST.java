package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;

import java.text.ParseException;

/**
 * Test class for StopWatch.
 *
 * @author John Doe
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @SuppressWarnings("static-access")
    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    // Test case for method: start()
    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    // Test case for method: stop()
    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    // Test case for method: reset()
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    // Test case for method: split()
    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertEquals(0, stopWatch.getTime());
        assertNotNull(stopWatch.getSplitTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
        assertEquals("0.000:00.000.001", stopWatch.toSplitString());
    }

    // Test case for method: unsplit()
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    // Test case for method: suspend()
    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    // Test case for method: resume()
    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        assertEquals(0, stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertEquals("0.000:00.000.001", stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    // Test case for method: getTime()
    @Test
    public void testGetTime() {
        stopWatch.start();
        long time = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(time - stopWatch.getStartTime(), stopWatch.getTime());
    }

    // Test case for method: getSplitTime()
    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long time = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(time - stopWatch.getStartTime(), stopWatch.getSplitTime());
    }

    // Test case for method: toString()
    @Test
    public void testToString() throws ParseException {
        stopWatch.start();
        long time = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.stop();
        String expected = "0.001:00.002.003";
        assertEquals(expected, stopWatch.toString());
    }

    // Test case for method: toSplitString()
    @Test
    public void testToSplitString() throws ParseException {
        stopWatch.start();
        long time = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.stop();
        String expected = "0.001:00.002.003";
        assertEquals(expected, stopWatch.toSplitString());
    }

    // Test case for illegal states
    @Test
    public void testIllegalStates() {
        stopWatch = new StopWatch();

        // Test case: start() before reset()
        assertThrows(IllegalStateException.class, () -> stopWatch.start());

        // Test case: stop() before start()
        assertThrows(IllegalStateException.class, () -> stopWatch.stop());

        // Test case: resume() before suspend()
        assertThrows(IllegalStateException.class, () -> stopWatch.resume());

        // Test case: split() before start()
        assertThrows(IllegalStateException.class, () -> stopWatch.split());

        // Test case: unsplit() before split()
        assertThrows(IllegalStateException.class, () -> stopWatch.unsplit());

        // Test case: start() after start()
        assertThrows(IllegalStateException.class, () -> stopWatch.start());

        // Test case: stop() after stop()
        assertThrows(IllegalStateException.class, () -> stopWatch.stop());

        // Test case: split() after split()
        assertThrows(IllegalStateException.class, () -> stopWatch.split());

        // Test case: unsplit() after unsplit()
        assertThrows(IllegalStateException.class, () -> stopWatch.unsplit());

        // Test case: suspend() after suspend()
        assertThrows(IllegalStateException.class, () -> stopWatch.suspend());

        // Test case: resume() after resume()
        assertThrows(IllegalStateException.class, () -> stopWatch.resume());
    }
}