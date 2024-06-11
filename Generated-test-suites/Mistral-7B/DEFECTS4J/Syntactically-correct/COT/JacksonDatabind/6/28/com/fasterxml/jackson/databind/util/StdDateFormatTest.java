package com.example.junit;

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

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertEquals("Default StdDateFormat instance", stdDateFormat.getClass().getName(), StdDateFormat.class.getName());
    }

    @Test
    public void testParseWithValidDateString() {
        Date date;
        DateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-12-31T12:00:00.000Z";
        ParsePosition pos = new ParsePosition(0);

        date = stdDateFormat.parse(dateString, pos);
        assertNull("Date parsing should not throw ParseException", pos.getErrorIndex());
        assertEquals("Parsed date should match the original dateString", dateString, stdDateFormat.format(date));
    }

    @Test
    public void testParseWithInvalidDateString() {
        DateFormat stdDateFormat = new StdDateFormat();
        String dateString = "invalidDateString";
        ParsePosition pos = new ParsePosition(0);

        assertThrows(ParseException.class, () -> stdDateFormat.parse(dateString, pos));
    }

    @Test
    public void testParseWithNullDateString() {
        DateFormat stdDateFormat = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);

        assertThrows(NullPointerException.class, () -> stdDateFormat.parse(null, pos));
    }

    @Test
    public void testFormat() {
        Date date = new Date();
        DateFormat stdDateFormat = new StdDateFormat();
        String formattedDate = stdDateFormat.format(date);
        assertFalse("Formatted date should not be empty", formattedDate.isEmpty());
        assertTrue("Formatted date should start with 'Sat'", formattedDate.startsWith("Sat"));
    }

    @Test
    public void testFormatWithNullDate() {
        DateFormat stdDateFormat = new StdDateFormat();

        assertNull("Format method should return null when passed null date", stdDateFormat.format(null));
    }

    @Test
    public void testSetTimeZone() {
        DateFormat stdDateFormat = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("Europe/Paris");

        StdDateFormat newStdDateFormat = stdDateFormat.withTimeZone(newTimeZone);
        TimeZone newStdDateFormatTimeZone = newStdDateFormat.getTimeZone();
        assertEquals("Timezone should be set to the provided TimeZone", newTimeZone, newStdDateFormatTimeZone);
    }

    @Test
    public void testSetLocale() {
        DateFormat stdDateFormat = new StdDateFormat();
        Locale newLocale = Locale.FRENCH;

        StdDateFormat newStdDateFormat = stdDateFormat.withLocale(newLocale);
        Locale newStdDateFormatLocale = newStdDateFormat.getLocale();
        assertEquals("Locale should be set to the provided Locale", newLocale, newStdDateFormatLocale);
    }

    @Test
    public void testClone() {
        DateFormat stdDateFormat = new StdDateFormat();
        StdDateFormat clonedStdDateFormat = stdDateFormat.clone();

        TimeZone clonedTimeZone = clonedStdDateFormat.getTimeZone();
        Locale clonedLocale = clonedStdDateFormat.getLocale();
        DateFormat clonedFormatRFC1123 = clonedStdDateFormat.getRFC1123Format(null, null);
        DateFormat clonedFormatISO8601 = clonedStdDateFormat.getISO8601Format(null, null);
        DateFormat clonedFormatISO8601_z = clonedStdDateFormat.getISO8601FormatWithOffset(null, null);

        assertEquals("Cloned StdDateFormat should have the same timezone as the original", stdDateFormat.getTimeZone(), clonedStdDateFormat.getTimeZone());
        assertEquals("Cloned StdDateFormat should have the same locale as the original", stdDateFormat.getLocale(), clonedStdDateFormat.getLocale());
        assertNotSame("Cloned StdDateFormat should not be the same object as the original formatRFC1123", stdDateFormat.getRFC1123Format(null, null), clonedStdDateFormat.getRFC1123Format(null, null));
        assertNotSame("Cloned StdDateFormat should not be the same object as the original formatISO8601", stdDateFormat.getISO8601Format(null, null), clonedStdDateFormat.getISO8601Format(null, null));
        assertNotSame("Cloned StdDateFormat should not be the same object as the original formatISO8601_z", stdDateFormat.getISO8601FormatWithOffset(null, null), clonedStdDateFormat.getISO8601FormatWithOffset(null, null));
    }

    
}