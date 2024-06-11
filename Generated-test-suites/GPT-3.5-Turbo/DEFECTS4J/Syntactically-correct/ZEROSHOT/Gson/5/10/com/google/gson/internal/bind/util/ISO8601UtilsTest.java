package com.google.gson.internal.bind.util;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest{
    @Test
    public void testFormat(){
        Date date = new Date();
        String expected = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatMillis(){
        Date date = new Date();
        String expected = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone(){
        Date date = new Date();
        String expected = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+05:30"));
        String actual = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+05:30"));
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillisAndTimeZone(){
        Date date = new Date();
        String expected = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT+05:30"));
        String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT+05:30"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException{
        String date = "2021-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(date, pos);
        pos.setIndex(0);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithOffset() throws ParseException{
        String date = "2021-01-01T12:00:00+05:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(date, pos);
        pos.setIndex(0);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
    }
}