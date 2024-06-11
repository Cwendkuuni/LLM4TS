package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();

        // Test default behavior
        String expectedDefault = "yyyy-MM-ddThh:mm:ssZ";
        String actualDefault = ISO8601Utils.format(date);
        assertEquals(expectedDefault, actualDefault);

        // Test with millis precision
        String expectedMillis = "yyyy-MM-ddThh:mm:ss.SSSZ";
        String actualMillis = ISO8601Utils.format(date, true);
        assertEquals(expectedMillis, actualMillis);

        // Test with custom timezone
        TimeZone customTimezone = TimeZone.getTimeZone("GMT+05:30");
        String expectedCustomTz = "yyyy-MM-ddThh:mm:ssZ";
        String actualCustomTz = ISO8601Utils.format(date, false, customTimezone);
        assertEquals(expectedCustomTz, actualCustomTz);
    }

    @Test
    public void testParse() {
        // Test valid ISO-8601 string
        String validIsoString = "2021-12-01T08:30:00+05:30";
        String expectedIsoString = "yyyy-MM-ddThh:mm:ssZ";
        ParsePosition pos = new ParsePosition(0);
        try {
            Date parsedDate = ISO8601Utils.parse(validIsoString, pos);
            assertEquals(expectedIsoString, ISO8601Utils.format(parsedDate));
        } catch (ParseException e) {
            fail("Failed to parse valid ISO-8601 string.");
        }

        // Test invalid ISO-8601 string
        String invalidIsoString = "2021/12/01 08:30:00+05:30";
        try {
            ISO8601Utils.parse(invalidIsoString, pos);
            fail("Parsed invalid ISO-8601 string should result in ParseException.");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test partial valid ISO-8601 string
        String partialValidIsoString = "2021-12-01";
        try {
            ISO8601Utils.parse(partialValidIsoString, pos);
            fail("Parsed partial valid ISO-8601 string should result in ParseException.");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test valid ISO-8601 string with missing time component
        String validIsoStringWithoutTime = "2021-12-01T";
        try {
            ISO8601Utils.parse(validIsoStringWithoutTime, pos);
            fail("Parsed valid ISO-8601 string without time component should result in ParseException.");
        } catch (ParseException e) {
            // Expected exception
        }

        // Test valid ISO-8601 string with missing timezone
        String validIsoStringWithoutTimezone = "2021-12-01T08:30:00";
        try {
            ISO8601Utils.parse(validIsoStringWithoutTimezone, pos);
            fail("Parsed valid ISO-8601 string without timezone should result in ParseException.");
        } catch (ParseException e) {
            // Expected exception
        }
    }

}