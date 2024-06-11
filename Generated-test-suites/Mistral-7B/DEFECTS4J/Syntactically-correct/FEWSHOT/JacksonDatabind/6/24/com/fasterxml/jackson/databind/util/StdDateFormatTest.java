package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithTimezone() {
        StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("2022-12-31T23:59:59.123+01:00");
        assertEquals("2022-12-31 23:59:59.123", sdf.format(date));

        date = sdf.parse("2022-12-31T23:59:59.123Z");
        assertEquals("2022-12-31 23:59:59.123", sdf.format(date));

        date = sdf.parse("2022-12-31T23:59:59.123-05:00");
        assertEquals("2022-12-31 18:59:59.123", sdf.format(date));
    }

    @Test
    public void testParseISO8601NoTimezone() {
        StdDateFormat sdf = new StdDateFormat(null, Locale.US);
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("2022-12-31T23:59:59.123Z");
        assertEquals("2022-12-31 23:59:59.123", sdf.format(date));

        date = sdf.parse("2022-12-31T23:59:59.123");
        assertEquals("2022-12-31 23:59:59.123", sdf.format(date));

        date = sdf.parse("2022-12-31T23:59:59.123-05:00");
        assertEquals("2022-12-31 18:59:59.123", sdf.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("Thu, 31 Dec 2022 23:59:59 GMT");
        assertEquals("2022-12-31 23:59:59", sdf.format(date));
    }

    @Test
    public void testParsePlainDate() {
        StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = sdf.parse("2022-12-31");
        assertEquals("2022-12-31", sdf.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        String dateStr = "InvalidFormat";
        ParsePosition pos = new ParsePosition(0);

        sdf.parse(dateStr, pos);
        fail("Expected ParseException but got: " + sdf.format(pos.getIndex()));
    }
}