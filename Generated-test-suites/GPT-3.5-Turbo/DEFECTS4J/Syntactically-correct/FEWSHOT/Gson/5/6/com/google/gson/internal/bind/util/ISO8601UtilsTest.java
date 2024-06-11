
package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test if the date is formatted correctly without milliseconds
        Calendar calendar = new GregorianCalendar(2021, Calendar.JANUARY, 1, 12, 0, 0);
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        String expected = "2021-01-01T12:00:00Z";
        String result = ISO8601Utils.format(calendar.getTime(), false, timezone);
        assertEquals(expected, result);

        // Test if the date is formatted correctly with milliseconds
        calendar = new GregorianCalendar(2021, Calendar.JANUARY, 1, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 123);
        expected = "2021-01-01T12:00:00.123Z";
        result = ISO8601Utils.format(calendar.getTime(), true, timezone);
        assertEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        // Test if the date is parsed correctly without milliseconds
        String input = "2021-01-01T12:00:00Z";
        ParsePosition position = new ParsePosition(0);
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        Calendar expected = new GregorianCalendar(timezone);
        expected.set(Calendar.YEAR, 2021);
        expected.set(Calendar.MONTH, Calendar.JANUARY);
        expected.set(Calendar.DAY_OF_MONTH, 1);
        expected.set(Calendar.HOUR_OF_DAY, 12);
        expected.set(Calendar.MINUTE, 0);
        expected.set(Calendar.SECOND, 0);
        expected.set(Calendar.MILLISECOND, 0);
        Date result = ISO8601Utils.parse(input, position);
        assertEquals(expected.getTime(), result);

        // Test if the date is parsed correctly with milliseconds
        input = "2021-01-01T12:00:00.123Z";
        position.setIndex(0);
        expected = new GregorianCalendar(timezone);
        expected.set(Calendar.YEAR, 2021);
        expected.set(Calendar.MONTH, Calendar.JANUARY);
        expected.set(Calendar.DAY_OF_MONTH, 1);
        expected.set(Calendar.HOUR_OF_DAY, 12);
        expected.set(Calendar.MINUTE, 0);
        expected.set(Calendar.SECOND, 0);
        expected.set(Calendar.MILLISECOND, 123);
        result = ISO8601Utils.parse(input, position);
        assertEquals(expected.getTime(), result);
    }
}
