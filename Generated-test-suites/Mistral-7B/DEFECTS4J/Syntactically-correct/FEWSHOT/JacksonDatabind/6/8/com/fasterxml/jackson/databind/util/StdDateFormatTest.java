package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testGetBlueprintISO8601Format() {
        // This method is deprecated, but let's test it for completeness
        assertNotNull(StdDateFormat.getBlueprintISO8601Format());
    }

    @Test(expected = Deprecated.class)
    public void testGetISO8601FormatNullTimeZone() {
        StdDateFormat.getISO8601Format(null);
    }

    @Test
    public void testGetISO8601Format() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat result = StdDateFormat.getISO8601Format(tz, Locale.US);
        assertNotNull(result);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", result.toPattern());
        assertEquals(tz, result.getTimeZone());
        assertEquals(Locale.US, result.getLocale());
    }

    @Test
    public void testGetISO8601FormatWithLocale() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat result = StdDateFormat.getISO8601Format(tz, Locale.FRENCH);
        assertNotNull(result);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", result.toPattern());
        assertEquals(tz, result.getTimeZone());
        assertEquals(Locale.FRENCH, result.getLocale());
    }

    @Test
    public void testGetRFC1123Format() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat result = StdDateFormat.getRFC1123Format(tz, Locale.US);
        assertNotNull(result);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", result.toPattern());
        assertEquals(tz, result.getTimeZone());
        assertEquals(Locale.US, result.getLocale());
    }

    @Test
    public void testGetRFC1123FormatWithLocale() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat result = StdDateFormat.getRFC1123Format(tz, Locale.FRENCH);
        assertNotNull(result);
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", result.toPattern());
        assertEquals(tz, result.getTimeZone());
        assertEquals(Locale.FRENCH, result.getLocale());
    }

    @Test
    public void testParseDateFormat() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        String dateString = "2023-03-22T14:32:12.345-04:00";
        Date date = formatter.parse(dateString);
        assertNotNull(date);

        formatter = StdDateFormat.getRFC1123Format(tz, Locale.US);

        dateString = "Wed, 22 Mar 2023 14:32:12 -0400";
        date = formatter.parse(dateString);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormat() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        String invalidDateString = "2023-3-22T14:32:12.345Z";
        formatter.parse(invalidDateString);
    }

    @Test
    public void testParseNullDateString() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        String nullDateString = null;
        try {
            formatter.parse(nullDateString);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormatDateFormat() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        Date date = new Date(167880800000L); // 22 Mar 2023 12:00:00 GMT
        String formattedDateString = formatter.format(date);
        assertEquals("2023-03-22T12:00:00Z", formattedDateString);
    }

    @Test
    public void testFormatNullDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        String nullFormattedDateString = formatter.format(null);
        assertNull(nullFormattedDateString);
    }

    @Test
    public void testClone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        DateFormat clonedFormatter = formatter.clone();

        assertEquals(formatter.getTimeZone(), clonedFormatter.getTimeZone());
        assertEquals(formatter.getLocale(), clonedFormatter.getLocale());
    }

    @Test
    public void testToString() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat formatter = StdDateFormat.getISO8601Format(tz, Locale.US);

        String toString = formatter.toString();
        assertTrue(toString.contains("DateFormat " + StdDateFormat.class
                .getName() + " (timezone: " + tz + ")"));
        assertTrue(toString.contains("locale: " + Locale.US));
    }
}