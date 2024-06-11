package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {
    

    @Test
    public void testFormat_DateWithDefaultTimezone() {
        Date date = new Date(1516748420000L); // 2018-01-12 10:00:00
        String formatted = ISO8601Utils.format(date);
        String expected = "2018-01-12T10:00:00Z";
        assertEquals(expected, formatted);
    }

    @Test
    public void testFormat_DateWithMilliseconds() {
        Date date = new Date(1516748420012L); // 2018-01-12 10:00:00.012
        String formatted = ISO8601Utils.format(date, true, TimeZone.getDefault());
        String expected = "2018-01-12T10:00:00.012Z";
        assertEquals(expected, formatted);
    }

    @Test
    public void testFormat_DateWithCustomTimezone() {
        Date date = new Date(1516748420000L); // 2018-01-12 10:00:00
        String timezoneID = "America/Los_Angeles";
        TimeZone tz = TimeZone.getTimeZone(timezoneID);
        String formatted = ISO8601Utils.format(date, false, tz);
        String expected = "2018-01-12T16:00:00-08:00";
        assertEquals(expected, formatted);
    }

    @Test
    public void testFormat_DateWithInvalidTimezoneID() {
        Date date = new Date(1516748420000L); // 2018-01-12 10:00:00
        String timezoneID = "InvalidTimezone";
        TimeZone tz = TimeZone.getTimeZone(timezoneID);
        String formatted = ISO8601Utils.format(date, false, tz);
        assertNull(formatted);
        assertEquals("Invalid timezone ID: InvalidTimezone", tz.getID());
    }

    @Test
    public void testParse_ValidDateString() throws ParseException {
        Date date = ISO8601Utils.parse("2018-01-12T10:00:00Z", new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int expectedYear = 2018;
        int expectedMonth = 1;
        int expectedDay = 12;
        int expectedHour = 10;
        int expectedMinute = 0;
        int expectedSecond = 0;
        int expectedMillisecond = 0;
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
        assertEquals(expectedMillisecond, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testParse_DateStringWithTimezoneOffset() throws ParseException {
        Date date = ISO8601Utils.parse("2018-01-12T10:00:00-08:00", new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int expectedYear = 2018;
        int expectedMonth = 1;
        int expectedDay = 12;
        int expectedHour = 10;
        int expectedMinute = 0;
        int expectedSecond = 0;
        int expectedMillisecond = 0;
        int expectedOffsetHours = -8;
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
        assertEquals(expectedMillisecond, calendar.get(Calendar.MILLISECOND));
        assertEquals(expectedOffsetHours, calendar.get(Calendar.EXTRA));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDateString() throws ParseException {
        Date date = ISO8601Utils.parse("2018-01-12T10:00:00XXX", new ParsePosition(0));
        fail("Should throw ParseException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidTimezoneID() throws ParseException {
        Date date = new Date();
        String timezoneID = "InvalidTimezone";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date.toString(), pos);
        String message = pos.getErrorMessage();
        String expectedMessage = "Invalid time zone indicator 'X'";
        assertEquals(expectedMessage, message);
        throw new IllegalArgumentException(message);
    }

    
}