package com.google.gson.internal.bind.util;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    
    @Test
    public void testFormat_Date() {
        Date date = new Date(1596451200000L); // 2020-08-03T00:00:00Z
        String expected = "2020-08-03T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_Date_WithMillis() {
        Date date = new Date(1596451200000L); // 2020-08-03T00:00:00Z
        String expected = "2020-08-03T00:00:00.000Z";
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_Date_WithTimeZone() {
        Date date = new Date(1596451200000L); // 2020-08-03T00:00:00Z
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String expected = "2020-08-03T02:00:00+02:00";
        String actual = ISO8601Utils.format(date, false, tz);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_ValidIsoString() throws ParseException {
        String dateStr = "2020-08-03T00:00:00Z";
        Date expected = new Date(1596451200000L); // 2020-08-03T00:00:00Z
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
    }
    
    @Test(expected = ParseException.class)
    public void testParse_InvalidDateFormat() throws ParseException {
        String dateStr = "2020-08-03XYZ";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_InvalidTimeZone() throws ParseException {
        String dateStr = "2020-08-03T00:00:00XYZ";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_IncompleteIsoString() throws ParseException {
        String dateStr = "2020-08-03T00:00:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

}