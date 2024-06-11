package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class ISO8601UtilsTest {

    private TimeZone timeZone;

    @Before
    public void setup() {
        timeZone = TimeZone.getTimeZone("UTC");
    }

    @Test
    public void testFormat() throws Exception {
        Date date = new GregorianCalendar(TimeZone.getTimeZone("UTC"), 2022, 10, 31, 15, 30, 0).getTime();

        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2022-10-31T15:30:00Z", formattedDate);

        String formattedDateWithMilliseconds = ISO8601Utils.format(date, true, timeZone);
        assertEquals("2022-10-31T15:30:30.000Z", formattedDateWithMilliseconds);

        String formattedDateWithCustomTimezone = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2022-10-31T21:30:30.000-07:00", formattedDateWithCustomTimezone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidDate() {
        ISO8601Utils.format(null, false, timeZone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNegativeYear() {
        Date date = new GregorianCalendar(TimeZone.getTimeZone("UTC"), -1, 12, 31, 15, 30, 0).getTime();
        ISO8601Utils.format(date, false, timeZone);
    }

    @Test
    public void testParse() throws Exception {
        String dateString1 = "2022-10-31T15:30:00Z";
        Date date1 = ISO8601Utils.parse(dateString1, new ParsePosition(0));
        Calendar calendar1 = new GregorianCalendar(timeZone);
        calendar1.setTime(date1);
        assertEquals(2022, calendar1.get(Calendar.YEAR));
        assertEquals(10, calendar1.get(Calendar.MONTH) + 1);
        assertEquals(31, calendar1.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar1.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar1.get(Calendar.MINUTE));
        assertEquals(0, calendar1.get(Calendar.SECOND));
        assertEquals(0, calendar1.get(Calendar.MILLISECOND));

        String dateString2 = "2022-10-31T15:30:30.123Z";
        Date date2 = ISO8601Utils.parse(dateString2, new ParsePosition(0));
        Calendar calendar2 = new GregorianCalendar(timeZone);
        calendar2.setTime(date2);
        assertEquals(2022, calendar2.get(Calendar.YEAR));
        assertEquals(10, calendar2.get(Calendar.MONTH) + 1);
        assertEquals(31, calendar2.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar2.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar2.get(Calendar.MINUTE));
        assertEquals(30, calendar2.get(Calendar.SECOND));
        assertEquals(123, calendar2.get(Calendar.MILLISECOND));

        String dateString3 = "2022-10-31T15:30:00-07:00";
        Date date3 = ISO8601Utils.parse(dateString3, new ParsePosition(0));
        Calendar calendar3 = new GregorianCalendar(timeZone);
        calendar3.setTime(date3);
        assertEquals(2022, calendar3.get(Calendar.YEAR));
        assertEquals(10, calendar3.get(Calendar.MONTH) + 1);
        assertEquals(31, calendar3.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar3.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar3.get(Calendar.MINUTE));
        assertEquals(0, calendar3.get(Calendar.SECOND));
        assertEquals(-7 * 60 * 60 * 1000, calendar3.get(Calendar.ZONE_OFFSET));

        String dateString4 = "2022-10-31T15:30:30.123+07:00";
        Date date4 = ISO8601Utils.parse(dateString4, new ParsePosition(0));
        Calendar calendar4 = new GregorianCalendar(timeZone);
        calendar4.setTime(date4);
        assertEquals(2022, calendar4.get(Calendar.YEAR));
        assertEquals(10, calendar4.get(Calendar.MONTH) + 1);
        assertEquals(31, calendar4.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar4.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar4.get(Calendar.MINUTE));
        assertEquals(30, calendar4.get(Calendar.SECOND));
        assertEquals(123, calendar4.get(Calendar.MILLISECOND));
        assertEquals(7 * 60 * 60 * 1000, calendar4.get(Calendar.ZONE_OFFSET));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidDateFormat() throws Exception {
        String dateString = "2022-10-31-15:30:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidYear() throws Exception {
        String dateString = "0-10-31T15:30:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidMonth() throws Exception {
        String dateString = "2022-13-31T15:30:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidDay() throws Exception {
        String dateString = "2022-10-33T15:30:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidHour() throws Exception {
        String dateString = "2022-10-31T25:30:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidMinute() throws Exception {
        String dateString = "2022-10-31T15:60:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidSecond() throws Exception {
        String dateString = "2022-10-31T15:30:60";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidMillisecond() throws Exception {
        String dateString = "2022-10-31T15:30:30.124";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidTimezoneFormat() throws Exception {
        String dateString = "2022-10-31T15:30:30.123Zzz";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidTimezoneOffset() throws Exception {
        String dateString = "2022-10-31T15:30:30.123+24:60";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidTimezoneCharacter() throws Exception {
        String dateString = "2022-10-31T15:30:30.123@ABC";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidCharacter() throws Exception {
        String dateString = "2022-10-31T15:30:30.123.";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }
}