package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test cases for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    /**
     * Test format method with default timezone and no milliseconds precision
     */
    @Test
    public void testFormatDefaultTimezoneNoMillisecondsPrecision() {
        Date date = new Date(1573492800000L); // Mon Jan 20 00:00:00 UTC 2020
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2020-01-20T00:00:00Z", formattedDate);
    }

    /**
     * Test format method with GMT timezone and milliseconds precision
     */
    @Test
    public void testFormatGMTWithMillisecondsPrecision() {
        Date date = new Date(1573492800123L); // Mon Jan 20 00:00:12.300 UTC 2020
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        assertEquals("2020-01-20T00:00:12.300Z", formattedDate);
    }

    /**
     * Test format method with custom timezone and no milliseconds precision
     */
    @Test
    public void testFormatCustomTimezoneNoMillisecondsPrecision() {
        Date date = new Date(1573492800000L); // Mon Jan 20 00:00:00 UTC 2020
        String formattedDate = ISO8601Utils.format(date, null, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2020-01-19T21:00:00-08:00", formattedDate);
    }

    /**
     * Test parse method with a valid ISO-8601 formatted date string
     */
    @Test
    public void testParseValidIso8601FormattedDateString() throws ParseException {
        String iso8601DateString = "2020-01-20T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(iso8601DateString, pos);
        assertNotNull(parsedDate);
        assertEquals(1573492800000L, parsedDate.getTime()); // Mon Jan 20 00:00:00 UTC 2020
        assertEquals(0, pos.getIndex());
    }

    /**
     * Test parse method with an invalid ISO-8601 formatted date string
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidIso8601FormattedDateString() throws ParseException {
        String iso8601DateString = "2020-01-20T00:00:00Z2";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(iso8601DateString, pos);
        fail("ParseException expected but not thrown");
    }

    /**
     * Test parse method with a date string without a time component and timezone
     */
    @Test
    public void testParseDateStringWithoutTimeComponentAndTimezone() throws ParseException {
        String iso8601DateString = "2020-01-20";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(iso8601DateString, pos);
        assertNotNull(parsedDate);
        assertEquals(1573436800000L, parsedDate.getTime()); // Mon Jan 20 00:00:00 UTC 2020
        assertEquals(iso8601DateString.length(), pos.getIndex());
    }

    /**
     * Test parse method with a date string without a time component but with a timezone
     */
    @Test(expected = ParseException.class)
    public void testParseDateStringWithoutTimeComponentButWithTimezone() throws ParseException {
        String iso8601DateString = "2020-01-20+01:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(iso8601DateString, pos);
        fail("ParseException expected but not thrown");
    }

    /**
     * Test parse method with a date string with a time component and no timezone
     */
    @Test(expected = ParseException.class)
    public void testParseDateStringWithTimeComponentAndNoTimezone() throws ParseException {
        String iso8601DateString = "2020-01-20T01:00:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(iso8601DateString, pos);
        fail("ParseException expected but not thrown");
    }

    /**
     * Test parse method with a date string with a time component and a timezone
     */
    @Test
    public void testParseDateStringWithTimeComponentAndTimezone() throws ParseException {
        String iso8601DateString = "2020-01-20T01:00:00+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(iso8601DateString, pos);
        assertNotNull(parsedDate);
        assertEquals(1573439400000L, parsedDate.getTime()); // Mon Jan 20 02:00:00 CET 2020
        assertEquals(iso8601DateString.length(), pos.getIndex());
    }

}