package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    // Test format(Date) method
    @Test
    public void testFormatOnlyDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date date = calendar.getTime();

        String expected = "2020-01-31T00:00:00Z";
        String actual = ISO8601Utils.format(date);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 45);
        calendar.set(Calendar.MILLISECOND, 500);
        Date date = calendar.getTime();

        String expected = "2020-01-31T12:30:45.500Z";
        String actual = ISO8601Utils.format(date, true);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZoneOffset() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        Date date = calendar.getTime();

        String expected = "2020-01-31T12:30:00+05:30";
        String actual = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+05:30"));

        assertEquals(expected, actual);
    }

    // Test parse(String, ParsePosition) method
    @Test
    public void testParseOnlyDate() throws ParseException {
        String input = "2020-01-31T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date expected = calendar.getTime();

        Date actual = ISO8601Utils.parse(input, pos);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWithMillis() throws ParseException {
        String input = "2020-01-31T12:30:45.500Z";
        ParsePosition pos = new ParsePosition(0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 45);
        calendar.set(Calendar.MILLISECOND, 500);
        Date expected = calendar.getTime();

        Date actual = ISO8601Utils.parse(input, pos);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWithTimeZoneOffset() throws ParseException {
        String input = "2020-01-31T12:30:00+05:30";
        ParsePosition pos = new ParsePosition(0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        Date expected = calendar.getTime();

        Date actual = ISO8601Utils.parse(input, pos);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

}