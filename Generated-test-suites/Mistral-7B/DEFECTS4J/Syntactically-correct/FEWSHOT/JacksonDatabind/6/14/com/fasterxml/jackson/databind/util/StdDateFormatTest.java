package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("2023-03-15T14:30:45.123+02:00");
        assertEquals("Wed Mar 15 16:30:45 CET 2023", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZone_MissingMilliseconds() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("2023-03-15T14:30:45+02:00");
        assertEquals("Wed Mar 15 16:30:45 CET 2023", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZone_MissingTimeZoneOffset() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        assertThrows(ParseException.class, () -> dateFormat.parse("2023-03-15T14:30:45"));
    }

    @Test
    public void testParseISO8601WithTimeZone_MissingSeconds() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("2023-03-15T14:30+02:00");
        assertEquals("Wed Mar 15 16:30:00 CET 2023", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZone_MissingMinutes() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("2023-03-15T14+02:00");
        assertEquals("Wed Mar 15 14:00:00 CET 2023", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZone_MissingHours() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("2023-03-15+02:00");
        assertEquals("Wed Mar 15 00:00:00 CET 2023", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZone_MissingDate() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        assertThrows(ParseException.class, () -> dateFormat.parse("+02:00"));
    }

    @Test
    public void testParseISO8601WithPlainDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2023-03-15");
        assertEquals("Wed Mar 15 00:00:00 GMT 2023", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Wed, 15 Mar 2023 14:30:45 GMT");
        assertEquals("Wed Mar 15 14:30:45 GMT 2023", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123WithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("Wed, 15 Mar 2023 14:30:45 CET");
        assertEquals("Wed Mar 15 16:30:45 CET 2023", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123WithoutTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Wed, 15 Mar 2023 14:30:45 GMT");
        assertEquals("Wed Mar 15 14:30:45 GMT 2023", dateFormat.format
                (date));
    }

    @Test
    public void testParseRFC1123WithWrongFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        assertThrows(ParseException.class, () -> dateFormat.parse("Wed Mar 15 14:30:45"));
    }
}