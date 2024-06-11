package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

/**
 * Test class for StdDateFormat class
 */
public class StdDateFormatTest {

    @Test
    public void testStandardDateFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test parse method with various date strings and check for correctness
        Date date;
        String dateString;
        ParsePosition parsePosition;

        dateString = "2022-12-31";
        date = stdDateFormat.parse(dateString);
        parsePosition = new ParsePosition(0);
        assertEquals("Invalid date from string: " + dateString, date, stdDateFormat.parse(dateString, parsePosition));

        dateString = "2022-12-31T12:30:45.123Z";
        date = stdDateFormat.parse(dateString);
        parsePosition = new ParsePosition(0);
        assertEquals("Invalid date from string: " + dateString, date, stdDateFormat.parse(dateString, parsePosition));

        dateString = "Mon, 01 Jan 2023 00:00:01 GMT";
        date = stdDateFormat.parse(dateString);
        parsePosition = new ParsePosition(0);
        assertEquals("Invalid date from string: " + dateString, date, stdDateFormat.parse(dateString, parsePosition));

        dateString = "1 Jan 2023 00:00:01 GMT";
        date = stdDateFormat.parse(dateString);
        parsePosition = new ParsePosition(0);
        assertEquals("Invalid date from string: " + dateString, date, stdDateFormat.parse(dateString, parsePosition));

        dateString = "2023-01-01 12:30:45.123+01:00";
        date = stdDateFormat.parse(dateString);
        parsePosition = newParsePosition(0);
        assertEquals("Invalid date from string: " + dateString, date, stdDateFormat.parse(dateString, parsePosition));

        // Test parse method with invalid date strings and check for ParseException
        try {
            dateString = "2023-13-01";
            stdDateFormat.parse(dateString);
            fail("Expected ParseException for invalid date: " + dateString);
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateString = "2023-01-32";
            stdDateFormat.parse(dateString);
            fail("Expected ParseException for invalid date: " + dateString);
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateString = "2023-01-01 25:30:45.123Z";
            stdDateFormat.parse(dateString, parsePosition);
            fail("Expected ParseException for invalid hour: " + dateString);
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateString = "2023-01-01 12:60:45.123Z";
            stdDateFormat.parse(dateString, parsePosition);
            fail("Expected ParseException for invalid minute: " + dateString);
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateString = "2023-01-01 12:30:60.123Z";
            stdDateFormat.parse(dateString, parsePosition);
            fail("Expected ParseException for invalid second: " + dateString);
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateString = "2023-01-01 12:30:45.123-02:30";
            stdDateFormat.parse(dateString, parsePosition);
            fail("Expected ParseException for invalid timezone: " + dateString);
        } catch (ParseException e) {
            // Expected
        }

        // Test format method with various date objects and check for correctness
        Date givenDate;
        String formattedDate;
        StringBuilder stringBuilder;

        givenDate = new Date(1671139200000L); // Wed Jan 04 00:00:00 GMT 2023
        formattedDate = stdDateFormat.format(givenDate);
        stringBuilder = new StringBuilder(formattedDate);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Remove 'Z'
        assertEquals("Incorrect date format for given date", "Wed, 04 Jan 2023 00:00:00 GMT", formattedDate);

        givenDate = new Date(1671225600000L); // Thu Jan 05 00:00:00 GMT 2023
        formattedDate = stdDateFormat.format(givenDate);
        assertEquals("Incorrect date format for given date", "Thu, 05 Jan 2023 00:00:00 GMT", formattedDate);

        givenDate = new Date(1671312000000L); // Fri Jan 06 00:00:00 GMT 2023
        formattedDate = stdDateFormat.format(givenDate);
        stringBuilder = new StringBuilder(formattedDate);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Remove 'Z'
        assertEquals("Incorrect date format for given date", "Fri, 06 Jan 2023 00:00:00 GMT", formattedDate);

        givenDate = new Date(1671312000000L + 3600000); // Fri Jan 06 01:00:00 GMT 2023
        formattedDate = stdDateFormat.format(givenDate);
        stringBuilder = new StringBuilder(formattedDate);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Remove 'Z'
        assertEquals("Incorrect date format for given date", "Fri, 06 Jan 2023 01:00:00 GMT", formattedDate);
    }

    @Test
    public void testGetStandardDateFormatInstances() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test getISO8601Format method
        DateFormat iso8601Format = stdDateFormat.getISO8601Format();
        assertEquals("ISO-8601 format instances should be equal", stdDateFormat.parse("2022-12-31T12:30:45.123Z"), iso8601Format.parse("2022-12-31T12:30:45.123Z"));

        // Test getRFC1123Format method
        DateFormat rfc1123Format = stdDateFormat.getRFC1123Format();
        assertEquals("RFC-1123 format instances should be equal", stdDateFormat.parse("Mon, 01 Jan 2023 00:00:01 GMT"), rfc1123Format.parse("Mon, 01 Jan 2023 00:00:01 GMT"));
    }

    @Test
    public void testSetTimezoneAndLocale() {
        StdDateFormat stdDateFormat = new StdDateFormat();

        // Test withTimeZone method
        stdDateFormat = stdDateFormat.withTimeZone(TimeZone.getTimeZone("America/New_York"));
        assertEquals("Timezone should be changed", "America/New_York", stdDateFormat.getTimeZone().getID());

        Date date = stdDateFormat.parse("2022-12-31T12:30:45.123Z");
        assertEquals("Date should not change when timezone is changed", new Date(date.getTime() - 18000000L), stdDateFormat.parse("2022-12-31T12:30:45.123Z"));

        String formattedDate = stdDateFormat.format(date);
        String expectedFormatPattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        assertEquals("Format pattern should be changed when timezone is changed", expectedFormatPattern, stdDateFormat.toPattern());

        // Test withLocale method
        stdDateFormat = stdDateFormat.withLocale(Locale.FRANCE);
        assertEquals("Locale should be changed", Locale.FRANCE, stdDateFormat.getLocale());

        date = stdDateFormat.parse("2022-12-31T12:30:45.123Z");
        assertEquals("Date should not change when locale is changed", new Date(date.getTime()), stdDateFormat.parse("2022-12-31T12:30:45.123Z"));

        formattedDate = stdDateFormat.format(date);
        expectedFormatPattern = "EEE j MMMM d 'à' H:mm:ss zzz yyyy";
        assertEquals("Format pattern should be changed when locale is changed", expectedFormatPattern, stdDateFormat.toPattern());

        // Test clone method
        StdDateFormat clonedStdDateFormat = stdDateFormat.clone();
        assertEquals("Cloned instance should be equal to original", stdDateFormat, clonedStdDateFormat);
    }
}