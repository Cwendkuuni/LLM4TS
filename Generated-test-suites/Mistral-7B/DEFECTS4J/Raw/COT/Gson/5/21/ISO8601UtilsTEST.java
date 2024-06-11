package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date(); // current date
        String expected = ISO8601Utils.format(date);
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date(); // current date
        boolean millis = true;
        String expected = ISO8601Utils.format(date, millis);
        String actual = ISO8601Utils.format(date, millis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date(); // current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        TimeZone timeZone = calendar.getTimeZone();
        String expected = ISO8601Utils.format(date, false, timeZone);
        String actual = ISO8601Utils.format(date, false, timeZone);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateInvalidFormat1() {
        String dateString = "2022-12-32";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected IllegalArgumentException for date format: " + dateString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateInvalidFormat2() {
        String dateString = "2022-12-31-32";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected IllegalArgumentException for date format: " + dateString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateInvalidFormat3() {
        String dateString = "2022-12-31T00:60:00Z"; // invalid total hours in timezone indicator
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected IllegalArgumentException for date format: " + dateString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateInvalidFormat4() {
        String dateString = "2022-12-31T00:00:00+3:60"; // invalid total hours and minutes in timezone indicator
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected IllegalArgumentException for date format: " + dateString);
    }

    @Test(expected = ParseException.class)
    public void testParseDateParseException() throws ParseException {
        String dateString = "2022-12-31T23:59:60"; // date out of range
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException for date format: " + dateString);
    }
}