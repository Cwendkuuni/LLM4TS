package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithDefaultTimeZone() {
        Date date = new Date(1633059200000L); // Wed Nov 02 00:00:00 UTC 2011
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2011-11-02T00:00:00Z", formattedDate);
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        Date date = new Date(1633059200000L); // Wed Nov 02 00:00:00 UTC 2011
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        String formattedDate = ISO8601Utils.format(date, true, tz);
        assertEquals("2011-11-02T07:00:00-08:00", formattedDate);
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2011-11-02T07:00:00-08:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(1633023600000L, parsedDate.getTime()); // Wed Nov 02 00:00:00 PST 2011
        assertEquals(15, pos.getIndex());
    }
}