
package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_Default() {
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0);
        Date date = calendar.getTime();

        String expected = "2022-01-01T12:00:00Z";
        String actual = ISO8601Utils.format(date);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_WithMillis() {
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 123);
        Date date = calendar.getTime();

        String expected = "2022-01-01T12:00:00.123Z";
        String actual = ISO8601Utils.format(date, true);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat_WithTimeZone() {
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0);
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Date date = calendar.getTime();

        String expected = "2022-01-01T20:00:00Z";
        String actual = ISO8601Utils.format(date, false, timeZone);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_Default() throws ParseException {
        String dateStr = "2022-01-01T12:00:00Z";
        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0).getTime();

        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDate() throws ParseException {
        String dateStr = "2022-01-01T12:00:00";
        ParsePosition pos = new ParsePosition(0);

        ISO8601Utils.parse(dateStr, pos);
    }

    @Test
    public void testParse_WithOffset() throws ParseException {
        String dateStr = "2022-01-01T12:00:00+05:30";
        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 6, 30, 0).getTime();

        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateStr, pos);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}

