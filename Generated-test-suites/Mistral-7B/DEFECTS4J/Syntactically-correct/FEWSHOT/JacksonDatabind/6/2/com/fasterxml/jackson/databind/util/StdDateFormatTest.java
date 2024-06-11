package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseWithDefaultInstance() {
        StdDateFormat instance = StdDateFormat.instance;
        Date date = null;
        try {
            date = instance.parse("2023-03-15");
        } catch (ParseException e) {
            fail("Failed to parse date with default instance: " + e.getMessage());
        }
        assertEquals("Thu Mar 16 00:00:00 GMT 2023", instance.format(date));

        try {
            date = instance.parse("2023-03-15T12:34:56.789Z");
        } catch (ParseException e) {
            fail("Failed to parse date with default instance: " + e.getMessage());
        }
        assertEquals("Thu Mar 16 12:34:56.789 GMT 2023", instance.format(date));

        try {
            date = instance.parse("Wed, 15 Mar 2023 12:34:56 GMT");
        } catch (ParseException e) {
            fail("Failed to parse date with default instance: " + e.getMessage());
        }
        assertEquals("Wed Mar 15 12:34:56 GMT 2023", instance.format(date));
    }

    @Test
    public void testParseWithCustomInstance() {
        StdDateFormat customInstance = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.GERMANY);
        Date date = null;
        try {
            date = customInstance.parse("2023-03-15");
        } catch (ParseException e) {
            fail("Failed to parse date with custom instance: " + e.getMessage());
        }
        assertEquals("Thu Mar 16 01:00:00 CET 2023", customInstance.format(date));

        try {
            date = customInstance.parse("2023-03-15T12:34:56.789Z");
        } catch (ParseException e) {
            fail("Failed to parse date with custom instance: " + e.getMessage());
        }
        assertEquals("Thu Mar 16 13:34:56.789 CET 2023", customInstance.format(date));

        try {
            date = customInstance.parse("Wed, 15 Mar 2023 12:34:56 GMT");
        } catch (ParseException e) {
            fail("Failed to parse date with custom instance: " + e.getMessage());
        }
        assertEquals("Wed Mar 15 13:34:56 CET 2023", customInstance.format(date));
    }

    @Test
    public void testParseWithInvalidFormat() {
        StdDateFormat instance = StdDateFormat.instance;
        assertNull("Default instance should not be able to parse invalid date: 13moose",
                instance.parse("13moose"));

        StdDateFormat customInstance = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.GERMANY);
        assertNull("Custom instance should not be able to parse invalid date: 13moose",
                customInstance.parse("13moose"));
    }

    @Test
    public void testFormat() {
        StdDateFormat instance = StdDateFormat.instance;
        assertEquals("2023-03-15", instance.format(new Date(1678846400000L)));
        assertEquals("2023-03-15T12:34:56.789Z", instance.format(new Date(1678846456789L)));
        assertEquals("Wed, 15 Mar 2023 12:34:56 GMT", instance.format(new Date(1678846456789L)));

        StdDateFormat customInstance = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.GERMANY);
        assertEquals("2023-03-15", customInstance.format(new Date(1678846400000L)));
        assertEquals("2023-03-15T13:34:56.789 CET", customInstance.format(new Date(1678846456789L)));
        assertEquals("Wed, 15 Mar 2023 13:34:56 CET", customInstance.format(new Date(1678846456789L)));
    }

    @Test
    public void testToString() {
        StdDateFormat instance = StdDateFormat.instance;
        String toString = instance.toString();
        assertTrue("Default instance toString should contain class name",
                toString.contains("DateFormat"));

        StdDateFormat customInstance = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.GERMANY);
        toString = customInstance.toString();
        assertTrue("Custom instance toString should contain class name",
                toString.contains("DateFormat"));
        assertTrue("Custom instance toString should contain timezone",
                toString.contains("timezone: UTC+1"));
        assertTrue("Custom instance toString should contain locale",
                toString.contains("locale: de_DE"));
    }
}