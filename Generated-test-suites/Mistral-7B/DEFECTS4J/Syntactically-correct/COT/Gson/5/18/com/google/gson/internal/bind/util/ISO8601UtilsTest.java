package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithDefaultTimeZone() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("Expected output should be in 'yyyy-MM-ddThh:mm:ssZ' format.",
                "yyyy-MM-ddThh:mm:ssZ", formattedDate.substring(0, formattedDate.length() - 1));
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Kolkata"), Locale.US);
        calendar.setTime(date);
        String formattedDate = ISO8601Utils.format(date, false, calendar.getTimeZone());
        assertEquals("Expected output should be in 'yyyy-MM-ddThh:mm:ss[.sss][Z|[+-]hh:mm]' format.",
                "yyyy-MM-ddThh:mm:ss[+05:30]" + System.lineSeparator(), formattedDate);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("Expected output should be in 'yyyy-MM-ddThh:mm:ss[.sss]Z' format.",
                "yyyy-MM-ddThh:mm:ss.SSSZ", formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithInvalidDate() {
        String invalidDate = "1970-02-31";
        ISO8601Utils.format(new Date(Long.parseLong("0")), false, TimeZone.getTimeZone("UTC"));
        assertEquals("Expected IllegalArgumentException to be thrown.",
                "Invalid date: " + invalidDate, throwable(new IllegalArgumentException()).getMessage());
    }

    @Test
    public void testParseDateWithDefaultTimeZone() throws ParseException {
        Date parsedDate = ISO8601Utils.parse("2023-03-22T14:30:45Z", new ParsePosition(0));
        assertNotNull("Expected parsed date to not be null.", parsedDate);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setTime(parsedDate);
        assertEquals("Expected parsed date to have the correct year, month and day.",
                2023, calendar.get(Calendar.YEAR));
        assertEquals("Expected parsed date to have the correct hour, minute and second.",
                14, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals("Expected parsed date to have the correct hour, minute and second.",
                30, calendar.get(Calendar.MINUTE));
        assertEquals("Expected parsed date to have the correct hour, minute and second.",
                45, calendar.get(Calendar.SECOND));
    }

    @Test
    public void testParseDateWithCustomTimeZone() throws ParseException {
        Date parsedDate = ISO8601Utils.parse("2023-03-22T14:30:45+05:30", new ParsePosition(0));
        assertNotNull("Expected parsed date to not be null.", parsedDate);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Kolkata"), Locale.US);
        calendar.setTime(parsedDate);
        assertEquals("Expected parsed date to have the correct year, month and day.",
                2023, calendar.get(Calendar.YEAR));
        assertEquals("Expected parsed date to have the correct hour, minute and second.",
                19, calendar.get(Calendar.HOUR_OF_DAY)); // Note: IST is 5 hours and 30 minutes ahead of UTC
        assertEquals("Expected parsed date to have the correct hour, minute and second.",
                30, calendar.get(Calendar.MINUTE));
        assertEquals("Expected parsed date to have the correct hour, minute and second.",
                45, calendar.get(Calendar.SECOND));
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidDate() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String invalidDate = "1970-02-31T00:00:00Z";
        ISO8601Utils.parse(invalidDate, pos);
        assertEquals("Expected ParseException to be thrown.",
                "Failed to parse date [" + invalidDate + "]: Invalid date: Invalid date '1970-02-31'",
                throwable(new ParseException("Failed to parse date [" + invalidDate + "]: Invalid date: ")).getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimeZoneIndicator() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String invalidTimeZoneIndicator = "2023-03-22T14:30:45X";
        ISO8601Utils.parse(invalidTimeZoneIndicator, pos);
        assertEquals("Expected ParseException to be thrown.",
                "Failed to parse date [" + invalidTimeZoneIndicator + "]: Invalid time zone indicator 'X'",
                throwable(new ParseException("Failed to parse date [" + invalidTimeZoneIndicator + "]: Invalid time zone indicator 'X'")).getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String invalidFormat = "2023-03-22T14:30:45InvalidFormat";
        ISO8601Utils.parse(invalidFormat, pos);
        assertEquals("Expected ParseException to be thrown.",
                "Failed to parse date [" + invalidFormat + "]: Invalid format: The string \"2023-03-22T14:30:45InvalidFormat\" does not match the pattern",
                throwable(new ParseException("Failed to parse date [" + invalidFormat + "]: Invalid format: The string \"2023-03-22T14:30:45InvalidFormat\" does not match the pattern")).getMessage());
    }

    private RuntimeException throwable(Exception e) {
        try {
            throw e;
        } catch (Throwable ex) {
            return (RuntimeException) ex;
        }
    }
}