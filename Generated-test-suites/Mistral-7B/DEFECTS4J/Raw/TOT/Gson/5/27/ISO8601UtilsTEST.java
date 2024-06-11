package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Test cases for ISO8601Utils class.
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormatAndParseUTCDate() throws ParseException {
        // Given
        long utcDate = 1633046223000L; // 2021-10-01 12:10:23.000 UTC

        // When
        String formattedDate = ISO8601Utils.format(new Date(utcDate), false, TimeZone.getTimeZone("UTC"));
        Date parsedDate = ISO8601Utils.parse(formattedDate, new ParsePosition(0));

        // Then
        Assert.assertEquals("Expected formatted date to be '2021-10-01T12:10:23.000Z'", formattedDate, "2021-10-01T12:10:23.000Z");
        Assert.assertEquals("Expected parsed date to be the same as original date", new Date(utcDate), parsedDate);
    }

    @Test
    public void testFormatAndParseDateWithTimeZone() throws ParseException {
        // Given
        long dateWithTimeZone = 1633046223536L; // 2021-10-01 12:10:23.000 +02:00
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");

        // When
        String formattedDate = ISO8601Utils.format(new Date(dateWithTimeZone), false, timeZone);
        Date parsedDate = ISO8601Utils.parse(formattedDate, new ParsePosition(0), timeZone);

        // Then
        Assert.assertEquals("Expected formatted date to be '2021-10-01T14:10:23.000+02:00'", formattedDate, "2021-10-01T14:10:23.000+02:00");
        Assert.assertEquals("Expected parsed date to be the same as original date", new Date(dateWithTimeZone), parsedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidDate() {
        // Given
        long invalidDate = Long.MIN_VALUE;

        // When
        String formattedDate = ISO8601Utils.format(new Date(invalidDate), false, TimeZone.getTimeZone("UTC"));

        // Then
        // Expected IllegalArgumentException to be thrown
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        // Given
        String invalidDateString = "2021-10-01T12:10:23.000abc";

        // When
        Date parsedDate = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));

        // Then
        // Expected ParseException to be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidTimeZone() {
        // Given
        String dateString = "2021-10-01T12:10:23.000Z";

        // When
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0), new TimeZone("InvalidTimeZone"));

        // Then
        // Expected IllegalArgumentException to be thrown
    }
}