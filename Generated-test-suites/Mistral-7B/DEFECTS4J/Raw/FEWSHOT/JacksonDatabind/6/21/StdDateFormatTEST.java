package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithGMT() {
        StdDateFormat sdf = StdDateFormat.getInstance();
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("2023-03-01T12:34:56.789Z");
        assertEquals("2023-03-01 12:34:56.789", sdf.format(date));

        date = sdf.parse("2023-03-01T12:34:56.789+00:00");
        assertEquals("2023-03-01 12:34:56.789", sdf.format(date));

        date = sdf.parse("2023-03-01T12:34:56.789-05:00");
        assertEquals("2023-03-01 07:34:56.789", sdf.format(date));
    }

    @Test
    public void testParseISO8601WithCustomTimeZone() {
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat sdf = new StdDateFormat(customTimeZone, Locale.US);

        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("2023-03-01T12:34:56.789Z");
        assertEquals("2023-03-01 19:34:56.789", sdf.format(date));

        date = sdf.parse("2023-03-01T12:34:56.789+00:00");
        assertEquals("2023-03-01 19:34:56.789", sdf.format(date));

        date = sdf.parse("2023-03-01T12:34:56.789-05:00");
        assertEquals("2023-03-01 14:34:56.789", sdf.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat sdf = StdDateFormat.getInstance();
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("Wed, 01 Mar 2023 12:34:56 GMT");
        assertEquals("2023-03-01 12:34:56.0", sdf.format(date));
    }

    @Test
    public void testParseRFC1123WithCustomTimeZone() {
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat sdf = new StdDateFormat(customTimeZone, Locale.US);

        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("Wed, 01 Mar 2023 12:34:56 GMT");
        assertEquals("2023-03-01 16:34:56.0", sdf.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        StdDateFormat sdf = StdDateFormat.getInstance();
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("InvalidDateString");
        fail("Expected ParseException but got: " + sdf.format(date));
    }
}