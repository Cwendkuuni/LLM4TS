package com.fasterxml.jackson.databind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    private static final StdDateFormat DATE_FORMAT = StdDateFormat.instance;

    @Test
    public void testParseISO8601() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31T23:59:59.123Z");
        Assert.assertNotNull(date);
        Assert.assertEquals(2022, date.getYear());
        Assert.assertEquals(12, date.getMonth());
        Assert.assertEquals(31, date.getDate());
        Assert.assertEquals(23, date.getHours());
        Assert.assertEquals(59, date.getMinutes());
        Assert.assertEquals(59, date.getSeconds());
        Assert.assertEquals(123, (int) date.getTime());
    }

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31T23:59:59.123+01:00");
        Assert.assertNotNull(date);
        Assert.assertEquals(2022, date.getYear());
        Assert.assertEquals(12, date.getMonth());
        Assert.assertEquals(31, date.getDate());
        Assert.assertEquals(23, date.getHours());
        Assert.assertEquals(59, date.getMinutes());
        Assert.assertEquals(59, date.getSeconds());
        Assert.assertEquals(123 * 1000L + 60 * 60 * 1000L, date.getTime()); // 1 hour ahead
    }

    @Test
    public void testParsePlainDate() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31");
        Assert.assertNotNull(date);
        Assert.assertEquals(2022, date.getYear());
        Assert.assertEquals(12, date.getMonth());
        Assert.assertEquals(31, date.getDate());
        Assert.assertEquals(0, date.getHours());
        Assert.assertEquals(0, date.getMinutes());
        Assert.assertEquals(0, date.getSeconds());
        Assert.assertEquals(0, (int) date.getTime());
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        Date date = DATE_FORMAT.parse("Tue, 31 Dec 2022 23:59:59 GMT");
        Assert.assertNotNull(date);
        Assert.assertEquals(2022, date.getYear());
        Assert.assertEquals(12, date.getMonth());
        Assert.assertEquals(31, date.getDate());
        Assert.assertEquals(23, date.getHours());
        Assert.assertEquals(59, date.getMinutes());
        Assert.assertEquals(59, date.getSeconds());
        Assert.assertEquals(0, (int) date.getTime());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        DATE_FORMAT.parse("2022-13-31");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        DATE_FORMAT.parse("2022-12-31T23:59:59.123XYZ");
    }

    @Test
    public void testFormatISO8601() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31T23:59:59.123Z");
        String formatted = DATE_FORMAT.format(date, new StringBuffer(), null);
        Assert.assertEquals("2022-12-31T23:59:59.123Z", formatted.toString());
    }

    @Test
    public void testFormatISO8601WithTimeZone() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31T23:59:59.123+01:00");
        String formatted = DATE_FORMAT.format(date, new StringBuffer(), null);
        Assert.assertEquals("2022-12-31T23:59:59.123+01:00", formatted.toString());
    }

    @Test
    public void testFormatPlainDate() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31");
        String formatted = DATE_FORMAT.format(date, new StringBuffer(), null);
        Assert.assertEquals("2022-12-31", formatted.toString());
    }

    @Test
    public void testFormatRFC1123() throws ParseException {
        Date date = DATE_FORMAT.parse("2022-12-31T23:59:59.123GMT");
        String formatted = DATE_FORMAT.format(date, new StringBuffer(), null);
        Assert.assertEquals("Tue, 31 Dec 2022 23:59:59 GMT", formatted.toString());
    }
}