package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;
        String isoDate = "2023-03-26T14:30:45.123Z";

        date = dateFormat.parse(isoDate);
        assertEquals("26 Mar 2023, 14:30:45 UTC", dateFormat.format(date));

        // Test with timezone
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        date = dateFormat.parse(isoDate);
        assertEquals("26 Mar 2023, 15:30:45 CET", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithOffset() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;
        String isoDate = "2023-03-26T14:30:45-04:00";

        date = dateFormat.parse(isoDate);
        assertEquals("26 Mar 2023, 10:30:45 EDT", dateFormat.format(date));

        // Test with timezone and locale
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("America/New_York"))
                .withLocale(Locale.US);
        date = dateFormat.parse(isoDate);
        assertEquals("26 Mar 2023, 10:30:45 EDT", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;
        String rfcDate = "Mon, 26 Mar 2023 14:30:45 GMT";

        date = dateFormat.parse(rfcDate);
        assertEquals("26 Mar 2023, 14:30:45 UTC", dateFormat.format(date));

        // Test with timezone
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        date = dateFormat.parse(rfcDate);
        assertEquals("26 Mar 2023, 15:30:45 CET", dateFormat.format(date));
    }

    @Test
    public void testParsePlainDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;
        String plainDate = "26-Mar-2023";

        date = dateFormat.parse(plainDate);
        assertEquals("26 Mar 2023, 00:00:00 UTC", dateFormat.format(date));

        // Test with timezone and locale
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"))
                .withLocale(Locale.FRENCH);
        date = dateFormat.parse(plainDate);
        assertEquals("26 mars 2023", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testInvalidDateFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        String invalidDate = "InvalidDate";

        dateFormat.parse(invalidDate);
        fail("ParseException expected");
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date(1679442400000L); // Mon Mar 26 00:00:00 GMT 2023

        assertEquals("Mon Mar 26 00:00:00 GMT 2023", dateFormat.format(date));

        // Test with timezone
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Mon Mar 26 01:00:00 CET 2023", dateFormat.format(date));

        // Test with locale
        dateFormat = dateFormat.withLocale(Locale.FRENCH);
        assertEquals("lun. 26 mars 2023", dateFormat.format(date));
    }
}