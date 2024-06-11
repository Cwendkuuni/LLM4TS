package com.fasterxml.jackson.databind.util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test cases for StdDateFormat class.
 */
public class StdDateFormatTest {

    @Test
    public void testParseISO8601Date() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Invalid date format: '2022-12-31T23:59:59.123Z'", new Date(1671547199123L), date);
    }

    @Test
    public void testParseISO8601DateWithTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);
        Date date = dateFormat.parse("2022-12-31T04:59:59.123-05:00");
        assertEquals("Invalid date format: '2022-12-31T04:59:59.123-05:00'", new Date(1671504199123L), date);
    }

    @Test
    public void testParseRFC1123Date() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Sat, 31 Dec 2022 23:59:59 EST");
        assertEquals("Invalid date format: 'Sat, 31 Dec 2022 23:59:59 EST'", new Date(1671547199000L), date.getTime());
    }

    @Test
    public void testParseRFC1123DateWithTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);
        Date date = dateFormat.parse("Sat, 31 Dec 2022 04:59:59 EST");
        assertEquals("Invalid date format: 'Sat, 31 Dec 2022 04:59:59 EST'", new Date(1671504199000L), date.getTime());
    }

    @Test
    public void testParseInvalidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        try {
            dateFormat.parse("Invalid date string");
            fail("Expected ParseException for invalid date string");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormatDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String formattedDate = dateFormat.format(new Date(1671547199123L));
        assertEquals("Invalid formatted date", "2022-12-31T23:59:59.123Z", formattedDate);
    }

    @Test
    public void testFormatDateWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/New_York"), Locale.US);
        String formattedDate = dateFormat.format(new Date(1671504199123L));
        assertEquals("Invalid formatted date", "Sat Dec 31 04:59:59 EST 2022", formattedDate);
    }
}