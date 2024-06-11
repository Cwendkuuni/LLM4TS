package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test parsing with default timezone (GMT)
        Date date = dateFormat.parse("2022-12-31T23:59:59Z");
        assertEquals("2022-12-31 23:59:59.000Z", dateFormat.format(date));

        date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("2022-12-31 23:59:59.123Z", dateFormat.format(date));

        // Test parsing with specific timezone
        dateFormat = dateFormat.withTimeZone(ZoneId.of("America/New_York"));
        date = dateFormat.parse("2022-12-31T04:59:59-05:00");
        assertEquals("2022-12-31 09:59:59.000-05:00", dateFormat.format(date));

        date = dateFormat.parse("2022-12-31T04:59:59.123-05:00");
        assertEquals("2022-12-31 09:59:59.123-05:00", dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test parsing with default timezone (GMT)
        Date date = dateFormat.parse("Saturday, 31 December 2022 23:59:59 GMT");
        assertEquals("Saturday, 31 December 2022 23:59:59.000 GMT", dateFormat.format(date));

        date = dateFormat.parse("Saturday, 31 December 2022 23:59:59.123 GMT");
        assertEquals("Saturday, 31 December 2022 23:59:59.123 GMT", dateFormat.format(date));

        // Test parsing with specific timezone
        dateFormat = dateFormat.withTimeZone(ZoneId.of("America/New_York"));
        date = dateFormat.parse("Saturday, 31 December 2022 04:59:59 EST");
        assertEquals("Saturday, 31 December 2022 09:59:59.000 EST", dateFormat.format(date));

        date = dateFormat.parse("Saturday, 31 December 2022 04:59:59.123 EST");
        assertEquals("Saturday, 31 December 2022 09:59:59.123 EST", dateFormat.format(date));
    }

    @Test
    public void testParseInvalid() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        try {
            dateFormat.parse("Invalid date string");
            fail();
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("Invalid date string with time", new ParsePosition(0));
            fail();
        } catch (ParseException e) {
            // Expected
        }
    }
}