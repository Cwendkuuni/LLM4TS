package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

    @Test
    public void testFormat() {
        // Test formatting a date with default timezone and no millis
        Date date = new Date();
        String expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);

        // Test formatting a date with timezone and millis
        date = new Date();
        expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
        actual = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws Exception {
        String dateString = "2022-01-01T12:00:00.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, 5, 4);
        assertEquals("0005", buffer.toString());
    }
}