package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(2022, 11, 25, 15, 30, 45);
      
        String expected = "2022-12-25T15:30:45Z";
        String actual = ISO8601Utils.format(date);
      
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(2022, 11, 25, 15, 30, 45);
      
        String expected = "2022-12-25T15:30:45.000Z";
        String actual = ISO8601Utils.format(date, true);
      
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date(2022, 11, 25, 15, 30, 45);
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
      
        String expected = "2022-12-25T15:30:45+05:30";
        String actual = ISO8601Utils.format(date, false, tz);
      
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2022-12-25T15:30:45Z";
      
        Date expected = new Date(2022, 11, 25, 15, 30, 45);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
      
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    public void testParseWithMillis() throws ParseException {
        String dateStr = "2022-12-25T15:30:45.000Z";
      
        Date expected = new Date(2022, 11, 25, 15, 30, 45);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
      
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    public void testParseWithOffset() throws ParseException {
        String dateStr = "2022-12-25T15:30:45+05:30";
      
        Date expected = new Date(2022, 11, 25, 15, 30, 45);
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
      
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
        assertEquals(tz, pos.getTimeZone());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateStr = "2022-12-25T15:30";
      
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseInvalidTimeZoneIndicator() throws ParseException {
        String dateStr = "2022-12-25T15:30:45+05";
      
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }
}