package com.fasterxml.jackson.databind.util;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2019-10-31T12:30:00.000Z";
        long expectedTime = stdDateFormat.parse(dateStr).getTime();
        assertEquals(expectedTime, 1572517800000L);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "Thu, 31 Oct 2019 12:30:00 GMT";
        long expectedTime = stdDateFormat.parse(dateStr).getTime();
        assertEquals(expectedTime, 1572517800000L);
    }

    // Edge cases
    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2019/10/31";
        stdDateFormat.parse(dateStr);
    }

    // Exception handling scenarios
    @Test(expected = ParseException.class)
    public void testParseNullDateStr() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = null;
        stdDateFormat.parse(dateStr);
    }
}