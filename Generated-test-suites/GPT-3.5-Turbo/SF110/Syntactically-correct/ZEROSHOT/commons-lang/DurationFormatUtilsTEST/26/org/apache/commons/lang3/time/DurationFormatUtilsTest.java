package org.apache.commons.lang3.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3600000L;
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3600000L;
        String expected = "PT1H";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3600000L;
        String format = "H:mm:ss.SSS";
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 3600000L;
        String format = "H:mm:ss.S";
        boolean padWithZeros = true;
        String expected = "1:00:00.0";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 900000L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = " 0 days 0 hours 15 minutes 0 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = System.currentTimeMillis() - 86400000L;
        long endMillis = System.currentTimeMillis();
        String expected = "P1D";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = System.currentTimeMillis() - 86400000L;
        long endMillis = System.currentTimeMillis();
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        String expected = "1 days 0 hours 0 minutes 0 seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = System.currentTimeMillis() - 86400000L;
        long endMillis = System.currentTimeMillis();
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        boolean padWithZeros = true;
        String expected = "01 days 00 hours 00 minutes 00 seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @Test
    public void testLexx() {
        String format = "H:mm:ss.S";
        DurationFormatUtils.Token[] expected = new DurationFormatUtils.Token[]{
                new DurationFormatUtils.Token(DurationFormatUtils.H),
                new DurationFormatUtils.Token(":"),
                new DurationFormatUtils.Token(DurationFormatUtils.m),
                new DurationFormatUtils.Token(DurationFormatUtils.m),
                new DurationFormatUtils.Token(":"),
                new DurationFormatUtils.Token(DurationFormatUtils.s),
                new DurationFormatUtils.Token("."),
                new DurationFormatUtils.Token(DurationFormatUtils.S)
        };
        DurationFormatUtils.Token[] result = DurationFormatUtils.lexx(format);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testPaddedValue() {
        long value = 1L;
        boolean padWithZeros = true;
        int count = 2;
        String expected = "01";
        String result = DurationFormatUtils.paddedValue(value, padWithZeros, count);
        assertEquals(expected, result);
    }
}