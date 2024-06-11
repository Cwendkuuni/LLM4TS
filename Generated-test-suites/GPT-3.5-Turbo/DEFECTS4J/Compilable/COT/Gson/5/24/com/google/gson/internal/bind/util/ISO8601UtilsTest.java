package com.google.gson.internal.bind.util;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1635864951000L); // October 3, 2021 13:35:51 GMT
        String expected = "2021-10-03T13:35:51Z";
        String actual = ISO8601Utils.format(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date(1635864951234L); // October 3, 2021 13:35:51.234 GMT
        String expected = "2021-10-03T13:35:51.234Z";
        String actual = ISO8601Utils.format(date, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date(1635864951000L); // October 3, 2021 13:35:51 GMT
        String expected = "2021-10-03T13:35:51+00:00";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        String actual = ISO8601Utils.format(date, false, timeZone);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2021-10-03T13:35:51Z";
        Date expected = new Date(1635864951000L);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(dateString.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateString = "2021-10-03T13:35:51";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String dateString = "2021-13-35T13:79:51Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidTimeZone() throws ParseException {
        String dateString = "2021-10-03T13:35:51+02:AA";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

}