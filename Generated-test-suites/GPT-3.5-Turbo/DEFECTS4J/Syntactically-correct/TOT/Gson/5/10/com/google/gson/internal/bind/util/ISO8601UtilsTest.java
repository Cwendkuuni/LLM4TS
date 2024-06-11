
package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDefaultTimeZone() {
        Date date = new GregorianCalendar(2020, Calendar.MARCH, 8, 10, 30, 0).getTime();
        String expected = "2020-03-08T10:30:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCustomTimeZone() {
        Date date = new GregorianCalendar(2020, Calendar.MARCH, 8, 12, 0, 0).getTime();
        TimeZone timeZone = TimeZone.getTimeZone("GMT-05:00");
        String expected = "2020-03-08T12:00:00-05:00";
        String actual = ISO8601Utils.format(date, true, timeZone);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseDateOnly() throws ParseException {
        String dateString = "2020-03-08";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2020, Calendar.MARCH, 8).getTime();
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseDateAndTime() throws ParseException {
        String dateString = "2020-03-08T12:30:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new GregorianCalendar(2020, Calendar.MARCH, 8, 12, 30, 0).getTime();
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateString = "2020-02-30";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateWithTime() throws ParseException {
        String dateString = "2020-03-08T25:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateString() throws ParseException {
        String dateString = "ABCD-EF-GH";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}

