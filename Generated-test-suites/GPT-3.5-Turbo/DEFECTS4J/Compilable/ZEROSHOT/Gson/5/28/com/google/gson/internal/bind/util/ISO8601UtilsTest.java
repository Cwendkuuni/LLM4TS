package com.google.gson.internal.bind.util;

import org.junit.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        Assert.assertNotNull(formattedDate);
        // Add additional assertions for the format
    }

    @Test
    public void testFormat_withMillis() {
        Date date = new Date();
        boolean millis = true;
        String formattedDate = ISO8601Utils.format(date, millis);
        Assert.assertNotNull(formattedDate);
        // Add additional assertions for the format
    }

    @Test
    public void testFormat_withTimeZone() {
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("PST");
        String formattedDate = ISO8601Utils.format(date, false, tz);
        Assert.assertNotNull(formattedDate);
        // Add additional assertions for the format
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2021-01-20T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateStr, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals(dateStr.length(), pos.getIndex());
        // Add additional assertions for the parsed date
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidDate() throws ParseException {
        String dateStr = "2021-13-20T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
        // Expecting exception, so no assertions needed
    }

    // Add test cases for other methods

}