package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date(1611185200000L); // Wed Sep 22 01:00:00 UTC 2021
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-09-22T01:00:00Z", formatted);

        date.setTime(1611185200000L + 2 * 60 * 60 * 1000); // Wed Sep 22 03:00:00 UTC 2021
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-09-22T03:00:00Z", formatted);

        date.setTime(1611185200000L + 1 * 60 * 60 * 1000); // Wed Sep 22 02:00:00 UTC 2021
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/London"));
        assertEquals("2021-09-22T01:00:00+01:00", formatted);
    }

    @Test
    public void testFormatDateWithoutTimeZone() {
        Date date = new Date(1611185200000L); // Wed Sep 22 01:00:00 UTC 2021
        String formatted = ISO8601Utils.format(date);
        assertEquals("2021-09-22T01:00:00", formatted);

        date.setTime(1611185200000L + 2 * 60 * 60 * 1000); // Wed Sep 22 03:00:00 UTC 2021
        formatted = ISO8601Utils.format(date);
        assertEquals("2021-09-22T03:00:00", formatted);

        date.setTime(1611185200000L + 1 * 60 * 60 * 1000); // Wed Sep 22 02:00:00 UTC 2021
        formatted = ISO8601Utils.format(date);
        assertEquals("2021-09-22T02:00:00", formatted);
    }

    @Test
    public void testFormatDateWithMillis() {
        Date date = new Date(1611185200001L); // Wed Sep 22 01:00:01 UTC 2021
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-09-22T01:00:01.001Z", formatted);

        date.setTime(1611185200012L); // Wed Sep 22 01:00:01.002 UTC 2021
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-09-22T01:00:01.002Z", formatted);
    }

    @Test
    public void testFormatDateWithMillisAndTimeZone() {
        Date date = new Date(1611185200001L); // Wed Sep 22 01:00:01 UTC 2021
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/London"));
        assertEquals("2021-09-22T01:00:01.001+01:00", formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedToFormatDate() {
        Date date = new Date(Long.MAX_VALUE); // Wed Jan 19 03:14:07 GMT 2202-01-19 15:16:31.035
        String formatted = ISO8601Utils.format(date);
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2021-09-22T01:00:00Z";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(9, calendar.get(Calendar.MONTH));
        assertEquals(22, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testParseDateWithOffset() throws ParseException {
        String dateString = "2021-09-22T01:00:00+01:00";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/London"));
        calendar.setTime(date);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(9, calendar.get(Calendar.MONTH));
        assertEquals(22, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(2, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testParseDateWithTimeZone() throws ParseException {
        String dateString = "2021-09-22T01:00:00Z";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0), TimeZone.getTimeZone("Europe/London"));
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/London"));
        calendar.setTime(date);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(9, calendar.get(Calendar.MONTH));
        assertEquals(22, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test(expected = ParseException.class)
    public void testFailedToParseDate() throws ParseException {
        String dateString = "2021-09-22T01:00:00X";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testFailedToParseDateWithOffset() throws ParseException {
        String dateString = "2021-09-22T01:00:00+00X";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testFailedToParseDateWithTimeZone() throws ParseException {
        String dateString = "2021-09-22T01:00:00ZX";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0), TimeZone.getTimeZone("Europe/London"));
    }

    @Test(expected = ParseException.class)
    public void testFailedToParseDateWithOffsetAndTimeZone() throws ParseException {
        String dateString = "2021-09-22T01:00:00+01:00X";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0), TimeZone.getTimeZone("Europe/London"));
    }
}