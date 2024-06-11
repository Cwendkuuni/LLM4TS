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
    public void testFormatDateUTC() {
        Date date = new Date(0L);
        assertEquals("1970-01-01T00:00:00Z", ISO8601Utils.format(date));
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date(System.currentTimeMillis());
        assertEquals("" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date) + "",
                ISO8601Utils.format(date));
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date(System.currentTimeMillis());
        assertEquals("" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date) + "",
                ISO8601Utils.format(date, true, TimeZone.getDefault()));
    }

    @Test
    public void testParseDateUTC() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse("1970-01-01T00:00:00Z", pos);
        assertEquals(0L, date.getTime());
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseDateWithTimeZone() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse("2023-03-14T15:30:45.123+01:00", pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        calendar.setTime(date);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(3, calendar.get(Calendar.MONTH));
        assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(45, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
        assertEquals(1, calendar.get(Calendar.EXACT_DAY_OF_WEEK));
        assertEquals(15, calendar.get(Calendar.DAY_OF_WEEK));
        assertEquals(1, calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        assertEquals(3, calendar.get(Calendar.DAY_OF_YEAR));
        assertEquals(1, calendar.get(Calendar.WEEK_OF_YEAR));
        assertEquals(3, calendar.get(Calendar.WEEK_OF_MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_WEEK_IN_YEAR));
        assertEquals(1, calendar.get(Calendar.WEEK_YEAR));
        assertEquals(1, calendar.get(Calendar.YEAR_WEEK));
        assertEquals("Europe/Paris", TimeZone.getDefault().getDisplayName());
        assertEquals(0, pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse("2023-03-14T15:30:45", pos);
        fail();
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat2() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse("2023-03-14T15:30:45.123+01:00Z", pos);
        fail();
    }
}