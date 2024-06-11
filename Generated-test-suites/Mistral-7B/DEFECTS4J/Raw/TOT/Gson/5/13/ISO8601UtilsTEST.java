package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Test cases for ISO8601Utils class.
 */
public class ISO8601UtilsTest
{
    @Test
    public void testFormatDateWithDefaultTimeZone()
    {
        Date date = new GregorianCalendar(2022, Calendar.AUGUST, 17).getTime();
        String formatted = ISO8601Utils.format(date);
        String expected = "2022-08-17T00:00:00Z";
        assertEquals(expected, formatted);
    }

    @Test
    public void testFormatDateWithCustomTimeZone()
    {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new GregorianCalendar(2022, Calendar.AUGUST, 17, 13, 30, 45, 0, timeZone).getTime();
        String formatted = ISO8601Utils.format(date, true, timeZone);
        String expected = "2022-08-17T13:30:45.000+09:00";
        assertEquals(expected, formatted);
    }

    @Test
    public void testFormatDateWithInvalidMilliseconds()
    {
        Date date = new GregorianCalendar(2022, Calendar.AUGUST, 17).getTime();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        String expected = "2022-08-17T00:00:00.000Z";
        assertEquals(expected, formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithInvalidFormat()
    {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String input = "2022-08-17T13:30:45.000invalid";
        ISO8601Utils.parse(input, new ParsePosition(0));
    }

    @Test
    public void testParseDateWithDefaultTimeZone() throws ParseException
    {
        Date date = ISO8601Utils.parse("2022-08-17T00:00:00Z", new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        assertEquals(2022, year);
        assertEquals(8, month);
        assertEquals(17, day);
        assertEquals(0, hour);
        assertEquals(0, minute);
        assertEquals(0, second);
    }

    @Test
    public void testParseDateWithCustomTimeZone() throws ParseException
    {
        Date date = ISO8601Utils.parse("2022-08-17T13:30:45.000+09:00", new ParsePosition(0));
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        assertEquals(2022, year);
        assertEquals(8, month);
        assertEquals(17, day);
        assertEquals(13, hour);
        assertEquals(30, minute);
        assertEquals(45, second);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException
    {
        String dateString = "2022-08-17T13:30:45invalid";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but got nothing");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingTimeZone() throws ParseException
    {
        String dateString = "2022-08-17T13:30:45";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but got nothing");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimeZoneOffset() throws ParseException
    {
        String dateString = "2022-08-17T13:30:45.000+24:60";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but got nothing");
    }
}