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
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    // test for start method

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartBeforeReset() {
        stopWatch.start();
        stopWatch.reset();
        stopWatch.start();
    }

    // test for stop method

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    // test for reset method

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertNull(stopWatch.getTime());
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
    }

    @Test
    public void testResetAfterSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.reset();
        assertNull(stopWatch.getSplitTime());
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
    }

    // test for split method

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.split();
    }

    // test for unsplit method

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.unsplit();
    }

    // test for suspend method

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendAfterSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.suspend();
    }

    // test for resume method

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

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeAfterSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.resume();
    }

    // test for getTime method

    @Test
    public void testGetTime() {
        stopWatch.start();
        long elapsedTime = stopWatch.getTime();
        assertTrue(elapsedTime > 0);
    }

    @Test
    public void testGetTimeAfterSplit() {
        stopWatch.start();
        stopWatch.split();
        long elapsedTimeBeforeSplit = stopWatch.getTime();
        stopWatch.stop();
        long elapsedTimeAfterSplit = stopWatch.getTime();
        long elapsedTime = elapsedTimeBeforeSplit + elapsedTimeAfterSplit;
        assertEquals(elapsedTime, stopWatch.getTime());
    }

    // test for getSplitTime method

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        long elapsedTime = stopWatch.getSplitTime();
        assertTrue(elapsedTime > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        stopWatch.getSplitTime();
    }

    // test for toString method

    @Test
    public void testToString() {
        String expected = "0:0:0.001";
        stopWatch.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringAfterSplit() {
        String expected = "0:0:0.001";
        stopWatch.start();
        stopWatch.split();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    // test for toSplitString method

    @Test
    public void testToSplitString() {
        String expected = "0:0:0.001";
        stopWatch.start();
        stopWatch.split();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String actual = stopWatch.toSplitString();
        assertEquals(expected, actual);
    }

    // test for getDurationFormatUtils method

    @Test
    public void testDurationFormatUtils() throws ParseException {
        long duration = 1000L;
        String format = "HH:mm:ss.SSS";
        String expected = "0:0:1.000";
        String actual = DurationFormatUtils.formatDuration(duration, format);
        assertEquals(expected, actual);
    }
}