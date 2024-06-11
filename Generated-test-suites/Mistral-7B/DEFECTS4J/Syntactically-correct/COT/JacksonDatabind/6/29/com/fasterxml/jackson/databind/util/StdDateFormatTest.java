package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormatInstance() {
        StdDateFormat instance = StdDateFormat.instance;
        assertNotNull(instance);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat format = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, format.toPattern());
    }

    @Test(expected = Deprecated.class)
    public void testGetBlueprintISO8601FormatDeprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat format = StdDateFormat.getISO8601Format(timeZone, locale);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, format.toPattern());
    }

    @Test(expected = Deprecated.class)
    public void testGetISO8601FormatDeprecated() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        StdDateFormat.getISO8601Format(timeZone);
    }

    @Test
    public void testGetISO8601FormatWithDefaultTimeZone() {
        DateFormat format = StdDateFormat.getISO8601Format(Locale.US);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, format.toPattern());
    }

    @Test
    public void testGetISO8601FormatWithDefaultLocale() {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getDefault());
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, format.toPattern());
    }

    @Test
    public void testGetRFC1123Format() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat format = StdDateFormat.getRFC1123Format(timeZone, locale);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_RFC1123, format.toPattern());
    }

    @Test(expected = Deprecated.class)
    public void testGetRFC1123FormatDeprecated() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        StdDateFormat.getRFC1123Format(timeZone);
    }

    @Test
    public void testGetRFC1123FormatWithDefaultTimeZone() {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.US);
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_RFC1123, format.toPattern());
    }

    @Test
    public void testGetRFC1123FormatWithDefaultLocale() {
        DateFormat format = StdDateFormat.getRFC1123Format(TimeZone.getDefault());
        assertNotNull(format);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_RFC1123, format.toPattern());
    }

    @Test
    public void testParseISO8601Date() throws ParseException {
        DateFormat format = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("Asia/Kolkata"));
        Date date = format.parse("2022-12-31T15:30:45.123Z");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601InvalidDate() throws ParseException {
        DateFormat format = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("Asia/Kolkata"));
        format.parse("2022-12-31T15:30:45.123X");
    }

    @Test
    public void testParseRFC1123Date() throws ParseException {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("Europe/Paris"));
        Date date = format.parse("Sat, 31 Dec 2022 15:30:45 +0100");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123InvalidDate() throws ParseException {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("Europe/Paris"));
        format.parse("Sat, 31 Dec 2022 15:30:45 +0100Z");
    }

    @Test
    public void testFormatISO8601Date() {
        DateFormat format = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("Asia/Kolkata"));
        Date date = new Date();
        String formattedDate = format.format(date);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[.\\d{3}]\\d*Z"));
    }

    @Test
    public void testFormatRFC1123Date() {
        DateFormat format = StdDateFormat.getRFC1123Format(Locale.US, TimeZone.getTimeZone("Europe/Paris"));
        Date date = new Date();
        String formattedDate = format.format(date);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches("\\w{3}, \\d{1,2} \\w{3} \\d{4} \\d{2}:\\d{2}:\\d{2} \\d{4}"));
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        dateFormat.setTimeZone(timeZone);
        DateFormat format = dateFormat.getISO8601Format(Locale.US);
        assertEquals(timeZone, format.getTimeZone());
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = new StdDateFormat(Locale.US, TimeZone.getTimeZone("Europe/Paris"));
        StdDateFormat clonedDateFormat = (StdDateFormat) dateFormat.clone();
        assertNotSame(dateFormat, clonedDateFormat);
        assertEquals(dateFormat.getLocale(), clonedDateFormat.getLocale());
        assertEquals(dateFormat.getTimeZone(), clonedDateFormat.getTimeZone());
    }
}