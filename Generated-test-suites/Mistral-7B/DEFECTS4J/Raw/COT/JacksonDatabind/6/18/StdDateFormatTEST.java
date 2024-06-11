package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    private static final String ISO_8601 = "2022-12-31T23:59:59.123Z";
    private static final String RFC_1123 = "Sat, 31 Dec 2022 23:59:59 GMT";
    private static final String PLAIN = "2022-12-31";

    @Test
    public void testParseISO8601Format() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse(ISO_8601);
        assertEquals("Incorrect date from ISO-8601 format", new Date(1672467199000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601Format_InvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParsePlainFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse(PLAIN);
        assertEquals("Incorrect date from plain format", new Date(1672467200000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParsePlainFormat_InvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse(RFC_1123);
        assertEquals("Incorrect date from RFC-1123 format", new Date(1672467199000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123Format_InvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParseISO8601FormatWithCustomTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        Date date = stdDateFormat.parse(ISO_8601);
        assertEquals("Incorrect date from ISO-8601 format with custom timezone", new Date(1672467199000L + 3600000), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601FormatWithCustomTimeZone_InvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParsePlainFormatWithCustomTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        Date date = stdDateFormat.parse(PLAIN);
        assertEquals("Incorrect date from plain format with custom timezone", new Date(1672467200000L + 3600000), date);
    }

    @Test(expected = ParseException.class)
    public void testParsePlainFormatWithCustomTimeZone_InvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParseRFC1123FormatWithCustomTimeZone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        Date date = stdDateFormat.parse(RFC_1123);
        assertEquals("Incorrect date from RFC-1123 format with custom timezone", new Date(1672467199000L + 3600000), date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123FormatWithCustomTimeZone_InvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testFormatISO8601Format() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String formattedDate = stdDateFormat.format(new Date(1672467199000L), new StringBuilder(), new FieldPosition(0));
        assertEquals("Incorrect format of date from ISO-8601 format", ISO_8601, formattedDate);
    }

    @Test
    public void testFormatPlainFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String formattedDate = stdDateFormat.format(new Date(1672467200000L), new StringBuilder(), new FieldPosition(0));
        assertEquals("Incorrect format of date from plain format", PLAIN, formattedDate);
    }

    @Test
    public void testFormatRFC1123Format() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String formattedDate = stdDateFormat.format(new Date(1672467199000L), new StringBuilder(), new FieldPosition(0));
        assertEquals("Incorrect format of date from RFC-1123 format", RFC_1123, formattedDate);
    }

    @Test
    public void testFormatISO8601FormatWithCustomLocale() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.SIMPLIFIED_CHINESE);
        String formattedDate = stdDateFormat.format(new Date(1672467199000L), new StringBuilder(), new FieldPosition(0));
        assertEquals("Incorrect format of date from ISO-8601 format with custom locale", "2022-12-31T23:59:59.123+08:00", formattedDate);
    }

    @Test
    public void testFormatPlainFormatWithCustomLocale() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.SIMPLIFIED_CHINESE);
        String formattedDate = stdDateFormat.format(new Date(1672467200000L), new StringBuilder(), new FieldPosition(0));
        assertEquals("Incorrect format of date from plain format with custom locale", "2022-12-31", formattedDate);
    }

    @Test
    public void testFormatRFC1123FormatWithCustomLocale() {
        StdDateFormat stdDateFormat = new StdDateFormat(Locale.SIMPLIFIED_CHINESE);
        String formattedDate = stdDateFormat.format(new Date(1672467199000L), new StringBuilder(), new FieldPosition(0));
        assertEquals("Incorrect format of date from RFC-1123 format with custom locale", "星期六, 31 十二月 2022 23:59:59 CST", formattedDate);
    }
}