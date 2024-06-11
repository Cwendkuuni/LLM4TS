package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

    @Test
    public void testFormat() {
        Date date = new Date();
        String expected = ISO8601Utils.format(date, false, TIMEZONE_UTC);
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String expected = ISO8601Utils.format(date, true, TIMEZONE_UTC);
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCustomTimeZone() {
        Date date = new Date();
        TimeZone customTimeZone = TimeZone.getTimeZone("GMT+3");
        String expected = ISO8601Utils.format(date, false, customTimeZone);
        String actual = ISO8601Utils.format(date, false, customTimeZone);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCustomTimeZoneAndMillis() {
        Date date = new Date();
        TimeZone customTimeZone = TimeZone.getTimeZone("GMT+3");
        String expected = ISO8601Utils.format(date, true, customTimeZone);
        String actual = ISO8601Utils.format(date, true, customTimeZone);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String date = "2021-07-21T10:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(date, pos);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String date = "2021-07-XXT10:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }

    @Test
    public void testParseWithOffset() throws ParseException {
        String date = "2021-07-21T10:00:00+03:00";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(date, pos);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithOffsetAndMillis() throws ParseException {
        String date = "2021-07-21T10:00:00.123+03:00";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(date, pos);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOffset() throws ParseException {
        String date = "2021-07-21T10:00:00+05:30";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }
    
    // Add more test methods for other methods in the class
    
}