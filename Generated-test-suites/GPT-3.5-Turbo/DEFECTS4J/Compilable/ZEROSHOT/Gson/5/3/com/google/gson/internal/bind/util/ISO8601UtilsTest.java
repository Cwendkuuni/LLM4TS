package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class ISO8601UtilsTest {

    private ISO8601Utils iso8601Utils;

    @Before
    public void setUp() {
        iso8601Utils = new ISO8601Utils();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testFormat_dateOnly() {
        Date date = createDate(2021, 10, 25);

        String expected = "2021-10-25T00:00:00Z";
        String actual = iso8601Utils.format(date);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_withMillis() {
        Date date = createDate(2021, 10, 25, 10, 30, 45, 678);

        String expected = "2021-10-25T10:30:45.678Z";
        String actual = iso8601Utils.format(date, true);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_withTimeZone() {
        Date date = createDate(2021, 10, 25, 10, 30, 45);

        TimeZone timeZone = TimeZone.getTimeZone("GMT+02:00");

        String expected = "2021-10-25T08:30:45+02:00";
        String actual = iso8601Utils.format(date, false, timeZone);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_dateOnly() throws ParseException {
        String dateString = "2021-10-25T00:00:00Z";
        Date expected = createDate(2021, 10, 25);

        ParsePosition pos = new ParsePosition(0);
        Date actual = iso8601Utils.parse(dateString, pos);

        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParse_withMillis() throws ParseException {
        String dateString = "2021-10-25T10:30:45.678Z";
        Date expected = createDate(2021, 10, 25, 10, 30, 45, 678);

        ParsePosition pos = new ParsePosition(0);
        Date actual = iso8601Utils.parse(dateString, pos);

        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParse_withTimeZone() throws ParseException {
        String dateString = "2021-10-25T10:30:45+02:00";
        Date expected = createDate(2021, 10, 25, 8, 30, 45);

        ParsePosition pos = new ParsePosition(0);
        Date actual = iso8601Utils.parse(dateString, pos);

        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParse_invalidDateString() {
        String dateString = "2021-10-25T10:30:45.678";

        try {
            ParsePosition pos = new ParsePosition(0);
            iso8601Utils.parse(dateString, pos);
            fail("ParseException should have been thrown.");
        } catch (ParseException e) {
            assertEquals("Unparseable date: \"2021-10-25T10:30:45.678\"", e.getMessage());
            assertEquals(19, e.getErrorOffset());
        }
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        return calendar.getTime();
    }

    private Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }

    private Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }
}