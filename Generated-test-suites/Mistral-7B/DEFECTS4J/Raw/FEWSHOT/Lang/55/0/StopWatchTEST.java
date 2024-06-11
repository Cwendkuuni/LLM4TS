package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStartStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
    }

    @Test
    public void testStartSplitStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
        assertEquals(0, stopwatch.getSplitTime());
    }

    @Test
    public void testStartSplitUnsplitStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
        assertEquals(0, stopwatch.getSplitTime());
    }

    @Test
    public void testStartSplitUnsplitSplitUnsplitStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
        assertEquals(0, stopwatch.getSplitTime());
    }

    @Test
    public void testStartSplitStopSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
        assertEquals(DurationFormatUtils.parseDuration("PT0.001S"), stopwatch.getSplitTime());
        assertEquals(DurationFormatUtils.parseDuration("PT0.002S"), stopwatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.start();
    }

    @Test
    public void testResumeSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
    }

    @Test
    public void testResumeSuspendTwice() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.suspend();
        stopwatch.resume();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitTwice() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.unsplit();
    }

    @Test
    public void testSplitUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    @Test
    public void testStartSplitUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertEquals(0, stopwatch.getTime());
        assertEquals(0, stopwatch.getSplitTime());
    }

    @Test
    public void testToString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.toString());
    }

    @Test
    public void testToStringSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.toString());
        assertEquals("0.000S", stopwatch.toSplitString());
    }

    @Test
    public void testToStringSplitSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.toString());
        assertEquals("0.000S", stopwatch.toSplitString());
        assertEquals("0.001S", stopwatch.getSplitTimeAsString());
        assertEquals("0.002S", stopwatch.getSplitTimeAsString(1));
    }

    @Test
    public void testToStringSplitSplitSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.unsplit();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.toString());
        assertEquals("0.000S", stopwatch.toSplitString());
        assertEquals("0.001S", stopwatch.getSplitTimeAsString());
        assertEquals("0.002S", stopwatch.getSplitTimeAsString(1));
        assertEquals("0.003S", stopwatch.getSplitTimeAsString(2));
    }

    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        stopwatch.reset();
        assertFalse(stopwatch.isRunning());
        assertSame(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    @Test
    public void testResetAfterSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        stopwatch.reset();
        assertFalse(stopwatch.isRunning());
        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    @Test
    public void testResetAfterStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        stopwatch.reset();
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.hasSplit());
        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    @Test
    public void testResetAfterSuspend() throws InterruptedException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        Thread.sleep(100);
        stopwatch.resume();
        stopwatch.stop();
        stopwatch.reset();
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.hasSplit());
        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    @Test
    public void testGetTimeAsString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.getTimeAsString());
        assertEquals("0.000S", stopwatch.getSplitTimeAsString());
    }

    @Test
    public void testGetSplitTimeAsString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.getTimeAsString());
        assertEquals("0.000S", stopwatch.getSplitTimeAsString());
        assertEquals("0.000S", stopwatch.getSplitTimeAsString(0));
    }

    @Test
    public void testGetSplitTimeAsStringMultipleSplits() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
        stopwatch.unsplit();
        stopwatch.unsplit();
        stopwatch.stop();
        assertEquals("0.000S", stopwatch.getTimeAsString());
        assertEquals("0.000S", stopwatch.getSplitTimeAsString());
        assertEquals("0.001S", stopwatch.getSplitTimeAsString(0));
        assertEquals("0.002S", stopwatch.getSplitTimeAsString(1));
    }

    @Test
    public void testFormatDurationHMS() throws ParseException {
        String durationStr = DurationFormatUtils.formatDurationHMS(0);
        assertEquals("0.000S", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(100);
        assertEquals("0:00:01.000", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("PT1S"));
        assertEquals("0:00:01.000", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("PT2S"));
        assertEquals("0:00:02.000", durationStr);
    }

    @Test
    public void testFormatDurationHMSNegative() throws ParseException {
        String durationStr = DurationFormatUtils.formatDurationHMS(-1);
        assertEquals("-0:00:01.000", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("PT-1S"));
        assertEquals("-0:00:01.000", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("P-1S"));
        assertEquals("-0:00:01.000", durationStr);
    }

    @Test
    public void testFormatDurationHMSLong() throws ParseException {
        String durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("PT10000S"));
        assertEquals("2:46:40.000", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("PT2H30M45S"));
        assertEquals("2:30:45.000", durationStr);
    }

    @Test
    public void testFormatDurationHMSLongNegative() throws ParseException {
        String durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("P-1H"));
        assertEquals("-1:01:00.000", durationStr);

        durationStr = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.parseDuration("P-30M"));
        assertEquals("-0:30:00.000", durationStr);
    }
}