package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatWithDefaultDateAndTime() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String expected = String.format(expectedFormat, date.getYear() + 1900, date.getMonth() + 1, date.getDate(),
                date.getHours(), date.getMinutes(), date.getSeconds());
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String expected = String.format(expectedFormat, date.getYear() + 1900, date.getMonth() + 1, date.getDate(),
                date.getHours(), date.getMinutes(), date.getSeconds(), date.getMilliseconds());
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatWithCustomTimezone() {
        Date date = new Date();
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        String formattedDate = ISO8601Utils.format(date, false, customTimeZone);
        int timeZoneOffset = customTimeZone.getOffset(date.getTime());
        int hours = Math.abs(timeZoneOffset / (60 * 1000)) / 60;
        int minutes = Math.abs(timeZoneOffset / (60 * 1000)) % 60;
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss[Z|+hh:mm]";
        String expected = String.format(expectedFormat, date.getYear() + 1900, date.getMonth() + 1, date.getDate(),
                date.getHours(), date.getMinutes(), date.getSeconds(), hours, minutes);
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatWithInvalidDate() {
        try {
            ISO8601Utils.format(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Calendar calendar = new GregorianCalendar(2022, 12, 32);
        try {
            ISO8601Utils.format(calendar.getTime());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseValidDate() {
        String dateString = "2022-12-31T23:59:59.999Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(2022, parsedDate.getYear() + 1900);
        assertEquals(12, parsedDate.getMonth() + 1);
        assertEquals(31, parsedDate.getDate());
        assertEquals(23, parsedDate.getHours());
        assertEquals(59, parsedDate.getMinutes());
        assertEquals(59, parsedDate.getSeconds());
        assertEquals(999, parsedDate.getMilliseconds());
    }

    @Test
    public void testParseDateWithMillis() {
        String dateString = "2022-12-31T23:59:59.999+01:00";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(2022, parsedDate.getYear() + 1900);
        assertEquals(12, parsedDate.getMonth() + 1);
        assertEquals(31, parsedDate.getDate());
        assertEquals(23, parsedDate.getHours());
        assertEquals(59, parsedDate.getMinutes());
        assertEquals(59, parsedDate.getSeconds());
        assertEquals(999, parsedDate.getMilliseconds());
        int timeZoneOffset = 1 * 60 * 60 * 1000;
        assertEquals(timeZoneOffset, parsedDate.getTimezoneOffset());
    }

    @Test
    public void testParseInvalidDate() {
        String dateString = "2022-12-32T23:59:59.999Z";
        try {
            ISO8601Utils.parse(dateString, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        String dateStringWithInvalidTimeZone = "2022-12-31T23:59:59.999XXX";
        try {
            ISO8601Utils.parse(dateStringWithInvalidTimeZone, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}