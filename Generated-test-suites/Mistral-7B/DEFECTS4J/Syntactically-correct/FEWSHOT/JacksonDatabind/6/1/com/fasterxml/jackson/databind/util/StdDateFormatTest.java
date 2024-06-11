package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        String[] testCases = new String[]{"2023-03-15T12:30:45.012Z", "2023-03-15T12:30:45.012+01:00", "2023-03-15T12:30:45.012-05:00", "2023-03-15T12:30:45.012+01:00:12", "2023-03-15T12:30:45.012Z00:00:00.001"};
        for (String testCase : testCases) {
            Date date = sdf.parse(testCase);
            assertNotNull(date);
            Instant instant = date.toInstant();
            Instant expected = Instant.parse(testCase);
            assertEquals(expected, instant);
        }
    }

    @Test
    public void testParseRFC1123() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        String[] testCases = new String[]{"Wed, 15 Mar 2023 12:30:45 GMT", "Wed, 15 Mar 2023 12:30:45 +0100", "Wed, 15 Mar 2023 12:30:45 -0500"};
        for (String testCase : testCases) {
            Date date = sdf.parse(testCase);
            assertNotNull(date);
            Instant instant = date.toInstant();
            Instant expected = Instant.parse(testCase.replace("GMT", "Z"));
            assertEquals(expected, instant);
        }
    }

    @Test
    public void testParsePlain() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        String[] testCases = new String[]{"2023-03-15"};
        for (String testCase : testCases) {
            Date date = sdf.parse(testCase);
            assertNotNull(date);
            Instant instant = date.toInstant();
            Instant expected = Instant.ofEpochDay(Calendar.toInstant(GregorianCalendar.from(Locale.US, 2023, 3, 15).toZonedDateTime().with(TemporalAdjusters.atStartOfDay())).toInstant());
            assertEquals(expected, instant);
        }
    }

    @Test
    public void testParseInvalid() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        String[] testCases = new String[]{"2023-14-03T12:30:45.012Z", "2023-03-32T12:30:45.012Z", "2023-03-15T25:30:45.012Z", "2023-03-15T12:30:60.012Z", "2023-03-15T12:30:45.012+++", "2023-03-15T12:30:45.012--:--"};
        for (String testCase : testCases) {
            try {
                sdf.parse(testCase);
                fail("Expected ParseException");
            } catch (ParseException e) {
                // Expected
            }
        }
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        Date date = Calendar.getInstance().getTime();
        String expected = sdf.format(date);
        String formatted = sdf.format(date, new StringBuilder().append("_formatted"), new FieldPosition(0, 0));
        assertEquals(expected, formatted);
    }

    @Test
    public void testGetDefaultTimeZone() {
        assertEquals("GMT", TimeZone.getDefault().getID());
        assertEquals("GMT", StdDateFormat.getDefaultTimeZone().getID());
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        StdDateFormat sdfWithTimeZone = sdf.withTimeZone(timeZone);
        assertEquals(timeZone, sdfWithTimeZone.getTimeZone());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat sdf = new StdDateFormat();
        Locale locale = Locale.FRENCH;
        StdDateFormat sdfWithLocale = sdf.withLocale(locale);
        assertEquals(locale, sdfWithLocale.getLocale());
    }

    @Test
    public void testClone() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        DateFormat cloned = sdf.clone();
        assertEquals("class com.fasterxml.jackson.databind.util.StdDateFormat", cloned.getClass().getName());
        assertEquals(sdf.getTimeZone(), cloned.getTimeZone());
        assertEquals(sdf.getLocale(), cloned.getLocale());
    }

    @Test
    public void testGetBlueprintISO8601Format() throws Exception {
        assertEquals("class com.fasterxml.jackson.databind.util.StdDateFormat", StdDateFormat.getBlueprintISO8601Format().getClass().getName());
        assertEquals("GMT", StdDateFormat.getBlueprintISO8601Format().getTimeZone().getID());
        assertEquals(Locale.US, StdDateFormat.getBlueprintISO8601Format().getLocale());
    }

    @Test
    public void testGetISO8601Format() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        DateFormat isoFormat = sdf.getISO8601Format(Locale.US, TimeZone.getTimeZone("Europe/London"));
        assertEquals("class com.fasterxml.jackson.databind.util.StdDateFormat", isoFormat.getClass().getName());
        assertEquals(TimeZone.getTimeZone("Europe/London"), isoFormat.getTimeZone());
        assertEquals(Locale.US, isoFormat.getLocale());
    }

    @Test
    public void testGetRFC1123Format() throws Exception {
        StdDateFormat sdf = new StdDateFormat();
        DateFormat rfcFormat = sdf.getRFC1123Format(Locale.US, TimeZone.getTimeZone("Europe/London"));
        assertEquals("class com.fasterxml.jackson.databind.util.StdDateFormat", rfcFormat.getClass().getName());
        assertEquals(TimeZone.getTimeZone("Europe/London"), rfcFormat.getTimeZone());
        assertEquals(Locale.US, rfcFormat.getLocale());
    }
}