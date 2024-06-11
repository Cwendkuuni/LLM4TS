package org.apache.commons.lang.time;

import static org.apache.commons.lang3.StringUtils.equals;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

/**
 * Test cases for {@link StopWatch} class.
 */
public class StopWatchTest {

    /**
     * Test for {@link StopWatch#start()} method.
     */
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertFalse(stopWatch.isStarted());
        assertTrue(stopWatch.isRunning());
        assertNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#stop()} method.
     */
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isRunning());
        assertNotNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#reset()} method.
     */
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#split()} method.
     */
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplitted());
        assertFalse(stopWatch.isRunning());
        assertNotNull(stopWatch.getSplitTime());
        assertNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#unsplit()} method.
     */
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplitted());
        assertFalse(stopWatch.isRunning());
        assertNotNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#suspend()} method.
     */
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isRunning());
        assertNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#resume()} method.
     */
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertNull(stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#getTime()} method.
     */
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Date time = new Date(System.currentTimeMillis());
        stopWatch.stop();
        long expectedTime = DurationFormatUtils.parseDuration(
                formatDurationHMS(DurationFormatUtils.formatDurationHMS(1L), ""), true).getMilliseconds();

        assertEquals(expectedTime, stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#getSplitTime()} method.
     */
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Date time = new Date(System.currentTimeMillis());
        stopWatch.stop();
        long expectedSplitTime = DurationFormatUtils.parseDuration(
                formatDurationHMS(DurationFormatUtils.formatDurationHMS(0, 1, 2, 345), ""), true).getMilliseconds();

        assertEquals(expectedSplitTime, stopWatch.getSplitTime());
    }

    /**
     * Test for {@link StopWatch#toString()} method.
     */
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Date time = new Date(System.currentTimeMillis());
        stopWatch.stop();
        String expectedResult = formatDurationHMS(DurationFormatUtils.parseDuration(
                formatDurationHMS(1L, 0, 0, 0), "").getMillis()) + " - " + formatDurationHMS(0, 1, 2, 345);

        assertEquals(expectedResult, stopWatch.toString());
    }

    /**
     * Test for {@link StopWatch#toSplitString()} method.
     */
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        Date time = new Date(System.currentTimeMillis());
        stopWatch.stop();
        String expectedResult = formatDurationHMS(DurationFormatUtils.parseDuration(
                formatDurationHMS(0, 1, 2, 345), "").getMillis());

        assertEquals(expectedResult, stopWatch.toSplitString());
    }

    /**
     * Test for illegal calls to methods.
     */
    @Test
    public void testIllegalCalls() {
        StopWatch stopWatch = new StopWatch();

        assertThrows(IllegalStateException.class, () -> stopWatch.split());
        stopWatch.start();

        assertThrows(IllegalStateException.class, () -> stopWatch.split());
        assertThrows(IllegalStateException.class, () -> stopWatch.stop());
        assertThrows(IllegalStateException.class, () -> stopWatch.split());
        assertThrows(IllegalStateException.class, () -> stopWatch.unsplit());
        assertThrows(IllegalStateException.class, () -> stopWatch.resume());
        stopWatch.suspend();

        assertThrows(IllegalStateException.class, () -> stopWatch.resume());
        assertThrows(IllegalStateException.class, () -> stopWatch.stop());
        assertThrows(IllegalStateException.class, () -> stopWatch.split());
        assertThrows(IllegalStateException.class, () -> stopWatch.unsplit());

        stopWatch.start();
        assertThrows(IllegalStateException.class, () -> stopWatch.stop());
        assertThrows(IllegalStateException.class, () -> stopWatch.start());
    }
}