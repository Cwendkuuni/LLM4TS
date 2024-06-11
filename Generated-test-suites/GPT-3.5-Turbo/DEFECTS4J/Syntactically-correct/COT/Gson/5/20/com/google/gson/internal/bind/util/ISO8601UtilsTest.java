
package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormatWithDefaultTimeZoneAndNoMillis() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2021, Calendar.JULY, 15, 10, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String expected = "2021-07-15T10:30:00Z";
        String actual = ISO8601Utils.format(calendar.getTime());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDefaultTimeZoneAndMillis() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2021, Calendar.JULY, 15, 10, 30, 0);
        calendar.set(Calendar.MILLISECOND, 500);

        String expected = "2021-07-15T10:30:00.500Z";
        String actual = ISO8601Utils.format(calendar.getTime(), true);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCustomTimeZoneAndNoMillis() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        calendar.set(2021, Calendar.JULY, 15, 10, 30, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String expected = "2021-07-15T10:30:00+09:00";
        String actual = ISO8601Utils.format(calendar.getTime(), false, TimeZone.getTimeZone("Asia/Tokyo"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCustomTimeZoneAndMillis() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        calendar.set(2021, Calendar.JULY, 15, 10, 30, 0);
        calendar.set(Calendar.MILLISECOND, 500);

        String expected = "2021-07-15T10:30:00.500+09:00";
        String actual = ISO8601Utils.format(calendar.getTime(), true, TimeZone.getTimeZone("Asia/Tokyo"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithValidISOFormat() throws ParseException {
        String date = "2021-07-15T10:30:00.500Z";
        Date expected = new GregorianCalendar(2021, Calendar.JULY, 15, 10, 30, 0).getTime();
        expected.setMilliseconds(500);
        ParsePosition pos = new ParsePosition(0);

        Date actual = ISO8601Utils.parse(date, pos);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidISOFormat() throws ParseException {
        String date = "2021/07/15T10:30:00.500Z";
        ParsePosition pos = new ParsePosition(0);

        ISO8601Utils.parse(date, pos);
    }
}