package com.example; // replace with your test package name

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateUTC() throws Exception {
        Date date = new Date(0L);
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", formattedDate);
    }

    @Test
    public void testFormatDateWithTimeAndMillisecondsUTC() throws Exception {
        Date date = new Date(System.currentTimeMillis() - (10 * 60 * 1000));
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertEquals(sdf.format(date), formattedDate);
    }

    @Test
    public void testFormatDateWithTimeAndTimezone() throws Exception {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/Los_Angeles"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-08:00'");
        assertEquals(sdf.format(date), formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidDateFormat() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-02-31";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidYear() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1900-12-31T12:00:00Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid year: 1900'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMonth() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1999-03-32T12:00:00Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid month: 32'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidDay() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1999-02-31T12:00:00Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid day: 31'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTime() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1999-12-31T25:00:00Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid hour: 25'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMinute() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1999-12-31T12:60:00Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid minute: 60'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidSecond() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1999-12-31T12:00:60Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid second: 60'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMillisecond() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "1999-12-31T12:00:00.1001Z";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid millisecond: 1001'");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimezone() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-02-28T12:00:00ILM";
        ISO8601Utils.parse(dateString, pos);
        fail("ParseException expected with message: 'Invalid time zone indicator: I'");
    }

    @Test
    public void testParseDateWithValidDateTime() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-02-28T12:00:00Z";
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(System.currentTimeMillis() - (28 * 24 * 60 * 60 * 1000 + 12 * 60 * 60 * 1000)), date);
    }

    @Test
    public void testParseDateWithValidDateTimeAndTimezoneOffset() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-02-28T12:00:00-05:00";
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(System.currentTimeMillis() - (28 * 24 * 60 * 60 * 1000 + 12 * 60 * 60 * 1000 - 5 * 60 * 60 * 1000)), date);
    }

    @Test
    public void testParseDateWithValidDateTimeAndTimezoneID() throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "2022-02-28T12:00:00 America/New_York";
        Date date = ISO8601Utils.parse(dateString, pos);
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), timeZone.toZoneId());
        LocalDateTime ldt = zdt.toLocalDateTime();
        assertEquals(ldt, LocalDateTime.of(2022, 2, 28, 12, 0));
    }

}