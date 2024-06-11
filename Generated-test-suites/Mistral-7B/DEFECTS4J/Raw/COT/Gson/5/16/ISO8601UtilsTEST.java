package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test cases for ISO8601Utils class.
 *
 * @author John Doe
 */
public class ISO8601UtilsTest {

    

    /**
     * Test method for {@link ISO8601Utils#format(Date)}.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testFormat() throws Exception {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date);
        String expected = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"; // Default format
        assertEquals(expected, formatted);
    }

    /**
     * Test method for {@link ISO8601Utils#format(Date, boolean, TimeZone)}.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testFormatWithMillisAndTimeZone() throws Exception {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        String expected = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // Format with milliseconds and UTC timezone
        assertEquals(expected, formatted);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)}.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testParse() throws Exception {
        String dateString = "2022-12-31T23:59:59.999Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);

        assertEquals("Expected parsed date to not be null", !null, parsedDate);
        assertEquals("Expected parsed date to equal original string", dateString, ISO8601Utils.format(parsedDate, true, TimeZone.getTimeZone("UTC")));
        assertEquals("Expected ParsePosition to be updated correctly", 0, pos.getIndex());
    }

    /**
     * Test edge case for {@link ISO8601Utils#parse(String, ParsePosition)}.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = ParseException.class)
    public void testParseEdgeCase() throws Exception {
        String dateString = "2022-12-31T23:59:59.999+01:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test edge case for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid format.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws Exception {
        String dateString = "2022-12-31T23:59:59.999-01:00A";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    
}