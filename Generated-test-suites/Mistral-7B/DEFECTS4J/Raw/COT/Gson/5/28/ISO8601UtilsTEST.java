package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatNormalCase() {
        Date date = new GregorianCalendar(2022, 11, 30, 15, 30, 45, 123).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2022-12-30T15:30:45.123Z", formatted);
    }

    @Test
    public void testFormatWithoutMilliseconds() {
        Date date = new GregorianCalendar(2022, 11, 30, 15, 30, 45).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2022-12-30T15:30:45Z", formatted);
    }

    @Test
    public void testFormatWithInvalidTimezoneId() {
        Date date = new GregorianCalendar(2022, 11, 30, 15, 30, 45, 123).getTime();
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8601Utils.format(date, true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'X'").getTimeZone());
        });
    }

    @Test
    public void testParseNormalCase() throws ParseException {
        String dateString = "2022-12-30T15:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(45, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testParseWithoutMilliseconds() throws ParseException {
        String dateString = "2022-12-30T15:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testParseWithInvalidTimezoneIndicator() throws ParseException {
        String dateString = "2022-12-30T15:30:45+0001";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateString, pos);
        });
    }

    @Test
    public void testParseWithTimezoneOffsetTooLong() throws ParseException {
        String dateString = "2022-12-30T15:30:45+1440:00";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateString, pos);
        });
    }

    @Test
    public void testParseWithTimezoneOffsetInvalidFormat() throws ParseException {
        String dateString = "2022-12-30T15:30:45+14:60";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateString, pos);
        });
    }

    @Test
    public void testParseWithTimezoneOffsetNegative() throws ParseException {
        String dateString = "2022-12-30T15:30:45-05:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(45, calendar.get(Calendar.SECOND));
        assertEquals(-05, calendar.get(Calendar.EXTRA));
    }
}