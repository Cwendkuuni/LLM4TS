package com.google.gson.internal.bind.util;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormatWithDate() {
        Date date = new Date(1585273581000L);
        String expected = "2020-03-27T07:06:21Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDateAndMillis() {
        Date date = new Date(1585273581000L);
        String expected = "2020-03-27T07:06:21.000Z";
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDateAndTimeZone() {
        Date date = new Date(1585273581000L);
        TimeZone timeZone = TimeZone.getTimeZone("GMT+5:30");
        String expected = "2020-03-27T12:36:21+05:30";
        String actual = ISO8601Utils.format(date, false, timeZone);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDateAndMillisAndTimeZone() {
        Date date = new Date(1585273581000L);
        TimeZone timeZone = TimeZone.getTimeZone("GMT+5:30");
        String expected = "2020-03-27T12:36:21.000+05:30";
        String actual = ISO8601Utils.format(date, true, timeZone);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String date = "2020-03-27T07:06:21Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1585273581000L);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
        assertEquals(date.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFormat() throws ParseException {
        String date = "20200327T070621Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }

    @Test
    public void testParseWithMillis() throws ParseException {
        String date = "2020-03-27T07:06:21.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1585273581000L);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
        assertEquals(date.length(), pos.getIndex());
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        String date = "2020-03-27T12:36:21+05:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1585273581000L);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
        assertEquals(date.length(), pos.getIndex());
    }

    @Test
    public void testParseWithMillisAndTimeZone() throws ParseException {
        String date = "2020-03-27T12:36:21.000+05:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1585273581000L);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
        assertEquals(date.length(), pos.getIndex());
    }
    
}