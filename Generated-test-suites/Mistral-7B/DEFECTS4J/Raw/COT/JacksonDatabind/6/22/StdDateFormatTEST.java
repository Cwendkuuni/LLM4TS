package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    // Test constructor
    @Test
    public void testStdDateFormatConstruction() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertEquals(Locale.US, stdDateFormat.getLocale());
        assertNull(stdDateFormat.getTimeZone());

        StdDateFormat stdDateFormatWithTimeZone = new StdDateFormat(TimeZone.getTimeZone("UTC"));
        assertEquals(Locale.US, stdDateFormatWithTimeZone.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), stdDateFormatWithTimeZone.getTimeZone());

        StdDateFormat stdDateFormatWithLocale = new StdDateFormat(Locale.FRENCH);
        assertEquals(Locale.FRENCH, stdDateFormatWithLocale.getLocale());
        assertNull(stdDateFormatWithLocale.getTimeZone());
    }

    // Test getBlueprintISO8601Format and getBlueprintRFC1123Format methods
    @Test
    public void testGetBlueprintFormatMethods() {
        DateFormat blueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", blueprintISO8601Format.toPattern());
        assertNull(blueprintISO8601Format.getTimeZone());

        DateFormat blueprintRFC1123Format = StdDateFormat.getBlueprintRFC1123Format();
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", blueprintRFC1123Format.toPattern());
        assertNull(blueprintRFC1123Format.getTimeZone());

        Date date = new Date();
        String formatISO8601 = blueprintISO8601Format.format(date);
        assertEquals("2023-03-23T14:30:45.123-04:00", formatISO8601);

        String formatRFC1123 = blueprintRFC1123Format.format(date);
        assertEquals("Thu, 23 Mar 2023 14:30:45 EDT", formatRFC1123);
    }

    // Test parsing basic ISO-8601 date strings
    @Test
    public void testParseISO8601String() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();

        Date date1 = stdDateFormat.parse("2023-03-23T14:30:45.123+04:00");
        assertEquals("Thu Mar 23 18:30:45 IST 2023", stdDateFormat.format(date1));

        Date date2 = stdDateFormat.parse("2023-03-23T14:30:45.123Z");
        assertEquals("Thu Mar 23 14:30:45 GMT 2023", stdDateFormat.format(date2));

        Date date3 = stdDateFormat.parse("2023-03-23T14:30:45.123-04:00");
        assertEquals("Thu Mar 23 10:30:45 EDT 2023", stdDateFormat.format(date3));
    }

    // Test parsing basic RFC-1123 date strings
    @Test
    public void testParseRFC1123String() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();

        Date date1 = stdDateFormat.parse("Thu, 23 Mar 2023 14:30:45 EDT");
        assertEquals("Thu Mar 23 18:30:45 IST 2023", stdDateFormat.format(date1));

        Date date2 = stdDateFormat.parse("Thu, 23 Mar 2023 14:30:45 GMT");
        assertEquals("Thu Mar 23 14:30:45 GMT 2023", stdDateFormat.format(date2));
    }

    // Test parsing invalid date strings
    @Test(expected = ParseException.class)
    public void testParseInvalidString() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("Invalid date string");
    }
}