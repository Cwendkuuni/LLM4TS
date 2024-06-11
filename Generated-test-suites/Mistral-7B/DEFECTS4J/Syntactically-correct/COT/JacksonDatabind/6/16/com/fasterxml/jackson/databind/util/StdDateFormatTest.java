package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        assertEquals(StdDateFormat.class, StdDateFormat.instance.getClass());
    }

    @Test
    public void testGetBlueprintFormat() {
        assertSame(StdDateFormat.DATE_FORMAT_ISO8601, StdDateFormat.getBlueprintISO8601Format());
        assertSame(StdDateFormat.DATE_FORMAT_RFC1123, StdDateFormat.getBlueprintRFC1123Format());
    }

    @Test
    public void testGetFormatWithTimeZone() {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_ISO8601, format);
    }

    @Test
    public void testGetFormatWithLocale() {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.FRENCH);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_RFC1123, format);
    }

    @Test
    public void testGetFormatWithTimeZoneAndLocale() {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_ISO8601, format);
    }

    @Test
    public void testParseWithValidDateString() {
        DateFormat format = StdDateFormat.getISO8601Format();
        String dateString = "2022-12-31T23:59:59.999Z";
        Date date = format.parse(dateString);
        assertNotNull(date);
    }

    @Test
    public void testParseWithInvalidDateString() {
        DateFormat format = StdDateFormat.getISO8601Format();
        String dateString = "2022-12-31T23:59:59.999A";
        assertThrows(ParseException.class, () -> format.parse(dateString));
    }

    @Test
    public void testParseWithEmptyString() {
        DateFormat format = StdDateFormat.getISO8601Format();
        assertThrows(ParseException.class, () -> format.parse(""));
    }

    @Test
    public void testParseWithNullString() {
        DateFormat format = StdDateFormat.getISO8601Format();
        assertThrows(NullPointerException.class, () -> format.parse(null));
    }

    @Test
    public void testFormat() {
        DateFormat format = StdDateFormat.getISO8601Format();
        Date date = new Date();
        String dateString = format.format(date);
        assertNotNull(dateString);
        Date parsedDate = format.parse(dateString);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone oldTimeZone = stdDateFormat.getTimeZone();
        TimeZone newTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
        stdDateFormat.setTimeZone(newTimeZone);
        TimeZone newOldTimeZone = stdDateFormat.getTimeZone();
        assertEquals(newTimeZone, newOldTimeZone);
        assertFalse(oldTimeZone.equals(newTimeZone));
    }

    @Test
    public void testClone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        StdDateFormat clonedFormat = stdDateFormat.clone();
        assertNotSame(stdDateFormat, clonedFormat);
        assertEquals(stdDateFormat.getLocale(), clonedFormat.getLocale());
        assertEquals(stdDateFormat.getTimeZone(), clonedFormat.getTimeZone());
    }

}