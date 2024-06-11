package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat instance = StdDateFormat.instance;
        assertNotNull(instance);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat df = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(df);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, df.toPattern());
    }

    @Test(expected = DeprecatedException.class)
    public void testGetBlueprintISO8601FormatDeprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat df = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertNotNull(df);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, df.toPattern());
    }

    @Test(expected = NullPointerException.class)
    public void testGetISO8601FormatNullTimeZone() {
        StdDateFormat.getISO8601Format(Locale.US, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetISO8601FormatInvalidLocale() {
        StdDateFormat.getISO8601Format(Locale.CHINA, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetISO8601FormatInvalidTimeZone() {
        StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("Asia/Tokyo"));
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat df = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertNotNull(df);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_RFC1123, df.toPattern());
    }

    @Test(expected = DeprecatedException.class)
    public void testGetRFC1123FormatDeprecated() {
        StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testGetRFC1123FormatWithTimeZone() {
        DateFormat df = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertNotNull(df);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_RFC1123, df.toPattern());
    }

    @Test(expected = NullPointerException.class)
    public void testGetRFC1123FormatNullLocale() {
        StdDateFormat.getRFC1123Format(null, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRFC1123FormatInvalidLocale() {
        StdDateFormat.getRFC1123Format(Locale.CHINA, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRFC1123FormatInvalidTimeZone() {
        StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("Asia/Tokyo"));
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        Date date = df.parse("2023-03-21T14:25:36.789+02:00");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        df.parse("InvalidDateString");
        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidTimeZone() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        df.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        df.parse("2023-03-21T14:25:36.789Z");
        fail("ParseException expected");
    }

    @Test
    public void testFormat() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = expectedFormat.parse("2023-03-21T14:25:36.789+02:00");
        String result = df.format(date);
        assertEquals("2023-03-21T14:25:36.789+02:00", result);
    }

    @Test
    public void testFormatWithTimeZone() throws ParseException {
        StdDateFormat df = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
        SimpleDateFormat expectedFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date date = expectedFormat.parse("Tue, 21 Mar 2023 14:25:36 GMT");
        String result = df.format(date);
        assertEquals("Tue, 21 Mar 2023 14:25:36 GMT", result);
    }

    @Test
    public void testClone() throws ParseException {
        StdDateFormat original = new StdDateFormat();
        StdDateFormat clone = original.clone();
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2023-03-21T14:25:36.789+02:00");
        String originalFormat = original.format(date);
        String cloneFormat = clone.format(date);
        assertEquals(originalFormat, cloneFormat);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat df = new StdDateFormat();
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(TimeZone.getTimeZone("UTC"), df.getTimeZone());
    }

    @Test
    public void testSetLocale() {
        StdDateFormat df = new StdDateFormat();
        df.setLocale(Locale.CHINA);
        assertEquals(Locale.CHINA, df.getLocale());
    }
}