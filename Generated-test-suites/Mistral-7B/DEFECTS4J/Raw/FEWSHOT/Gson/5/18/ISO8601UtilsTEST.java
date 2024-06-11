package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatEmptyDate() {
        Date date = null;
        assertEquals("", ISO8601Utils.format(date));
    }

    @Test
    public void testFormatSimpleDate() {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24); // Yesterday
        assertEquals("2023-03-27", ISO8601Utils.format(date));
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date(System.currentTimeMillis());
        assertEquals("2023-03-27T14:37:18.538Z", ISO8601Utils.format(date, true, null));
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60); // One hour ago
        assertEquals("2023-03-27T13:37:18.538-01:00", ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York")));
    }

    @Test
    public void testParseValidDate() throws ParseException {
        String dateString = "2023-03-27T14:37:18.538Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals("Mon Mar 27 14:37:18 IST 2023", new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(date));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeZoneOffset() throws ParseException {
        String dateString = "2023-03-27T13:37:18.538-01:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals("Sun Mar 26 22:37:18 IST 2023", new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(date));
        assertEquals(13, pos.getIndex());
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        String dateString = "2023-03-27T14:37:18.538A";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseNullInput() {
        assertNull(ISO8601Utils.parse(null, new ParsePosition(0)));
    }
}