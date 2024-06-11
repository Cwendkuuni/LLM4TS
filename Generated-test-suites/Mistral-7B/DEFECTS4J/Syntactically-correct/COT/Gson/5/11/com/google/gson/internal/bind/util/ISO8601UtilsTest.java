package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Test class for ISO8601Utils
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test formatting with default timezone (UTC) and no milliseconds precision
        Date date = new GregorianCalendar(2021, 11, 25).getTime();
        String formattedDate = ISO8601Utils.format(date);
        String expected = "2021-12-25T00:00:00Z";
        Assert.assertEquals(expected, formattedDate);

        // Test formatting with milliseconds precision
        Date dateWithMillis = new GregorianCalendar(2021, 11, 25, 12, 30, 45).getTime();
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, true);
        String expectedWithMillis = "2021-12-25T12:30:45.045Z";
        Assert.assertEquals(expectedWithMillis, formattedDateWithMillis);

        // Test formatting with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        Date dateWithCustomTimeZone = new GregorianCalendar(2021, 11, 25, 17, 0, 0, customTimeZone).getTime();
        String formattedDateWithCustomTimeZone = ISO8601Utils.format(dateWithCustomTimeZone, false, customTimeZone);
        String expectedWithCustomTimeZone = "2021-12-25T17:00:00-05:00";
        Assert.assertEquals(expectedWithCustomTimeZone, formattedDateWithCustomTimeZone);
    }

    @Test
    public void testParse() throws ParseException {
        // Test parsing with default timezone (UTC) and no timezone indicator
        String dateString = "2021-12-25";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar expectedCalendar = new GregorianCalendar(2021, 11, 25);
        Assert.assertEquals(expectedCalendar.getTime(), parsedDate);

        // Test parsing with timezone indicator
        String dateStringWithTimeZone = "2021-12-25T12:30:45-05:00";
        Date parsedDateWithTimeZone = ISO8601Utils.parse(dateStringWithTimeZone, new ParsePosition(0));
        Calendar expectedCalendarWithTimeZone = new GregorianCalendar(2021, 11, 25, 12, 30, 45, TimeZone.getTimeZone("America/New_York"));
        Assert.assertEquals(expectedCalendarWithTimeZone.getTime(), parsedDateWithTimeZone);

        // Test parsing with invalid format
        String invalidDateString = "2021-12-25T12:30:45Z-5";
        Date parsedInvalidDate = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNegativeYear() {
        Date date = new GregorianCalendar(-1, 1, 1).getTime();
        // The format method should throw IllegalArgumentException for negative year
        ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNegativeMonth() {
        Date date = new GregorianCalendar(2021, -1, 1).getTime();
        // The format method should throw IllegalArgumentException for negative month
        ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNegativeDayOfMonth() {
        Date date = new GregorianCalendar(2021, 11, -1).getTime();
        // The format method should throw IllegalArgumentException for negative day of month
        ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNegativeYear() throws ParseException {
        String dateString = "yyyy-MM-dd";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        // The parse method should throw IllegalArgumentException for negative year
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNegativeMonth() throws ParseException {
        String dateString = "yyyy-MM-dd";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        // The parse method should throw IllegalArgumentException for negative month
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNegativeDayOfMonth() throws ParseException {
        String dateString = "yyyy-MM-dd";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        // The parse method should throw IllegalArgumentException for negative day of month
        Assert.fail();
    }
}