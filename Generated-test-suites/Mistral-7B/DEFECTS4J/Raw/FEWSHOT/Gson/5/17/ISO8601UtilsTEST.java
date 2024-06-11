package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1623333345000L); // Thu Apr 08 11:32:25 UTC 2021

        // Default timezone, no milliseconds precision
        String formattedDefault = ISO8601Utils.format(date);
        assertEquals("2021-04-08T11:32:25Z", formattedDefault);

        // GMT timezone, with milliseconds precision
        String formattedMillis = ISO8601Utils.format(date, true, null);
        assertEquals("2021-04-08T11:32:25.000Z", formattedMillis);

        // Specific timezone, with milliseconds precision
        Date dateWithTimezone = new Date(1623333345000L + 60 * 60 * 1000); // Thu Apr 08 12:32:25 UTC 2021
        String formattedTimezone = ISO8601Utils.format(dateWithTimezone, true, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("2021-04-08T13:32:25.000+02:00", formattedTimezone);
    }

    @Test
    public void testParse() throws Exception {
        String testCases[] = new String[]{
                "2019-12-31T23:59:59Z",
                "2019-12-31T23:59:59.999Z",
                "2019-12-31T23:59:59+01:00",
                "2019-12-31T23:59:59-01:00",
                "2019-12-31T23:59:59.001+01:00",
                "2019-12-31T23:59:59.001Z",
                "2019-12-31T23:59:59.123+01:00",
                null
        };

        for (String testCase : testCases) {
            ParsePosition pos = new ParsePosition(0);
            Date parsedDate = ISO8601Utils.parse(testCase, pos);
            if (testCase == null) {
                assertNull(parsedDate);
                continue;
            }
            if (pos.getIndex() < testCase.length()) {
                fail("Date [" + testCase + "] parsing failed, remaining characters: " + testCase.substring(pos.getIndex()));
            }
            assertEquals(testCase, ISO8601Utils.format(parsedDate, true, null));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidDate() {
        String invalidDate = "2019-12-32T23:59:59Z";
        ISO8601Utils.parse(invalidDate, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        String invalidFormat = "2019-12-31T23:59:59:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidFormat, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatInvalidTimezone() {
        ISO8601Utils.format(new Date(), true, new TimeZone("InvalidTimeZone"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFormatIndexOutOfBounds() {
        ISO8601Utils.format(new Date(), true, null);
    }
}