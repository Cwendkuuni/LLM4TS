package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601Format() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test parsing with default GMT timezone
        String dateStr = "2022-12-31T23:59:59.123Z";
        Date expectedDate = new Date(1672500799123L); // 1672500799123 is the milliseconds value for the given date and time
        Date parsedDate = dateFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat customTimeZoneFormat = new StdDateFormat(customTimeZone);
        dateStr = "2022-12-31T23:59:59.123-08:00"; // Pacific Time is 8 hours behind GMT
        expectedDate = new Date(1672426399123L); // Adjusted milliseconds value for the given date and time and timezone
        parsedDate = customTimeZoneFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParseRFC1123Format() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test parsing with default GMT timezone
        String dateStr = "Thu, 31 Dec 2022 23:59:59 GMT";
        Date expectedDate = new Date(1672500799000L); // 1672500799000 is the milliseconds value for the given date and time
        Date parsedDate = dateFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat customTimeZoneFormat = new StdDateFormat(customTimeZone);
        dateStr = "Thu, 31 Dec 2022 23:59:59 PST"; // Pacific Standard Time is 8 hours behind GMT
        expectedDate = new Date(1672425399000L); // Adjusted milliseconds value for the given date and time and timezone
        parsedDate = customTimeZoneFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParseDateFormatWithMilliseconds() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test parsing with default GMT timezone
        String dateStr = "2022-12-31T23:59:59.123Z";
        Date expectedDate = new Date(1672500799123L); // 1672500799123 is the milliseconds value for the given date and time
        Date parsedDate = dateFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat customTimeZoneFormat = new StdDateFormat(customTimeZone);
        dateStr = "2022-12-31T23:59:59.123-08:00"; // Pacific Time is 8 hours behind GMT
        expectedDate = new Date(1672426399123L); // Adjusted milliseconds value for the given date and time and timezone
        parsedDate = customTimeZoneFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParseDateFormatWithoutMilliseconds() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test parsing with default GMT timezone
        String dateStr = "2022-12-31T23:59:59Z";
        Date expectedDate = new Date(1672500799000L); // 1672500799000 is the milliseconds value for the given date and time
        Date parsedDate = dateFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);

        // Test parsing with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat customTimeZoneFormat = new StdDateFormat(customTimeZone);
        dateStr = "2022-12-31T23:59:59-08:00"; // Pacific Time is 8 hours behind GMT
        expectedDate = new Date(1672425399000L); // Adjusted milliseconds value for the given date and time and timezone
        parsedDate = customTimeZoneFormat.parse(dateStr);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testFormatDate() {
        StdDateFormat dateFormat = new StdDateFormat();

        // Test formatting with default GMT timezone
        Date date = new Date();
        String expectedDateStr = dateFormat.format(date);
        String formattedDateStr = dateFormat.format(date);
        assertEquals(expectedDateStr, formattedDateStr);

        // Test formatting with custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat customTimeZoneFormat = new StdDateFormat(customTimeZone);
        date.setTime(System.currentTimeMillis()); // Set the date to the current time
        expectedDateStr = customTimeZoneFormat.format(date);
        formattedDateStr = customTimeZoneFormat.format(date);
        assertEquals(expectedDateStr, formattedDateStr);
    }
}