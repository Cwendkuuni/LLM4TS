package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for StdDateFormat
 */
public class StdDateFormatTest {

    private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String ISO8601_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String RFC1123_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";

    @Test
    public void testParseISO8601DateFormat() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.getInstance();
        Date date = stdDateFormat.parse(ISO8601_FORMAT, new ParsePosition(0));
        assertEquals("2022-12-31T23:59:59.999+0000", stdDateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601DateFormat() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.getInstance();
        stdDateFormat.parse("InvalidDateString", new ParsePosition(0));
    }

    @Test
    public void testParseRFC1123DateFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        stdDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = stdDateFormat.parse("Fri, 31 Dec 2022 23:59:59 GMT", new ParsePosition(0));
        assertEquals("Fri Dec 31 23:59:59 UTC 2022", stdDateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123DateFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        stdDateFormat.parse("InvalidDateString", new ParsePosition(0));
    }

    @Test
    public void testParseDateFormatPlain() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        Date date = stdDateFormat.parse("2022-12-31", new ParsePosition(0));
        assertEquals("Fri Dec 31 00:00:00 UTC 2022", stdDateFormat.format(date));
    }

    @Test
    public void testFormatDateFormatISO8601() {
        StdDateFormat stdDateFormat = StdDateFormat.getInstance();
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        assertTrue(formattedDate.matches(ISO8601_FORMAT));
    }

    @Test
    public void testFormatDateFormatISO8601Z() {
        StdDateFormat stdDateFormat = StdDateFormat.getInstance();
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        assertTrue(formattedDate.matches(ISO8601_FORMAT_Z));
    }

    @Test
    public void testFormatDateFormatRFC1123() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        assertTrue(formattedDate.matches(RFC1123_FORMAT));
    }

    @Test
    public void testFormatDateFormatPlain() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        assertTrue(formattedDate.matches(DATE_FORMAT_STR_PLAIN));
    }

    @Test
    public void testClone() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        StdDateFormat clone = stdDateFormat.clone();
        assertNotSame(stdDateFormat, clone);
        assertEquals(stdDateFormat.getClass(), clone.getClass());
        assertEquals(stdDateFormat.getLocale(), clone.getLocale());
        assertEquals(stdDateFormat.getTimeZone(), clone.getTimeZone());
    }
}