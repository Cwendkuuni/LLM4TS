package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        TimeZone defaultTimeZone = StdDateFormat.getDefaultTimeZone();
        Locale defaultLocale = StdDateFormat.DEFAULT_LOCALE;

        // Test default date format with GMT timezone
        Date date = dateFormat.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Expected correct date from default format", new Date(1673116799000L), date);

        // Test parsing with custom timezone and locale
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/London");
        Locale customLocale = Locale.ENGLISH;
        StdDateFormat customFormat = new StdDateFormat(customTimeZone, customLocale);
        date = customFormat.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Expected correct date from custom format", new Date(1673116799000L), date);

        // Test cloning
        StdDateFormat clonedFormat = customFormat.clone();
        date = clonedFormat.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Cloned format should parse the same date", new Date(1673116799000L), date);

        // Test parsing different date formats
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(defaultTimeZone, defaultLocale);
        Date rfc1123Date = rfc1123Format.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Expected correct date from RFC 1123 format", new Date(1673116799000L), rfc1123Date);

        DateFormat iso8601Format = StdDateFormat.getISO8601Format(defaultTimeZone, defaultLocale);
        Date iso8601Date = iso8601Format.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Expected correct date from ISO-8601 format", new Date(1673116799000L), iso8601Date);

        DateFormat plainFormat = StdDateFormat.getISO8601Format(defaultTimeZone, defaultLocale);
        Date plainDate = plainFormat.parse("2022-12-31");
        assertEquals("Expected correct date from plain format", new Date(1673110400000L), plainDate);

        // Test parsing invalid date strings
        try {
            dateFormat.parse("This is not a date");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("2022-12-32");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("2022-12-31T24:60:00.000Z"); // Invalid hour or minute
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("2022-12-31T23:59:59.000AAA"); // Invalid timezone indicator
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("2022-12-31T23:59:59.000+++"); // Invalid timezone offset
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}