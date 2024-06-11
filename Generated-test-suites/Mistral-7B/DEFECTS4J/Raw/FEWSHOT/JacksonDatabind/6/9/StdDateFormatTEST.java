package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithMilliseconds() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = null;
        try {
            date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        } catch (ParseException e) {
            fail("Failed to parse ISO8601 date with milliseconds: " + e.getMessage());
        }
        assertNotNull(date);
    }

    @Test
    public void testParseISO8601WithTimezoneZ() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = null;
        try {
            date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        } catch (ParseException e) {
            fail("Failed to parse ISO8601 date with timezone Z: " + e.getMessage());
        }
        assertNotNull(date);
    }

    @Test
    public void testParseISO8601WithTimezoneOffset() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = null;
        try {
            date = dateFormat.parse("2022-12-31T23:59:59.123-05:00");
        } catch (ParseException e) {
            fail("Failed to parse ISO8601 date with timezone offset: " + e.getMessage());
        }
        assertNotNull(date);
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = null;
        try {
            date = dateFormat.parse("Wed, 31 Dec 2022 23:59:59 GMT");
        } catch (ParseException e) {
            fail("Failed to parse RFC1123 date: " + e.getMessage());
        }
        assertNotNull(date);
    }

    @Test
    public void testParseRFC1123WithTimezone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC-5"), Locale.US);
        Date date = null;
        try {
            date = dateFormat.parse("Wed, 31 Dec 2022 02:59:59 EST");
        } catch (ParseException e) {
            fail("Failed to parse RFC1123 date with timezone: " + e.getMessage());
        }
        assertNotNull(date);
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date(1672492799000L);
        String formattedDate = dateFormat.format(date);
        assertEquals("Sat Dec 31 22:13:19 UTC 2022", formattedDate);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC-5"), Locale.US);
        formattedDate = dateFormat.format(date);
        assertEquals("Sat Dec 31 05:13:19 EST 2022", formattedDate);
    }

    @Test
    public void testClone() {
        StdDateFormat original = new StdDateFormat();
        StdDateFormat cloned = original.clone();
        assertNotSame(original, cloned);
        assertEquals(original.getClass(), cloned.getClass());
    }
}