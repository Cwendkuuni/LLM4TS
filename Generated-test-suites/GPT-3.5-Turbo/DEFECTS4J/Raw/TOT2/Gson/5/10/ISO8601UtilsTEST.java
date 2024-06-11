package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_defaultTimezone() {
        // Create a date object
        Date date = new Date(1613225757000L); // 13 February 2021 15:09:17 GMT

        // Format the date with default timezone
        String formattedDate = ISO8601Utils.format(date);

        // Assert the formatted date
        assertEquals("2021-02-13T15:09:17Z", formattedDate);
    }

    @Test
    public void testFormat_withMillis() {
        // Create a date object
        Date date = new Date(1613225757333L); // 13 February 2021 15:09:17.333 GMT

        // Format the date with millis included
        String formattedDate = ISO8601Utils.format(date, true);

        // Assert the formatted date with millis
        assertEquals("2021-02-13T15:09:17.333Z", formattedDate);
    }

    @Test
    public void testParse_validDate() throws ParseException {
        // Create an ISO-8601 formatted date string
        String isoDate = "2021-02-13T15:09:17.333Z";

        // Parse the ISO-8601 formatted date
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        // Create a calendar object and set the expected date values
        Calendar expected = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        expected.setTimeInMillis(1613225757333L); // 13 February 2021 15:09:17.333 GMT

        // Assert the parsed date
        assertEquals(expected.getTime(), parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidDate() throws ParseException {
        // Create an invalid ISO-8601 formatted date string
        String invalidDate = "2021-02-13T25:09:17.333Z";

        // Try to parse the invalid date string
        ISO8601Utils.parse(invalidDate, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_missingTimeZone() throws ParseException {
        // Create a valid ISO-8601 formatted date string without timezone
        String dateWithoutTimeZone = "2021-02-13T15:09:17.333";

        // Try to parse the date without timezone
        ISO8601Utils.parse(dateWithoutTimeZone, new ParsePosition(0));
    }

}