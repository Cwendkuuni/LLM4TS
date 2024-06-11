package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Invalid date from ISO8601 format: '2022-12-31T23:59:59.123Z'", date, new Date(1671542399000L));

        date = stdDateFormat.parse("2022-12-31T23:59:59.123+01:00");
        assertEquals("Invalid date from ISO8601 format with timezone: '2022-12-31T23:59:59.123+01:00'",
                new Date(1671555599000L), date);

        date = stdDateFormat.parse("2022-12-31T23:59:59.123-05:00");
        assertEquals("Invalid date from ISO8601 format with timezone: '2022-12-31T23:59:59.123-05:00'",
                new Date(1671508199000L), date);

        date = stdDateFormat.parse("2022-12-31");
        assertEquals("Invalid date from 'plain' format: '2022-12-31'",
                new Date(1671542000000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        String invalidDate = "InvalidDate";
        ParsePosition pos = new ParsePosition(0);

        stdDateFormat.parse(invalidDate, pos);
        fail("Expected ParseException: " + invalidDate);
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        Date date = new Date();
        String formattedDate = stdDateFormat.format(date, new StringBuilder(), new FieldPosition[0]).toString();
        String expectedFormat = "Sat Jan 01 00:00:00 GMT 1970";
        assertEquals("Invalid date format: " + formattedDate, expectedFormat, formattedDate);
    }

    @Test
    public void testToString() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: GMT, locale: US)",
                stdDateFormat.toString());
    }

    @Test
    public void testClone() {
        StdDateFormat originalStdDateFormat = StdDateFormat.instance;
        StdDateFormat clonedStdDateFormat = originalStdDateFormat.clone();

        assertNotSame(originalStdDateFormat, clonedStdDateFormat);
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: GMT, locale: US)",
                clonedStdDateFormat.toString());
    }
}