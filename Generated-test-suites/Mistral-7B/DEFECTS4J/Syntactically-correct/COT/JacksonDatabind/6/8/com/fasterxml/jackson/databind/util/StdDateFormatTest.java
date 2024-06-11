package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat format = StdDateFormat.instance;
        assertEquals(StdDateFormat.DEFAULT_TIMEZONE, format.getTimeZone());
        assertEquals(StdDateFormat.DEFAULT_LOCALE, format.getLocale());
    }

    @Test
    public void testParseISO8601WithGMT() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date date = format.parse("2022-03-01T14:30:45.123Z");
        assertEquals(new Date(1646133045000L), date);
    }

    @Test
    public void testParseISO8601WithCustomTimezone() throws ParseException {
        StdDateFormat format = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), StdDateFormat.DEFAULT_LOCALE);
        Date date = format.parse("2022-03-01T14:30:45.123Z");
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 2, 1, 15, 30, 45, 123, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(expectedCalendar.getTime(), date);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date date = format.parse("Wed, 01 Mar 2022 14:30:45 EST");
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 2, 1, 14, 30, 45, 0, TimeZone.getTimeZone("America/New_York"));
        assertEquals(expectedCalendar.getTime(), date);
    }

    @Test
    public void testParsePlainDate() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date date = format.parse("2022-03-01");
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 2, 1);
        assertEquals(expectedCalendar.getTime(), date);
    }

    @Test
    public void testParseInvalidDate() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        assertThrows(ParseException.class, () -> format.parse("XXX"));
    }

    @Test
    public void testFormat() {
        StdDateFormat format = new StdDateFormat();
        StringBuilder stringBuilder = new StringBuilder();
        format.format(new Date(1646133045000L), stringBuilder, new FieldPosition(0, ""));
        assertEquals("Wed Mar 01 14:30:45 GMT+00:00 2022", stringBuilder.toString());
    }

    @Test
    public void testGetISO8601Format() {
        StdDateFormat format = StdDateFormat.instance;
        DateFormat df = format.getISO8601Format();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", df.toPattern());
    }

    @Test
    public void testGetRFC1123Format() {
        StdDateFormat format = StdDateFormat.instance;
        DateFormat df = format.getRFC1123Format();
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", df.toPattern());
    }

    @Test
    public void testGetPlainFormat() {
        StdDateFormat format = StdDateFormat.instance;
        DateFormat df = format.getPlainFormat();
        assertEquals("yyyy-MM-dd", df.toPattern());
    }

    @Test
    public void testClone() {
        StdDateFormat original = new StdDateFormat();
        StdDateFormat cloned = original.clone();
        assertEquals(original.getLocale(), cloned.getLocale());
        assertEquals(original.getTimeZone(), cloned.getTimeZone());
        assertNull(cloned._formatRFC1123);
        assertNull(cloned._formatISO8601);
        assertNull(cloned._formatISO8601_z);
        assertNull(cloned._formatPlain);
    }
}