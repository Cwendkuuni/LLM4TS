package com.example.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    final StdDateFormat instance = StdDateFormat.instance;

    @Test
    public void testConstructors() {
        assertNotNull(instance);

        StdDateFormat tzus = new StdDateFormat(Locale.US);
        StdDateFormat tzgmt = new StdDateFormat(TimeZone.getTimeZone("GMT"));
        assertNotSame(tzus, tzgmt);

        assertEquals(Locale.US, tzus.getLocale());
        assertEquals(TimeZone.getDefault(), tzus.getTimeZone());

        assertEquals(Locale.US, tzus.getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), tzgmt.getTimeZone());
    }

    @Test
    public void testGetDefaultTimeZone() {
        assertEquals(TimeZone.getDefault(), StdDateFormat.getDefaultTimeZone());
    }

    @Test
    public void testParseISO8601FormatWithTimezone() throws ParseException {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = format.parse("2022-12-31T23:59:59.999-05:00");

        assertEquals("Sat Dec 31 22:59:59 UTC 2022", format.format(date));
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        DateFormat format = StdDateFormat.getRFC1123Format(TimeZone.getDefault(), Locale.US);
        Date date = format.parse("Fri, 31 Dec 2022 23:59:59 GMT");

        assertEquals("Fri Dec 31 23:59:59 GMT 2022", format.format(date));
    }

    @Test
    public void testParsePlainFormat() throws ParseException {
        DateFormat format = StdDateFormat.getInstance();
        Date date = format.parse("2022-12-31");

        assertEquals("Sat Dec 31 00:00:00 GMT 2022", format.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        DateFormat format = StdDateFormat.getInstance();
        format.parse("invalid date");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeZoneWithNullTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(Locale.US);
        dateFormat.setTimeZone(null);
    }

    @Test
    public void testFormatDate() {
        DateFormat format = StdDateFormat.getInstance();

        Date date = new Date();
        String formattedDate = format.format(date);
        Date parsedDate = null;

        try {
            parsedDate = format.parse(formattedDate);
        } catch (ParseException e) {
            fail("Failed to parse the date back to the original format.");
        }

        assertEquals(date, parsedDate);
    }
}