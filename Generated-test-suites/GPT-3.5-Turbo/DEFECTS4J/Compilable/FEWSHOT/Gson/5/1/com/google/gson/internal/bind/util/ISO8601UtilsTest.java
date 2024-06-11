package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        assertNotNull(ISO8601Utils.format(date));
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        assertNotNull(ISO8601Utils.format(date, true));
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        assertNotNull(ISO8601Utils.format(date, true, TimeZone.getDefault()));
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2022-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    public void testParseWithOffset() throws ParseException {
        String dateStr = "2022-01-01T12:00:00+05:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateStr = "2022-01-XYZ";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

}