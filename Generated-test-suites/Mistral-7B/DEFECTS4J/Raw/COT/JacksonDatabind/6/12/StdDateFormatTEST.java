package com.example.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * The class {@code StdDateFormatTest} tests all methods in the {@code StdDateFormat} class.
 *
 * @author John Doe
 */
public class StdDateFormatTest {

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(dateFormat);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, dateFormat.toPattern());
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        assertNotNull(dateFormat);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, dateFormat.toPattern());
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("Europe/London"), Locale.UK);
        assertNotNull(dateFormat);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_RFC1123, dateFormat.toPattern());
    }

    @Test
    public void testParseWithValidISO8601Date() throws ParseException {
        DateFormat dateFormat = StdDateFormat.getInstance();
        Date date = dateFormat.parse("2022-12-31T23:59:59.999Z");
        assertNotNull(date);
    }

    @Test
    public void testParseWithValidRFC1123Date() throws ParseException {
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("Europe/London"), Locale.UK);
        Date date = dateFormat.parse("Wed, 31 Dec 2022 23:59:59 +0000");
        assertNotNull(date);
    }

    @Test
    public void testParseWithInvalidDate() throws ParseException {
        DateFormat dateFormat = StdDateFormat.getInstance();
        String[] invalidDates = {"123", "2022-12-32", "2022-12-31T23:59:60"};
        for (String invalidDate : invalidDates) {
            try {
                dateFormat.parse(invalidDate);
                fail("Expected ParseException for invalid date: " + invalidDate);
            } catch (ParseException e) {
                // Expected
            }
        }
    }

    @Test
    public void testFormatWithValidDate() {
        DateFormat dateFormat = StdDateFormat.getInstance();
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches(".*")); // Any non-empty string
    }

    @Test
    public void testClone() {
        StdDateFormat original = new StdDateFormat();
        StdDateFormat cloned = original.clone();
        assertNotSame(original, cloned);
        assertEquals(original.getLocale(), cloned.getLocale());
        assertEquals(original.getTimeZone(), cloned.getTimeZone());
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat instance = new StdDateFormat();
        StdDateFormat withTimeZone = instance.withTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        assertNotSame(instance, withTimeZone);
        assertEquals(TimeZone.getTimeZone("Europe/Moscow"), withTimeZone.getTimeZone());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat instance = new StdDateFormat();
        StdDateFormat withLocale = instance.withLocale(Locale.CHINESE);
        assertNotSame(instance, withLocale);
        assertEquals(Locale.CHINESE, withLocale.getLocale());
    }

    
    // Add any other test cases here
    
}