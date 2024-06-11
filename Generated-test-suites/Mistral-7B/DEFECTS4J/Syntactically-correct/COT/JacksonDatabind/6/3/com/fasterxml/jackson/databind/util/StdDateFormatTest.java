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

    @Test
    public void testStandardFormatInstance() {
        StdDateFormat sf = StdDateFormat.instance;
        assertEquals(StdDateFormat.class, sf.getClass());
    }

    @Test
    public void testWithTimeZoneAndLocale() {
        StdDateFormat sf = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.CHINESE);
        assertNotSame(StdDateFormat.instance, sf);
        assertEquals(Locale.CHINESE, sf._locale);
        assertEquals(TimeZone.getTimeZone("UTC"), sf._timezone);
    }

    @Test
    public void testParseISO8601FormatWithValidDate() {
        StdDateFormat sf = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123Z";
        Date date = sf.parse(dateString);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601FormatWithInvalidDate() {
        StdDateFormat sf = StdDateFormat.instance;
        String dateString = "2022-12-32T23:59:59.123Z";
        sf.parse(dateString);
    }

    @Test
    public void testParseRFC1123FormatWithValidDate() {
        StdDateFormat sfRFC1123 = StdDateFormat.getRFC1123Format(TimeZone.getDefault(), Locale.getDefault());
        String dateString = "Fri, 31 Dec 2022 23:59:59 GMT";
        Date date = sfRFC1123.parse(dateString);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123FormatWithInvalidDate() {
        StdDateFormat sfRFC1123 = StdDateFormat.getRFC1123Format(TimeZone.getDefault(), Locale.getDefault());
        String dateString = "Fri, 31 Xmas 2022 23:59:59 GMT";
        sfRFC1123.parse(dateString);
    }

    @Test
    public void testParsePlainFormatWithValidDate() {
        StdDateFormat sfPlain = StdDateFormat.getISO8601Format(null, Locale.getDefault());
        String dateString = "2022-12-31";
        Date date = sfPlain.parse(dateString);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParsePlainFormatWithInvalidDate() {
        StdDateFormat sfPlain = StdDateFormat.getISO8601Format(null, Locale.getDefault());
        String dateString = "31-12-2022";
        sfPlain.parse(dateString);
    }

    @Test
    public void testFormatDateWithValidDate() {
        StdDateFormat sf = StdDateFormat.instance;
        Date date = new Date();
        String formattedDate = sf.format(date, new StringBuffer(), new FieldPosition(0, 0));
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String expectedDate = expectedFormat.format(date);
        assertEquals(expectedDate, formattedDate);
    }

    @Test
    public void testToString() {
        StdDateFormat sf = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.CHINESE);
        String expectedToString = "DateFormat com.fasterxml.jackson.databind.util.StdDateFormat@5428a4d2";
        String actualToString = sf.toString();
        assertEquals(expectedToString, actualToString);
    }
}