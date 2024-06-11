package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * ISO8601UtilsTest.java
 *
 * Test file for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    /**
     * Test format method with default timezone and no milliseconds precision
     *
     * @throws ParseException
     */
    @Test
    public void testFormatDefault() throws ParseException {
        Date date = new GregorianCalendar(2023, 1, 31).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2023-02-01T00:00:00Z", formattedDate);
    }

    /**
     * Test format method with GMT timezone and milliseconds precision
     *
     * @throws ParseException
     */
    @Test
    public void testFormatGMTWithMillis() throws ParseException {
        Date date = new GregorianCalendar(2023, 1, 31, 12, 34, 567).getTime();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        assertEquals("2023-02-01T12:34:56.7Z", formattedDate);
    }

    /**
     * Test format method with custom timezone and no milliseconds precision
     *
     * @throws ParseException
     */
    @Test
    public void testFormatCustomTimeZone() throws ParseException {
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Date date = new GregorianCalendar(2023, 1, 31).getTime();
        String formattedDate = ISO8601Utils.format(date, false, customTimeZone);
        assertEquals("2023-02-01T07:00:00-08:00", formattedDate);
    }

    /**
     * Test parse method with valid ISO-8601 formatted string
     *
     * @throws ParseException
     */
    @Test
    public void testParseValidString() throws ParseException {
        String isoDate = "2023-02-01T12:34:56.7Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(isoDate, ISO8601Utils.format(parsedDate, true, TimeZone.getTimeZone("GMT")));
    }

    /**
     * Test parse method with invalid ISO-8601 formatted string
     *
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidString() throws ParseException {
        String isoDate = "2023-02-01T12:34:56.7ZInvalid";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, pos);
    }
}