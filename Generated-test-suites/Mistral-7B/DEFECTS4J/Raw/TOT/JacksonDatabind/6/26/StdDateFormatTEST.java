package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        // Test parsing valid ISO-8601 dates with various timezones
        Date date1 = df.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Expected valid date from ISO-8601 format with 'Z'", new Date(0), date1);

        Date date2 = df.parse("2022-12-31T23:59:59.123-05:00");
        assertEquals("Expected valid date from ISO-8601 format with timezone offset", new Date(-1 * 5 * 60 * 60 * 1000L), date2);

        Date date3 = df.parse("2022-12-31T23:59:59.123+02:00");
        assertEquals("Expected valid date from ISO-8601 format with timezone offset", new Date(2 * 60 * 60 * 1000L), date3);

        // Test parsing valid ISO-8601 dates without timezone
        Date date4 = df.parse("2022-12-31T23:59:59.123");
        assertEquals("Expected valid date from ISO-8601 format with no timezone specified", new Date(0), date4);

        // Test parsing valid ISO-8601 dates with milliseconds but no timezone
        Date date5 = df.parse("2022-12-31T23:59:59.123456");
        assertEquals("Expected valid date from ISO-8601 format with milliseconds and no timezone", new Date(0 + 123456), date5);

        // Test parsing valid ISO-8601 date with just the date
        Date date6 = df.parse("2022-12-31");
        assertEquals("Expected valid date from ISO-8601 format with just the date", new Date(0), date6);

        // Test parsing invalid ISO-8601 format
        try {
            df.parse("2022-12-31T23:59:59.123abc");
            fail("Expected ParseException for invalid format");
        } catch (ParseException e) {
            // Expected
        }

        try {
            df.parse("2022-12-31T23:59:59.123-05:00abc");
            fail("Expected ParseException for invalid format");
        } catch (ParseException e) {
            // Expected
        }

        try {
            df.parse("2022-12-31T23:59:59.123+02:00abc");
            fail("Expected ParseException for invalid format");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseRFC1123() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        // Test parsing valid RFC-1123 dates with various timezones
        Date date1 = df.parse("Fri, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Expected valid date from RFC-1123 format with GMT timezone", new Date(0), date1);

        Date date2 = df.parse("Fri, 31 Dec 2022 23:59:59 EST");
        assertEquals("Expected valid date from RFC-1123 format with EST timezone", new Date(-1 * 5 * 60 * 60 * 1000L), date2);

        Date date3 = df.parse("Fri, 31 Dec 2022 23:59:59 PST");
        assertEquals("Expected valid date from RFC-1123 format with PST timezone", new Date(2 * 60 * 60 * 1000L), date3);

        // Test parsing valid RFC-1123 dates without timezone
        Date date4 = df.parse("Fri, 31 Dec 2022 23:59:59");
        assertEquals("Expected valid date from RFC-1123 format with no timezone specified", new Date(0), date4);

        // Test parsing invalid RFC-1123 format
        try {
            df.parse("Fri, 31 Dec 2022 23:59:59 GMTabc");
            fail("Expected ParseException for invalid format");
        } catch (ParseException e) {
            // Expected
        }

        try {
            df.parse("Fri, 31 Dec 2022 23:59:59 ESTabc");
            fail("Expected ParseException for invalid format");
        } catch (ParseException e) {
            // Expected
        }

        try {
            df.parse("Fri, 31 Dec 2022 23:59:59 PSTabc");
            fail("Expected ParseException for invalid format");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat df = StdDateFormat.instance;

        Date date = new Date();

        // Test formatting valid dates using various formats and timezones
        String formatted1 = df.format(date, new StringBuilder(), null);
        assertEquals("Expected valid formatted date using default format and timezone", "Sat Jan 22 11:11:11 GMT", formatted1);

        String formatted2 = df.format(date, new StringBuilder(), new GregorianCalendar(2022, 11, 31, 23, 59, 59, 0),Locale.US);
        assertEquals("Expected valid formatted date using specified format, timezone and locale", "Sat Dec 31 23:59:59 EST 2022", formatted2);

        String formatted3 = df.format(date, new StringBuilder(), new GregorianCalendar(2022, 11, 31, 23, 59, 59, 0),Locale.ENGLISH);
        assertEquals("Expected valid formatted date using specified format, timezone and locale", "Sat Dec 31 23:59:59 EST 2022", formatted3);

        String formatted4 = df.format(date, new StringBuilder(), new GregorianCalendar(2022, 11, 31, 23, 59, 59, 0),Locale.FRENCH);
        assertEquals("Expected valid formatted date using specified format, timezone and locale", "sam. 31 déc. 2022 23:59:59 EST", formatted4);
    }

    @Test
    public void testClone() throws Exception {
        StdDateFormat df1 = new StdDateFormat();
        StdDateFormat df2 = df1.clone();

        // Test that cloned DateFormat instances have same format, locale and timezone
        assertEquals("Expected cloned DateFormat instances to have same format", df1.getClass(), df2.getClass());
        assertEquals("Expected cloned DateFormat instances to have same locale", df1.getLocale(), df2.getLocale());
        assertEquals("Expected cloned DateFormat instances to have same timezone", df1.getTimeZone(), df2.getTimeZone());

        // Test that cloning does not change original DateFormat instance
        df1.setTimeZone(TimeZone.getTimeZone("UTC+01:00"));
        assertNotSame("Expected cloned DateFormat instance to be different from original", df1, df2);
    }
}