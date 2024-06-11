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
    public void testParseISO8601WithTimezone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        DateFormat dateFormatISO8601Z = stdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        String isoDateWithTimezone = "2023-03-23T14:30:45.123Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = dateFormatISO8601Z.parse(isoDateWithTimezone, parsePosition);
        assertEquals("Thu Mar 23 14:30:45 UTC 2023", date.toString());
    }

    @Test
    public void testParseISO8601WithoutTimezone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        DateFormat dateFormatISO8601 = stdDateFormat.getBlueprintISO8601Format();
        String isoDate = "2023-03-23T14:30:45.123";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = dateFormatISO8601.parse(isoDate, parsePosition);
        assertEquals("Thu Mar 23 14:30:45 UTC 2023", date.toString());
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        DateFormat dateFormatRFC1123 = stdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.US);
        String rfc1123Date = "Thu, 23 Mar 2023 14:30:45 UTC";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = dateFormatRFC1123.parse(rfc1123Date, parsePosition);
        assertEquals("Thu Mar 23 14:30:45 UTC 2023", date.toString());
    }

    @Test
    public void testParsePlainDate() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        DateFormat dateFormatPlain = stdDateFormat.getBlueprintFormat();
        String plainDate = "2023-03-23";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = dateFormatPlain.parse(plainDate, parsePosition);
        assertEquals("Thu Mar 23 00:00:00 UTC 2023", date.toString());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        DateFormat dateFormatISO8601 = stdDateFormat.getBlueprintISO8601Format();
        String invalidDate = "2023-33-23T14:30:45.123";
        ParsePosition parsePosition = new ParsePosition(0);
        dateFormatISO8601.parse(invalidDate, parsePosition);
        fail("Expected ParseException but got: " + parsePosition.getErrorIndex());
    }
}