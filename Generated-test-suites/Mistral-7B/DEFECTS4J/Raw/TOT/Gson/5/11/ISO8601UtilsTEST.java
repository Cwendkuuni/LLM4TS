package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ISO8601Utils Test Class
 */
public class ISO8601UtilsTest {

    /**
     * Test format method with valid date and default timezone
     */
    @Test
    public void testFormatValidDateDefaultTimezone() {
        Date date = new Date(1563808000000L); // 2019-06-15T00:00:00Z
        String expected = "2019-06-15T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test format method with valid date, milliseconds precision, and custom timezone
     */
    @Test
    public void testFormatValidDateMillisPrecisionCustomTimezone() {
        Date date = new Date(1563808012345L); // 2019-06-15T00:00:12.345Z
        String expected = "2019-06-15T00:00:12.345Z";
        String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test format method with null date
     */
    @Test(expected = NullPointerException.class)
    public void testFormatNullDate() {
        ISO8601Utils.format(null);
    }

    /**
     * Test parse method with valid date string and default timezone
     */
    @Test
    public void testParseValidDateDefaultTimezone() {
        String dateString = "2019-06-15T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        long expectedTime = 1563808000000L; // 2019-06-15T00:00:00Z
        long actualTime = date.getTime();
        Assert.assertEquals(expectedTime, actualTime);
        Assert.assertEquals(0, pos.getIndex());
    }

    /**
     * Test parse method with valid date string, milliseconds precision, and custom timezone
     */
    @Test
    public void testParseValidDateMillisPrecisionCustomTimezone() {
        String dateString = "2019-06-15T00:00:12.345Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        long expectedTime = 1563808012345L; // 2019-06-15T00:00:12.345Z
        long actualTime = date.getTime();
        Assert.assertEquals(expectedTime, actualTime);
        Assert.assertEquals(dateString.length(), pos.getIndex());
    }

    /**
     * Test parse method with invalid date string
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDateString() {
        String dateString = "2019-13-32T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test parse method with null date string
     */
    @Test(expected = NullPointerException.class)
    public void testParseNullDateString() {
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(null, pos);
    }
}