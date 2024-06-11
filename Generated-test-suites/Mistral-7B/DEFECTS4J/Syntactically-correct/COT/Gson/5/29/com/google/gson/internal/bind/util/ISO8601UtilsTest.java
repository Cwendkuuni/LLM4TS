package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();

        // Test default format
        String formatted = ISO8601Utils.format(date);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formatted.substring(0, formatted.length() - 1));
        assertEquals(date, ISO8601Utils.parse(formatted, new ParsePosition(0)).getTime());

        // Test format with millis
        String formattedMillis = ISO8601Utils.format(date, true);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formattedMillis.substring(0, formattedMillis.length() - 1));
        assertEquals(date, ISO8601Utils.parse(formattedMillis, new ParsePosition(0)).getTime());

        // Test format with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");
        String formattedCustomTimeZone = ISO8601Utils.format(date, false, customTimeZone);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss'Z'", formattedCustomTimeZone.substring(0, formattedCustomTimeZone.length() - 1));
        assertEquals(date, ISO8601Utils.parse(formattedCustomTimeZone, new ParsePosition(0)).getTime());

        // Test negative timezone offset
        TimeZone negativeTimeZone = TimeZone.getTimeZone("Pacific/Apia");
        String formattedNegativeTimeZone = ISO8601Utils.format(date, false, negativeTimeZone);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss[-]HH:mm'Z'", formattedNegativeTimeZone.substring(0, formattedNegativeTimeZone.length() - 1));
        assertEquals(date, ISO8601Utils.parse(formattedNegativeTimeZone, new ParsePosition(0)).getTime());
    }

    @Test
    public void testParse() {
        // Test parsing valid dates

        // Test parsing date without time and timezone
        Date date = ISO8601Utils.parse("2022-03-01", new ParsePosition(0));
        assertEquals("Wed Mar 01 00:00:00 CET 2022", new GregorianCalendar(TimeZone.getDefault(), Locale.US).format(date));

        // Test parsing date with time but no timezone
        Date dateWithTime = ISO8601Utils.parse("2022-03-01T12:34:56", new ParsePosition(0));
        assertEquals("Wed Mar 01 12:34:56 CET 2022", new GregorianCalendar(TimeZone.getDefault(), Locale.US).format(dateWithTime));

        // Test parsing date with time and timezone
        Date dateWithTimeAndZone = ISO8601Utils.parse("2022-03-01T12:34:56.789Z", new ParsePosition(0));
        assertEquals("Wed Mar 01 12:34:56.789 Z", new GregorianCalendar(TimeZone.getTimeZone("Z"), Locale.US).format(dateWithTimeAndZone));

        // Test parsing date with negative timezone
        Date dateWithNegativeTimeZone = ISO8601Utils.parse("2022-03-01T12:34:56.789-05:00", new ParsePosition(0));
        assertEquals("Sun Feb 26 17:34:56.789 CET 2022", new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"), Locale.US).format(dateWithNegativeTimeZone));

        // Test parsing date with time and custom timezone
        Date dateWithCustomTimeZone = ISO8601Utils.parse("2022-03-01T12:34:56.789+02:00", new ParsePosition(0));
        assertEquals("Wed Mar 01 14:34:56.789 CET 2022", new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"), Locale.US).format(dateWithCustomTimeZone));

        // Test parsing date with invalid format
        try {
            ISO8601Utils.parse("2022-3-01", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid year
        try {
            ISO8601Utils.parse("0-3-01", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid month
        try {
            ISO8601Utils.parse("2022-13-01", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid day
        try {
            ISO8601Utils.parse("2022-02-33", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid hour
        try {
            ISO8601Utils.parse("2022-03-01T25:34:56.789", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid minute
        try {
            ISO8601Utils.parse("2022-03-01T12:60:56.789", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid second
        try {
            ISO8601Utils.parse("2022-03-01T12:34:60.789", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid millisecond
        try {
            ISO8601Utils.parse("2022-03-01T12:34:56.123456789", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing date with invalid timezone
        try {
            ISO8601Utils.parse("2022-03-01T12:34:56.123+24:00", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}