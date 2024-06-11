
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
    public void testFormat_DateWithNoTimeComponent_ShouldReturnCorrectISOFormat() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime();
        String expected = "2023-01-01";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_DateWithTimeComponent_ShouldReturnCorrectISOFormat() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2023-01-01T12:30:45";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_DateWithTimeZone_ShouldReturnCorrectISOFormat() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 1, 12, 30, 45, 123, TimeZone.getTimeZone("America/Los_Angeles")).getTime();
        String expected = "2023-01-01T12:30:45-07:00";
        String actual = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormat_NullDate_ShouldThrowIllegalArgumentException() {
        String result = ISO8601Utils.format(null);
    }

    @Test
    public void testParse_ValidISOFormatWithTimeComponent_ShouldReturnCorrectDate() throws ParseException {
        String dateString = "2023-01-01T12:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int expectedYear = 2023;
        int expectedMonth = 1;
        int expectedDay = 1;
        int expectedHour = 12;
        int expectedMinute = 30;
        int expectedSecond = 45;

        assertEquals(expectedYear, calendar.get(Calendar.YEAR));
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH) + 1);
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedHour, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, calendar.get(Calendar.MINUTE));
        assertEquals(expectedSecond, calendar.get(Calendar.SECOND));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidISOFormat_ShouldThrowParseException() throws ParseException {
        String dateString = "2023-01-01A";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException should be thrown");
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidISOFormatWithTimeComponent_ShouldThrowParseException() throws ParseException {
        String dateString = "2023-01-01T12:30:45ABC";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException should be thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullString_ShouldThrowIllegalArgumentException() {
        String result = ISO8601Utils.parse(null, new ParsePosition(0));
    }
}