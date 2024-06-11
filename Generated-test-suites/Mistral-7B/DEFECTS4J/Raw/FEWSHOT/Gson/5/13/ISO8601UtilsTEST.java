package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithDefaultTimeZone() {
        Date date = new Date(0);
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", formattedDate);
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Date date = new Date(0);
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        assertEquals("1970-01-01T01:00:00+00:00", formattedDate);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date(System.currentTimeMillis());
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertNotSame(formattedDate, ISO8601Utils.format(date, false, TimeZone.getDefault()));
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2022-12-31T23:59:59.999Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals("Sat Dec 31 23:59:59 CET 2022", new SimpleDateFormat("E d MMM yyyy HH:mm:ss z").format(parsedDate));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseDateWithInvalidFormat() {
        String dateString = "2022-12-31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithInvalidTimeZoneIndicator() {
        String dateString = "2022-12-31T23:59:59+a";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithInvalidYear() {
        String dateString = "100-12-31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithInvalidMonth() {
        String dateString = "2022-13-31T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithInvalidDay() {
        String dateString = "2022-02-33T23:59:59Z";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithMissingTimeComponent() {
        String dateString = "2022-12-31";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithMissingTimeZoneIndicator() {
        String dateString = "2022-12-31T23:59:59";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithInvalidTimeZoneOffset() {
        String dateString = "2022-12-31T23:59:59+11:60";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithEmptyString() {
        String dateString = "";
        ParsePosition pos = new ParsePosition(0);
        assertNull(ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithNullString() {
        ParsePosition pos = new ParsePosition(0);
        assertNull(ISO8601Utils.parse(null, pos));
    }
}