package com.example; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_DateWithDefaultTimeZone() {
        Date date = new Date(1611459200000L); // Mon Jun 21 00:00:00 CEST 2021
        String formattedDate = ISO8601Utils.format(date);
        String expected = "2021-06-21T00:00:00+02:00";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_DateWithMilliseconds() {
        Date date = new Date(1611459200001L); // Mon Jun 21 00:00:00.001 CEST 2021
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        String expected = "2021-06-21T00:00:00.001+02:00";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_DateWithCustomTimeZone() {
        Date date = new Date(1611459200000L); // Mon Jun 21 00:00:00 CEST 2021
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String expected = "2021-06-21T01:00:00Z";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testParse_ValidDate() throws ParseException {
        String isoDate = "2021-06-21T13:30:45.123+02:00";
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
        String expected = "Mon Jun 21 15:30:45 CEST 2021";
        assertEquals(expected, new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(parsedDate));
    }

    @Test
    public void testParse_InvalidDateWithMissingTimeZone() throws ParseException {
        String isoDate = "2021-06-21T13:30:45";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, pos));
    }

    @Test
    public void testParse_InvalidDateWithInvalidFormat() throws ParseException {
        String isoDate = "2021-06-21T13:30:45ABC";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, pos));
    }

    @Test
    public void testParse_InvalidDateWithEmptyString() {
        ParsePosition pos = new ParsePosition(0);
        assertNull(ISO8601Utils.parse("", pos));
    }

}