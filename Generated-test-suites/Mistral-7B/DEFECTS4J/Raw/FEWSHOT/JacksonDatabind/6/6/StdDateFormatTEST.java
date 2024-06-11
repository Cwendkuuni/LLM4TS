package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601Format() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("2023-01-01T12:34:56.789Z");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(34, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(789, date.getMilliseconds());
        assertEquals(0, date.getTimeZone().getOffset(System.currentTimeMillis()));
    }

    @Test
    public void testParseISO8601FormatWithTimezone() throws ParseException {
        StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("UTC+2"), Locale.US);
        Date date = sdf.parse("2023-01-01T12:34:56.789Z");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(34, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(789, date.getMilliseconds());
        assertEquals(7200000, date.getTimeZone().getOffset(System.currentTimeMillis()));
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("Tue, 01 Jan 2023 12:34:56 GMT");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(34, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
        assertEquals(0, date.getTimeZone().getOffset(System.currentTimeMillis()));
    }

    @Test
    public void testParsePlainFormat() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("2023-01-01");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(0, date.getHours());
        assertEquals(0, date.getMinutes());
        assertEquals(0, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
        assertEquals(0, date.getTimeZone().getOffset(System.currentTimeMillis()));
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        String invalidDate = "InvalidDate";
        try {
            sdf.parse(invalidDate);
            assertFalse(false);
        } catch (ParseException e) {
            assertEquals("Can not parse date \"" + invalidDate + "\": not compatible with any of standard forms (\"[yyyy-MM-dd'T'HH:mm:ss.SSSZ]\", \"EEE, dd MMM yyyy HH:mm:ss zzz\", \"cccc, dddd MMMM d, yyyy\")", e.getMessage());
        }
    }

    @Test
    public void testParseWithLeadingZero() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("2023-01-01T12:03:56.789Z");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(3, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(789, date.getMilliseconds());
        assertEquals(0, date.getTimeZone().getOffset(System.currentTimeMillis()));
    }

    @Test
    public void testParseWithTrailingZero() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("2023-01-01T12:34:56.000Z");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(34, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
        assertEquals(0, date.getTimeZone().getOffset(System.currentTimeMillis()));
    }

    @Test
    public void testFormatISO8601Format() {
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date(1672492800000L); // 2023-01-01 12:00:00 UTC
        String formattedDate = sdf.format(date);
        assertEquals("2023-01-01T12:00:00Z", formattedDate);
    }

    @Test
    public void testFormatRFC1123Format() {
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date(1672492800000L); // 2023-01-01 12:00:00 UTC
        String formattedDate = sdf.format(date);
        assertEquals("Tue, 01 Jan 2023 12:00:00 GMT", formattedDate);
    }

    @Test
    public void testFormatPlainFormat() {
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date(1672492800000L); // 2023-01-01 12:00:00 UTC
        String formattedDate = sdf.format(date);
        assertEquals("2023-01-01", formattedDate);
    }

    @Test
    public void testFormatWithTimeZone() {
        StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("UTC+2"), Locale.US);
        Date date = new Date(1672635200000L); // 2023-01-01 15:00:00 CET
        String formattedDate = sdf.format(date);
        assertEquals("2023-01-01T15:00:00+02:00", formattedDate);
    }
}