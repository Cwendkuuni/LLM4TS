package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * ISO8601UtilsTest.java
 *
 * @author John Doe
 */
public class ISO8601UtilsTest {

    /**
     * Test formatting of dates.
     *
     * @throws Exception
     */
    @Test
    public void testFormat() throws Exception {
        Date date = new GregorianCalendar(2019, Calendar.AUGUST, 19).getTime();

        // Test default formatting
        String formattedDefault = ISO8601Utils.format(date);
        assertEquals("2019-08-19T00:00:00Z", formattedDefault);

        // Test formatting with milliseconds
        String formattedWithMilliseconds = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals("2019-08-19T00:00:12.345Z", formattedWithMilliseconds);

        // Test formatting with custom timezone
        TimeZone easternTimeZone = TimeZone.getTimeZone("America/New_York");
        String formattedWithCustomTimeZone = ISO8601Utils.format(date, true, easternTimeZone);
        assertEquals("2019-08-19T04:00:12.345-04:00", formattedWithCustomTimeZone);
    }

    /**
     * Test parsing of dates.
     *
     * @throws ParseException
     */
    @Test
    public void testParse() throws ParseException {
        String dateString = "2019-08-19T04:00:12.345-04:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);

        // Test parsed date
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        assertEquals(Calendar.AUGUST, calendar.get(Calendar.MONTH));
        assertEquals(Calendar.AUGUST, calendar.get(Calendar.MONTH) + 1);
        assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(4, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(12, calendar.get(Calendar.SECOND));
        assertEquals(345, calendar.get(Calendar.MILLISECOND));
        assertEquals(-4 * 60 * 60 * 1000, calendar.get(Calendar.ZONE_OFFSET));

        // Test parse position
        assertEquals(dateString.length(), pos.getIndex());
    }
}