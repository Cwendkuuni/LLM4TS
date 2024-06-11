package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_DateWithTimeZone() {
        Date date = new Date(System.currentTimeMillis());
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/London"));
        assertNotNull(formattedDate);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZZ", formattedDate.length(), formattedDate.substring(0, 17));
        assertTrue(formattedDate.endsWith("+01:00")); // London timezone
    }

    @Test
    public void testFormat_DateWithoutTimeZone() {
        Date date = new Date(System.currentTimeMillis());
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", formattedDate.length(), formattedDate.substring(0, 16));
        assertTrue(formattedDate.endsWith("Z"));
    }

    @Test
    public void testFormat_DateWithMilliseconds() {
        Date date = new Date(System.currentTimeMillis() + 123);
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertNotNull(formattedDate);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZZ", formattedDate.length(), formattedDate.substring(0, 18));
        assertTrue(formattedDate.endsWith("Z"));
    }

    @Test
    public void testFormat_DateWithoutMilliseconds() {
        Date date = new Date(System.currentTimeMillis());
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
        assertNotNull(formattedDate);
        assertEquals("yyyy-MM-dd'T'HH:mm:ssZ", formattedDate.length(), formattedDate.substring(0, 15));
        assertTrue(formattedDate.endsWith("Z"));
    }

    @Test
    public void testParse_DateWithoutTimeZone() throws ParseException {
        String isoDate = "2023-03-20T14:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        int expectedYear = calendar.get(Calendar.YEAR);
        int expectedMonth = calendar.get(Calendar.MONTH) + 1;
        int expectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int expectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int expectedMinute = calendar.get(Calendar.MINUTE);
        int expectedSecond = calendar.get(Calendar.SECOND);
        int expectedMillisecond = calendar.get(Calendar.MILLISECOND);

        assertEquals(expectedYear, 2023);
        assertEquals(expectedMonth, 3);
        assertEquals(expectedDay, 20);
        assertEquals(expectedHour, 14);
        assertEquals(expectedMinute, 30);
        assertEquals(expectedSecond, 45);
        assertEquals(expectedMillisecond, 0);

        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_DateWithTimeZone() throws ParseException {
        String isoDate = "2023-03-20T14:30:45+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        int expectedYear = calendar.get(Calendar.YEAR);
        int expectedMonth = calendar.get(Calendar.MONTH) + 1;
        int expectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int expectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int expectedMinute = calendar.get(Calendar.MINUTE);
        int expectedSecond = calendar.get(Calendar.SECOND);
        int expectedMillisecond = calendar.get(Calendar.MILLISECOND);
        TimeZone timeZone = calendar.getTimeZone();

        assertEquals(expectedYear, 2023);
        assertEquals(expectedMonth, 3);
        assertEquals(expectedDay, 20);
        assertEquals(expectedHour, 15); // convert from UTC+1:00 to UTC
        assertEquals(expectedMinute, 30);
        assertEquals(expectedSecond, 45);
        assertEquals(expectedMillisecond, 0);
        assertEquals("Europe/London", timeZone.getID());

        assertEquals(isoDate.length(), pos.getIndex() + parsedDate.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidDateFormat() throws ParseException {
        String isoDate = "2023-03-20T14:30:45A";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, pos);
    }
}