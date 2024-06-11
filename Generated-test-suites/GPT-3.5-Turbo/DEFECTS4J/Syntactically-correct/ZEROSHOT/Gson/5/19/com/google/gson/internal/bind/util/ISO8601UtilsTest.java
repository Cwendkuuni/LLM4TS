
package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test with default timezone and no milliseconds precision
        Date date1 = new Date();
        String result1 = ISO8601Utils.format(date1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expected1 = df.format(date1);
        assertEquals(expected1, result1);

        // Test with default timezone and milliseconds precision
        Date date2 = new Date();
        String result2 = ISO8601Utils.format(date2, true);
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df2.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expected2 = df2.format(date2);
        assertEquals(expected2, result2);

        // Test with custom timezone and milliseconds precision
        Date date3 = new Date();
        String result3 = ISO8601Utils.format(date3, true, TimeZone.getTimeZone("GMT+05:30"));
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+05:30'");
        df3.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        String expected3 = df3.format(date3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testParse() throws ParseException {
        // Test with date and time separated by 'T'
        String dateString1 = "2022-01-01T12:00:00Z";
        ParsePosition pos1 = new ParsePosition(0);
        Date result1 = ISO8601Utils.parse(dateString1, pos1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date expected1 = sdf1.parse(dateString1);
        assertEquals(expected1, result1);
        assertEquals(dateString1.length(), pos1.getIndex());

        // Test with date and time separated by 'T' and milliseconds precision
        String dateString2 = "2022-01-01T12:00:00.123Z";
        ParsePosition pos2 = new ParsePosition(0);
        Date result2 = ISO8601Utils.parse(dateString2, pos2);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date expected2 = sdf2.parse(dateString2);
        assertEquals(expected2, result2);
        assertEquals(dateString2.length(), pos2.getIndex());

        // Test with date and time separated by 'T' and custom timezone
        String dateString3 = "2022-01-01T12:00:00+05:30";
        ParsePosition pos3 = new ParsePosition(0);
        Date result3 = ISO8601Utils.parse(dateString3, pos3);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf3.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        Date expected3 = sdf3.parse(dateString3);
        assertEquals(expected3, result3);
        assertEquals(dateString3.length(), pos3.getIndex());
    }
}

