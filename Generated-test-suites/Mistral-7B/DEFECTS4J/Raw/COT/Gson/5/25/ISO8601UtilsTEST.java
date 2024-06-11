package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

/**
 * Test cases for ISO8601Utils class
 *
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        // Test format method with default timezone and no milliseconds precision
        Date date = new GregorianCalendar(2023, 11, 25).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2023-12-25T00:00:00Z", formattedDate);

        // Test format method with timezone and milliseconds precision
        Date dateWithMillis = new GregorianCalendar(2023, 11, 25, 12, 30, 45).getTime();
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, true, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2023-12-25T19:30:45-08:00", formattedDateWithMillis);

        // Test format method with timezone and no milliseconds precision
        Date dateWithTimezone = new GregorianCalendar(2023, 11, 25, 12, 30, 45, 123).getTime();
        String formattedDateWithTimezoneNoMillis = ISO8601Utils.format(dateWithTimezone, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2023-12-25T19:30:45Z", formattedDateWithTimezoneNoMillis);
    }

    @Test
    public void testParse() throws Exception {
        // Test parse method with valid ISO8601 date string
        String isoDate = "2023-12-25T19:30:45Z";
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
        assertEquals(ISO8601Utils.format(parsedDate), isoDate);

        // Test parse method with invalid ISO8601 date string
        String invalidIsoDate = "2023-12-25T19:30:45XXX";
        try {
            Date parsedInvalidDate = ISO8601Utils.parse(invalidIsoDate, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parse method with valid ISO8601 date string with timezone
        String isoDateWithTimezone = "2023-12-25T19:30:45-08:00";
        Date parsedDateWithTimezone = ISO8601Utils.parse(isoDateWithTimezone, new ParsePosition(0));
        assertEquals(ISO8601Utils.format(parsedDateWithTimezone, TimeZone.getTimeZone("America/Los_Angeles")), isoDateWithTimezone);

        // Test parse method with invalid ISO8601 date string with timezone
        String invalidIsoDateWithTimezone = "2023-12-25T19:30:45XXX-08:00";
        try {
            Date parsedInvalidDateWithTimezone = ISO8601Utils.parse(invalidIsoDateWithTimezone, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormatAndParseRoundTrip() throws Exception {
        // Test format and parse round trip for various date cases
        Date date1 = new GregorianCalendar(2023, 11, 25).getTime();
        String isoDate1 = ISO8601Utils.format(date1);
        Date parsedDate1 = ISO8601Utils.parse(isoDate1, new ParsePosition(0));
        assertEquals(ISO8601Utils.format(parsedDate1), isoDate1);

        Date date2 = new GregorianCalendar(2023, 11, 25, 12, 30, 45).getTime();
        String isoDate2 = ISO8601Utils.format(date2, true, TimeZone.getTimeZone("America/Los_Angeles"));
        Date parsedDate2 = ISO8601Utils.parse(isoDate2, new ParsePosition(0));
        assertEquals(ISO8601Utils.format(parsedDate2, TimeZone.getTimeZone("America/Los_Angeles")), isoDate2);
    }

    @Test
    public void testFormatAndParseRoundTripWithInvalidString() {
        // Test format and parse round trip for invalid dates should fail
        String invalidIsoDate = "2023-12-25T19:30:45XXX";
        try {
            Date parsedDateInvalid = ISO8601Utils.parse(invalidIsoDate, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            String isoDateInvalid = ISO8601Utils.format(new Date(System.currentTimeMillis() + 1000));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}