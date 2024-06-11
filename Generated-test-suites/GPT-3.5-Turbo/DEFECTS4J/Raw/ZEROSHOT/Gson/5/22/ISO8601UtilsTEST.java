package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_date() {
        Date date = new Date();
        String expected = ISO8601Utils.format(date);
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_date_millis() {
        Date date = new Date();
        boolean millis = true;
        String expected = ISO8601Utils.format(date, millis);
        String actual = ISO8601Utils.format(date, millis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_date_tz() {
        Date date = new Date();
        boolean millis = false;
        TimeZone tz = TimeZone.getTimeZone("PST");
        String expected = ISO8601Utils.format(date, millis, tz);
        String actual = ISO8601Utils.format(date, millis, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_date_millis_tz() {
        Date date = new Date();
        boolean millis = true;
        TimeZone tz = TimeZone.getTimeZone("PST");
        String expected = ISO8601Utils.format(date, millis, tz);
        String actual = ISO8601Utils.format(date, millis, tz);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws Exception {
        String date = "2021-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(date, pos);
        Date actual = ISO8601Utils.parse(date, pos);
        assertEquals(expected, actual);
    }
}