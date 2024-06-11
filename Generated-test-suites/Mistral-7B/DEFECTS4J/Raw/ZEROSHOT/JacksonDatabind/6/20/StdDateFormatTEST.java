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
    public void testISO8601() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2019-12-31T23:59:59.123Z";
        Date date = dateFormat.parse(dateString);
        assertEquals("Incorrect date parsed from ISO8601 format",
                "Wed Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test
    public void testISO8601WithTimezone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        String dateString = "2019-12-31T23:59:59.123-08:00";
        Date date = dateFormat.parse(dateString);
        assertEquals("Incorrect date parsed from ISO8601 with timezone format",
                "Wed Dec 31 15:59:59 PST 2019", dateFormat.format(date));
    }

    @Test
    public void testRFC1123() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "Wed, 31 Dec 1999 23:59:59 GMT";
        Date date = dateFormat.parse(dateString);
        assertEquals("Incorrect date parsed from RFC1123 format",
                "Wed Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "InvalidDateFormat";
        dateFormat.parse(dateString);
        fail("Expected ParseException but no exception was thrown");
    }

    @Test
    public void testParsePosition() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2019-12-31T23:59:59.123Z";
        ParsePosition pp = new ParsePosition(0);
        Date date = dateFormat.parse(dateString, pp);
        assertEquals("Incorrect index", dateString.length(), pp.getIndex());
        assertEquals("Incorrect parsed date", date, dateFormat.parse(dateString));
    }

    @Test
    public void testClone() throws Exception {
        StdDateFormat original = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);
        StdDateFormat clone = original.clone();
        Date date1 = original.parse("2020-01-01T00:00:00.000-05:00");
        Date date2 = clone.parse("2020-01-01T00:00:00.000-05:00");
        assertEquals("Cloned instance should parse the same input", date1, date2);
    }
}