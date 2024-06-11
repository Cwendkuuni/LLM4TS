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
    public void testParseISO8601WithZulu() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123Z";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(123, (int) date.getTime() % 1000);
    }

    @Test
    public void testParseISO8601WithTimezoneOffset() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123-05:00";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(123, (int) date.getTime() % 1000);
        assertEquals(-5 * 60 * 60 * 1000, date.getTimezoneOffset());
    }

    @Test
    public void testParseISO8601WithTimezoneName() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123+01:00";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(123, (int) date.getTime() % 1000);
        assertEquals(60 * 60 * 1000, date.getTimezoneOffset());
    }

    @Test
    public void testParsePlainDate() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "Sat, 31 Dec 2022 23:59:59 GMT";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(0, (int) date.getTime() % 1000);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-32";
        dateFormat.parse(dateString);
        fail("Expected ParseException but got: " + dateFormat);
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }
}