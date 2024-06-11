package com.yourcompany.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithoutMilliseconds() {
        Date date = new Date(); // Current date and time
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("Expected format: yyyy-MM-ddThh:mm:ssZ", "yyyy-MM-ddThh:mm:ssZ", formattedDate);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date(); // Current date and time
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("Expected format: yyyy-MM-ddThh:mm:ss.sssZ", "yyyy-MM-ddThh:mm:ss.sssZ", formattedDate);
    }

    @Test
    public void testFormatDateWithUTCTimeZone() {
        Date date = new Date(); // Current date and time
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("Expected format: yyyy-MM-ddThh:mm:ssZ", "yyyy-MM-ddThh:mm:ssZ", formattedDate);
    }

    @Test
    public void testParseDateWithoutTimeZone() throws ParseException {
        String isoDate = "2022-12-31T23:59:59";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, parsePosition);
        assertEquals("Expected parse date without timezone: '2022-12-31 23:59:59.0'", "Sat Dec 31 23:59:59 GMT",
                new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz").format(parsedDate));
    }

    @Test
    public void testParseDateWithTimeZone() throws ParseException {
        String isoDate = "2022-12-31T23:59:59+01:00";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, parsePosition);
        assertEquals("Expected parse date with timezone: '2022-12-31 22:59:59 CET'", "Sat Dec 31 22:59:59 CET",
                new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz").format(parsedDate));
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() throws ParseException {
        String isoDate = "2022-12-31T23:59:59XXX";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, parsePosition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateInvalidTimeZone() throws ParseException {
        String isoDate = "2022-12-31T23:59:59XXX";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, parsePosition, new TimeZone("XXX"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseDateInvalidOffset() throws ParseException {
        String isoDate = "2022-12-31T23:59:59+01:00:01";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, parsePosition);
    }
}