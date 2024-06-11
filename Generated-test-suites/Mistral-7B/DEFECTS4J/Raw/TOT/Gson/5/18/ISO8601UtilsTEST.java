package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

/**
 * Tests for ISO8601Utils class.
 *
 * @author Johnson.Dias
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        String expectedFormattedDate = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // Format using placeholders

        assertEquals(expectedFormattedDate, formattedDate);

        formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/London"));
        expectedFormattedDate = "yyyy-MM-dd'T'HH:mm:ss'Z'"; // Format without milliseconds and with specified timezone

        assertEquals(expectedFormattedDate, formattedDate);

        // Testing with milliseconds
        formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/London"));
        expectedFormattedDate = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // Format with milliseconds and with specified timezone

        assertEquals(expectedFormattedDate, formattedDate);
    }

    @Test
    public void testParse() {
        String validDate = "2021-12-31T23:59:59.999Z";
        Date parsedDate = null;
        ParsePosition parsePosition = new ParsePosition(0);

        try {
            parsedDate = ISO8601Utils.parse(validDate, parsePosition);
        } catch (ParseException e) {
            fail("Failed to parse valid date [" + validDate + "]");
        }

        assertEquals("Failed to parse valid date [" + validDate + "]: ", validDate, ISO8601Utils.format(parsedDate));

        // Testing parsing with timezone offset
        String dateWithOffset = "2021-12-31T23:59:59.999+01:00";
        try {
            parsedDate = ISO8601Utils.parse(dateWithOffset, parsePosition);
        } catch (ParseException e) {
            fail("Failed to parse date [" + dateWithOffset + "]");
        }

        assertEquals("Failed to parse date [" + dateWithOffset + "]: ", dateWithOffset, ISO8601Utils.format(parsedDate, true, TimeZone.getTimeZone("Europe/London")));

        // Testing parsing with invalid format
        String invalidDate = "2021-12-31T23:59:59.999ZZ";
        try {
            ISO8601Utils.parse(invalidDate, parsePosition);
            fail("Expected ParseException for invalid date [" + invalidDate + "]");
        } catch (ParseException e) {
            // Expected
        }
    }
}