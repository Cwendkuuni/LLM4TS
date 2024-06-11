package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import org.junit.Test;
import java.text.ParseException;


public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test with default timezone and no milliseconds precision
        String result1 = ISO8601Utils.format(new Date());
        assertNotNull(result1);

        // Test with GMT timezone and no milliseconds precision
        String result2 = ISO8601Utils.format(new Date(), false);
        assertNotNull(result2);

        // Test with GMT timezone and milliseconds precision
        String result3 = ISO8601Utils.format(new Date(), true);
        assertNotNull(result3);

        // Test with custom timezone and no milliseconds precision
        String result4 = ISO8601Utils.format(new Date(), false, TimeZone.getTimeZone("GMT+5:30"));
        assertNotNull(result4);

        // Test with custom timezone and milliseconds precision
        String result5 = ISO8601Utils.format(new Date(), true, TimeZone.getTimeZone("GMT+5:30"));
        assertNotNull(result5);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date result = ISO8601Utils.parse(dateString, pos);
        assertNotNull(result);
        assertEquals(dateString, ISO8601Utils.format(result));

        String dateTimeString = "2020-01-01T12:34:56Z";
        pos = new ParsePosition(0);
        result = ISO8601Utils.parse(dateTimeString, pos);
        assertNotNull(result);
        assertEquals(dateTimeString, ISO8601Utils.format(result));

        String dateTimeStringMillis = "2020-01-01T12:34:56.789Z";
        pos = new ParsePosition(0);
        result = ISO8601Utils.parse(dateTimeStringMillis, pos);
        assertNotNull(result);
        assertEquals(dateTimeStringMillis, ISO8601Utils.format(result));
    }
}