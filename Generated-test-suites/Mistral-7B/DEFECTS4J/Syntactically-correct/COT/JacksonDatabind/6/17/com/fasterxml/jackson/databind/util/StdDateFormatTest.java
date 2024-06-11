package com.fasterxml.jackson.databind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdDateFormatTest {

    private static final StdDateFormat STD_DATE_FORMAT = StdDateFormat.instance;

    @Test
    public void testFormatAndParseISO8601Date() throws Exception {
        Date date = new Date(1562226345000L); // Fri Jan 18 15:35:45 GMT 2019

        String iso8601Date = STD_DATE_FORMAT.format(date);
        assertEquals("2019-01-18T15:35:45.000Z", iso8601Date);

        Date parsedDate = STD_DATE_FORMAT.parse(iso8601Date);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testFormatAndParseISO8601DateWithTimeZone() throws Exception {
        Date date = new Date(1562226345000L); // Fri Jan 18 15:35:45 GMT 2019
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");

        StdDateFormat stdDateFormat = new StdDateFormat(timeZone, Locale.ENGLISH);
        String iso8601Date = stdDateFormat.format(date);
        assertEquals("2019-01-18T16:35:45.000+00:00", iso8601Date);

        Date parsedDate = stdDateFormat.parse(iso8601Date);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testFormatAndParseRFC1123Date() throws Exception {
        Date date = new Date(1562226345000L); // Fri Jan 18 15:35:45 GMT 2019

        String rfc1123Date = STD_DATE_FORMAT.format(date, new StringBuffer(), null);
        assertEquals("Fri, 18 Jan 2019 15:35:45 GMT", rfc1123Date.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = STD_DATE_FORMAT.parse(rfc1123Date, parsePosition);
        assertEquals(date, parsedDate);
        assertEquals(rfc1123Date.length(), parsePosition.getIndex());
    }

    @Test
    public void testParseInvalidDate() throws Exception {
        String invalidDate = "xxxx-xx-xxTxx:xx:xx.xxxxZ";
        assertNull(STD_DATE_FORMAT.parse(invalidDate));
    }

    @Test
    public void testParseDateWithoutTimeZoneIndicator() throws Exception {
        Date date = new Date(1562226345000L); // Fri Jan 18 15:35:45 GMT 2019

        String plainDate = "2019-01-18";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = STD_DATE_FORMAT.parse(plainDate, parsePosition);
        assertNull(parsedDate);
        assertEquals(plainDate.length(), parsePosition.getIndex());

        parsedDate = STD_DATE_FORMAT.parse(plainDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testParseDateWithInvalidTimeZoneIndicator() throws Exception {
        String dateWithInvalidTimeZone = "2019-01-18T15:35:45+xxxx";
        ParsePosition parsePosition = new ParsePosition(0);
        assertNull(STD_DATE_FORMAT.parse(dateWithInvalidTimeZone, parsePosition));
        assertEquals(dateWithInvalidTimeZone.length(), parsePosition.getIndex());

        parsedDate = STD_DATE_FORMAT.parse(dateWithInvalidTimeZone);
        assertNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws Exception {
        String invalidDateFormat = "2019-01-18-T15:35:45.000Z";
        STD_DATE_FORMAT.parse(invalidDateFormat);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormatAndTimeZoneIndicator() throws Exception {
        String invalidDateFormatWithTimeZone = "2019-01-18-T15:35:45.000Z+01:00";
        STD_DATE_FORMAT.parse(invalidDateFormatWithTimeZone);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat blueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals(DATE_FORMAT_ISO8601, blueprintISO8601Format);
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        assertEquals(_formatISO8601, iso8601Format);
    }

    @Test
    public void testGetISO8601FormatWithInvalidTimeZone() {
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(null, Locale.ENGLISH);
        assertNull(iso8601Format);
    }

    @Test
    public void testGetISO8601FormatWithInvalidLocale() {
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), null);
        assertNull(iso8601Format);
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        assertEquals(_formatRFC1123, rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithInvalidTimeZone() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(null, Locale.ENGLISH);
        assertNull(rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithInvalidLocale() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), null);
        assertNull(rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithValidTimeZoneAndLocale() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.CHINESE);
        assertEquals(_formatRFC1123, rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithValidTimeZone() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"));
        assertEquals(_formatRFC1123, rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithValidLocale() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(Locale.CHINESE);
        assertEquals(_formatRFC1123, rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithValidTimeZoneAndLocale() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.CHINESE);
        assertEquals(_formatRFC1123, rfc1123Format);
    }

    @Test
    public void testToString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: null, locale: java.util.Locale[US])", stdDateFormat.toString());

        StdDateFormat stdDateFormatWithTimeZone = new StdDateFormat(TimeZone.getTimeZone("Europe/London"), Locale.ENGLISH);
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: Europe/London, locale: java.util.Locale[EN])", stdDateFormatWithTimeZone.toString());
    }
}