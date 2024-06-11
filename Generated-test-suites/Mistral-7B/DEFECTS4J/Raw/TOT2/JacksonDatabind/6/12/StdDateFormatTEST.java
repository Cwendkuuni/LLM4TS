package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testStandardFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123Z";
        Date date = dateFormat.parse(dateString);
        assertEquals("Invalid date parsed", new Date(Instant.ofEpochMilli(1674211199123L).toEpochMilli()), date);
    }

    @Test
    public void testDefaultTimezone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-12-31T23:59:59.123Z";
        Date date = dateFormat.parse(dateString);
        assertEquals("Invalid date parsed with default timezone", new Date(Instant.ofEpochMilli(1674211199123L).toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), date.getTime());
    }

    @Test
    public void testCustomTimezone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        String dateString = "2022-12-31T23:59:59.123Z";
        Date date = dateFormat.parse(dateString);
        assertEquals("Invalid date parsed with custom timezone", new Date(Instant.ofEpochMilli(1674211199123L).atZone(ZoneId.systemDefault()).toInstant().withZoneSameInstant(TimeZone.getTimeZone("America/Los_Angeles")).toInstant().toEpochMilli()), date.getTime());
    }

    @Test
    public void testParseInvalidFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "InvalidFormat";
        assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
    }

    @Test
    public void testParseEmptyString() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        assertNull("Parse empty string should return null", dateFormat.parse(""));
    }

    @Test
    public void testParseNull() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        assertNull("Parse null should return null", dateFormat.parse(null));
    }

    @Test
    public void testFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date(1674211199123L);
        String dateString = dateFormat.format(date);
        assertEquals("Incorrect date format", "2022-12-31T23:59:59.123Z", dateString);
    }

    @Test
    public void testFormatWithCustomTimezone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        Date date = new Date(1674211199123L);
        String dateString = dateFormat.format(date);
        assertEquals("Incorrect date format with custom timezone", "2022-12-31T22:59:59.123-08:00", dateString);
    }

    @Test
    public void testClone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertNotNull("Clone should not be null", clonedDateFormat);
        assertEquals("Cloned date format should be equal to original", dateFormat, clonedDateFormat);
    }
}