package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String expected = date.toString();

        String result = ISO8601Utils.format(date);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String expected = date.toString();

        String result = ISO8601Utils.format(date, true);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        String expected = date.toString();

        String result = ISO8601Utils.format(date, false, TimeZone.getDefault());

        assertEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2022-01-01T12:00:00Z";

        Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0).getTime();

        ParsePosition pos = new ParsePosition(0);
        Date result = ISO8601Utils.parse(dateStr, pos);

        assertEquals(expected, result);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateStr = "2022-111-01T12:00:00Z";

        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String dateStr = "2022-01-01T12:00:00PST";

        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }
}