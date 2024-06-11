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

    private final ISO8601Utils iso8601Utils = new ISO8601Utils();

    @Test
    public void testFormat1() {
        // Typical Use Case: Format date with default timezone and no milliseconds precision
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1).getTime();
        String expected = "2022-01-01T00:00:00Z";

        String actual = iso8601Utils.format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat2() {
        // Typical Use Case: Format date with GMT timezone and milliseconds precision
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2022-01-01T12:30:45.000Z";

        String actual = iso8601Utils.format(date, true);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat3() {
        // Typical Use Case: Format date with custom timezone and seconds precision
        TimeZone customTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();
        String expected = "2022-01-01T12:30:45-08:00";

        String actual = iso8601Utils.format(date, false, customTimeZone);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse1() throws ParseException {
        // Typical Use Case: Parse valid ISO-8601 formatted string with default timezone
        String dateStr = "2022-01-01T12:30:45.000Z";
        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 45).getTime();

        Date actual = iso8601Utils.parse(dateStr, new ParsePosition(0));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse2() throws ParseException {
        // Edge Case: Parse valid ISO-8601 formatted string with custom timezone and leap second
        String dateStr = "2022-01-01T12:30:60.123-08:00";
        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 31, 0).getTime();

        Date actual = iso8601Utils.parse(dateStr, new ParsePosition(0));

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParse3() throws ParseException {
        //Error Scenario: Parse invalid ISO-8601 formatted string
        String dateStr = "2022-01-01T12:30:00";
        Date actual = iso8601Utils.parse(dateStr, new ParsePosition(0));

        // Exception is expected to be thrown
    }

}