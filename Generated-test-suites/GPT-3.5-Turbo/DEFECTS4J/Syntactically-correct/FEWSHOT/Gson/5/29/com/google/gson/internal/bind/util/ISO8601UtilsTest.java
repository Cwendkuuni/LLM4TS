package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {
    @Test
    public void testFormat() {
        Date date = new Date(116, 2, 18, 15, 51, 32);
        assertNotNull(ISO8601Utils.format(date));
        assertNotNull(ISO8601Utils.format(date, true));
        assertNotNull(ISO8601Utils.format(date, false));
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date(116, 2, 18, 15, 51, 32);
        TimeZone tz = TimeZone.getTimeZone("GMT");
        assertNotNull(ISO8601Utils.format(date, true, tz));
        assertNotNull(ISO8601Utils.format(date, false, tz));
    }

    @Test
    public void testParse() {
        String dateString = "2021-11-30";
        ParsePosition pos = new ParsePosition(0);
        try {
            assertNotNull(ISO8601Utils.parse(dateString, pos));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseWithOffset() {
        String dateString = "2021-11-30+05:00";
        ParsePosition pos = new ParsePosition(0);
        try {
            assertNotNull(ISO8601Utils.parse(dateString, pos));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidDate() {
        String dateString = "2021-13-01";
        ParsePosition pos = new ParsePosition(0);
        try {
            assertNull(ISO8601Utils.parse(dateString, pos));
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"2021-13-01\"]", e.getMessage());
        }
    }
}