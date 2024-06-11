package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * StdDateFormatTest.java - JUnit 4 test file for StdDateFormat class
 */
public class StdDateFormatTest {

    // Test data
    private static final String DATE_STR_ISO8601 = "2022-12-31T23:59:59.999Z";
    private static final String DATE_STR_ISO8601_Z = "2022-12-31T23:59:59.999Z";
    private static final String DATE_STR_ISO8601_PLAIN = "2022-12-31";
    private static final String DATE_STR_RFC1123 = "Sat, 31 Dec 2022 23:59:59 GMT";
    private static final String DATE_STR_RFC1123_Z = "Sat, 31 Dec 2022 23:59:59 +0000";
    private static final String DATE_STR_INVALID_1 = "2022-12-32";
    private static final String DATE_STR_INVALID_2 = "2022-12-31T24:60:60.000Z";
    private static final String DATE_STR_INVALID_3 = "Sat, 32 Dec 2022 23:59:59 GMT";

    @Test
    public void testGetInstance() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertEquals("Calendar format: " + dateFormat.getClass().getName(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateFormat.getDateFormatString());
        assertEquals("Timezone: " + dateFormat.getTimeZone(), "GMT", dateFormat.getTimeZone().getID());
    }

    @Test
    public void testGetBlueprintISO8601Format() throws Exception {
        DateFormat blueprintFormat = StdDateFormat.getBlueprintISO8601Format();
        assertEquals("Calendar format: " + blueprintFormat.getClass().getName(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", blueprintFormat.getDateFormatString());
        assertEquals("Timezone: " + blueprintFormat.getTimeZone(), "GMT", blueprintFormat.getTimeZone().getID());
    }

    @Test
    public void testGetISO8601Format() throws Exception {
        DateFormat isoFormat = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        assertEquals("Calendar format: " + isoFormat.getClass().getName(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", isoFormat.getDateFormatString());
        assertEquals("Timezone: " + isoFormat.getTimeZone(), "UTC", isoFormat.getTimeZone().getID());
    }

    @Test
    public void testGetISO8601FormatWithCustomTimeZone() throws Exception {
        DateFormat isoFormat = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("America/New_York"), Locale.US);
        assertEquals("Calendar format: " + isoFormat.getClass().getName(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", isoFormat.getDateFormatString());
        assertEquals("Timezone: " + isoFormat.getTimeZone(), "America/New_York", isoFormat.getTimeZone().getID());
    }

    @Test
    public void testGetRFC1123Format() throws Exception {
        DateFormat rfcFormat = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.US);
        assertEquals("Calendar format: " + rfcFormat.getClass().getName(), "EEE, dd MMM yyyy HH:mm:ss zzz", rfcFormat.getDateFormatString());
        assertEquals("Timezone: " + rfcFormat.getTimeZone(), "UTC", rfcFormat.getTimeZone().getID());
    }

    @Test
    public void testGetRFC1123FormatWithCustomTimeZone() throws Exception {
        DateFormat rfcFormat = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        assertEquals("Calendar format: " + rfcFormat.getClass().getName(), "EEE, dd MMM yyyy HH:mm:ss zzz", rfcFormat.getDateFormatString());
        assertEquals("Timezone: " + rfcFormat.getTimeZone(), "America/Los_Angeles", rfcFormat.getTimeZone().getID());
    }

    @Test
    public void testParseFormatISO8601() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse(DATE_STR_ISO8601);
        assertNull("Parsing ISO-8601 date should return null", date);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        date = dateFormat.parse(DATE_STR_ISO8601);
        assertEquals("Parsed ISO-8601 date: " + date, "Fri Dec 31 23:59:59 UTC 2022", date.toString());
    }

    @Test
    public void testParseFormatISO8601_Z() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse(DATE_STR_ISO8601_Z);
        assertNull("Parsing ISO-8601 date with 'Z' should return null", date);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        date = dateFormat.parse(DATE_STR_ISO8601_Z);
        assertEquals("Parsed ISO-8601 date with 'Z': " + date, "Fri Dec 31 23:59:59 UTC 2022", date.toString());
    }

    @Test
    public void testParseFormatISO8601Plain() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse(DATE_STR_ISO8601_PLAIN);
        assertNull("Parsing plain ISO-8601 date should return null", date);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        date = dateFormat.parse(DATE_STR_ISO8601_PLAIN);
        assertEquals("Parsed plain ISO-8601 date: " + date, "Fri Dec 31 00:00:00 UTC 2022", date.toString());
    }

    @Test
    public void testParseFormatRFC1123() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse(DATE_STR_RFC1123);
        assertNull("Parsing RFC-1123 date should return null", date);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        date = dateFormat.parse(DATE_STR_RFC1123);
        assertEquals("Parsed RFC-1123 date: " + date, "Sat Dec 31 23:59:59 UTC 2022", date.toString());
    }

    @Test
    public void testParseFormatRFC1123_Z() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse(DATE_STR_RFC1123_Z);
        assertNull("Parsing RFC-1123 date with 'Z' should return null", date);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        date = dateFormat.parse(DATE_STR_RFC1123_Z);
        assertEquals("Parsed RFC-1123 date with 'Z': " + date, "Sat Dec 31 23:59:59 UTC 2022", date.toString());
    }

    @Test(expected = ParseException.class)
    public void testParseFormatInvalidDate_1() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        dateFormat.parse(DATE_STR_INVALID_1);
    }

    @Test(expected = ParseException.class)
    public void testParseFormatInvalidDate_2() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        dateFormat.parse(DATE_STR_INVALID_2);
    }

    @Test(expected = ParseException.class)
    public void testParseFormatInvalidDate_3() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        dateFormat.parse(DATE_STR_INVALID_3);
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date();
        StringBuilder sb = new StringBuilder();
        String formattedDate = dateFormat.format(date, sb, new FieldPosition(0));
        String expectedFormat = "Sat Dec 17 12:02:18 EET 2022";
        assertEquals("Formatted date: " + formattedDate, expectedFormat, formattedDate);

        dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        formattedDate = dateFormat.format(date, sb, new FieldPosition(0));
        expectedFormat = "Sat Dec 17 11:02:18 UTC 2022";
        assertEquals("Formatted UTC date: " + formattedDate, expectedFormat, formattedDate);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTimeZoneNull() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(null);
    }

    @Test
    public void testClone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertEquals("Cloned date format timezone: " + clonedDateFormat.getTimeZone(), "UTC", clonedDateFormat.getTimeZone().getID());
        assertEquals("Cloned date format locale: " + clonedDateFormat.getLocale(), Locale.US, clonedDateFormat.getLocale());
    }
}