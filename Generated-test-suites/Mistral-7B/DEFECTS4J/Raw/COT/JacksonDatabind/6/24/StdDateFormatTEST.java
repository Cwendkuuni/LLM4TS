package com.example; // replace with your package name

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat expected = StdDateFormat.DATE_FORMAT_ISO8601;
        DateFormat actual = StdDateFormat.getBlueprintISO8601Format();
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.toLocaleString(), actual.toLocaleString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBlueprintISO8601Format_Deprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        DateFormat expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale);
        expected.setTimeZone(timeZone);

        DateFormat actual = StdDateFormat.getISO8601Format(timeZone, locale);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.toLocaleString(), actual.toLocaleString());
    }

    @Test
    public void testParse_ISO8601() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2023-02-01T13:45:56.789Z");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(2, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(13, date.getHours());
        assertEquals(45, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(789, date.getMilliseconds());
        assertEquals(0, date.getTimezoneOffset());
    }

    @Test(expected = ParseException.class)
    public void testParse_ISO8601_Invalid() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("2023-02-01T13:45:56.789A");
    }

    @Test
    public void testParse_RFC1123() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Tue, 01 Feb 2023 13:45:56 GMT");
        assertNotNull(date);
        assertEquals(2023, date.getYear());
        assertEquals(2, date.getMonth());
        assertEquals(1, date.getDate());
        assertEquals(13, date.getHours());
        assertEquals(45, date.getMinutes());
        assertEquals(56, date.getSeconds());
        assertEquals(0, date.getTimezoneOffset());
    }

    @Test(expected = ParseException.class)
    public void testParse_RFC1123_Invalid() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("Tue, 01 Feb 2023 13:45:56 GMTabc");
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        StringBuilder expected = new StringBuilder();
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        stdDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String actual = stdDateFormat.format(date);
        expected.append(simpleDateFormat.format(date));
        expected.append("Z");

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testToString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String expected = "DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,usesDaylight=false], locale: java.util.Locale[language=en,country=US])";
        String actual = stdDateFormat.toString();
        assertEquals(expected, actual);
    }
}