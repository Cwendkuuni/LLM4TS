package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        Date date = new Date();
        String expected = "2023-03-21T12:34:56.789Z";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);

        date.setTime(date.getTime() + 3600000);
        expected = "2023-03-21T14:34:56.789Z";
        result = ISO8601Utils.format(date);
        assertEquals(expected, result);

        date.setTime(date.getTime() - 3600000);
        expected = "2023-03-21T13:34:56.789Z";
        result = ISO8601Utils.format(date);
        assertEquals(expected, result);

        expected = "2023-03-21T12:34:56Z";
        result = ISO8601Utils.format(date, false, null);
        assertEquals(expected, result);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        date = calendar.getTime();
        expected = "2023-03-21T13:34:56.789+01:00";
        result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(expected, result);
    }

    @Test
    public void testParse() throws Exception {
        String dateString1 = "2023-03-21T12:34:56.789Z";
        Date date1 = ISO8601Utils.parse(dateString1, new ParsePosition(0));
        Date expected1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString1);
        assertEquals(expected1.getTime(), date1.getTime());

        String dateString2 = "2023-03-21T12:34:56+01:00";
        ParsePosition pos2 = new ParsePosition(0);
        Date date2 = ISO8601Utils.parse(dateString2, pos2);
        Date expected2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString2.substring(0, dateString2.length() - 1));
        expected2.setTime(expected2.getTime() + 3600000);
        assertEquals(expected2.getTime(), date2.getTime());

        String dateString3 = "2023-03-21T12:34:56.789-01:00";
        ParsePosition pos3 = new ParsePosition(0);
        Date date3 = ISO8601Utils.parse(dateString3, pos3);
        Date expected3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString3.substring(0, dateString3.length() - 1));
        expected3.setTime(expected3.getTime() - 3600000);
        assertEquals(expected3.getTime(), date3.getTime());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws Exception {
        String dateString = "2023-03-21T12:34:56.789A";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but no exception was thrown.");
    }
}