package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(0);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setTime(date);

        String formatted = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T00:00:00.000Z", formatted);

        calendar.add(Calendar.HOUR_OF_DAY, 2);
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T02:00:00Z", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T02:00:00.000Z", formatted);

        calendar.add(Calendar.MINUTE, 30);
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T02:30:00Z", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T02:30:00.000Z", formatted);

        calendar.add(Calendar.MILLISECOND, 123);
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T02:30:00.123Z", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-01T02:30:00.123Z", formatted);

        calendar.add(Calendar.HOUR_OF_DAY, 24);
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-02T00:00:00Z", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-02T00:00:00.000Z", formatted);

        TimeZone timezone = TimeZone.getTimeZone("Europe/Paris");
        formatted = ISO8601Utils.format(date, false, timezone);
        assertEquals("1970-01-01T01:00:00+01:00", formatted);

        formatted = ISO8601Utils.format(date, true, timezone);
        assertEquals("1970-01-01T01:00:00.000+01:00", formatted);

        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Asia/Tokyo"));
        assertEquals("1970-01-01T06:00:00+09:00", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Asia/Tokyo"));
        assertEquals("1970-01-01T06:00:00.000+09:00", formatted);
    }

    @Test
    public void testFormatWithInvalidInput() {
        try {
            ISO8601Utils.format(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            ISO8601Utils.format(new Date(Long.MAX_VALUE + 1), false, TimeZone.getTimeZone("UTC"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            ISO8601Utils.format(new Date(-1), false, TimeZone.getTimeZone("UTC"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse() throws Exception {
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = ISO8601Utils.parse("1970-01-01T00:00:00Z", pos);
        assertEquals(new Date(0), date);
        assertEquals(0, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T00:00:00.000Z", pos);
        assertEquals(new Date(0), date);
        assertEquals(12, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T00:00:00.000+00:00", pos);
        assertEquals(new Date(0), date);
        assertEquals(16, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T02:30:00Z", pos);
        assertEquals(new Date(0 + 2 * 60 * 60 * 1000), date);
        assertEquals(14, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T02:30:00.123Z", pos);
        assertEquals(new Date(0 + 2 * 60 * 60 * 1000 + 123), date);
        assertEquals(18, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T02:30:00.123+01:00", pos);
        assertEquals(new Date(0 + 2 * 60 * 60 * 1000 + 123 + 60 * 60 * 1000), date);
        assertEquals(20, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T02:30:00.123+09:00", pos);
        assertEquals(new Date(0 + 2 * 60 * 60 * 1000 + 123 + 9 * 60 * 60 * 1000), date);
        assertEquals(26, pos.getIndex());

        date = ISO8601Utils.parse("1970-01-01T02:30:00.123-09:00", pos);
        assertEquals(new Date(0 + 2 * 60 * 60 * 1000 + 123 - 9 * 60 * 60 * 1000), date);
        assertEquals(26, pos.getIndex());

        try {
            ISO8601Utils.parse("1970-01-01", pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals(11, pos.getIndex());
            assertEquals("Failed to parse date [1970-01-01]: Invalid format: missing 'T' and 'Z' or offset", e.getMessage());
        }

        try {
            ISO8601Utils.parse("1970-01-01T02:30:00Zzzz", pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals(11, pos.getIndex());
            assertEquals("Failed to parse date [1970-01-01T02:30:00Zzzz]: Invalid format: unknown timezone identifier 'zzz'", e.getMessage());
        }

        try {
            ISO8601Utils.parse("1970-01-01T02:30:00.123+24:00", pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals(16, pos.getIndex());
            assertEquals("Failed to parse date [1970-01-01T02:30:00.123+24:00]: Invalid format: offset value too large", e.getMessage());
        }

        try {
            ISO8601Utils.parse("1970-01-01T02:30:00.123+09:60", pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals(16, pos.getIndex());
            assertEquals("Failed to parse date [1970-01-01T02:30:00.123+09:60]: Invalid format: invalid number of minutes in offset", e.getMessage());
        }

        try {
            ISO8601Utils.parse("1970-01-01T02:30:00.123Z-", pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals(5, pos.getIndex());
            assertEquals("Failed to parse date [1970-01-01T02:30:00.123Z-]: Invalid format: unrecognized character '-'", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidInput() {
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
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        int value = 123;
        ISO8601Utils.padInt(buffer, value, 5);
        assertEquals("00123", buffer.toString());

        value = 1234;
        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, value, 4);
        assertEquals("1234", buffer.toString());

        value = 123;
        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, value, 7);
        assertEquals("000123", buffer.toString());
    }

    @Test
    public void testParseInt() {
        try {
            ISO8601Utils.parseInt(null, 0, 0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            ISO8601Utils.parseInt("", 0, 0);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            int i = ISO8601Utils.parseInt("123", 0, 3);
            assertEquals(123, i);
        } catch (NumberFormatException e) {
            fail("Expected no exception");
        }

        try {
            int i = ISO8601Utils.parseInt("123ab", 0, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            int i = ISO8601Utils.parseInt("123", 3, 3);
            assertEquals(123, i);
        } catch (NumberFormatException e) {
            fail("Expected no exception");
        }

        try {
            int i = ISO8601Utils.parseInt("12345", 3, 3);
            assertEquals(5, i);
        } catch (NumberFormatException e) {
            fail("Expected no exception");
        }
    }
}