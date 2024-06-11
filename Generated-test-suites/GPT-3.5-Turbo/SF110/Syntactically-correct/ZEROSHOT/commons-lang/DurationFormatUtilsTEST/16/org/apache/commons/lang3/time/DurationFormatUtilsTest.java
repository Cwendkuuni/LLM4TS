package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import java.util.*;
import org.junit.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3600000;
        String expected = "1:00:00.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 86400000;
        String expected = "P1970Y1M1DT0H0M0.000S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 60000;
        String format = "mm:ss";
        String expected = "01:00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 60000;
        String format = "mm:ss";
        boolean padWithZeros = true;
        String expected = "01:00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 86400000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = " 1 days 0 hours 0 minutes 0 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 86400000;
        String expected = "P1970Y1M2DT0H0M0.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 86400000;
        String format = "HH:mm:ss";
        String expected = "24:00:00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithPadding() {
        long startMillis = 0;
        long endMillis = 86400000;
        String format = "HH:mm:ss";
        boolean padWithZeros = true;
        String expected = "24:00:00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        Assert.assertEquals(expected, actual);
    }
}