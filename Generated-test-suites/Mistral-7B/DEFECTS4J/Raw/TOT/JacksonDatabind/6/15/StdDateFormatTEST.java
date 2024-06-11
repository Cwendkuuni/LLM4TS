package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public class StdDateFormatTest {

    private static final DateFormat instance = StdDateFormat.instance;

    @Test
    public void testParseISO8601_Valid() throws ParseException {
        Date date = instance.parse("2022-01-01T12:34:56.789Z");
        assertEquals("Valid ISO-8601 date parsed correctly", new Date(1645152056000L), date);
    }

    @Test
    public void testParseISO8601_WithTimezone() throws ParseException {
        Date date = instance.parse("2022-01-01T12:34:56.789+01:00");
        assertEquals("Valid ISO-8601 date with timezone parsed correctly", new Date(1645153656000L), date);
    }

    @Test
    public void testParseISO8601_MissingMilliseconds() throws ParseException {
        Date date = instance.parse("2022-01-01T12:34:56Z");
        assertEquals("Valid ISO-8601 date without milliseconds parsed correctly", new Date(1645152056000L), date);
    }

    @Test
    public void testParseISO8601_PlainDate() throws ParseException {
        Date date = instance.parse("2022-01-01");
        assertEquals("Valid plain date parsed correctly", new Date(1645152000000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601_InvalidFormat() throws ParseException {
        instance.parse("2022-01-01T12:34:56Invalid");
        fail("Invalid ISO-8601 date should not be parsed");
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123_Valid() throws ParseException {
        Date date = instance.parse("Tue, 01 Jan 2022 12:34:56 GMT");
        assertEquals("Valid RFC-1123 date parsed correctly", new Date(1645152056000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123_InvalidFormat() throws ParseException {
        instance.parse("Tue, 01 Jan 2022 12:34:56 Invalid");
        fail("Invalid RFC-1123 date should not be parsed");
    }

    @Test
    public void testFormat() throws ParseException {
        Date date = instance.parse("2022-01-01T12:34:56.789Z");
        String formattedDate = instance.format(date, new StringBuilder(), null);
        assertEquals("Formatted date matches the original", "2022-01-01T12:34:56.789Z", formattedDate);
    }

    @Test
    public void testSetTimeZone() {
        DateFormat df = instance.clone();
        df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String dateString = "2022-01-01T12:34:56.789Z";
        Date date = df.parse(dateString);
        assertEquals("Date parsed with new timezone", new Date(1645152056000L + (60*60*1000*2)), date);
    }
}