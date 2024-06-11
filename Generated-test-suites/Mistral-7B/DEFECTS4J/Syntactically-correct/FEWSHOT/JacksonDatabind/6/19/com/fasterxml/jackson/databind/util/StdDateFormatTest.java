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
    public void testParseISO8601Format() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        DateFormat dateFormat = stdDateFormat.getISO8601Format(null, null);

        // Test parsing a valid ISO-8601 date string
        String dateString = "2023-03-15T14:25:36.789Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals("Thu Mar 16 14:25:36 GMT+00:00 2023", parsedDate.toString());

        // Test parsing an ISO-8601 date string with timezone offset
        dateString = "2023-03-15T14:25:36.789-05:00";
        parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals("Wed Mar 15 19:25:36 EDT 2023", parsedDate.toString());
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        DateFormat dateFormat = stdDateFormat.getRFC1123Format(null, null);

        // Test parsing a valid RFC-1123 date string
        String dateString = "Wed, 15 Mar 2023 14:25:36 GMT";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals("Wed Mar 15 14:25:36 GMT 2023", parsedDate.toString());
    }

    @Test
    public void testParseInvalidDateString() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        DateFormat dateFormat = stdDateFormat.getISO8601Format(null, null);

        // Test parsing an invalid date string
        String dateString = "not-a-date";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            dateFormat.parse(dateString, parsePosition);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Can not parse date \"not-a-date\": not compatible with any of standard forms ([yyyy-MM-dd'T'HH:mm:ss.SSSZ, yyyy-MM-dd, EEE, dd MMM yyyy HH:mm:ss zzz])", e.getMessage());
        }
    }
}