package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String[] testCases = new String[]{"2022-12-31T23:59:59.999Z", "2022-12-31T23:59:59.999+01:00", "2022-12-31T23:59:59.999-05:00"};
        for (String testCase : testCases) {
            Date date = null;
            ParsePosition parsePosition = new ParsePosition(0);
            try {
                date = dateFormat.parse(testCase, parsePosition);
                assertEquals("Expected date to be parsed successfully for test case: " + testCase, testCase, dateFormat.format(date));
            } catch (ParseException e) {
                fail("Failed to parse expected test case: " + testCase + ". Error message: " + e.getMessage());
            }
        }
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String[] testCases = new String[]{"Mon, 31 Dec 2022 23:59:59 GMT", "Mon, 31 Dec 2022 23:59:59 +0100", "Mon, 31 Dec 2022 23:59:59 -0500"};
        for (String testCase : testCases) {
            Date date = null;
            ParsePosition parsePosition = new ParsePosition(0);
            try {
                date = dateFormat.parse(testCase, parsePosition);
                assertEquals("Expected date to be parsed successfully for test case: " + testCase, testCase, dateFormat.format(date));
            } catch (ParseException e) {
                fail("Failed to parse expected test case: " + testCase + ". Error message: " + e.getMessage());
            }
        }
    }

    @Test
    public void testParseISO8601WithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.US);
        String testCase = "2022-12-31T23:59:59.999+01:00";
        Date date = null;
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            date = dateFormat.parse(testCase, parsePosition);
            assertEquals("Expected date to be parsed successfully for test case: " + testCase, testCase, dateFormat.format(date));
        } catch (ParseException e) {
            fail("Failed to parse expected test case: " + testCase + ". Error message: " + e.getMessage());
        }
    }

    @Test
    public void testParseRFC1123WithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC-5"), Locale.US);
        String testCase = "Mon, 31 Dec 2022 23:59:59 -0500";
        Date date = null;
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            date = dateFormat.parse(testCase, parsePosition);
            assertEquals("Expected date to be parsed successfully for test case: " + testCase, testCase, dateFormat.format(date));
        } catch (ParseException e) {
            fail("Failed to parse expected test case: " + testCase + ". Error message: " + e.getMessage());
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date testDate = new Date(1672492799000L); // Mon Dec 31 23:59:59 GMT 2022
        String formattedDate = dateFormat.format(testDate, new StringBuffer(), new FieldPosition[0]);
        assertEquals("Expected formatted date to match the original date: " + testDate, "Mon, 31 Dec 2022 23:59:59 GMT", formattedDate.toString());
    }

    @Test
    public void testFormatWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.US);
        Date testDate = new Date(1672492799000L);
        String formattedDate = dateFormat.format(testDate, new StringBuffer(), new FieldPosition[0]);
        assertEquals("Expected formatted date to match the original date with +01:00 timezone: " + testDate, "Mon, 31 Dec 2022 23:59:59 +0100", formattedDate.toString());
    }
}