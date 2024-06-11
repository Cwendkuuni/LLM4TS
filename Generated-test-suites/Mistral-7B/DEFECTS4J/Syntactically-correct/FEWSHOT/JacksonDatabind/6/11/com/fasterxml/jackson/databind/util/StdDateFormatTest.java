package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        String iso8601Date = "2022-12-31T15:30:45.123Z";
        ParsePosition parsePosition = new ParsePosition(0);

        Date date = dateFormat.parse(iso8601Date, parsePosition);
        assertEquals("Invalid date parsed", iso8601Date, dateFormat.format(date));
        assertEquals("Invalid date parsed", "Sat Dec 31 15:30:45 GMT", dateFormat.format(date, new java.text.DateFormatSymbols(Locale.US)));
    }

    @Test
    public void testParseISO8601WithTimezone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/London"), Locale.US);
        String iso8601Date = "2022-12-31T15:30:45.123+00:00";
        ParsePosition parsePosition = new ParsePosition(0);

        Date date = dateFormat.parse(iso8601Date, parsePosition);
        assertEquals("Invalid date parsed", iso8601Date, dateFormat.format(date));
        assertEquals("Invalid date parsed", "Sat Dec 31 16:30:45 GMT", dateFormat.format(date, new java.text.DateFormatSymbols(Locale.US)));
    }

    @Test
    public void testParseISO8601WithMilliseconds() {
        StdDateFormat dateFormat = new StdDateFormat();
        String iso8601Date = "2022-12-31T15:30:45.123+00:00";
        ParsePosition parsePosition = new ParsePosition(0);

        Date date = dateFormat.parse(iso8601Date, parsePosition);
        assertEquals("Invalid date parsed", iso8601Date, dateFormat.format(date));
        assertEquals("Invalid date parsed", "Sat Dec 31 16:30:45 GMT", dateFormat.format(date, new java.text.DateFormatSymbols(Locale.US)));
    }

    @Test
    public void testParseISO8601WithInvalidFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        String iso8601Date = "2022-12-31T15:30:45+00:00";
        ParsePosition parsePosition = new ParsePosition(0);

        try {
            dateFormat.parse(iso8601Date, parsePosition);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected ParseException
        }
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        String rfc1123Date = "Sat, 31 Dec 2022 15:30:45 GMT";
        ParsePosition parsePosition = new ParsePosition(0);

        Date date = dateFormat.parse(rfc1123Date, parsePosition);
        assertEquals("Invalid date parsed", rfc1123Date, dateFormat.format(date));
        assertEquals("Invalid date parsed", "Sat Dec 31 15:30:45 GMT", dateFormat.format(date, new java.text.DateFormatSymbols(Locale.US)));
    }

    @Test
    public void testParseRFC1123WithTimezone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/London"), Locale.US);
        String rfc1123Date = "Sat, 31 Dec 2022 15:30:45 +0000";
        ParsePosition parsePosition = new ParsePosition(0);

        Date date = dateFormat.parse(rfc1123Date, parsePosition);
        assertEquals("Invalid date parsed", rfc1123Date, dateFormat.format(date));
        assertEquals("Invalid date parsed", "Sat Dec 31 16:30:45 GMT", dateFormat.format(date, new java.text.DateFormatSymbols(Locale.US)));
    }

    @Test
    public void testParseRFC1123WithInvalidTimezone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/London"), Locale.US);
        String rfc1123Date = "Sat, 31 Dec 2022 15:30:45 EEST";
        ParsePosition parsePosition = new ParsePosition(0);

        try {
            dateFormat.parse(rfc1123Date, parsePosition);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected ParseException
        }
    }

    @Test
    public void testParsePlainDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String plainDate = "2022-12-31";
        ParsePosition parsePosition = new ParsePosition(0);

        Date date = dateFormat.parse(plainDate, parsePosition);
        assertEquals("Invalid date parsed", plainDate, dateFormat.format(date));
        assertEquals("Invalid date parsed", "Fri Dec 31 00:00:00 GMT", dateFormat.format(date, new java.text.DateFormatSymbols(Locale.US)));
    }

    @Test
    public void testParsePlainDateWithInvalidFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        String plainDate = "31-12-2022";
        ParsePosition parsePosition = new ParsePosition(0);

        try {
            dateFormat.parse(plainDate, parsePosition);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected ParseException
        }
    }
}