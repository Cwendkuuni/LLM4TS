package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
        assertEquals(Locale.US, stdDateFormat.getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), stdDateFormat.getTimeZone());
    }

    @Test
    public void testParseISO8601FormatWithDefaultTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2023-03-20T14:30:45.123Z");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601FormatWithInvalidDate() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("InvalidDate");
    }

    @Test
    public void testParseISO8601FormatWithCustomTimeZone() throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat stdDateFormat = new StdDateFormat(timeZone, Locale.US);
        Date date = stdDateFormat.parse("2023-03-20T14:30:45.123Z");
        Instant instant = date.toInstant();
        Long expectedUtcMilliseconds = 1679229445123L;
        Long actualUtcMilliseconds = instant.toEpochMilli();
        assertEquals(expectedUtcMilliseconds, actualUtcMilliseconds);
    }

    @Test
    public void testParseRFC1123FormatWithDefaultTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Monday, 20-Mar-23 14:30:45 GMT");
        assertNotNull(date);
    }

    @Test
    public void testFormatWithDefaultTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = stdDateFormat.format(new Date(1679229445123L), new StringBuilder(), null);
        assertEquals("2023-03-20T14:30:45.123Z", dateString);
    }

    @Test
    public void testFormatWithCustomTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat stdDateFormat = new StdDateFormat(timeZone, Locale.US);
        String dateString = stdDateFormat.format(new Date(1679229445123L), new StringBuilder(), null);
        assertEquals("2023-03-20T21:30:45.123-07:00", dateString);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone initialTimeZone = stdDateFormat.getTimeZone();
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        stdDateFormat.setTimeZone(customTimeZone);
        assertEquals(customTimeZone, stdDateFormat.getTimeZone());
    }

    @Test
    public void testSetLocale() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Locale initialLocale = stdDateFormat.getLocale();
        Locale customLocale = Locale.FRENCH;
        stdDateFormat.setLocale(customLocale);
        assertEquals(customLocale, stdDateFormat.getLocale());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        StdDateFormat clonedStdDateFormat = (StdDateFormat) stdDateFormat.clone();
        assertNotSame(stdDateFormat, clonedStdDateFormat);
        assertEquals(stdDateFormat.getLocale(), clonedStdDateFormat.getLocale());
        assertEquals(stdDateFormat.getTimeZone(), clonedStdDateFormat.getTimeZone());
    }
}