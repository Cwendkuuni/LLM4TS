package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithMilliseconds() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01T12:34:56.123Z");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(34, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(123, date.getMilliseconds());
    }

    @Test
    public void testParseISO8601WithoutMilliseconds() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01T12:34:56Z");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(34, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
    }

    @Test
    public void testParseISO8601WithTimezoneOffset() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01T12:34:56-01:00");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(12, date.getHours());
        assertEquals(33, date.getMinutes()); // Daylight saving time adjustment
        assertEquals(56, date.getSeconds());
        assertEquals(0, date.getMilliseconds());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidFormat");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormatWithMilliseconds() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidFormatWithMilliseconds.123");
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertNotNull(parsedDate);
        assertEquals(date.getTime(), parsedDate.getTime());
    }

    @Test
    public void testFormatWithTimezone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
        dateFormat.setTimeZone(timezone);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertNotNull(parsedDate);
        assertEquals(date.getTime(), parsedDate.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeZoneWithNull() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFormatWithNullDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String formattedDate = dateFormat.format(null);
    }

    @Test
    public void testClone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        Date date = new Date();
        String formattedDate = clonedDateFormat.format(date);
        assertNotNull(formattedDate);
        Date parsedDate = clonedDateFormat.parse(formattedDate);
        assertNotNull(parsedDate);
        assertEquals(date.getTime(), parsedDate.getTime());
        assertEquals(dateFormat.hashCode(), clonedDateFormat.hashCode());
    }
}