package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    /**
     * Test method for {@link ISO8601Utils#format(Date)} with default timezone and no milliseconds precision.
     */
    @Test
    public void testFormatDefault() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 17).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2023-01-17", formatted);
    }

    /**
     * Test method for {@link ISO8601Utils#format(Date, boolean)} with default timezone and milliseconds precision.
     */
    @Test
    public void testFormatWithMillis() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 17, 15, 30, 45).getTime();
        String formatted = ISO8601Utils.format(date, true);
        assertEquals("2023-01-17T15:30:45.045Z", formatted);
    }

    /**
     * Test method for {@link ISO8601Utils#format(Date, boolean, TimeZone)} with a given timezone and milliseconds precision.
     */
    @Test
    public void testFormatWithCustomTimeZone() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 17, 15, 30, 45, 123).getTime();
        TimeZone centralEuropeanTime = TimeZone.getTimeZone("Europe/Paris");
        String formatted = ISO8601Utils.format(date, true, centralEuropeanTime);
        assertEquals("2023-01-17T16:30:45.045+01:00", formatted);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with a valid ISO date string.
     */
    @Test
    public void testParseValid() {
        String isoDate = "2023-01-17T15:30:45.045Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Wed Jan 17 15:30:45 CET 2023", parsedDate.toString());
        assertEquals(1, pos.getIndex());
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with an invalid ISO date string.
     */
    @Test
    public void testParseInvalid() {
        String invalidDate = "2023-13-17T15:30:45.045Z";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDate, pos));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with an ISO date string containing a timezone offset.
     */
    @Test
    public void testParseWithTimeZoneOffset() {
        String isoDate = "2023-01-17T15:30:45.045+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Wed Jan 17 16:30:45 CET 2023", parsedDate.toString());
        assertEquals(13, pos.getIndex());
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with an ISO date string containing an invalid timezone.
     */
    @Test
    public void testParseInvalidTimeZone() {
        String invalidTimeZone = "2023-01-17T15:30:45.045+XX:XX";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidTimeZone, pos));
    }
}