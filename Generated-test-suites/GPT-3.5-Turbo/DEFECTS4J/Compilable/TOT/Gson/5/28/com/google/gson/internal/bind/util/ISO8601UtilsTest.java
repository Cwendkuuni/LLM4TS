package com.google.gson.internal.bind.util;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case - format a date with default timezone and no milliseconds
        Date date = new Date(1577836800000L); // Jan 1, 2020 00:00:00 UTC
        String expected = "2020-01-01T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
        
        // Edge case - format a date with GMT timezone and milliseconds
        TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
        date = new Date(1577836800123L); // Jan 1, 2020 00:00:00.123 GMT
        expected = "2020-01-01T00:00:00.123Z";
        actual = ISO8601Utils.format(date, true, gmtTimeZone);
        assertEquals(expected, actual);
        
        // Error scenario - format a null date
        date = null;
        expected = null;
        actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        // Typical use case - parse a valid ISO-8601 formatted string with all components
        String dateStr = "2020-01-01T00:00:00.123+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1577788800123L); // Jan 1, 2020 00:00:00.123 GMT
        Date actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
        
        // Edge case - parse a string with only date and timezone
        dateStr = "2020-01-01+02:00";
        pos = new ParsePosition(0);
        expected = new Date(1577788800000L); // Jan 1, 2020 00:00:00 GMT
        actual = ISO8601Utils.parse(dateStr, pos);
        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
        
        // Error scenario - parse an invalid ISO-8601 formatted string
        dateStr = "2020-01-01T00:00:00.xxx+02:00";
        pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateStr, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"2020-01-01T00:00:00.xxx+02:00\"]: Invalid number: xxx", e.getMessage());
            assertEquals(dateStr.length()-1, pos.getErrorIndex());
        }
    }

}