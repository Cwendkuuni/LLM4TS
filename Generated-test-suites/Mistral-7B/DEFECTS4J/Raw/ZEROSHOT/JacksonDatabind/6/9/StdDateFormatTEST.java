package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    private static final String ISO_DATE_WITH_ZULU = "2022-12-31T23:59:59.123Z";
    private static final String ISO_DATE_WITH_TIMEZONE = "2022-12-31T23:59:59.123+01:00";
    private static final String RFC1123_DATE = "Sat, 31 Dec 2022 23:59:59 +0100";
    private static final String PLAIN_DATE = "2022-12-31";

    @Test
    public void testParseISO8601Date() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Date date = stdDateFormat.parse(ISO_DATE_WITH_ZULU);
        assertEquals("Expected a valid date", date, new Date(1672617599000L));
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601DateInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParseRFC1123Date() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Date date = stdDateFormat.parse(RFC1123_DATE);
        assertEquals("Expected a valid date", date, new Date(1672621599000L));
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123DateInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testParsePlainDate() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Date date = stdDateFormat.parse(PLAIN_DATE);
        assertEquals("Expected a valid date", date, new Date(1672652400000L));
    }

    @Test(expected = ParseException.class)
    public void testParsePlainDateInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testFormatDateAsISO8601() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        String dateString = stdDateFormat.format(new Date(1672617599000L), new StringBuffer(), null);
        assertEquals("Expected the date to be formatted as '2022-12-31T23:59:59.123Z'", ISO_DATE_WITH_ZULU, dateString);
    }

    @Test
    public void testFormatDateAsRFC1123() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        String dateString = stdDateFormat.format(new Date(1672621599000L), new StringBuffer(), null);
        assertEquals("Expected the date to be formatted as 'Sat, 31 Dec 2022 23:59:59 +0100'", RFC1123_DATE, dateString);
    }

    @Test
    public void testFormatDateAsPlain() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        String dateString = stdDateFormat.format(new Date(1672652400000L), new StringBuffer(), null);
        assertEquals("Expected the date to be formatted as '2022-12-31'", PLAIN_DATE, dateString);
    }

    @Test
    public void testCloneStdDateFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        StdDateFormat clonedStdDateFormat = (StdDateFormat) stdDateFormat.clone();
        assertNotSame("Cloned instance should not be the same as the original instance", stdDateFormat, clonedStdDateFormat);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat blueprintFormat = StdDateFormat.getISO8601Format(null, null);
        assertEquals("Expected the blueprint format to be an instance of SimpleDateFormat", SimpleDateFormat.class, blueprintFormat.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBlueprintISO8601FormatClone() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        DateFormat blueprintFormat = stdDateFormat.getBlueprintISO8601Format();
        blueprintFormat.clone();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBlueprintISO8601FormatWithTimezone() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.getBlueprintISO8601Format(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBlueprintISO8601FormatWithNullLocale() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.getBlueprintISO8601Format(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBlueprintRFC1123FormatWithNullLocale() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.getRFC1123Format(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBlueprintRFC1123FormatWithTimezone() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.getRFC1123Format(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBlueprintRFC1123Format() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.getRFC1123Format(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBlueprintISO8601Format() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        stdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetDefaultTimeZone() {
        TimeZone defaultTimeZone = StdDateFormat.getDefaultTimeZone();
        assertEquals("Expected the default timezone to be 'GMT'", "GMT", defaultTimeZone.getID());
    }

    @Test
    public void testGetDefaultLocale() {
        Locale defaultLocale = StdDateFormat.getDefaultLocale();
        assertEquals("Expected the default locale to be 'US'", "US", defaultLocale.toString());
    }
}