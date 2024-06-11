package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ISO8601UtilsTest {

    @Test
    public void testFormatUTCDate() {
        Date date = new GregorianCalendar(2022, Calendar.DECEMBER, 31).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2022-12-31T00:00:00Z", formatted);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new GregorianCalendar(2022, Calendar.DECEMBER, 31, 23, 59, 59, 999).getTime();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2022-12-31T23:59:59.999Z", formatted);
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        Date date = new GregorianCalendar(2022, Calendar.DECEMBER, 31).getTime();
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2022-12-31T05:00:00-05:00", formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithNullDate() {
        String formatted = ISO8601Utils.format(null);
    }

    @Test
    public void testParseUTCDate() {
        String dateString = "2022-12-31T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertNotNull(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testParseDateWithMilliseconds() {
        String dateString = "2022-12-31T23:59:59.999Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertNotNull(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(999, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testParseDateWithTimeZone() {
        String dateString = "2022-12-31T05:00:00-05:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertNotNull(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(5, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(-1 * (5 * 60 + 0), calendar.get(Calendar.MILLISECOND));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() {
        String dateString = "2022-12-31T23:59:59ZZ";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidZoneOffset() {
        String dateString = "2022-12-31T05:00:00-XX:XX";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithNullString() {
        String dateString = null;
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}