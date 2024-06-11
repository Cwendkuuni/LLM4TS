package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils} class.
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2022-01-01T00:00:00Z", formatted);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        calendar.add(Calendar.MINUTE, 30);
        calendar.add(Calendar.SECOND, 15);
        date = calendar.getTime();
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2022-01-01T02:30:15.015Z", formatted);

        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        date = calendar.getTime();
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2022-01-01T05:30:15.015-05:00", formatted);

        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        date = calendar.getTime();
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/London"));
        assertEquals("2022-01-01T06:30:15.015+00:00", formatted);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2022-01-01T00:00:00Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(new GregorianCalendar(2022, Calendar.JANUARY, 1).getTime(), parsedDate);

        dateString = "2022-01-01T02:30:15.015-05:00";
        ParsePosition pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        calendar.setTime(parsedDate);
        assertEquals(Calendar.HOUR_OF_DAY, 5);
        assertEquals(Calendar.MINUTE, 30);
        assertEquals(Calendar.SECOND, 15);
        assertEquals(Calendar.MILLISECOND, 15);
        assertEquals(pos.getIndex(), dateString.length());

        dateString = "2022-01-01T06:30:15.015+00:00";
        parsedDate = ISO8601Utils.parse(dateString, pos);
        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/London"));
        calendar.setTime(parsedDate);
        assertEquals(Calendar.HOUR_OF_DAY, 6);
        assertEquals(Calendar.MINUTE, 30);
        assertEquals(Calendar.SECOND, 15);
        assertEquals(Calendar.MILLISECOND, 15);
        assertEquals(pos.getIndex(), dateString.length());

        try {
            ISO8601Utils.parse("2022-01-01", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("No time zone indicator"));
        }

        String invalidFormat = "2022-01-01T02:30:15.015-05:00:01";
        try {
            ISO8601Utils.parse(invalidFormat, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Invalid format:"));
        }

        String invalidFormatNoTimeZone = "2022-01-01T02:30:15.015";
        try {
            ISO8601Utils.parse(invalidFormatNoTimeZone, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("No time zone indicator"));
        }
    }
}