package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws ParseException {
        Date date = new GregorianCalendar(2022, 11, 24, 15, 30, 45, 123).getTime();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals("2022-12-24T15:30:45.123Z", formattedDate);

        Date dateUTC = new GregorianCalendar(2022, 11, 24, 15, 30, 45, 123).getTime();
        String formattedDateUTC = ISO8601Utils.format(dateUTC, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2022-12-24T15:30:45.123Z", formattedDateUTC);

        Date dateWithOffset = new GregorianCalendar(2022, 11, 24, 15, 30, 45, 123, -180, 0).getTime(); // -5 hours offset
        String formattedDateWithOffset = ISO8601Utils.format(dateWithOffset, true, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2022-12-24T20:30:45.123-05:00", formattedDateWithOffset);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2022-12-24T15:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(2022, 11, 24, 15, 30, 45, 123).getTime(), parsedDate);
        assertEquals(pos.getIndex(), dateString.length());

        String dateStringWithOffset = "2022-12-24T15:30:45.123-05:00";
        pos = new ParsePosition(0);
        Date parsedDateWithOffset = ISO8601Utils.parse(dateStringWithOffset, pos);
        assertEquals(new GregorianCalendar(2022, 11, 24, 15, 30, 45, 123, -5 * 60 * 60 * 1000, 0).getTime(), parsedDateWithOffset);
        assertEquals(pos.getIndex(), dateStringWithOffset.length());

        String invalidDateString = "2022-12-24T15:30:45.123fooZ";
        pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFormatAndParseRoundTrip() throws ParseException {
        for (int year = 1000; year <= 9999; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= 31; day++) {
                    for (int hour = 0; hour <= 23; hour++) {
                        for (int minute = 0; minute <= 59; minute++) {
                            for (int second = 0; second <= 59; second++) {
                                for (int millisecond = 0; millisecond <= 999; millisecond++) {
                                    Date date = new GregorianCalendar(year, month - 1, day, hour, minute, second, millisecond).getTime();
                                    String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
                                    Date parsedDate = ISO8601Utils.parse(formattedDate, new ParsePosition(0));
                                    assertEquals(date, parsedDate);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testParseWithInvalidYear() throws ParseException {
        String invalidDateString = "0-1-31T15:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 1;
        assertEquals("Invalid year: 0", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidMonth() throws ParseException {
        String invalidDateString = "200-1-31T15:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 5;
        assertEquals("Invalid value for month: 11", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidDay() throws ParseException {
        String invalidDateString = "2022-13-31T15:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 8;
        assertEquals("Invalid value for day of month: 13", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidHour() throws ParseException {
        String invalidDateString = "2022-12-24T24:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 8;
        assertEquals("Invalid value for hour of day (24): 24", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidMinute() throws ParseException {
        String invalidDateString = "2022-12-24T15:60:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 10;
        assertEquals("Invalid value for minute: 60", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithEmptyString() throws ParseException {
        String invalidDateString = "";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 0;
        assertEquals("Text '': failed to parse at index 0", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithNoTimePart() throws ParseException {
        String invalidDateString = "2022-12-24";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 10;
        assertEquals("Text '2022-12-24': failed to parse at index 10", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithNoTimeZonePart() throws ParseException {
        String invalidDateString = "2022-12-24T15:30:45.123";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 23;
        assertEquals("No time zone offset: \"\"", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidTimeZoneIdentifier() throws ParseException {
        String invalidDateString = "2022-12-24T15:30:45.123InvalidTimeZone";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        int expectedIndex = 25;
        assertEquals("Unknown timezone identifier: 'InvalidTimeZone'", e.getMessage());
        assertEquals(expectedIndex, pos.getIndex());
    }
}