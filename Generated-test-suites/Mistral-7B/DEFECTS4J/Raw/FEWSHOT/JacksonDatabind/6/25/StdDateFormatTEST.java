package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithTimezone() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;

        // Test with GMT
        date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals("2022-01-01 12:34:56.789", dateFormat.format(date));

        // Test with timezone
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals("2022-01-01 13:34:56.789", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithoutTimezone() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;

        // Test with GMT
        date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals("2022-01-01 12:34:56.789", dateFormat.format(date));

        // Test with local date
        dateFormat = dateFormat.withTimeZone(TimeZone.getDefault());
        date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        assertEquals(localFormat.format(date), dateFormat.format(date));
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;

        // Test with GMT
        date = dateFormat.parse("Thu, 01 Jan 2022 12:34:56 GMT");
        assertEquals("Thu Jan 01 12:34:56 GMT 2022", dateFormat.format(date));

        // Test with local time
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        date = dateFormat.parse("Thu, 01 Jan 2022 12:34:56 GMT");
        assertEquals("Thu Jan 01 13:34:56 CET 2022", dateFormat.format(date));
    }

    @Test
    public void testParseInvalidFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date;
        ParsePosition pos = new ParsePosition(0);

        try {
            date = dateFormat.parse("InvalidDate");
            fail();
        } catch (ParseException e) {
            // Expected
        }

        try {
            date = dateFormat.parse("2022-01-33T12:34:56.789Z", pos);
            fail();
        } catch (ParseException e) {
            // Expected
            assertEquals("Can not parse date \"2022-01-33T12:34:56.789Z\": not compatible with any of standard forms ([\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\", \"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\", \"EEE, dd MMM yyyy HH:mm:ss zzz\", \"yyyy-MM-dd\"]) ", e.getMessage());
        }
    }
}