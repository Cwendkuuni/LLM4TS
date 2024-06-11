package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601Date() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-03-01T14:30:15.000Z";
        Date date = dateFormat.parse(dateString);
        assertEquals("2022-03-01 14:30:15.000", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601DateWithInvalidFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-03-01T14:30:15.000"; // missing Z at the end
        dateFormat.parse(dateString); // should throw ParseException
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601DateWithInvalidTimeZone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-03-01T14:30:15.000+01:00"; // invalid timezone
        dateFormat.parse(dateString); // should throw ParseException
    }

    @Test
    public void testParseRFC1123Date() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "Wed, 01 Mar 2022 14:30:15 GMT";
        Date date = dateFormat.parse(dateString);
        assertEquals("2022-03-01 14:30:15.000", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123DateWithInvalidFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "Wed, 01 Mar 2022 14:30:15 GMT"; // missing GMT at the end
        dateFormat.parse(dateString); // should throw ParseException
    }

    @Test
    public void testParsePlainDate() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2022-03-01";
        Date date = dateFormat.parse(dateString);
        assertEquals("2022-03-01.000", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParsePlainDateWithInvalidFormat() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "01-03-2022"; // invalid order of month, day and year
        dateFormat.parse(dateString); // should throw ParseException
    }
}