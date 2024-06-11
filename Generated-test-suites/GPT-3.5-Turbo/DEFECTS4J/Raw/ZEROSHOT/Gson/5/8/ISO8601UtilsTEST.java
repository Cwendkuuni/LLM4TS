package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ISO8601UtilsTest {

    @Test
    public void testFormatWithDefaultTimezone() {
        Date date = new Date();

        String expected = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));

        String actual = ISO8601Utils.format(date);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithoutMillis() {
        Date date = new Date();

        String expected = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT"));

        String actual = ISO8601Utils.format(date, false);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();

        String expected = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));

        String actual = ISO8601Utils.format(date, true);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCustomTimezone() {
        Date date = new Date();
        TimeZone timezone = TimeZone.getTimeZone("GMT-08:00");

        String expected = ISO8601Utils.format(date, true, timezone);

        String actual = ISO8601Utils.format(date, true, timezone);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithValidInput() throws ParseException {
        String dateString = "2021-10-10T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2021, Calendar.OCTOBER, 10, 12, 00, 00).getTime();

        Date actual = ISO8601Utils.parse(dateString, pos);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidInput() throws ParseException {
        String dateString = "2021-10-10T12:00";
        ParsePosition pos = new ParsePosition(0);

        ISO8601Utils.parse(dateString, pos);
    }

}