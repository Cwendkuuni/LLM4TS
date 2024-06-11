package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 5000;
        String expected = "0:00:05.000";
        String result = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 5000;
        String expected = "PT0S";
        String result = DurationFormatUtils.formatDurationISO(duration);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long duration = 5000;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:05.000";
        String result = DurationFormatUtils.formatDuration(duration, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        long duration = 5000;
        String format = "H:mm:ss.S";
        String expected = "0:00:05.0";
        String result = DurationFormatUtils.formatDuration(duration, format, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 5000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = " 0 days 0 hours 0 minutes 5 seconds";
        String result = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 5000;
        String expected = "PT0.005S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 5000;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:05.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 0;
        long endMillis = 5000;
        String format = "H:mm:ss.S";
        String expected = "0:00:05.0";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test
    public void testFormat() {
        long years = 1;
        long months = 2;
        long days = 3;
        long hours = 4;
        long minutes = 5;
        long seconds = 6;
        long milliseconds = 7;
        boolean padWithZeros = true;
        String expected = "01 02 03 04 05 06 007";
        String format = "yyyy MM dd HH mm ss SSS";
        String result = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
        assertEquals(expected, result);
    }

    @Test
    public void testPaddedValue() {
        long value = 5;
        boolean padWithZeros = true;
        int count = 2;
        String expected = "05";
        String result = DurationFormatUtils.paddedValue(value, padWithZeros, count);
        assertEquals(expected, result);
    }

    @Test
    public void testLexx() {
        String format = "H:mm:ss.S";
        Token[] expected = new Token[]{
            new Token("H"),
            new Token(":"),
            new Token("mm"),
            new Token(":"),
            new Token("ss"),
            new Token("."),
            new Token("S")
        };
        Token[] result = DurationFormatUtils.lexx(format);
        assertArrayEquals(expected, result);
    }
}