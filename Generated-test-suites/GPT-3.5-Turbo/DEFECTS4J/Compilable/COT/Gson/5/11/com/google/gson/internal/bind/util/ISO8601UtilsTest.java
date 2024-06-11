package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals(formattedDate.length(), 20);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals(formattedDate.length(), 24);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        assertEquals(formattedDate.contains("GMT"), true);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2020-01-01T12:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(pos.getErrorIndex(), -1);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateString = "2020-01-01T12:00:00.000Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String dateString = "2020-01-01T12:00:00+0500";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}
