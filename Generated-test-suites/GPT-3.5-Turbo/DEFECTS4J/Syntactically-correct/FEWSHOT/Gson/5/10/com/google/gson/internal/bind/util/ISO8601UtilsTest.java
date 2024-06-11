
package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test with default timezone and no milliseconds precision
        Date date1 = new Date(1621306661000L);
        TimeZone tz1 = TimeZone.getTimeZone("UTC");
        assertEquals("2021-05-17T14:31:01Z", ISO8601Utils.format(date1));

        // Test with GMT timezone and including milliseconds precision
        Date date2 = new Date(1621306661000L);
        TimeZone tz2 = TimeZone.getTimeZone("GMT");
        assertEquals("2021-05-17T14:31:01.000Z", ISO8601Utils.format(date2, true, tz2));
    }

    @Test
    public void testParse() throws ParseException {
        // Test parsing a complete date string with timezone
        String dateString = "2021-05-17T14:31:01.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date expectedDate = new Date(1621306661000L);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(expectedDate, parsedDate);

        // Test parsing a date string without timezone
        dateString = "2021-05-17T14:31:01";
        pos = new ParsePosition(0);
        expectedDate = new Date(1621306261000L);
        parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(expectedDate, parsedDate);
    }
    
    @Test(expected = ParseException.class)
    public void testParseParseException() throws ParseException {
        // Test parsing an invalid date string
        String dateString = "2021/05/17T14:31:01.000Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseIndexOutOfBoundsException() throws ParseException {
        // Test parsing a date string with an invalid timezone indicator
        String dateString = "2021-05-17T14:31:01.000X";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
    
    @Test(expected = NullPointerException.class)
    public void testParseNullPointerException() throws ParseException {
        // Test parsing a null date string
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(null, pos);
    }

}

