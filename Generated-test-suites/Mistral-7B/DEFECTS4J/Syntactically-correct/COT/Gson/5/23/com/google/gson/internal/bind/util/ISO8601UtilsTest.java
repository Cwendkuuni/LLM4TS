package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

/**
 * Test class for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    /**
     * Test method for {@link ISO8601Utils#format(Date)}
     */
    @Test
    public void testFormat() {
        Date date = new Date(System.currentTimeMillis());

        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        assertEquals("Expected format to be 'yyyy-MM-ddThh:mm:ssZ'", formattedDate.startsWith("yyyy-"), formattedDate);
        assertEquals("Expected format to contain 'T'", formattedDate.contains("T"), formattedDate);
        assertEquals("Expected format to contain 'Z' for UTC", formattedDate.contains("Z"), formattedDate);
    }

    /**
     * Test method for {@link ISO8601Utils#format(Date, boolean, TimeZone)}
     */
    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date(System.currentTimeMillis());
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");

        String formattedDate = ISO8601Utils.format(date, true, timeZone);
        assertNotNull(formattedDate);
        assertEquals("Expected format to be 'yyyy-MM-ddThh:mm:ss.sss[Z|[+-]hh:mm]'", formattedDate.matches(".*[+-]\\d{2}:\\d{2}.*"), formattedDate);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)}
     */
    @Test
    public void testParse() {
        String dateString = "2022-03-31T14:25:33Z";
        ParsePosition parsePosition = new ParsePosition(0);

        Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals("Expected parsed date to match the original string", dateString, ISO8601Utils.format(parsedDate));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with an invalid date string
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        String dateString = "2022-33-31T14:25:33Z";
        ParsePosition parsePosition = new ParsePosition(0);

        ISO8601Utils.parse(dateString, parsePosition);
        fail("Expected a ParseException to be thrown");
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with a date string containing a time zone offset
     */
    @Test
    public void testParseWithTimeZoneOffset() {
        String dateString = "2022-03-31T14:25:33-07:00";
        ParsePosition parsePosition = new ParsePosition(0);

        Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals("Expected parsed date to have the correct time zone offset", -25200000, parsedDate.getTime() % 3600000);
    }
}