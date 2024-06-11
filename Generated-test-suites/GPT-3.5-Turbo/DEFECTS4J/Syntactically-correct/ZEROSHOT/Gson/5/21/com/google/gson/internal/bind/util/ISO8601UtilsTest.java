package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        TimeZone tz = TimeZone.getTimeZone("UTC");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JUNE, 15, 12, 30, 45);
        calendar.set(Calendar.MILLISECOND, 0);

        String expected = "2022-06-15T12:30:45Z";
        String actual = ISO8601Utils.format(calendar.getTime(), false, tz);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        TimeZone tz = TimeZone.getTimeZone("UTC");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JUNE, 15, 12, 30, 45);
        calendar.set(Calendar.MILLISECOND, 123);

        String expected = "2022-06-15T12:30:45.123Z";
        String actual = ISO8601Utils.format(calendar.getTime(), true, tz);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");

        String dateStr = "2022-06-15T12:30:45Z";
        Date expected = new GregorianCalendar(2022, Calendar.JUNE, 15, 12, 30, 45).getTime();

        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);

        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    public void testParseWithMillis() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");

        String dateStr = "2022-06-15T12:30:45.123Z";
        Date expected = new GregorianCalendar(2022, Calendar.JUNE, 15, 12, 30, 45).getTime();
        expected.setMilliseconds(123);

        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);

        assertEquals(expected, actual);
        assertEquals(dateStr.length(), pos.getIndex());
    }
}