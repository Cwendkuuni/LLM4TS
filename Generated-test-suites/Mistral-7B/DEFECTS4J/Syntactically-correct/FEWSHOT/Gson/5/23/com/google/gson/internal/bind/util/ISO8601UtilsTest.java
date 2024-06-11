package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatAndParseDates() throws ParseException {
        // Test formatting and parsing of simple date strings
        Date date = new Date(1651937200000L); // Thu, 03 Mar 2022 00:00:00 GMT

        String formattedDate = ISO8601Utils.format(date);
        Date parsedDate = ISO8601Utils.parse(formattedDate, new ParsePosition(0));

        assertTrue(parsedDate.equals(date));

        // Test formatting and parsing of date strings with time and timezone
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        calendar.add(Calendar.MINUTE, 15);
        calendar.add(Calendar.SECOND, 30);
        calendar.add(Calendar.MILLISECOND, 456);

        String formattedDateWithTimeAndTimezone = ISO8601Utils.format(calendar.getTime(), true, TimeZone.getTimeZone("UTC"));
        Date parsedDateWithTimeAndTimezone = ISO8601Utils.parse(formattedDateWithTimeAndTimezone, new ParsePosition(0));

        Calendar parsedCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        parsedCalendar.setTime(parsedDateWithTimeAndTimezone);

        assertFalse(parsedDateWithTimeAndTimezone.equals(date)); // Different due to time and timezone
        assertTrue(parsedCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
        assertTrue(parsedCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH));
        assertTrue(parsedCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH));
        assertTrue(parsedCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY) + 2);
        assertTrue(parsedCalendar.get(Calendar.MINUTE) == calendar.get(Calendar.MINUTE) + 15);
        assertTrue(parsedCalendar.get(Calendar.SECOND) == calendar.get(Calendar.SECOND()) + 30);
        assertTrue(parsedCalendar.get(Calendar.MILLISECOND) == calendar.get(Calendar.MILLISECOND()) + 456);

        // Test parsing with missing milliseconds, seconds or timezone
        String dateString1 = "2022-03-03";
        ParsePosition pos1 = new ParsePosition(0);
        Date dateParsed1 = ISO8601Utils.parse(dateString1, pos1);
        assertNull(dateParsed1);
        assertEquals(pos1.getIndex(), dateString1.length());

        String dateString2 = "2022-03-03T14:30";
        ParsePosition pos2 = new ParsePosition(0);
        Date dateParsed2 = ISO8601Utils.parse(dateString2, pos2);
        assertNull(dateParsed2);
        assertEquals(pos2.getIndex(), dateString2.length());

        String dateString3 = "2022-03-03T14:30:59Z";
        Date dateParsed3 = ISO8601Utils.parse(dateString3, pos2);
        assertTrue(dateParsed3 != null);
        Calendar parsedCalendar3 = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        parsedCalendar3.setTime(dateParsed3);
        assertTrue(parsedCalendar3.get(Calendar.YEAR) == 2022);
        assertTrue(parsedCalendar3.get(Calendar.MONTH) == 2);
        assertTrue(parsedCalendar3.get(Calendar.DAY_OF_MONTH) == 3);
        assertTrue(parsedCalendar3.get(Calendar.HOUR_OF_DAY) == 14);
        assertTrue(parsedCalendar3.get(Calendar.MINUTE) == 30);
        assertTrue(parsedCalendar3.get(Calendar.SECOND) == 59);
        assertTrue(parsedCalendar3.get(Calendar.MILLISECOND) == 0);
        assertEquals(pos2.getIndex(), dateString3.length());

        // Test parsing with invalid timezone
        String dateString4 = "2022-03-03T14:30:59XXX";
        ParsePosition pos4 = new ParsePosition(0);
        Date dateParsed4 = ISO8601Utils.parse(dateString4, pos4);
        assertNull(dateParsed4);
        assertEquals(pos4.getIndex(), dateString4.length());
    }

    @Test
    public void testFormatAndParseDatesWithInvalidFormat() {
        String[] invalidFormats = {"2022-3-03", "2022-03-03T14:30:59.", "2022-03-03T14:30:59ZZ"};
        for (String invalidFormat : invalidFormats) {
            ParsePosition pos = new ParsePosition(0);
            Date dateParsed = ISO8601Utils.parse(invalidFormat, pos);
            assertNull(dateParsed);
            assertEquals(pos.getIndex(), invalidFormat.length());
        }
    }

    @Test
    public void testFormatAndParseDatesWithNull() {
        String dateString = null;
        Date dateParsed = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertNull(dateParsed);
        assertEquals(0, new ParsePosition(0).getIndex());
    }

    @Test
    public void testFormatAndParseDatesWithEmptyString() {
        String dateString = "";
        Date dateParsed = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertNull(dateParsed);
        assertEquals(0, new ParsePosition(0).getIndex());
    }
}