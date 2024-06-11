package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    // Test utility method to create test dates with known values
    private Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        gregorianCalendar.set(Calendar.YEAR, year);
        gregorianCalendar.set(Calendar.MONTH, month - 1);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, day);
        gregorianCalendar.set(Calendar.HOUR_OF_DAY, hour);
        gregorianCalendar.set(Calendar.MINUTE, minute);
        gregorianCalendar.set(Calendar.SECOND, second);
        gregorianCalendar.set(Calendar.MILLISECOND, millisecond);
        return gregorianCalendar.getTime();
    }

    /**
     * Test for format method when all values are provided.
     */
    @Test
    public void testFormat_AllValuesProvided() {
        Date date = createDate(2023, 3, 13, 14, 30, 25, 350);
        String expected = "2023-03-13T14:30:25.350Z";
        String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals(expected, actual);
    }

    /**
     * Test for format method when only date values are provided.
     */
    @Test
    public void testFormat_OnlyDateValuesProvided() {
        Date date = createDate(2023, 3, 13);
        String expected = "2023-03-13T00:00:00Z";
        String actual = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals(expected, actual);
    }

    /**
     * Test for format method when only time values are provided.
     */
    @Test
    public void testFormat_OnlyTimeValuesProvided() {
        Date date = createDate(2023, 3, 13);
        Date datePlusOneHour = new Date(date.getTime() + 3600000L);
        String expected = "2023-03-13T15:00:00Z";
        String actual = ISO8601Utils.format(datePlusOneHour, false, TimeZone.getTimeZone("UTC"));
        assertEquals(expected, actual);
    }

    /**
     * Test for format method when time zone is not provided.
     */
    @Test
    public void testFormat_NoTimeZoneProvided() {
        Date date = createDate(2023, 3, 13, 14, 30, 25, 350);
        String expected = "2023-03-13T14:30:25.350";
        String actual = ISO8601Utils.format(date, true, null);
        assertEquals(expected, actual);
    }

    /**
     * Test for format method when milliseconds are not provided.
     */
    @Test
    public void testFormat_NoMillisecondsProvided() {
        Date date = createDate(2023, 3, 13, 14, 30, 25);
        String expected = "2023-03-13T14:30:25Z";
        String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals(expected, actual);
    }

    /**
     * Test for parse method when all values are provided.
     */
    @Test
    public void testParse_AllValuesProvided() throws ParseException {
        String dateString = "2023-03-13T14:30:25.350Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1678983025350L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test for parse method when only date values are provided.
     */
    @Test
    public void testParse_OnlyDateValuesProvided() throws ParseException {
        String dateString = "2023-03-13";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1678947200000L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test for parse method when only time values are provided.
     */
    @Test
    public void testParse_OnlyTimeValuesProvided() throws ParseException {
        String dateString = "14:30:25.350";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(System.currentTimeMillis() + (14 * 60 * 60 * 1000) + 350 * 1000);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test for parse method when time zone is not provided.
     */
    @Test
    public void testParse_NoTimeZoneProvided() throws ParseException {
        String dateString = "2023-03-13T14:30:25.350";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1678983025350L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test for parse method when milliseconds are not provided.
     */
    @Test
    public void testParse_NoMillisecondsProvided() throws ParseException {
        String dateString = "2023-03-13T14:30:25";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1678983025000L);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test for parse method when invalid time zone is provided.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParse_InvalidTimeZoneProvided() throws ParseException {
        String dateString = "2023-03-13T14:30:25.350+InvalidTimeZone";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test for parse method when invalid date string is provided.
     */
    @Test(expected = ParseException.class)
    public void testParse_InvalidDateStringProvided() throws ParseException {
        String dateString = "2023-03-13T14:30:25.350A";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test for parse method when date string is null.
     */
    @Test
    public void testParse_NullDateStringProvided() {
        ParsePosition pos = new ParsePosition(0);
        assertNull(ISO8601Utils.parse(null, pos));
    }
}