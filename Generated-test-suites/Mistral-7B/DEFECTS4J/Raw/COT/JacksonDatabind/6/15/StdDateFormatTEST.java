package com.example;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
        assertEquals(StdDateFormat.DEFAULT_LOCALE, stdDateFormat.getLocale());
        assertEquals(StdDateFormat.DEFAULT_TIMEZONE, stdDateFormat.getTimeZone());
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(dateFormat);
        assertSame(StdDateFormat.DATE_FORMAT_ISO8601, dateFormat);
    }

    @Test
    public void testGetISO8601Format() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        DateFormat dateFormat = StdDateFormat.getISO8601Format(timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(StdDateFormat.DATE_FORMAT_ISO8601, dateFormat);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        stdDateFormat.setTimeZone(timeZone);
        assertEquals(timeZone, stdDateFormat.getTimeZone());
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
        assertEquals(new Date(1671522399123L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date, new StringBuilder(), null);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.contains("Sat"));
        assertTrue(formattedDate.contains("01"));
        assertTrue(formattedDate.contains("JAN"));
        assertTrue(formattedDate.contains("2022"));
        assertTrue(formattedDate.contains("GMT"));
    }

    @Test
    public void testToString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: null, locale: java.util.Locale[US])",
            stdDateFormat.toString());
    }

    
}