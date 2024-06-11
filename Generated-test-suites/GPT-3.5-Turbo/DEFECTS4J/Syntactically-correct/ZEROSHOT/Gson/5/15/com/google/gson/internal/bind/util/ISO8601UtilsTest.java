package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date();
        String result = ISO8601Utils.format(date);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithMillis() {
        Date date = new Date();
        String result = ISO8601Utils.format(date, true);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithTimeZone() {
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String result = ISO8601Utils.format(date, false, tz);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithMillisAndTimeZone() {
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("GMT");
        String result = ISO8601Utils.format(date, true, tz);
        assertNotNull(result);
    }

    @Test
    public void testParse() throws ParseException {
        String date = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date result = ISO8601Utils.parse(date, pos);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        String date = "2021-01-0123";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormat() throws ParseException {
        String date = "2021/01/01";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(date, pos);
    }

    @Test
    public void testCheckOffset() {
        String value = "2021-01-01";
        int offset = 4;
        boolean result = ISO8601Utils.checkOffset(value, offset, '-');
        assertTrue(result);
    }

    @Test
    public void testCheckOffsetInvalid() {
        String value = "2021-01-01";
        int offset = 5;
        boolean result = ISO8601Utils.checkOffset(value, offset, '-');
        assertFalse(result);
    }

    @Test
    public void testParseInt() {
        String value = "2021";
        int beginIndex = 0;
        int endIndex = value.length();
        int result = ISO8601Utils.parseInt(value, beginIndex, endIndex);
        assertEquals(2021, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntInvalidNumber() {
        String value = "abcd";
        int beginIndex = 0;
        int endIndex = value.length();
        ISO8601Utils.parseInt(value, beginIndex, endIndex);
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        int value = 1;
        int length = 4;
        ISO8601Utils.padInt(buffer, value, length);
        assertEquals("0001", buffer.toString());
    }

    @Test
    public void testIndexOfNonDigit() {
        String string = "abcd1234";
        int offset = 0;
        int result = ISO8601Utils.indexOfNonDigit(string, offset);
        assertEquals(4, result);
    }

    @Test
    public void testIndexOfNonDigitWithDigitsOnly() {
        String string = "1234";
        int offset = 0;
        int result = ISO8601Utils.indexOfNonDigit(string, offset);
        assertEquals(4, result);
    }

}