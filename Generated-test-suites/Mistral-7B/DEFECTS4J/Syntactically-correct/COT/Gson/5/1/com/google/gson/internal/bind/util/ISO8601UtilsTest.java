package com.example.junit; // Replace this package name with the actual package you are using

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

/**
 * Tests for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() throws Exception {
        Date date = new Date(0L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 45);
        calendar.set(Calendar.MILLISECOND, 567);
        calendar.set(Calendar.MILLISECOND, 567); // Set millisecond twice to test its handling

        String expected = "2022-01-01T12:30:45.567+01:00";
        String actual = ISO8601Utils.format(calendar.getTime(), true, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(expected, actual);

        expected = "2022-01-01T12:30:45Z";
        actual = ISO8601Utils.format(calendar.getTime(), true, TimeZone.getTimeZone(ISO8601Utils.UTC_ID));
        assertEquals(expected, actual);

        expected = "2022-01-01T12:30:45"; // No time zone or milliseconds
        actual = ISO8601Utils.format(calendar.getTime());
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithNullDate() {
        String result = ISO8601Utils.format(null);
        fail("Expected IllegalArgumentException, but got: " + result);
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2022-01-01T12:30:45.567+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1641513045567L); // The long value represents the number of milliseconds since 1970-01-01 00:00:00 UTC
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());

        expected = new Date(1641513045000L); // Test parsing without milliseconds
        pos.setIndex(dateString.length() - 3); // Set index to the position before the timezone offset
        actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());

        expected = new Date(1641512000000L); // Test parsing with a different timezone offset
        pos.setIndex(dateString.length() - 6); // Set index to the position before the timezone offset "+01:00"
        String timezoneOffset = dateString.substring(pos.getIndex());
        TimeZone timeZone = TimeZone.getTimeZone("GMT" + timezoneOffset); // The timezone offset is added to GMT to get the actual timezone
        actual = ISO8601Utils.parse(dateString, pos, timeZone);
        assertEquals(expected.getTime(), actual.getTime());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() {
        String invalidDateString = "2022-01-01T12:30:45.567+01:00A";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidDateString, pos);
        fail("Expected ParseException, but got: " + pos.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithNullString() {
        String input = null;
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(input, pos);
        fail("Expected IllegalArgumentException, but got: null");
    }
}