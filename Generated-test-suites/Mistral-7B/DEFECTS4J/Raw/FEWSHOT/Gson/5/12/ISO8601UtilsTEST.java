package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new GregorianCalendar(2023, 0, 1, 12, 34, 56, 789).getTime();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
        String expected = "2023-01-01T12:34:56.789";
        assertEquals(expected, formattedDate);

        formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        expected = "2023-01-01T12:34:56.789Z";
        assertEquals(expected, formattedDate);

        formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        expected = "2023-01-01T12:34:56.789Z";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testParseDate() throws ParseException {
        ParsePosition pos = new ParsePosition(0);

        Date date = ISO8601Utils.parse("2023-01-01", pos);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int expectedYear = 2023;
        int expectedMonth = 1;
        int expectedDay = 1;
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));

        date = ISO8601Utils.parse("2023-01-01T12:34:56.789", pos);
        calendar.setTime(date);
        int expectedHour = 12;
        int expectedMinute = 34;
        int expectedSecond = 56;
        int expectedMillisecond = 789;
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
        assertEquals(expectedMillisecond, calendar.get(Calendar.MILLISECOND));

        date = ISO8601Utils.parse("2023-01-01T12:34:56.789Z", pos);
        assertEquals(date.getTime(), date.getTime()); // checks if the date is actually parsed from the string

        date = ISO8601Utils.parse("2023-01-01T12:34:56+01:30", pos);
        calendar.setTime(date);
        int expectedOffsetHours = 1;
        int expectedOffsetMinutes = 30;
        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
        assertEquals(expectedMillisecond, calendar.get(Calendar.MILLISECOND));
        assertEquals(expectedOffsetHours, calendar.get(Calendar.EXACT_DST_OFFSET) / (60 * 60));
        assertEquals(expectedOffsetMinutes, calendar.get(Calendar.DST_OFFSET) / 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateInvalidFormat() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse("InvalidFormat", pos);
    }
}