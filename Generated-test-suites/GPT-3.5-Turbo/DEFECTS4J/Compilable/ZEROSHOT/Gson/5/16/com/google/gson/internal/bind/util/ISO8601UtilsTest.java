package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_Date() {
        Date date = new Date();
        String expected = "";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_DateBoolean() {
        Date date = new Date();
        boolean millis = true;
        String expected = "";
        String result = ISO8601Utils.format(date, millis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_DateBooleanTimeZone() {
        Date date = new Date();
        boolean millis = true;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        String expected = "";
        String result = ISO8601Utils.format(date, millis, tz);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_StringParsePosition() throws ParseException {
        String date = "";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date();
        Date result = ISO8601Utils.parse(date, pos);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParse_StringParsePosition_InvalidFormat() throws ParseException {
        String date = "invalid";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_StringParsePosition_NoTimeZoneIndicator() throws ParseException {
        String date = "2022-12-01T12:00:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }
}