package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;
        Date date = df.parse("2022-03-01T14:30:45.123Z");
        assertEquals("2022-03-01 14:30:45.123", df.format(date));
    }

    @Test
    public void testParseISO8601WithTimezone() throws ParseException {
        StdDateFormat df = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("UTC+2"));
        Date date = df.parse("2022-03-01T14:30:45.123Z");
        assertEquals("2022-03-01 16:30:45.123", df.format(date));
    }

    @Test
    public void testParseISO8601WithMillis() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;
        Date date = df.parse("2022-03-01T14:30:45.123Z");
        assertEquals("2022-03-01 14:30:45.123", df.format(date));

        date = df.parse("2022-03-01T14:30:45.123+02:00");
        assertEquals("2022-03-01 16:30:45.123", df.format(date));

        date = df.parse("2022-03-01T14:30:45.123-02:00");
        assertEquals("2022-03-01 12:30:45.123", df.format(date));
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;
        Date date = df.parse("Wed, 01 Mar 2022 14:30:45 GMT");
        assertEquals("2022-03-01 14:30:45.000", df.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;
        df.parse("2022-03-01T14:30:45"); // missing timezone
        fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;
        df.parse("Wed, 01 Mar 2022 14:30:45 GMT GMT"); // double timezone
        fail("Expected ParseException");
    }
}