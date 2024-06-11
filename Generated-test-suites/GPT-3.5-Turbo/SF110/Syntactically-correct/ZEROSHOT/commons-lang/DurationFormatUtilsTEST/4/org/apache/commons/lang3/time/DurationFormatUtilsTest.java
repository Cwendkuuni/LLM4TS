package org.apache.commons.lang3.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3600000L;
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 86400000L;
        String expected = "P1DT0H0M0.000S";
        String result = DurationFormatUtils.formatDurationISO(duration);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long duration = 60000L;
        String format = "mm:ss";
        String expected = "01:00";
        String result = DurationFormatUtils.formatDuration(duration, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        long duration = 3600000L;
        String format = "H:mm:ss.SSS";
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatDuration(duration, format, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsSuppressLeadingZeroElements() {
        long duration = 86400000L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 day";
        String result = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsSuppressTrailingZeroElements() {
        long duration = 60000L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expected = "0 days 0 hours 1 minute";
        String result = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long start = 0L;
        long end = 86400000L;
        String expected = "P1DT0H0M0.000S";
        String result = DurationFormatUtils.formatPeriodISO(start, end);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long start = 0L;
        long end = 86400000L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        String expected = "1 days 0 hours 0 minutes 0 seconds";
        String result = DurationFormatUtils.formatPeriod(start, end, format);
        assertEquals(expected, result);
    }
}