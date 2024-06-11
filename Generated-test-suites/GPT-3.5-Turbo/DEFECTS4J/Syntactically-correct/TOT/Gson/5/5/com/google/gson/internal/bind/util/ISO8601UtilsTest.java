
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
    public void testFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JULY, 15, 12, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        String expected = "2021-07-15T12:30:00Z";
        String actual = ISO8601Utils.format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JULY, 15, 12, 30, 0);
        calendar.set(Calendar.MILLISECOND, 123);
        Date date = calendar.getTime();

        String expected = "2021-07-15T12:30:00.123Z";
        String actual = ISO8601Utils.format(date, true);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JULY, 15, 12, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("PST"));
        Date date = calendar.getTime();

        String expected = "2021-07-15T12:30:00-08:00";
        String actual = ISO8601Utils.format(date, false, calendar.getTimeZone());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2021-07-15T12:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(dateStr, pos);

        Assert.assertNotNull(expected);
        Assert.assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateStr = "202107-15T12:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test
    public void testParseWithMinutesOffset() throws ParseException {
        String dateStr = "2021-07-15T12:30:00+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(dateStr, pos);

        Assert.assertNotNull(expected);
        Assert.assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    public void testParseWithSecondsOffset() throws ParseException {
        String dateStr = "2021-07-15T12:30:00+02:30";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(dateStr, pos);

        Assert.assertNotNull(expected);
        Assert.assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    public void testParseWithMilliseconds() throws ParseException {
        String dateStr = "2021-07-15T12:30:00.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = ISO8601Utils.parse(dateStr, pos);

        Assert.assertNotNull(expected);
        Assert.assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidTimeZone() throws ParseException {
        String dateStr = "2021-07-15T12:30:00-18:00";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }
}

