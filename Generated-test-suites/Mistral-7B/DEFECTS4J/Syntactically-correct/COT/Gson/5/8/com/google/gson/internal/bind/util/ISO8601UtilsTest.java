package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat_ValidDateWithNoMillis_UTC() {
        Date date = new Date(1516139520000L);
        String formatted = ISO8601Utils.format(date);
        assertEquals("2017-10-02T14:25:20Z", formatted);
    }

    @Test
    public void testFormat_ValidDateWithMillis_UTC() {
        Date date = new Date(1516139520123L);
        String formatted = ISO8601Utils.format(date, true);
        assertEquals("2017-10-02T14:25:20.123Z", formatted);
    }

    @Test
    public void testFormat_ValidDateWithMillis_CustomTimeZone() {
        Date date = new Date(1516139520123L);
        String formatted = ISO8601Utils.format(date, true, new TimeZone("Europe/Berlin"));
        assertEquals("2017-10-02T15:25:20.123+02:00", formatted);
    }

    @Test
    public void testFormat_NullDate() {
        String formatted = ISO8601Utils.format(null);
        assertNull(formatted);
    }

    @Test
    public void testParse_ValidDateStringWithMillis_UTC() throws ParseException {
        String dateString = "2017-10-02T14:25:20.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Date expected = new Date(1516139520123L);
        assertEquals(expected, date);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_ValidDateStringWithoutMillis_UTC() throws ParseException {
        String dateString = "2017-10-02T14:25:20Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Date expected = new Date(1516139520000L);
        assertEquals(expected, date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParse_ValidDateStringWithMillis_CustomTimeZone() throws ParseException {
        String dateString = "2017-10-02T14:25:20.123+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Date expected = new Date(1516139740123L);
        assertEquals(expected, date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParse_InvalidDateString() throws ParseException {
        String dateString = "2017-10-02T14:25:20.123X";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_InvalidDateStringWithoutTimeZone() throws ParseException {
        String dateString = "2017-10-02T14:25:20";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_InvalidDateStringWithInvalidTimeZone() throws ParseException {
        String dateString = "2017-10-02T14:25:20.123INVALID";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_EmptyString() throws ParseException {
        String dateString = "";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, -1, 3);
        assertEquals("000", buffer.toString());

        buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, 1, 3);
        assertEquals("001", buffer.toString());

        buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, 123, 3);
        assertEquals("123", buffer.toString());
    }
}