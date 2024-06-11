package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test case 1
        Date date1 = new Date(2021, 11, 25, 10, 30, 0);
        String expected1 = "2021-11-25T10:30:00Z";
        String result1 = ISO8601Utils.format(date1);
        assertEquals(expected1, result1);
        
        // Test case 2
        Date date2 = new Date(2021, 11, 25, 10, 30, 0);
        String expected2 = "2021-11-25T10:30:00.000Z";
        String result2 = ISO8601Utils.format(date2, true);
        assertEquals(expected2, result2);
        
        // Test case 3
        Date date3 = new Date(2021, 11, 25, 10, 30, 0);
        TimeZone tz3 = TimeZone.getTimeZone("GMT+02:00");
        String expected3 = "2021-11-25T12:30:00+02:00";
        String result3 = ISO8601Utils.format(date3, false, tz3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testParse() throws ParseException {
        // Test case 1
        String date1 = "2021-11-25T10:30:00Z";
        Date expected1 = new Date(2021, 11, 25, 10, 30, 0);
        ParsePosition pos1 = new ParsePosition(0);
        Date result1 = ISO8601Utils.parse(date1, pos1);
        assertEquals(expected1, result1);
        
        // Test case 2
        String date2 = "2021-11-25T10:30:00.000Z";
        Date expected2 = new Date(2021, 11, 25, 10, 30, 0);
        ParsePosition pos2 = new ParsePosition(0);
        Date result2 = ISO8601Utils.parse(date2, pos2);
        assertEquals(expected2, result2);
        
        // Test case 3
        String date3 = "2021-11-25T12:30:00+02:00";
        Date expected3 = new Date(2021, 11, 25, 10, 30, 0);
        ParsePosition pos3 = new ParsePosition(0);
        Date result3 = ISO8601Utils.parse(date3, pos3);
        assertEquals(expected3, result3);
    }
    
}