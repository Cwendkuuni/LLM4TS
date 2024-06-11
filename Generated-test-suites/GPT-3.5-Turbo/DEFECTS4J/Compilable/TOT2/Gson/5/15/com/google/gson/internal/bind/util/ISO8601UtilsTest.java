package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date(1583592918000L); // 2020-03-07 12:41:58 GMT
        String formattedDate = ISO8601Utils.format(date);

        assertEquals("2020-03-07T12:41:58Z", formattedDate);
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateStr = "2020-03-07T12:41:58Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateStr, pos);

        assertEquals(1583592918000L, parsedDate.getTime());
    }

    @Test(expected = ParseException.class)
    public void testInvalidDateParsing() throws ParseException {
        String invalidDateStr = "2020-20-50T24:70:80Z"; // Invalid date format
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidDateStr, pos);
    }

    @Test
    public void testFormatDateWithMillis() {
        Date date = new Date(1583592918123L); // 2020-03-07 12:41:58.123 GMT
        String formattedDate = ISO8601Utils.format(date, true);

        assertEquals("2020-03-07T12:41:58.123Z", formattedDate);
    }

    @Test
    public void testParseDateWithOffset() throws ParseException {
        String dateStr = "2020-03-07T12:41:58+02:30";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateStr, pos);

        assertEquals("2020-03-07T12:41:58+02:30", ISO8601Utils.format(parsedDate, false));
    }

    @Test(expected = ParseException.class)
    public void testInvalidDateParsingWithNoTimezone() throws ParseException {
        String invalidDateStr = "2020-03-07T12:41:58"; // Missing timezone
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidDateStr, pos);
    }
}