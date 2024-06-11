package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        DateFormat dateFormat = stdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        String dateString = "2023-03-31T12:34:56.789+02:00";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertEquals("Sat Mar 31 14:34:56 CET 2023", dateFormat.format(parsedDate));
    }

    @Test
    public void testParseISO8601Plain() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        DateFormat dateFormat = stdDateFormat.getISO8601Format(null, Locale.US);
        String dateString = "2023-03-31T12:34:56.789Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertEquals("Sat Mar 31 12:34:56.789 UTC 2023", dateFormat.format(parsedDate));
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        DateFormat dateFormat = stdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.US);
        String dateString = "Fri, 31 Mar 2023 12:34:56 +0200";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertEquals("Fri Mar 31 14:34:56 CET 2023", dateFormat.format(parsedDate));
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        DateFormat dateFormat = stdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        String dateString = "InvalidFormat";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            dateFormat.parse(dateString, parsePosition);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        DateFormat dateFormat = stdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("2023-03-31T13:45:23.123Z", formattedDate);
    }
}