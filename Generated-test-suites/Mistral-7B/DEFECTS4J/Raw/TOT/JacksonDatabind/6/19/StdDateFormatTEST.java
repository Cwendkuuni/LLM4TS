package com.fasterxml.jackson.databind.util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class StdDateFormatTest {

    @Test
    public void testISO8601Format() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01T12:34:56.078-05:00");
        assertEquals("Expected date '2022-01-01T12:34:56.078-05:00' to be parsed to 'Sat Jan 01 17:34:56 GMT-05:00 2022'",
                new Date(1641216096000L), date);
    }

    @Test
    public void testISO8601FormatGMT() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01T12:34:56.078Z");
        assertEquals("Expected date '2022-01-01T12:34:56.078Z' to be parsed to 'Sat Jan 01 17:34:56 GMT'",
                new Date(1641216096000L), date);
    }

    @Test(expected = ParseException.class)
    public void testISO8601FormatInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidFormat");
    }

    @Test
    public void testRFC1123Format() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setLocale(Locale.ENGLISH);
        Date date = dateFormat.parse("Thu, 01 Jan 2022 12:34:56 GMT");
        assertEquals("Expected date 'Thu, 01 Jan 2022 12:34:56 GMT' to be parsed to 'Thu Jan 01 12:34:56 GMT 2022'",
                new Date(1641216096000L), date);
    }

    @Test(expected = ParseException.class)
    public void testRFC1123FormatInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setLocale(Locale.ENGLISH);
        dateFormat.parse("Thu, 01 Jan 2022 12:34:56 InvalidFormat");
    }

    @Test
    public void testPlainFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-01-01");
        assertEquals("Expected date '2022-01-01' to be parsed to 'Fri Jan 01 00:00:00 GMT 2022'",
                new Date(1641132400000L), date);
    }

    @Test(expected = ParseException.class)
    public void testPlainFormatInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("InvalidFormat");
    }

    @Test
    public void testCustomTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.ENGLISH);
        Date date = dateFormat.parse("2022-01-01T13:34:56.078+01:00");
        assertEquals("Expected date '2022-01-01T13:34:56.078+01:00' to be parsed to 'Sat Jan 01 14:34:56 CET 2022'",
                new Date(1641232696000L), date);
    }

    @Test(expected = ParseException.class)
    public void testCustomTimeZoneInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.ENGLISH);
        dateFormat.parse("InvalidFormat");
    }

    @Test
    public void testCustomLocale() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(null, Locale.FRENCH);
        Date date = dateFormat.parse("mer., 01 jan. 2022 12:34:56 GMT");
        assertEquals("Expected date 'mer., 01 jan. 2022 12:34:56 GMT' to be parsed to 'mer. jan. 01 12:34:56 GMT 2022'",
                new Date(1641216096000L), date);
    }

    @Test(expected = ParseException.class)
    public void testCustomLocaleInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(null, Locale.FRENCH);
        dateFormat.parse("InvalidFormat");
    }
}