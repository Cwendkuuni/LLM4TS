package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test file for ISO8601Utils class.
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date testDate = new Date();

        // Test without milliseconds and timezone
        assertEquals("2023-03-16T15:23:45Z", ISO8601Utils.format(testDate));

        // Test with milliseconds but without timezone
        assertEquals("2023-03-16T15:23:45.123", ISO8601Utils.format(testDate, true, null));

        // Test with timezone and milliseconds
        assertEquals("2023-03-16T15:23:45.123-05:00", ISO8601Utils.format(testDate, true, TimeZone.getTimeZone("America/New_York")));

        // Test with timezone but without milliseconds
        assertEquals("2023-03-16T15:23:45Z", ISO8601Utils.format(testDate, false, TimeZone.getTimeZone("UTC")));

        // Test with only date
        assertEquals("2023-03-16", ISO8601Utils.format(new Date(0)));
    }

    @Test
    public void testParse() throws ParseException {
        String testDateString = "2023-03-16T15:23:45Z";

        // Test parsing with UTC timezone
        Date parsedDate = ISO8601Utils.parse(testDateString, new ParsePosition(0));
        assertEquals(new Date(1678844235000L), parsedDate);

        // Test parsing with different timezones
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        calendar.setTime(new Date(1678844235000L));
        String testDateStringWithTimezone = sdf.format(calendar.getTime());
        parsedDate = ISO8601Utils.parse(testDateStringWithTimezone, new ParsePosition(0));
        assertEquals(new Date(1678844235000L + (-5 * 60 * 60 * 1000)), parsedDate);

        // Test parsing with missing milliseconds or timezone
        parsedDate = ISO8601Utils.parse("2023-03-16T15:23:45", new ParsePosition(0));
        assertEquals(new Date(1678844235000L), parsedDate);

        parsedDate = ISO8601Utils.parse("2023-03-16", new ParsePosition(0));
        assertEquals(new Date(0), parsedDate);

        parsedDate = ISO8601Utils.parse("2023-03-16T15:23", new ParsePosition(0));
        assertEquals(new Date(1678844235000L), parsedDate);

        parsedDate = ISO8601Utils.parse("2023-03-16T15:23:45Z", new ParsePosition(13));
        assertEquals(new Date(1678844235000L), parsedDate);

        parsedDate = ISO8601Utils.parse("2023-03-16T15:23:45+01:00", new ParsePosition(15));
        assertEquals(new Date(1678844235000L + (1 * 60 * 60 * 1000)), parsedDate);

        parsedDate = ISO8601Utils.parse("2023-03-16T15:23:45-05:00", new ParsePosition(15));
        assertEquals(new Date(1678844235000L - (5 * 60 * 60 * 1000)), parsedDate);
    }

}