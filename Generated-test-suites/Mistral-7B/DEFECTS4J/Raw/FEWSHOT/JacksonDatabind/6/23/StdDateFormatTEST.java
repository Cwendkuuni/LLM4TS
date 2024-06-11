package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithMillis() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Sat Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithoutMillis() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31T23:59:59Z");
        assertEquals("Sat Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimezoneOffset() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31T23:59:59-05:00");
        assertEquals("Sat Dec 31 21:59:59 GMT-05:00", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimezoneId() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31T23:59:59+01:00");
        assertEquals("Sun Dec 31 00:59:59 GMT+01:00", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Sat Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test
    public void testParsePlainDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31");
        assertEquals("Sat Dec 31 00:00:00 GMT", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("Invalid date");
    }
}