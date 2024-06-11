package com.example.junit; // replace with your test package if necessary

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

import com.google.giron.internal.bind.util.ISO8601Utils;

/**
 * Test class for ISO8601Utils
 */
public class ISO8601UtilsTest
{
    /**
     * Test method for {@link ISO8601Utils#format(Date)}
     */
    @Test
    public void testFormatDate()
    {
        Date date = new Date(); // use current date
        String formattedDate = ISO8601Utils.format(date);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        assertTrue(formattedDate.matches(expectedFormat));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with valid date
     */
    @Test
    public void testParseValidDate()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:59:59.123456Z";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals("2022-12-31 23:59:59.123", ISO8601Utils.format(parsedDate, false, null));
        assertEquals("2022-12-31T23:59:59.123Z", ISO8601Utils.format(parsedDate, true, null));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid date
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDate()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:59:59.123Z-invalid";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with timezone offset
     */
    @Test
    public void testParseDateWithTimezoneOffset()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:59:59.123+02:00";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        TimeZone timezone = TimeZone.getTimeZone("Europe/Paris");
        assertEquals("2022-12-31 21:59:59.123", ISO8601Utils.format(parsedDate, false, timezone));
        assertEquals("2022-12-31T21:59:59.123+02:00", ISO8601Utils.format(parsedDate, true, timezone));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with no timezone
     */
    @Test
    public void testParseDateWithNoTimezone()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:59:59.123";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals("2022-12-31 23:59:59.123", ISO8601Utils.format(parsedDate, false, null));
        assertEquals("2022-12-31T23:59:59.123Z", ISO8601Utils.format(parsedDate, true, null));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing year
     */
    @Test(expected = ParseException.class)
    public void testParseDateWithMissingYear()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "12-31T23:59:59.123Z";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing month
     */
    @Test(expected = ParseException.class)
    public void testParseDateWithMissingMonth()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12T23:59:59.123Z";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing day
     */
    @Test(expected = ParseException.class)
    public void testParseDateWithMissingDay()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-T23:59:59.123Z";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing hour
     */
    @Test(expected = ParseException.class)
    public void testParseDateWithMissingHour()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T-:59:59.123Z";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing minute
     */
    @Test(expected = ParseException.class)
    public void testParseDateWithMissingMinute()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:-:59.123Z";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing second
     */
    @Test(expected = ParseException.class)
    public void testParseDateWithMissingSecond()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:59:.";
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with date with missing millisecond
     */
    @Test
    public void testParseDateWithMissingMillisecond()
        throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-12-31T23:59:59.";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals("2022-12-31 23:59:59.000", ISO8601Utils.format(parsedDate, false, null));
        assertEquals("2022-12-31T23:59:59.000Z", ISO8601Utils.format(parsedDate, true, null));
    }
}