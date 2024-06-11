package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601Date() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-03-15T14:30:59.123Z");
        assertEquals("2022-03-15 14:30:59.123", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601DateWithTimezoneOffset() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-03-15T14:30:59.123-05:00");
        assertEquals("2022-03-15 09:30:59.123", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601DateWithTimezoneName() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-03-15T14:30:59.123+02:00");
        assertEquals("2022-03-15 16:30:59.123", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123Date() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Tue, 15 Mar 2022 14:30:59 -0500");
        assertEquals("Tue Mar 15 14:30:59 EDT 2022", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidDate");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidFormat");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeZoneWithNullTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(null);
    }

    @Test
    public void testClone() {
        StdDateFormat original = new StdDateFormat();
        StdDateFormat clone = original.clone();
        assertNotSame(original, clone);
        assertEquals(original.getClass(), clone.getClass());
    }

    @Test
    public void testGetBlueprintISO8601Format() throws ParseException {
        Date date = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US).parse("2022-03-15T14:30:59.123Z");
        assertEquals("2022-03-15 14:30:59.123", StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US).format(date));
    }
}