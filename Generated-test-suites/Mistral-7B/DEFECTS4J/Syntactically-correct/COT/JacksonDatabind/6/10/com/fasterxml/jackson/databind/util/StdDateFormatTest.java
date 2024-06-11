package com.example.stddateformat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    private StdDateFormat instance;

    @Before
    public void setUp() {
        instance = new StdDateFormat();
    }

    @Test
    public void testGetInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        Assert.assertNotNull(stdDateFormat);
    }

    @Test
    public void testParseWithDefaultFormat() throws ParseException {
        Date date = instance.parse("2023-02-28");
        Assert.assertNotNull(date);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd");
        Assert.assertEquals(expectedFormat.parse("2023-02-28"), date);
    }

    @Test
    public void testParseWithISO8601Format() throws ParseException {
        Date date = instance.parse("2023-02-28T12:30:45.678-05:00");
        Assert.assertNotNull(date);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Assert.assertEquals(expectedFormat.parse("2023-02-28T12:30:45.678-05:00"), date);
    }

    @Test
    public void testParseWithRFC1123Format() throws ParseException {
        Date date = instance.parse("Tue, 28 Feb 2023 12:30:45 EST");
        Assert.assertNotNull(date);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Assert.assertEquals(expectedFormat.parse("Tue, 28 Feb 2023 12:30:45 EST"), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        instance.parse("InvalidDateFormat");
    }

    @Test
    public void testFormat() throws ParseException {
        Date date = instance.parse("2023-02-28");
        String formattedDate = instance.format(date);
        Assert.assertEquals("2023-02-28", formattedDate);
    }

    @Test
    public void testSetTimeZone() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        StdDateFormat stdDateFormatUtc = new StdDateFormat(utc, Locale.US);
        Assert.assertEquals(utc, stdDateFormatUtc.getTimeZone());

        TimeZone est = TimeZone.getTimeZone("EST");
        StdDateFormat stdDateFormatEst = stdDateFormatUtc.withTimeZone(est);
        Assert.assertEquals(est, stdDateFormatEst.getTimeZone());
    }

    @Test
    public void testSetLocale() {
        Locale en_US = new Locale("en", "US");
        StdDateFormat stdDateFormatEnUs = new StdDateFormat(null, en_US);
        Assert.assertEquals(en_US, stdDateFormatEnUs.getLocale());

        Locale fr_FR = new Locale("fr", "FR");
        StdDateFormat stdDateFormatFrFr = stdDateFormatEnUs.withLocale(fr_FR);
        Assert.assertEquals(fr_FR, stdDateFormatFrFr.getLocale());
    }

    @Test
    public void testClone() {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        StdDateFormat clonedStdDateFormat = stdDateFormat.clone();
        Assert.assertNotNull(clonedStdDateFormat);
        Assert.assertNotSame(stdDateFormat, clonedStdDateFormat);
    }
}