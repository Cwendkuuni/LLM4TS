package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormatWithoutMillis() {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        String expected = String.format("%04d-%02d-%02dT%02d:%02d:%02dZ",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        Assert.assertEquals(expected, formatted);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        String expected = String.format("%04d-%02d-%02dT%02d:%02d:%02d.%03dZ",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND));
        Assert.assertEquals(expected, formatted);
    }

    @Test
    public void testParse() throws ParseException {
        String isoDate = "2021-01-01T12:34:56.789Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(parsedDate);
        Assert.assertEquals(2021, calendar.get(Calendar.YEAR));
        Assert.assertEquals(0, calendar.get(Calendar.MONTH));
        Assert.assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(34, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(56, calendar.get(Calendar.SECOND));
        Assert.assertEquals(789, calendar.get(Calendar.MILLISECOND));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String isoDate = "2021-01-0112:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String isoDate = "2021-01-01T12:34:56+01:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(isoDate, pos);
    }

}