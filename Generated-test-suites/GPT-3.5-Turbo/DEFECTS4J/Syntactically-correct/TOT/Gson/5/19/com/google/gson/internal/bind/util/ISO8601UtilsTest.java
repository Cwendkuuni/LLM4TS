package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test typical use case
        Date date = new Date(1632891552000L); // Sat Sep 30 2021 23:12:32
        String expected = "2021-09-30T23:12:32Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);

        // Test edge case
        date = new Date(0); // Thu Jan 01 1970 00:00:00
        expected = "1970-01-01T00:00:00Z";
        actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        // Test typical use case
        Date date = new Date(1632891552123L); // Sat Sep 30 2021 23:12:32.123
        String expected = "2021-09-30T23:12:32.123Z";
        String actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);

        // Test edge case
        date = new Date(0); // Thu Jan 01 1970 00:00:00
        expected = "1970-01-01T00:00:00.000Z";
        actual = ISO8601Utils.format(date, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone() {
        // Test typical use case
        Date date = new Date(1632891552000L); // Sat Sep 30 2021 23:12:32
        String expected = "2021-09-30T23:12:32+11:00";
        TimeZone tz = TimeZone.getTimeZone("Australia/Sydney");
        String actual = ISO8601Utils.format(date, false, tz);
        assertEquals(expected, actual);

        // Test edge case
        date = new Date(0); // Thu Jan 01 1970 00:00:00
        expected = "1970-01-01T00:00:00+00:00";
        tz = TimeZone.getTimeZone("GMT");
        actual = ISO8601Utils.format(date, false, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        // Test typical use case
        String input = "2021-09-30T23:12:32Z";
        int expectedOffset = 19;
        Date expectedDate = new Date(1632891552000L); // Sat Sep 30 2021 23:12:32
        ParsePosition pos = new ParsePosition(0);
        Date actualDate = ISO8601Utils.parse(input, pos);
        assertEquals(expectedOffset, pos.getIndex());
        assertEquals(expectedDate, actualDate);

        // Test edge case
        input = "1970-01-01T00:00:00Z";
        expectedOffset = 19;
        expectedDate = new Date(0); // Thu Jan 01 1970 00:00:00
        pos = new ParsePosition(0);
        actualDate = ISO8601Utils.parse(input, pos);
        assertEquals(expectedOffset, pos.getIndex());
        assertEquals(expectedDate, actualDate);
    }
}