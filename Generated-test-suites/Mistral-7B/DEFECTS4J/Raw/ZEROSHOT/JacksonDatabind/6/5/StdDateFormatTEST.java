package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Incorrect date parsed from ISO8601 format", new Date(1671711999000L), date);
    }

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Incorrect date parsed from ISO8601 format with Paris timezone", new Date(1671725599000L), date);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse("Thu, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Incorrect date parsed from RFC1123 format", new Date(1671711999000L), date);
    }

    @Test
    public void testParseRFC1123WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Date date = dateFormat.parse("Thu, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Incorrect date parsed from RFC1123 format with Paris timezone", new Date(1671725599000L), date);
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        try {
            dateFormat.parse("Invalid date format");
            fail("Expected ParseException when parsing an invalid date format");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date(1671711999000L);
        String formattedDate = dateFormat.format(date, new StringBuffer(), null);
        assertEquals("Incorrect date formatted", "2022-12-31T23:59:59.000Z", formattedDate);
    }

    @Test
    public void testFormatWithLocaleAndTimeZone() {
        StdDateFormat dateFormat = StdDateFormat.instance.withLocale(Locale.FRENCH).withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Date date = new Date(1671725599000L);
        String formattedDate = dateFormat.format(date, new StringBuffer(), null);
        assertEquals("Incorrect date formatted with French locale and Paris timezone", "mer. 31 déc. 2022 23:59:59 CET", formattedDate);
    }
}