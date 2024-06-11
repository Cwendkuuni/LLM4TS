package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for StdDateFormat class.
 */
public class StdDateFormatTest {

    /**
     * Test constructor and getDefaultTimeZone method.
     */
    @Test
    public void testConstructorAndGetDefaultTimeZone() {
        assertEquals("GMT", StdDateFormat.getDefaultTimeZone().getDisplayName());
    }

    /**
     * Test getISO8601Format method with default locale and timezone.
     */
    @Test
    public void testGetISO8601FormatWithDefaultLocaleAndTimezone() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateFormat.toPattern());
    }

    /**
     * Test parse method with valid ISO-8601 date string.
     */
    @Test
    public void testParseValidISO8601DateString() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        Date date = dateFormat.parse("2023-03-15T14:30:15.001Z");
        assertEquals("Wed Mar 15 14:30:15 UTC 2023", date.toString());
    }

    /**
     * Test parse method with valid RFC 1123 date string.
     */
    @Test
    public void testParseValidRFC1123DateString() {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format();
        Date date = dateFormat.parse("Wed, 15 Mar 2023 14:30:15 +0000");
        assertEquals("Wed Mar 15 14:30:15 UTC 2023", date.toString());
    }

    /**
     * Test parse method with invalid date string.
     */
    @Test
    public void testParseInvalidDateString() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        assertThrows(ParseException.class, () -> dateFormat.parse("InvalidDateString"));
    }

    /**
     * Test parse method with null date string.
     */
    @Test
    public void testParseNullDateString() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        assertNull(dateFormat.parse(null));
    }

    /**
     * Test format method with valid date object.
     */
    @Test
    public void testFormatValidDateObject() {
        Date date = new Date(1678400000000L); // Wed, 15 Mar 2023 14:30:15 UTC
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        String result = dateFormat.format(date);
        assertEquals("2023-03-15T14:30:15.000Z", result);
    }

    /**
     * Test format method with null date object.
     */
    @Test
    public void testFormatNullDateObject() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        assertNull(dateFormat.format(null));
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        DateFormat clonedDateFormat = dateFormat.clone();
        assertNotSame(dateFormat, clonedDateFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", clonedDateFormat.toPattern());
    }

    /**
     * Test setTimeZone method with valid timezone.
     */
    @Test
    public void testSetTimeZoneWithValidTimezone() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        dateFormat.setTimeZone(timeZone);
        assertEquals("America/New_York", dateFormat.getTimeZone().getID());
    }

    /**
     * Test setTimeZone method with null timezone.
     */
    @Test
    public void testSetTimeZoneWithNullTimezone() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        dateFormat.setTimeZone(null);
        assertNull(dateFormat.getTimeZone());
    }

    /**
     * Test setLocale method with valid locale.
     */
    @Test
    public void testSetLocaleWithValidLocale() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        Locale locale = Locale.FRENCH;
        dateFormat.setLocale(locale);
        assertEquals("fr_FR", locale.getLanguage());
    }

    /**
     * Test setLocale method with null locale.
     */
    @Test
    public void testSetLocaleWithNullLocale() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        dateFormat.setLocale(null);
        assertNull(dateFormat.getLocale());
    }
}