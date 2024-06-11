package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Tests for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        Date date = new Date(1516044320000L);
        String expected = "2017-10-02T12:05:20Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);

        // Test with milliseconds
        date.setTime(date.getTime() + 123);
        expected = "2017-10-02T12:05:20.123Z";
        actual = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals(expected, actual);

        // Test with time zone
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        expected = "2017-10-02T16:05:20.123-04:00";
        actual = ISO8601Utils.format(date, true, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws Exception {
        String dateString = "2017-10-02T12:05:20Z";
        Date expectedDate = new Date(1516044320000L);
        Date actualDate = null;

        ParsePosition pos = new ParsePosition(0);
        actualDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(expectedDate, actualDate);
        assertEquals(dateString.length(), pos.getIndex());

        // Test with time zone
        dateString += " -04:00";
        expectedDate = new Date(1516010520000L);
        actualDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(expectedDate, actualDate);
        assertEquals(dateString.length(), pos.getIndex());

        // Test with invalid date
        dateString = "2017-10-02T12:05:20-04:00X";
        actualDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(null, actualDate);
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParse_SimpleDateFormat() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = sdf.parse("2017-10-02T12:05:20Z");

        Date parsedDate = null;
        ParsePosition pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(sdf.format(date), pos);
        assertEquals(date, parsedDate);
        assertEquals(sdf.format(date).length(), pos.getIndex());
    }

    @Test
    public void testFormat_InvalidDate() {
        try {
            ISO8601Utils.format(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            ISO8601Utils.format(new Date(0));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidDate() {
        try {
            ISO8601Utils.parse("2017-10-02T12:05:20X", null);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("2017-10-02T12:05:20Z", new ParsePosition(-1));
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}