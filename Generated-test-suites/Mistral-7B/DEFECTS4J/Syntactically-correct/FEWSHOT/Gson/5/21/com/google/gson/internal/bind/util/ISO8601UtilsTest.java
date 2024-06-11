package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        // Test formatting with default timezone and no milliseconds
        Date date = new Date(0L);
        String formatted = ISO8601Utils.format(date);
        assertEquals("1970-01-01", formatted);

        // Test formatting with milliseconds and default timezone
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 123);
        date = calendar.getTime();
        formatted = ISO8601Utils.format(date, true, null);
        assertEquals("1970-01-01T00:00:01.123Z", formatted);

        // Test formatting with UTC timezone
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T00:00:01.123Z", formatted);

        // Test formatting with custom timezone
        TimeZone customTimezone = TimeZone.getTimeZone("America/New_York");
        formatted = ISO8601Utils.format(date, true, customTimezone);
        assertEquals("1970-01-01T05:00:01.123-05:00", formatted);
    }

    @Test
    public void testFormatDateWithInvalidInput() {
        try {
            ISO8601Utils.format(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseDate() throws ParseException {
        // Test parsing with default timezone and no milliseconds
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse("1970-01-01", pos);
        assertEquals(new Date(0L), date);
        assertEquals(0, pos.getIndex());

        // Test parsing with milliseconds and default timezone
        pos.setIndex(0);
        date = ISO8601Utils.parse("1970-01-01T00:00:01.123Z", pos);
        assertEquals(new Date(0L + 123), date);
        assertEquals(11, pos.getIndex());

        // Test parsing with UTC timezone
        pos.setIndex(0);
        date = ISO8601Utils.parse("1970-01-01T00:00:01.123Z", pos, TimeZone.getTimeZone("UTC"));
        assertEquals(new Date(0L + 123), date);
        assertEquals(11, pos.getIndex());

        // Test parsing with custom timezone
        TimeZone customTimezone = TimeZone.getTimeZone("America/New_York");
        pos.setIndex(0);
        date = ISO8601Utils.parse("1970-01-01T05:00:01.123-05:00", pos, customTimezone);
        assertEquals(new Date(0L + 123 - 5 * 60 * 60 * 1000), date);
        assertEquals(17, pos.getIndex());
    }

    @Test
    public void testParseDateWithInvalidInput() throws ParseException {
        try {
            ISO8601Utils.parse(null, new ParsePosition(0));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("1970-01-01T", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("1970-01-01T00:00:01.123ZZZ", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}