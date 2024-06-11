package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        DateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        String dateString = "2022-12-31T23:59:59.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("2022-12-31 23:59:59.123", dateFormat.format(parsedDate));
    }

    @Test
    public void testParseISO8601WithoutTimeZone() throws ParseException {
        DateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-12-31T23:59:59.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        DateFormat dateFormatUTC = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        assertEquals("2022-12-31 15:59:59.123", dateFormatUTC.format(parsedDate));
        assertEquals("Sat Dec 31 15:59:59 GMT", dateFormat.format(parsedDate));
    }

    @Test
    public void testParsePlainDate() throws ParseException {
        DateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-12-31";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Sat Dec 31 00:00:00 GMT", dateFormat.format(parsedDate));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601() throws ParseException {
        DateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-12-31T23:59:59.123A";
        dateFormat.parse(dateString);
        fail("Expected ParseException but no exception was thrown");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormat() throws ParseException {
        DateFormat dateFormat = new StdDateFormat();
        String dateString = "01-Jan-2022";
        dateFormat.parse(dateString);
        fail("Expected ParseException but no exception was thrown");
    }

    @Test
    public void testFormatRFC1123() {
        DateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String expected = "Wed Apr 26 13:39:34 EDT 2023";
        String formattedDate = dateFormat.format(date);
        assertEquals(expected, formattedDate);
    }
}