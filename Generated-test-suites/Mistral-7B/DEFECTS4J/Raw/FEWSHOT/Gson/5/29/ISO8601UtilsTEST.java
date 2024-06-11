package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithUTC() {
        Date date = new Date();
        String expected = date.toInstant().atZone(ZoneId.systemDefault()).toInstant().toString().substring(0, 26);
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithMillisecondsUTC() {
        Date date = new Date();
        boolean millis = true;
        String expected = date.toInstant().atZone(ZoneId.systemDefault()).toInstant().toString().substring(0, 31);
        String actual = ISO8601Utils.format(date, millis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        Date date = new Date();
        TimeZone customTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
        String expected = date.toInstant().atZone(ZoneId.systemDefault()).toInstant().toString().substring(0, 26) + "+05:30";
        String actual = ISO8601Utils.format(date, null, customTimeZone);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseDateWithUTC() throws ParseException {
        String dateString = "2023-02-15T14:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1676668605000L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
    }

    @Test
    public void testParseDateWithCustomTimeZonePlusOffset() throws ParseException {
        String dateString = "2023-02-15T19:45:30+05:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1676674130000L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
    }

    @Test
    public void testParseDateWithCustomTimeZoneMinusOffset() throws ParseException {
        String dateString = "2023-02-15T14:45:30-05:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1676660330000L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
    }

    @Test
    public void testParseDateWithInvalidFormat() throws ParseException {
        String dateString = "2023-02-15T14:45:30A";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithInvalidCustomTimeZone() throws ParseException {
        String dateString = "2023-02-15T14:45:30InvalidTimeZone";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos, null));
    }

    @Test
    public void testFormatAndParseDateWithLocalDateTime() {
        LocalDateTime date = LocalDateTime.of(2023, 2, 15, 14, 30, 45);
        String expectedFormatted = date.format(DateTimeFormatter.ISO_DATE_TIME);
        String actualFormatted = ISO8601Utils.format(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()), false, null);
        assertEquals(expectedFormatted, actualFormatted);

        ParsePosition pos = new ParsePosition(0);
        LocalDateTime expectedParsed = LocalDateTime.parse("2023-02-15T14:30:45", DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime actualParsed = new LocalDateTime();
        Date parsedDate = ISO8601Utils.parse(expectedFormatted, pos);
        assertNotNull(parsedDate);
        actualParsed = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault(), parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).getCalendarTime();
        assertEquals(expectedParsed, actualParsed);
    }
}