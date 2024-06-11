package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseWithGMT() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2023-03-11T12:34:56.789Z";
        Date date = dateFormat.parse(dateString);
        assertEquals(new Date(1678819046789L), date);
    }

    @Test
    public void testParseWithCustomTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);
        String dateString = "2023-03-11T12:34:56.789-04:00";
        Date date = dateFormat.parse(dateString);
        assertEquals(new Date(1678814646789L), date);
    }

    @Test
    public void testParseWithRFC1123Format() {
        StdDateFormat rfc1123Format = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("America/New_York"));
        String dateString = "Sat, 11 Mar 2023 12:34:56 EDT";
        Date date = rfc1123Format.parse(dateString);
        assertEquals(new Date(1678814646000L), date);
    }

    @Test
    public void testParseWithISO8601Format() {
        StdDateFormat iso8601Format = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("America/New_York"));
        String dateString = "2023-03-11T12:34:56.789-04:00";
        Date date = iso8601Format.parse(dateString);
        assertEquals(new Date(1678814646789L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "InvalidDate";
        Date date = dateFormat.parse(dateString);
        fail("Expected ParseException but got " + date);
    }
}