package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithMillisAndTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        String formattedDate = ISO8601Utils.format(date, true, timeZone);
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() {
        String dateString = "2022-01-01T12:00:00Z";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidDate() {
        String dateString = "2022-01-01T25:00:00Z";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);
            fail("Expected ParseException, but parsedDate: " + parsedDate);
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"2022-01-01T25:00:00Z\"]", e.getMessage());
            assertEquals(11, parsePosition.getErrorIndex());
        }
    }

    @Test
    public void testParseWithInvalidTimeZone() {
        String dateString = "2022-01-01T12:00:00GMT+00:00";
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);
            fail("Expected ParseException, but parsedDate: " + parsedDate);
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"2022-01-01T12:00:00GMT+00:00\"]", e.getMessage());
            assertEquals(16, parsePosition.getErrorIndex());
        }
    }
}