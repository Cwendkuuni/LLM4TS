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
    public void testStandardDateFormats() throws Exception {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test parsing with default timezone and locale
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Invalid date: 2022-12-31T23:59:59.123Z", date, new Date(1672492799123L));

        date = stdDateFormat.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Invalid date: Sat, 31 Dec 2022 23:59:59 GMT", date, new Date(1672492799000L));

        date = stdDateFormat.parse("Sat Dec 31 23:59:59 GMT 2022");
        assertEquals("Invalid date: Sat Dec 31 23:59:59 GMT 2022", date, new Date(1672492799000L));

        date = stdDateFormat.parse("31-Dec-2022 23:59:59");
        assertEquals("Invalid date: 31-Dec-2022 23:59:59", date, new Date(1672492799000L));

        date = stdDateFormat.parse("2022-12-31");
        assertEquals("Invalid date: 2022-12-31", date, new Date(1672492799000L));

        // Test parsing with custom timezone and locale
        StdDateFormat stdDateFormatCustomTz = stdDateFormat.withTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        date = stdDateFormatCustomTz.parse("2022-12-31T23:59:59.123Z");
        long expectedDate = 1672492799123L - (TimeZone.getTimeZone("America/Los_Angeles").getOffset(System.currentTimeMillis()) / 60000);
        assertEquals("Invalid date: 2022-12-31T23:59:59.123Z in America/Los_Angeles timezone", date, new Date(expectedDate));

        StdDateFormat stdDateFormatCustomLocale = stdDateFormat.withLocale(Locale.CHINESE);
        date = stdDateFormatCustomLocale.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Invalid date: 2022-12-31T23:59:59.123Z in Chinese locale", date, new Date(1672492799123L));

        // Test cloning
        StdDateFormat stdDateFormatCloned = stdDateFormat.clone();
        date = stdDateFormatCloned.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Cloned StdDateFormat should parse the same date as original", date, new Date(1672492799123L));

        // Test parsing edge cases
        try {
            stdDateFormat.parse("Invalid date format");
            fail("Expected ParseException when parsing invalid date format");
        } catch (ParseException e) {
            // Expected
        }

        try {
            stdDateFormat.parse("2022-12-32T23:59:59.123Z");
            fail("Expected ParseException when parsing invalid date (month out of range)");
        } catch (ParseException e) {
            // Expected
        }

        try {
            stdDateFormat.parse("2022-12-31T24:60:59.123Z");
            fail("Expected ParseException when parsing invalid date (hour out of range)");
        } catch (ParseException e) {
            // Expected
        }

        try {
            stdDateFormat.parse("2022-12-31T23:59:59.123AA");
            fail("Expected ParseException when parsing invalid date (invalid timezone)");
        } catch (ParseException e) {
            // Expected
        }

        try {
            stdDateFormat.parse("2022-12-31T23:59:59.123Z99999999999999999");
            fail("Expected ParseException when parsing invalid date (milliseconds out of range)");
        } catch (ParseException e) {
            // Expected
        }
    }
}