package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

    @Test
    public void testFormat() throws Exception {
        // Test formatting with default timezone (UTC) and no milliseconds precision
        Date date = new Date(0L);
        String formatted = ISO8601Utils.format(date);
        assertEquals("2000-01-01T00:00:00Z", formatted);

        // Test formatting with default timezone (UTC) and milliseconds precision
        date.setTime(System.currentTimeMillis());
        formatted = ISO8601Utils.format(date, true);
        long millis = System.currentTimeMillis();
        assertEquals("2000-01-01T" + millis / 1000L + "." + (millis % 1000L) + "Z", formatted);

        // Test formatting with custom timezone
        TimeZone customTimezone = TimeZone.getTimeZone("America/New_York");
        date.setTime(System.currentTimeMillis());
        formatted = ISO8601Utils.format(date, true, customTimezone);
        millis = System.currentTimeMillis();
        String expectedTimezone = customTimezone.getID().substring(3).equals("MST") ? "M" : "-"; // daylight saving time offset
        assertEquals("2000-01-01T" + millis / 1000L + "." + (millis % 1000L) + "[" + expectedTimezone + "0" + customTimezone.getOffset(millis) / 60 + ":" + (customTimezone.getOffset(millis) % 60) + "]", formatted);
    }

    @Test
    public void testParse() throws Exception {
        // Test parsing with default timezone (UTC) and no milliseconds precision
        String isoDate = "2000-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Sat Jan 01 00:00:00 GMT 2000", parsedDate.toString());
        assertEquals(0, pos.getIndex());

        // Test parsing with default timezone (UTC) and milliseconds precision
        isoDate = "2000-01-01T00:00:01.123Z";
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Sat Jan 01 00:00:01 GMT 2000", parsedDate.toString());
        assertEquals(isoDate.length(), pos.getIndex());

        // Test parsing with custom timezone
        TimeZone customTimezone = TimeZone.getTimeZone("America/New_York");
        isoDate = "2000-01-01T00:00:01.123" + customTimezone.getID();
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Sat Jan 01 04:00:01 EST 2000", parsedDate.toString());
        assertEquals(isoDate.length(), pos.getIndex());
    }

    @Test
    public void testFormatWithInvalidInput() {
        // Test formatting with null input
        assertNull(ISO8601Utils.format(null));

        // Test formatting with empty string as input
        assertNull(ISO8601Utils.format(""));

        // Test formatting with invalid format
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.format("invalidFormat"));
    }

    @Test
    public void testParseWithInvalidInput() {
        // Test parsing with null input
        assertNull(ISO8601Utils.parse(null, new ParsePosition(0)));

        // Test parsing with empty string as input
        assertNull(ISO8601Utils.parse("", new ParsePosition(0)));

        // Test parsing with invalid format
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("invalidFormat", new ParsePosition(0)));

        // Test parsing with invalid year
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("1900-01-01T00:00:00Z", new ParsePosition(0)));

        // Test parsing with invalid month
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-00-01T00:00:00Z", new ParsePosition(0)));

        // Test parsing with invalid day
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-01-33T00:00:00Z", new ParsePosition(0)));

        // Test parsing with invalid hour
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-01-01T24:00:00Z", new ParsePosition(0)));

        // Test parsing with invalid minute
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-01-01T00:60:00Z", new ParsePosition(0)));

        // Test parsing with invalid second
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-01-01T00:00:60Z", new ParsePosition(0)));

        // Test parsing with invalid timezone
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-01-01T00:00:00XXX", new ParsePosition(0)));

        // Test parsing with missing timezone
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2000-01-01T00:00:00", new ParsePosition(0)));
    }

    @Test
    public void testParseWithTimeZoneOffset() throws Exception {
        // Test parsing with timezone offset '+00:00'
        String isoDate = "2000-01-01T00:00:00+00:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Sat Jan 01 00:00:00 GMT 2000", parsedDate.toString());
        assertEquals(isoDate.length(), pos.getIndex());

        // Test parsing with timezone offset '-05:00'
        isoDate = "2000-01-01T00:00:00-05:00";
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Thu Dec 29 21:00:00 EST 2000", parsedDate.toString());
        assertEquals(isoDate.length(), pos.getIndex());
    }

    @Test
    public void testFormatWithCustomZoneId() throws Exception {
        // Test formatting with custom ZoneId
        ZoneId customZone = ZoneId.of("America/New_York");
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true, customZone.toZoneId());
        assertEquals("2000-01-01T" + getCurrentMillisWithZoneId(customZone) / 1000L + "." + (getCurrentMillisWithZoneId(customZone) % 1000L) + "[" + customZone.getId().substring(3).equals("MST") ? "M" : "-" + customZone.getRules().getOffset(date.getTime()) / 60 + ":" + (customZone.getRules().getOffset(date.getTime()) % 60) + "]", formatted);
    }

    @Test
    public void testParseWithCustomZoneId() throws Exception {
        // Test parsing with custom ZoneId
        ZoneId customZone = ZoneId.of("America/New_York");
        String isoDate = "2000-01-01T" + getCurrentMillisWithZoneId(customZone) / 1000L + "." + (getCurrentMillisWithZoneId(customZone) % 1000L) + "[" + customZone.getId().substring(3).equals("MST") ? "M" : "-" + customZone.getRules().getOffset(System.currentTimeMillis()) / 60 + ":" + (customZone.getRules().getOffset(System.currentTimeMillis()) % 60) +"]";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Thu Dec 29 21:00:00 EST 2000", parsedDate.toString());
        assertEquals(isoDate.length(), pos.getIndex());
    }

    @Test
    public void testFormatWithInvalidTimezoneId() throws Exception {
        // Test formatting with invalid custom ZoneId
        ZoneId customZone = ZoneId.of("CustomZone");
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true, customZone.toZoneId());
        assertNull(formatted);
    }

    @Test
    public void testParseWithInvalidTimezoneId() throws Exception {
        // Test parsing with invalid custom ZoneId
        ZoneId customZone = ZoneId.of("CustomZone");
        String isoDate = "2000-01-01T" + getCurrentMillisWithZoneId(customZone) / 1000L + "." + (getCurrentMillisWithZoneId(customZone) % 1000L) + "[" + customZone.getId().substring(3).equals("MST") ? "M" : "-" + customZone.getRules().getOffset(System.currentTimeMillis()) / 60 + ":" + (customZone.getRules().getOffset(System.currentTimeMillis()) % 60) + "]";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, pos));
    }

    private long getCurrentMillisWithZoneId(ZoneId customZone) {
        return System.currentTimeMillis() + customZone.getRules().getOffset(System.currentTimeMillis());
    }
}