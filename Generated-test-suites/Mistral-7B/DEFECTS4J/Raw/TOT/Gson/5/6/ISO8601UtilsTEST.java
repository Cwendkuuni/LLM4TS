package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ISO8601UtilsTest class for testing the functionality of ISO8601Utils class.
 */
public class ISO8601UtilsTest
{
    /**
     * Test for the 'format' method with valid date.
     */
    @Test
    public void testFormatValidDate() {
        Date date = new GregorianCalendar(2022, Calendar.AUGUST, 15).getTime();
        String expected = "2022-08-15T12:34:56Z";
        String actual = ISO8601Utils.format(date, false, TimeZone.getDefault());
        assertEquals(expected, actual);
    }

    /**
     * Test for the 'format' method with valid date and milliseconds.
     */
    @Test
    public void testFormatValidDateWithMillis() {
        Date date = new GregorianCalendar(2022, Calendar.AUGUST, 15, 12, 34, 56, 789).getTime();
        String expected = "2022-08-15T12:34:56.789Z";
        String actual = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals(expected, actual);
    }

    /**
     * Test for the 'format' method with valid date, milliseconds and custom timezone.
     */
    @Test
    public void testFormatValidDateWithMillisAndCustomTimezone() {
        Date date = new GregorianCalendar(2022, Calendar.AUGUST, 15, 12, 34, 56, 789, TimeZone.getTimeZone("America/Los_Angeles")).getTime();
        String expected = "2022-08-15T19:34:56.789-07:00";
        String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals(expected, actual);
    }

    /**
     * Test for the 'parse' method with valid ISO8601 formatted string.
     */
    @Test
    public void testParseValidISODate() throws ParseException {
        String dateString = "2022-08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2022, Calendar.AUGUST, 15).getTime();
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
    }

    /**
     * Test for the 'parse' method with valid ISO8601 formatted string with milliseconds.
     */
    @Test
    public void testParseValidISODateWithMillis() throws ParseException {
        String dateString = "2022-08-15T12:34:56.789Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2022, Calendar.AUGUST, 15, 12, 34, 56, 789).getTime();
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
    }

    /**
     * Test for the 'parse' method with valid ISO8601 formatted string with custom timezone.
     */
    @Test
    public void testParseValidISODateWithCustomTimezone() throws ParseException {
        String dateString = "2022-08-15T12:34:56.789-07:00";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2022, Calendar.AUGUST, 15, 15, 34, 56, 789, TimeZone.getTimeZone("America/Los_Angeles")).getTime();
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing year).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingYear() throws ParseException {
        String dateString = "08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing month).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingMonth() throws ParseException {
        String dateString = "2022-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing day).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingDay() throws ParseException {
        String dateString = "2022-08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString.substring(0, 14), pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing hour).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingHour() throws ParseException {
        String dateString = "2022-08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString.substring(0, 13), pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing minute).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingMinute() throws ParseException {
        String dateString = "2022-08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString.substring(0, 12), pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing second).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingSecond() throws ParseException {
        String dateString = "2022-08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString.substring(0, 11), pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing millisecond).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingMillisecond() throws ParseException {
        String dateString = "2022-08-15T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString.substring(0, 13), pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (invalid timezone offset).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateWithInvalidTimezoneOffset() throws ParseException {
        String dateString = "2022-08-15T12:34:56+123:45";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test for the 'parse' method with invalid ISO8601 formatted string (missing timezone indicator).
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISODateMissingTimezoneIndicator() throws ParseException {
        String dateString = "2022-08-15T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}