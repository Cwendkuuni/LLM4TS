package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat instance = StdDateFormat.instance;
        assertEquals(Locale.US, instance.getLocale());
        assertSame(TimeZone.getDefault(), instance.getTimeZone());
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals(Locale.US, format.getLocale());
        assertSame(TimeZone.getDefault(), format.getTimeZone());
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat format = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals(Locale.US, format.getLocale());
        assertSame(TimeZone.getTimeZone("UTC"), format.getTimeZone());
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals(Locale.US, format.getLocale());
        assertSame(TimeZone.getTimeZone("UTC"), format.getTimeZone());
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat format = new StdDateFormat();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(Locale.US, format.getLocale());
        assertSame(TimeZone.getTimeZone("UTC"), format.getTimeZone());
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date date = format.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(123, date.getMilliseconds());

        date = format.parse("2022-12-31");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(0, date.getHours());
        assertEquals(0, date.getMinutes());
        assertEquals(0, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        format.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        Date date = format.parse("2022-12-31T23:59:59.123+01:00");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(123, date.getMilliseconds());
        assertEquals(TimeZone.getTimeZone("UTC+1"), date.getTimeZone());
    }

    @Test
    public void testFormat() {
        StdDateFormat format = new StdDateFormat();
        String dateString = format.format(new Date(1672492800000L));
        assertEquals("Fri Dec 31 00:00:00 GMT", dateString);
    }

    @Test
    public void testFormatWithTimeZone() {
        StdDateFormat format = new StdDateFormat();
        format.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        String dateString = format.format(new Date(1672492800000L));
        assertEquals("Sat Jan 01 01:00:00 CET", dateString);
    }

    @Test
    public void testToString() {
        StdDateFormat format = new StdDateFormat();
        String str = format.toString();
        assertTrue(str.startsWith("DateFormat StdDateFormat"));
    }

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat format = new StdDateFormat();
        assertTrue(format.looksLikeISO8601("2022-12-31T23:59:59.123Z"));
        assertFalse(format.looksLikeISO8601("2022-12-31"));
    }

    @Test
    public void testParseAsISO8601() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date date = format.parseAsISO8601("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(123, date.getMilliseconds());

        date = format.parseAsISO8601("2022-12-31");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(0, date.getHours());
        assertEquals(0, date.getMinutes());
        assertEquals(0, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
    }

    @Test
    public void testParseAsRFC1123() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date date = format.parseAsRFC1123("Fri, 31 Dec 2022 00:00:00 GMT");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(0, date.getHours());
        assertEquals(0, date.getMinutes());
        assertEquals(0, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
    }
}