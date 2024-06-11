package org.apache.commons.lang3.time;

import org.junit.Assert;
import org.junit.Test;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        long duration = 123456789;
        String expected = "34:17:36.789";
        String actual = DurationFormatUtils.formatDurationHMS(duration);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationISO() {
        long duration = 123456789;
        String expected = "P0Y0M0DT34H17M36.789S";
        String actual = DurationFormatUtils.formatDurationISO(duration);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDuration() {
        long duration = 123456789;
        String format = "H:mm:ss.SSS";
        String expected = "34:17:36.789";
        String actual = DurationFormatUtils.formatDuration(duration, format);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationWithPadding() {
        long duration = 123456789;
        String format = "HH:mm:ss.SSS";
        String expected = "34:17:36.789";
        String actual = DurationFormatUtils.formatDuration(duration, format, true);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationWords() {
        long duration = 123456789;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "0 days 34 hours 17 minutes 36 seconds";
        String actual = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 123456789;
        long endMillis = 987654321;
        String expected = "P0Y0M2DT10H17M36.532S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 123456789;
        long endMillis = 987654321;
        String format = "H:mm:ss.SSS";
        String expected = "10:17:36.532";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodWithPadding() {
        long startMillis = 123456789;
        long endMillis = 987654321;
        String format = "HH:mm:ss.SSS";
        String expected = "10:17:36.532";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        Assert.assertEquals(expected, actual);
    }
}