package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateFormat.toPattern());
    }

    @Test(expected = Deprecated.class)
    public void testGetBlueprintISO8601FormatDeprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateFormat.toPattern());
    }

    @Test
    public void testParseISO8601Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("InvalidDate");
    }

    @Test
    public void testParseISO8601WithLocaleAndTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.FRANCE, TimeZone.getTimeZone("Europe/Paris"));
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
    }

    @Test
    public void testFormatDateISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        String expectedDate = simpleDateFormat.format(date);
        assertEquals(expectedDate, formattedDate);
    }

    @Test
    public void testFormatDateISO8601WithLocaleAndTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.FRANCE, TimeZone.getTimeZone("Europe/Paris"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        String expectedDate = simpleDateFormat.format(date);
        assertEquals(expectedDate, formattedDate);
    }

    @Test
    public void testFormatDateRFC1123() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        String expectedDate = simpleDateFormat.format(date);
        assertEquals(expectedDate, formattedDate);
    }

    @Test
    public void testParseRFC1123Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Mon, 31 Dec 2022 23:59:59 GMT");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123Date() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("InvalidDate");
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", dateFormat.toPattern());
    }

    @Test
    public void testGetRFC1123FormatWithLocale() {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(Locale.FRANCE);
        assertNotNull(dateFormat);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", dateFormat.toPattern());
    }

    @Test
    public void testGetRFC1123FormatWithTimeZone() {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("Europe/Paris"));
        assertNotNull(dateFormat);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", dateFormat.toPattern());
    }

    @Test
    public void testGetRFC1123FormatWithLocaleAndTimeZone() {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(Locale.FRANCE, TimeZone.getTimeZone("Europe/Paris"));
        assertNotNull(dateFormat);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", dateFormat.toPattern());
    }
}