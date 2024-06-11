package com.google.gson.internal.bind.util;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    // Test format(Date date)
    @Test
    public void testFormatDate() {
        Date date = new Date(1609459200000L); // January 1, 2021 00:00:00 UTC
        String expected = "2021-01-01T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        Assert.assertEquals(expected, actual);
    }
    
    // Test format(Date date, boolean millis)
    @Test
    public void testFormatDateWithMillis() {
        Date date = new Date(1609459200123L); // January 1, 2021 00:00:00.123 UTC
        String expected = "2021-01-01T00:00:00.123Z";
        String actual = ISO8601Utils.format(date, true);
        Assert.assertEquals(expected, actual);
    }
    
    // Test format(Date date, boolean millis, TimeZone tz)
    @Test
    public void testFormatDateWithMillisAndTimezone() {
        Date date = new Date(1609459200000L); // January 1, 2021 00:00:00 UTC
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String expected = "2021-01-01T02:00:00+02:00";
        String actual = ISO8601Utils.format(date, true, tz);
        Assert.assertEquals(expected, actual);
    }
    
    // Test parse(String date, ParsePosition pos)
    @Test
    public void testParseDate() throws ParseException {
        String date = "2021-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1609459200000L); // January 1, 2021 00:00:00 UTC
        Date actual = ISO8601Utils.parse(date, pos);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(date.length(), pos.getIndex());
    }
    
    // Test parse(String date, ParsePosition pos) with invalid format
    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        String date = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos); // Should throw ParseException
    }
    
    // Test parse(String date, ParsePosition pos) with invalid timezone
    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimezone() throws ParseException {
        String date = "2021-01-01T00:00:00+99:99";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos); // Should throw ParseException
    }
}