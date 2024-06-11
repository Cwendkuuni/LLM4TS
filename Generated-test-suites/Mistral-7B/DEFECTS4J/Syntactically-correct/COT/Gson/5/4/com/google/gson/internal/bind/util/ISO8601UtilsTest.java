package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithDefaultTimeZone() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-01-01T00:00:00Z", formattedDate);
    }

    @Test
    public void testFormatDateWithTimeZoneAndMillis() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1, 12, 30, 45, 123).getTime();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/London"));
        assertEquals("2021-01-01T12:30:45.123+00:00", formattedDate);
    }

    @Test
    public void testParseDateWithDefaultTimeZone() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2021-01-01";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime(), parsedDate);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseDateWithTimeZoneAndMillis() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2021-01-01T12:30:45.123+01:00";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"), Locale.US);
        calendar.setTime(parsedDate);
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(45, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
        assertEquals(1, calendar.get(Calendar.EXACTLY_AT_MIDNIGHT));
        assertEquals(TimeZone.getTimeZone("Europe/Paris").getID(), calendar.getTimeZone().getID());
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseDateWithInvalidFormat() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2021-01-01T12:30:45Z"; // missing millis
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));

        dateString = "2021-01-01T12:30:45.123"; // missing timezone
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));

        dateString = "2021-01-01T12:30:45.123+"; // invalid timezone
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testFormatDateWithInvalidYear() {
        Date date = new GregorianCalendar(-1, Calendar.JANUARY, 1).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithInvalidMonth() {
        Date date = new GregorianCalendar(2021, 13, 1).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithInvalidDay() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 31).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithInvalidHour() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1, 24).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithInvalidMinute() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1, 23, 60).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithInvalidSecond() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1, 23, 59, 60).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithInvalidMillisecond() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1, 23, 59, 60, 1001).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertNull(formattedDate);
    }

    @Test
    public void testFormatDateWithNegativeOffset() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime();
        String formattedDate = ISO8601Utils.format(date, false, new SimpleTimeZone(-120, " Hawaii-Aleutian"));
        assertEquals("2021-01-01T19:00:00-11:00", formattedDate);
    }

    @Test
    public void testFormatDateWithPositiveOffset() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime();
        String formattedDate = ISO8601Utils.format(date, false, new SimpleTimeZone(120, "Brunei Darussalam"));
        assertEquals("2021-01-01T12:00:00+11:00", formattedDate);
    }

    @Test
    public void testFormatDateWithZeroOffset() {
        Date date = new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-01-01T00:00:00Z", formattedDate);
    }

    @Test
    public void testParseDateWithZeroOffset() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2021-01-01T00:00:00Z";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime(), parsedDate);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseDateWithNegativeOffset() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2021-01-01T00:00:00-11:00";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Hawaii-Aleutian"), Locale.US);
        calendar.setTime(parsedDate);
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY)); // Hours adjusted for the timezone
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
        assertEquals(11, calendar.get(Calendar.EXACTLY_AT_MIDNIGHT));
        assertEquals("Hawaii-Aleutian", calendar.getTimeZone().getID());
        assertEquals(11 * 60 * 60 * 60 * 1000L, calendar.getTimeInMillis() - parsedDate.getTime());
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseDateWithPositiveOffset() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2021-01-01T00:00:00+11:00";
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Brunei Darussalam"), Locale.US);
        calendar.setTime(parsedDate);
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(11, calendar.get(Calendar.HOUR_OF_DAY)); // Hours adjusted for the timezone
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
        assertEquals(11, calendar.get(Calendar.EXACTLY_AT_MIDNIGHT));
        assertEquals("Brunei Darussalam", calendar.getTimeZone().getID());
        assertEquals(-11 * 60 * 60 * 60 * 1000L, calendar.getTimeInMillis() - parsedDate.getTime());
        assertEquals(0, pos.getIndex());
    }
}