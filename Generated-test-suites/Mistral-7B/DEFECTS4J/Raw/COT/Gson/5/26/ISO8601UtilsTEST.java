package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    /**
     * Test for format method with no time or timezone.
     * @throws ParseException
     */
    @Test
    public void testFormatNoTimeOrTimezone() throws ParseException {
        Date date = new GregorianCalendar(2000, Calendar.AUGUST, 25).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2000-08-25", formatted);
    }

    /**
     * Test for format method with time but no timezone.
     * @throws ParseException
     */
    @Test
    public void testFormatWithTimeButNoTimezone() throws ParseException {
        Date date = new GregorianCalendar(2000, Calendar.AUGUST, 25, 12, 30, 45).getTime();
        String formatted = ISO8601Utils.format(date, true);
        assertEquals("2000-08-25T12:30:45", formatted);
    }

    /**
     * Test for format method with time and timezone.
     * @throws ParseException
     */
    @Test
    public void testFormatWithTimeAndTimezone() throws ParseException {
        Date date = new GregorianCalendar(2000, Calendar.AUGUST, 25, 12, 30, 45, 123).getTime();
        String timeZoneId = "America/Los_Angeles";
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone(timeZoneId));
        assertEquals("2000-08-25T12:30:45-07:00", formatted);
    }

    /**
     * Test for format method with invalid milliseconds.
     * @throws ParseException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidMillis() throws ParseException {
        Date date = new GregorianCalendar(2000, Calendar.AUGUST, 25, 12, 30, 61).getTime();
        ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
    }

    /**
     * Test for parse method with valid date string.
     * @throws ParseException
     */
    @Test
    public void testParseValidDateString() throws ParseException {
        String dateString = "2000-08-25T12:30:45Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        GregorianCalendar expectedDate = new GregorianCalendar(2000, Calendar.AUGUST, 25, 12, 30, 45);
        assertEquals(expectedDate.getTime().getTime(), parsedDate.getTime());
    }

    /**
     * Test for parse method with invalid date string format.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDateStringFormat() throws ParseException {
        String dateString = "2000-08-25T12:30:45abc";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    /**
     * Test for parse method with valid date string and negative timezone.
     * @throws ParseException
     */
    @Test
    public void testParseValidDateStringWithNegativeTimezone() throws ParseException {
        String dateString = "2000-08-25T12:30:45-05:00";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        GregorianCalendar expectedDate = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"), 2000, Calendar.AUGUST, 25, 12, 30, 45);
        assertEquals(expectedDate.getTime().getTime(), parsedDate.getTime());
    }

    /**
     * Test for parse method with valid date string and positive timezone.
     * @throws ParseException
     */
    @Test
    public void testParseValidDateStringWithPositiveTimezone() throws ParseException {
        String dateString = "2000-08-25T12:30:45+02:00";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        GregorianCalendar expectedDate = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"), 2000, Calendar.AUGUST, 25, 12, 30, 45);
        assertEquals(expectedDate.getTime().getTime(), parsedDate.getTime());
    }

    /**
     * Test for parse method with null date string.
     * @throws ParseException
     */
    @Test
    public void testParseNullDateString() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(null, pos);
        assertNull(pos.getIndex());
    }
}