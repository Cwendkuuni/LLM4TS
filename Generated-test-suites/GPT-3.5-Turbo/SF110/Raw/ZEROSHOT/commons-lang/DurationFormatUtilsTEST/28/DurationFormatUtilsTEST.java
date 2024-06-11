package org.apache.commons.lang3.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3600000;
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 86400000;
        String expected = "P0000Y00M01DT00H00M00S000";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 60000;
        String format = "m:ss.SSS";
        String expected = "1:00.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPad() {
        long durationMillis = 120000;
        String format = "m:ss.SSS";
        boolean padWithZeros = true;
        String expected = "2:00.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3600000;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 hour";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 3600000;
        String expected = "P0000Y00M00DT01H00M00S000";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 3600000;
        String format = "H:mm:ss.SSS";
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPad() {
        long startMillis = 0;
        long endMillis = 120000;
        String format = "m:ss.SSS";
        boolean padWithZeros = true;
        String expected = "2:00.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, result);
    }
}