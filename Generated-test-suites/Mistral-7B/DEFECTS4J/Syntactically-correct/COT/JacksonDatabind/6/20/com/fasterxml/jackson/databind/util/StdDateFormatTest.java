package com.example; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testGetBlueprintFormat() {
        // Test getBlueprintISO8601Format()
        DateFormat format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", format.toPattern());

        // Test getBlueprintRFC1123Format()
        format = StdDateFormat.getBlueprintRFC1123Format();
        assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", format.toPattern());
    }

    @Test
    public void testParseDate_ISO8601() throws ParseException {
        // Test parsing a date in ISO8601 format
        String dateString = "2022-01-01T12:34:56.789Z";
        DateFormat format = StdDateFormat.getInstance();
        Date date = format.parse(dateString);

        // Check parsed date
        assertEquals("Sat Jan 01 12:34:56 GMT+00:00 2022", format.format(date));
        assertEquals(new Date(1641369296000L), date);
    }

    @Test
    public void testParseDate_RFC1123() throws ParseException {
        // Test parsing a date in RFC1123 format
        String dateString = "Sat, 01 Jan 22 12:34:56 GMT";
        DateFormat format = StdDateFormat.getRFC1123Format();
        Date date = format.parse(dateString);

        // Check parsed date
        assertEquals("Sat Jan 01 12:34:56 GMT", format.format(date));
        assertEquals(new Date(1641369296000L), date.getTime());
    }

    @Test
    public void testParseDate_InvalidFormat() throws ParseException {
        // Test parsing an invalid date format
        String dateString = "Jan 01 2022 12:34:56";
        DateFormat format = StdDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);

        assertThrows(ParseException.class, () -> format.parse(dateString, pos));
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParseDate_MissingTimeZone() throws ParseException {
        // Test parsing a date with missing timezone
        String dateString = "2022-01-01T12:34:56";
        DateFormat format = StdDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);

        assertThrows(ParseException.class, () -> format.parse(dateString, pos));
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParseDate_InvalidTimeZone() throws ParseException {
        // Test parsing a date with invalid timezone
        String dateString = "2022-01-01T12:34:56+13:30";
        DateFormat format = StdDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);

        assertThrows(ParseException.class, () -> format.parse(dateString, pos));
        assertEquals(-1, pos.getIndex());
    }

    @Test
    public void testParseDate_ISO8601_WithTimeZone() throws ParseException {
        // Test parsing a date in ISO8601 format with timezone
        String dateString = "2022-01-01T12:34:56.789+03:00";
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("Europe/Helsinki"), Locale.US);
        Date date = format.parse(dateString);

        // Check parsed date
        assertEquals("Tue Jan 03 15:34:56 EET 2022", format.format(date));
        assertEquals(new Date(1641404896000L), date.getTime());
    }

    @Test
    public void testParseDate_RFC1123_WithTimeZone() throws ParseException {
        // Test parsing a date in RFC1123 format with timezone
        String dateString = "Tue, 03 Jan 22 15:34:56 EET";
        DateFormat format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("Europe/Helsinki"), Locale.US);
        Date date = format.parse(dateString);

        // Check parsed date
        assertEquals("Tue Jan 03 15:34:56 EET 2022", format.format(date));
        assertEquals(new Date(1641404896000L), date.getTime());
    }

    @Test
    public void testFormatDate() {
        // Test formatting a date in ISO8601 format
        Date date = new Date();
        DateFormat format = StdDateFormat.getInstance();
        String formattedDate = format.format(date);

        // Check formatted date matches expected ISO8601 format
        assertTrue(formattedDate.matches(".*[+-]\\d{2}:\\d{2}:\\d{2}\\.\\d{3,}Z"));
    }

    @Test
    public void testSetTimeZone() {
        // Test setting timezone
        DateFormat format = StdDateFormat.getInstance();
        String originalTimeZone = format.getTimeZone().getId();

        format.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        String newTimeZone = format.getTimeZone().getId();

        assertFalse(originalTimeZone.equals(newTimeZone));
        assertEquals("UTC+1", newTimeZone);
    }
}