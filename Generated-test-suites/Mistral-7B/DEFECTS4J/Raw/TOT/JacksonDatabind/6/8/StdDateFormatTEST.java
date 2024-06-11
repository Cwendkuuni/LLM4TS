package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals(new Date(1671562399123L), date);
    }

    @Test
    public void testParseISO8601WithoutTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals(new Date(1671562399123L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601WithoutMilliseconds() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-12-31T23:59:59Z");
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601WithInvalidTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-12-31T23:59:59.123XYZ");
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testFormatWithLocale() {
        StdDateFormat dateFormat = new StdDateFormat(Locale.FRENCH);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testFormatWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testFormatWithInvalidFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        String formattedDate = dateFormat.format(new Date());
        dateFormat.parse(formattedDate + "X");
    }

    @Test
    public void testClone() {
        StdDateFormat original = new StdDateFormat();
        StdDateFormat cloned = original.clone();
        assertEquals(original, cloned);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(timeZone);
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSetLocale() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        Locale locale = Locale.FRENCH;
        dateFormat.setLocale(locale);
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSingletonInstance() {
        StdDateFormat instance1 = StdDateFormat.instance;
        StdDateFormat instance2 = StdDateFormat.instance;
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetISO8601Format() throws ParseException {
        Date date = new Date();
        DateFormat dateFormat = StdDateFormat.getISO8601Format();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetISO8601FormatWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.getISO8601Format(null, null);
    }

    @Test
    public void testGetISO8601FormatWithLocale() throws ParseException {
        Date date = new Date();
        Locale locale = Locale.FRENCH;
        DateFormat dateFormat = StdDateFormat.getISO8601Format(locale);
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetRFC1123Format() throws ParseException {
        Date date = new Date();
        DateFormat dateFormat = StdDateFormat.getRFC1123Format();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetRFC1123FormatWithTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        DateFormat rfc1123Format = dateFormat.getRFC1123Format(null, null);
        assertEquals(null, rfc1123Format);
    }

    @Test
    public void testGetRFC1123FormatWithLocaleAndTimeZone() throws ParseException {
        Date date = new Date();
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(locale, timeZone);
        String formattedDate = rfc1123Format.format(date);
        Date parsedDate = rfc1123Format.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetPlainDateFormat() throws ParseException {
        Date date = new Date();
        DateFormat dateFormat = StdDateFormat.getPlainDateFormat();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSetTimeZoneWithNull() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(null);
        assertEquals(StdDateFormat.DEFAULT_TIMEZONE, dateFormat.getTimeZone());
    }

    @Test
    public void testSetLocaleWithNull() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setLocale(null);
        assertEquals(StdDateFormat.DEFAULT_LOCALE, dateFormat.getLocale());
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Fri, 03 Mar 2023 14:59:59 GMT");
        assertEquals(new Date(1673348399000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123FormatWithInvalidTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("Fri, 03 Mar 2023 14:59:59 XYZ");
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123FormatWithInvalidDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("Fri, 03 Mar 2023 14:59:59 GMTX");
    }

    @Test
    public void testFormatRFC1123Format() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = dateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("Fri, 03 Mar 2023 14:59:59 GMT", formattedDate.toString());
    }

    @Test
    public void testFormatISO8601Format() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = dateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("2023-03-03T14:59:59.000Z", formattedDate.toString());
    }

    @Test
    public void testFormatPlainDateFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = dateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("2023-03-03", formattedDate.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeZoneWithInvalidTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(new GregorianCalendar(2023, Calendar.MARCH, 3, 14, 59, 59).getTimeZone());
    }

    @Test
    public void testSetLocaleWithInvalidLocale() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setLocale(new Locale("XYZ"));
    }
}