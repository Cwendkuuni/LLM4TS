package com.example; // replace with your test package name

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class ISO8601UtilsTest {

    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Test
    public void testFormatDate() throws Exception {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("Expected ISO format for current date: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formattedDate, ISO8601Utils.format(date));

        Date dateWithTime = new Date(System.currentTimeMillis() - 3600000);
        String formattedDateWithTime = ISO8601Utils.format(dateWithTime, true, ISO8601Utils.TIMEZONE_UTC);
        assertEquals("Expected ISO format for date with time and UTC timezone: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formattedDateWithTime, ISO8601Utils.format(dateWithTime, true, ISO8601Utils.TIMEZONE_UTC));
    }

    @Test
    public void testParseDateWithTime() throws Exception {
        String isoDateWithTime = "2022-01-01T12:34:56.789Z";
        Date parsedDate = ISO8601Utils.parse(isoDateWithTime, new ParsePosition(0));
        assertEquals("Expected parsed date with time and UTC timezone: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", parsedDate, dateFormat.parse(isoDateWithTime));
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingYear() throws Exception {
        String isoDateMissingYear = "MM-dd'T'HH:mm:ss.SSS'Z'";
        ISO8601Utils.parse(isoDateMissingYear, new ParsePosition(0));
        fail("Expected ParseException for missing year in ISO date: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingMonth() throws Exception {
        String isoDateMissingMonth = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        ISO8601Utils.parse(isoDateMissingMonth, new ParsePosition(0));
        fail("Expected ParseException for missing month in ISO date: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingDay() throws Exception {
        String isoDateMissingDay = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        ISO8601Utils.parse(isoDateMissingDay, new ParsePosition(0));
        fail("Expected ParseException for missing day in ISO date: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingTime() throws Exception {
        String isoDateMissingTime = "yyyy-MM-dd";
        ISO8601Utils.parse(isoDateMissingTime, new ParsePosition(0));
        fail("Expected ParseException for missing time in ISO date: yyyy-MM-dd");
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingTimeZone() throws Exception {
        String isoDateMissingTimeZone = "yyyy-MM-dd'T'HH:mm:ss";
        ISO8601Utils.parse(isoDateMissingTimeZone, new ParsePosition(0));
        fail("Expected ParseException for missing time zone in ISO date: yyyy-MM-dd'T'HH:mm:ss");
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidTimeZone() throws Exception {
        String isoDateInvalidTimeZone = "2022-01-01T12:34:56.789V";
        ISO8601Utils.parse(isoDateInvalidTimeZone, new ParsePosition(0));
        fail("Expected ParseException for invalid time zone in ISO date: yyyy-MM-dd'T'HH:mm:ss.SSS[V|Z|[+-]hh:mm]");
    }

    @Test(expected = NumberFormatException.class)
    public void testPadIntNegativeNumber() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, -1, 3);
        fail("Expected NumberFormatException for negative number: zero pad should not accept negative numbers");
    }
}