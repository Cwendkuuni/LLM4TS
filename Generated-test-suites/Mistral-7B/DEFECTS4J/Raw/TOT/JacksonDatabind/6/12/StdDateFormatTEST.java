package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParsingISO8601WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRANCE);
        Date date = dateFormat.parse("2019-12-31T23:59:59.123+01:00");
        assertEquals("2019-12-31 22:59:59.123", dateFormat.format(date));
    }

    @Test
    public void testParsingISO8601WithoutTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2019-12-31T23:59:59.123Z");
        assertEquals("2019-12-31 22:59:59.123", dateFormat.format(date));
    }

    @Test
    public void testParsingISO8601WithMissingMilliseconds() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2019-12-31T23:59:59Z");
        assertEquals("2019-12-31 22:59:59.000", dateFormat.format(date));
    }

    @Test
    public void testParsingISO8601WithMinusTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2019-12-31T23:59:59.123-01:00");
        assertEquals("2019-12-31 23:59:59.123", dateFormat.format(date));
    }

    @Test
    public void testParsingRFC1123WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/London"), Locale.ENGLISH);
        Date date = dateFormat.parse("Tue, 31 Dec 2019 23:59:59 GMT+00:00");
        assertEquals("Tue Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test
    public void testParsingRFC1123WithoutTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Tue, 31 Dec 2019 23:59:59 GMT");
        assertEquals("Tue Dec 31 23:59:59.000", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParsingInvalidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("Invalid date");
    }
}