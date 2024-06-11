package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithDefaultTimeZoneAndNoMillis() {
        Date date = new GregorianCalendar(2022, 11, 1, 12, 34).getTime();
        String expected = "2022-12-01T12:34:00Z";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDateWithDefaultTimeZoneAndMillis() {
        Date date = new GregorianCalendar(2022, 11, 1, 12, 34, 56).getTime();
        String expected = "2022-12-01T12:34:56.056Z";
        String result = ISO8601Utils.format(date, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDateWithCustomTimeZoneAndNoMillis() {
        Date date = new GregorianCalendar(2022, 11, 1, 12, 34).getTime();
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        String expected = "2022-12-01T19:34:00-08:00";
        String result = ISO8601Utils.format(date, null, timeZone);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDateWithCustomTimeZoneAndMillis() {
        Date date = new GregorianCalendar(2022, 11, 1, 12, 34, 56).getTime();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        String expected = "2022-12-01T22:34:56.056+01:00";
        String result = ISO8601Utils.format(date, true, timeZone);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithInvalidFormat() {
        String dateString = "not_a_date";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("An IllegalArgumentException should have been thrown");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidYear() {
        String dateString = "1900-01-01T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ParsePosition expectedPos = new ParsePosition(10);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(expectedPos.getIndex(), exception.getErrorOffset());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMonth() {
        String dateString = "2022-13-01T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid month: 13", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidDay() {
        String dateString = "2022-02-31T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid day of month: 31", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidHour() {
        String dateString = "2022-12-01T24:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid hour: 24", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMinute() {
        String dateString = "2022-12-01T12:60:56Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid minute: 60", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidSecond() {
        String dateString = "2022-12-01T12:34:60Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid second: 60", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMillisecond() {
        String dateString = "2022-12-01T12:34:56.10001Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid millisecond: 10001", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingTimeZoneIndicator() {
        String dateString = "2022-12-01T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("No time zone indicator", exception.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimeZoneIndicator() {
        String dateString = "2022-12-01T12:34:56abcZ";
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Invalid time zone indicator 'a'", exception.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithNullDate() {
        String expected = null;
        String result = ISO8601Utils.format(null);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithNullTimeZone() {
        Date date = new GregorianCalendar(2022, 11, 1, 12, 34, 56).getTime();
        String expected = null;
        String result = ISO8601Utils.format(date, true, null);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithNullString() {
        String dateString = null;
        ParsePosition pos = new ParsePosition(0);
        ParseException exception = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals("Date string is null", exception.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testParseDateWithNullParsePosition() {
        String dateString = "2022-12-01T12:34:56Z";
        ParsePosition pos = null;
        assertNull(ISO8601Utils.parse(dateString, pos));
    }

}