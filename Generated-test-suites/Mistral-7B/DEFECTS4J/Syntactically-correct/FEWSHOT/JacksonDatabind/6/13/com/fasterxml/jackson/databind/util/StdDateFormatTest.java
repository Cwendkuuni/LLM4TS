package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
        assertTrue(stdDateFormat instanceof StdDateFormat);
        assertNull(stdDateFormat._timezone);
        assertNotNull(stdDateFormat._locale);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        StdDateFormat stdDateFormat = new StdDateFormat(utcTimeZone, Locale.US);
        assertNotNull(stdDateFormat);
        assertNotNull(stdDateFormat._timezone);
        assertEquals(utcTimeZone, stdDateFormat._timezone);
        assertEquals(Locale.US, stdDateFormat._locale);
    }

    @Test
    public void testWithLocale() {
        Locale frLocale = Locale.FRENCH;
        StdDateFormat stdDateFormat = new StdDateFormat(null, frLocale);
        assertNotNull(stdDateFormat);
        assertNull(stdDateFormat._timezone);
        assertEquals(frLocale, stdDateFormat._locale);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat format = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(format);
        assertTrue(format instanceof SimpleDateFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.getTimeZone());
    }

    @Test
    public void testGetISO8601Format() {
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        DateFormat format = StdDateFormat.getISO8601Format(utcTimeZone, Locale.US);
        assertNotNull(format);
        assertTrue(format instanceof SimpleDateFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(utcTimeZone, format.getTimeZone());
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.US);
        assertNotNull(format);
        assertTrue(format instanceof SimpleDateFormat);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.getTimeZone());
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone pdtTimeZone = TimeZone.getTimeZone("PDT");
        stdDateFormat.setTimeZone(pdtTimeZone);
        assertNotNull(stdDateFormat._timezone);
        assertEquals(pdtTimeZone, stdDateFormat._timezone);
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-03-15T14:30:15.123Z";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Wed Mar 15 14:30:15 PDT 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseWithInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "InvalidDate";
        try {
            stdDateFormat.parse(dateString);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-03-15T14:30:15.123+01:00";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Wed Mar 15 15:30:15 CET 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseWithoutTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-03-15T14:30:15.123";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Wed Mar 15 14:30:15 PDT 2022", stdDateFormat.format(date));
    }

    @Test
    public void testParseWithRFC1123Format() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "Wed, 15 Mar 2022 14:30:15 PDT";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals(dateString, stdDateFormat.format(date));
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date(1647327200000L);
        String dateString = stdDateFormat.format(date);
        assertEquals("Tue Aug 31 00:00:00 UTC 2022", dateString);
    }

    @Test
    public void testToString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String toString = stdDateFormat.toString();
        assertTrue(toString.contains("DateFormat"));
        assertTrue(toString.contains(StdDateFormat.class.getName()));
    }

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString1 = "2022-03-15T14:30:15.123Z";
        String dateString2 = "InvalidDate";
        boolean result1 = stdDateFormat.looksLikeISO8601(dateString1);
        assertTrue(result1);
        boolean result2 = stdDateFormat.looksLikeISO8601(dateString2);
        assertFalse(result2);
    }
}