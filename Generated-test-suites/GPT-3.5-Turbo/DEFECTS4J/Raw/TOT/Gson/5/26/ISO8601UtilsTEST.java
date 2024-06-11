package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1551465600000L); // March 2, 2019 00:00:00 UTC
        String expected = "2019-03-02T00:00:00Z";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(1551465600123L); // March 2, 2019 00:00:00.123 UTC
        String expected = "2019-03-02T00:00:00.123Z";
        String result = ISO8601Utils.format(date, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithTimezoneOffset() {
        Date date = new Date(1551465600000L); // March 2, 2019 00:00:00 UTC
        String expected = "2019-03-02T12:00:00+12:00";
        String result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+12:00"));
        assertEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        String input = "2019-03-02T00:00:00Z";
        Date expected = new Date(1551465600000L); // March 2, 2019 00:00:00 UTC
        ParsePosition pos = new ParsePosition(0);
        Date result = ISO8601Utils.parse(input, pos);
        if (result == null) {
            fail("result is null");
        }
        assertEquals(expected, result);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWithMillis() throws ParseException {
        String input = "2019-03-02T00:00:00.123Z";
        Date expected = new Date(1551465600123L); // March 2, 2019 00:00:00.123 UTC
        ParsePosition pos = new ParsePosition(0);
        Date result = ISO8601Utils.parse(input, pos);
        if (result == null) {
            fail("result is null");
        }
        assertEquals(expected, result);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWithTimezoneOffset() throws ParseException {
        String input = "2019-03-02T12:00:00+12:00";
        Date expected = new Date(1551465600000L); // March 2, 2019 00:00:00 UTC
        ParsePosition pos = new ParsePosition(0);
        Date result = ISO8601Utils.parse(input, pos);
        if (result == null) {
            fail("result is null");
        }
        assertEquals(expected, result);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() throws ParseException {
        String input = "2019-03-32T00:00:00Z"; // Invalid day
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(input, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseIncompleteInput() throws ParseException {
        String input = "2019-03-02T00:00"; // Incomplete time
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(input, pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidNumber() throws ParseException {
        String input = "2019-03-02T00:00:00.123456Z"; // Invalid number of milliseconds
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(input, pos);
    }
}