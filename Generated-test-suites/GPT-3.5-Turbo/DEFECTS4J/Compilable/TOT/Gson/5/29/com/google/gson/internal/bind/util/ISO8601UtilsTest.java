package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case
        Date date = new Date(1603815600000L); // November 4, 2020 12:00:00 AM GMT+00:00
        String expected = "2020-11-04T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);

        // Edge case: Test formatting a date with milliseconds precision
        date = new Date(1603815600123L); // November 4, 2020 12:00:00.123 AM GMT+00:00
        expected = "2020-11-04T00:00:00.123Z";
        actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);

        // Test formatting a date with a different timezone
        date = new Date(1603815600000L); // November 4, 2020 12:00:00 AM GMT+00:00
        expected = "2020-11-04T03:00:00+03:00";
        TimeZone tz = TimeZone.getTimeZone("GMT+3");
        actual = ISO8601Utils.format(date, false, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String isoString = "2020-11-04T00:00:00Z";

        // Typical use case
        Date expected = new Date(1603815600000L); // November 4, 2020 12:00:00 AM GMT+00:00
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(isoString, pos);
        assertEquals(expected, actual);

        // Edge case: Test parsing a date with milliseconds precision
        isoString = "2020-11-04T00:00:00.123Z";
        expected = new Date(1603815600123L); // November 4, 2020 12:00:00.123 AM GMT+00:00
        pos = new ParsePosition(0);
        actual = ISO8601Utils.parse(isoString, pos);
        assertEquals(expected, actual);

        // Error scenario: Test parsing an invalid ISO string
        isoString = "202011-0420:00";
        pos = new ParsePosition(0);
        try {
            actual = ISO8601Utils.parse(isoString, pos);
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"202011-0420:00\"]: For input string: \"0420:00\"", e.getMessage());
        }
    }

}