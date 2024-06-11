package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testGetDefaultTimeZone() {
        assertEquals(TimeZone.getTimeZone("GMT"), StdDateFormat.getDefaultTimeZone());
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("PST");
        StdDateFormat newSdf = sdf.withTimeZone(newTimeZone);
        assertEquals(newTimeZone, newSdf.getTimeZone());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat sdf = new StdDateFormat();
        Locale newLocale = Locale.GERMAN;
        StdDateFormat newSdf = sdf.withLocale(newLocale);
        assertEquals(newLocale, newSdf.getLocale());
    }

    @Test
    public void testClone() {
        StdDateFormat sdf = new StdDateFormat();
        StdDateFormat cloneSdf = sdf.clone();
        assertNotSame(sdf, cloneSdf);
        assertEquals(sdf.getTimeZone(), cloneSdf.getTimeZone());
        assertEquals(sdf.getLocale(), cloneSdf.getLocale());
    }

    @Test
    public void testParseValidISO8601Date() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date expected = new Date(1551234567890L);
        String dateStr = "2019-02-26T15:46:07.890Z";
        Date result = sdf.parse(dateStr);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        String dateStr = "invalid-date";
        sdf.parse(dateStr);
    }

    @Test
    public void testFormatISO8601Date() {
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date(1551234567890L);
        StringBuffer sb = new StringBuffer();
        sdf.format(date, sb, null);
        assertEquals("2019-02-26T15:46:07.890Z", sb.toString());
    }

    // Test additional edge cases and exceptions for other methods can be added here

}