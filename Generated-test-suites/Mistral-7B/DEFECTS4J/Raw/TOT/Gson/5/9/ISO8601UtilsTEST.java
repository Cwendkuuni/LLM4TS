package com.google.gson.internal.bind.util;

import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * ISO8601Utils test case file
 */
public class ISO8601UtilsTest {

    /**
     * Test format method for different date and time cases
     */
    @Test
    public void testFormat() {
        // Format a date without time and milliseconds
        Date date = new Date(1516044520000L); // Thu Jan 11 01:12:00 GMT 2018
        String result = ISO8601Utils.format(date);
        String expected = "2018-01-11T01:12:00Z";
        assertEquals(expected, result);

        // Format a date with time and milliseconds
        Date dateWithTime = new Date(1516044520123L); // Thu Jan 11 01:12:00.123 GMT 2018
        result = ISO8601Utils.format(dateWithTime, true);
        expected = "2018-01-11T01:12:00.123Z";
        assertEquals(expected, result);

        // Format a date with time zone
        Date dateWithTimeZone = new Date(1516044520123L); // Thu Jan 11 01:12:00.123 GMT 2018
        result = ISO8601Utils.format(dateWithTime, true, new TimeZone("Europe/London"));
        expected = "2018-01-11T02:12:00.123+00:00";
        assertEquals(expected, result);
    }

    /**
     * Test parse method for different date and time cases
     */
    @Test
    public void testParse() throws ParseException {
        // Parse a date without time and milliseconds
        String dateString = "2018-01-11";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int expectedYear = 2018;
        int expectedMonth = 1;
        int expectedDay = 11;
        int actualYear = calendar.get(Calendar.YEAR);
        int actualMonth = calendar.get(Calendar.MONTH) + 1;
        int actualDay = calendar.get(Calendar.DAY_OF_MONTH);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedMonth, actualMonth);
        assertEquals(expectedDay, actualDay);

        // Parse a date with time and milliseconds
        String dateWithTimeString = "2018-01-11T12:34:56.789";
        Date dateWithTime = ISO8601Utils.parse(dateWithTimeString, pos);
        calendar.setTime(dateWithTime);
        int expectedHour = 12;
        int expectedMinute = 34;
        int expectedSecond = 56;
        int expectedMillisecond = 789;
        int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
        int actualMinute = calendar.get(Calendar.MINUTE);
        int actualSecond = calendar.get(Calendar.SECOND);
        int actualMillisecond = calendar.get(Calendar.MILLISECOND);
        assertEquals(expectedHour, actualHour);
        assertEquals(expectedMinute, actualMinute);
        assertEquals(expectedSecond, actualSecond);
        assertEquals(expectedMillisecond, actualMillisecond);

        // Parse a date with time zone
        String dateWithTimeZoneString = "2018-01-11T12:34:56.789+01:00";
        Date dateWithTimeZone = ISO8601Utils.parse(dateWithTimeZoneString, pos);
        calendar.setTime(dateWithTimeZone);
        int expectedHourOffset = 1;
        int expectedMinuteOffset = 0;
        int actualHourOffset = calendar.get(Calendar.HOUR);
        int actualMinuteOffset = calendar.get(Calendar.MINUTE);
        assertEquals(expectedHourOffset, actualHourOffset);
        assertEquals(expectedMinuteOffset, actualMinuteOffset);

        // Test invalid formats
        String invalidFormatDateString1 = "2018-01-11T12:34:56.789Z123";
        try {
            ISO8601Utils.parse(invalidFormatDateString1, pos);
            fail("Expected ParseException for: " + invalidFormatDateString1);
        } catch (ParseException e) {
            // Expected
        }

        String invalidFormatDateString2 = "2018-01-11T12:34:56.789+01:60";
        try {
            ISO8601Utils.parse(invalidFormatDateString2, pos);
            fail("Expected ParseException for: " + invalidFormatDateString2);
        } catch (ParseException e) {
            // Expected
        }

        String invalidFormatDateString3 = "2018-01-11T12:34:56.789Z-123";
        try {
            ISO8601Utils.parse(invalidFormatDateString3, pos);
            fail("Expected ParseException for: " + invalidFormatDateString3);
        } catch (ParseException e) {
            // Expected
        }
    }
}