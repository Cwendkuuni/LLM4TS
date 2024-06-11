package com.google.gson.internal.bind.util;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date);
        Assert.assertNotNull(formatted);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true);
        Assert.assertNotNull(formatted);
    }

    @Test
    public void testFormatWithTimezone() {
        Date date = new Date();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT+02:00"));
        Assert.assertNotNull(formatted);
    }

    @Test
    public void testParse() throws ParseException {
        String dateStr = "2021-05-06T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateStr, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateStr = "2021/05/06";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimezone() throws ParseException {
        String dateStr = "2021-05-06T12:34:56+10";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateStr, pos);
    }
}