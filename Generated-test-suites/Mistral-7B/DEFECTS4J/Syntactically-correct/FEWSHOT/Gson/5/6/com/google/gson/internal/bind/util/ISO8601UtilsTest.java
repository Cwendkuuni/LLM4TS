package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test default date with no time component and no timezone
        Date date = new Date(0L);
        String formatted = ISO8601Utils.format(date);
        assertEquals("1970-01-01", formatted);

        // Test date with time component but no timezone
        Calendar calendar = new GregorianCalendar(2015, Calendar.JANUARY, 15, 12, 30, 45);
        Date dateWithTime = calendar.getTime();
        formatted = ISO8601Utils.format(dateWithTime);
        assertEquals("2015-01-15T12:30:45", formatted);

        // Test date with time component and timezone
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        calendar = new GregorianCalendar(2015, Calendar.JANUARY, 15, 12, 30, 45, 0);
        calendar.setTimeZone(timeZone);
        dateWithTime = calendar.getTime();
        formatted = ISO8601Utils.format(dateWithTime, true, timeZone);
        assertEquals("2015-01-15T12:30:45.000-05:00", formatted);

        // Test invalid date
        try {
            ISO8601Utils.format(null);
            fail("expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseInvalidDate() {
        ParsePosition pos = new ParsePosition(0);
        String dateString = "InvalidDate";
        try {
            ISO8601Utils.parse(dateString, pos);
            fail("expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals("Failed to parse date ['InvalidDate']: (class java.text.ParseException)", e.getMessage());
            assertEquals(dateString.length(), pos.getIndex());
        }
    }

    @Test
    public void testParseValidDateWithNoTimeComponent() {
        String dateString = "2015-01-15";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070400000L), date);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponent() {
        String dateString = "2015-01-15T12:30:45.000-05:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070405045L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponentAndDifferentTimeZone() {
        String dateString = "2015-01-15T12:30:45.000+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070545045L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponentAndSameTimeZone() {
        String dateString = "2015-01-15T12:30:45.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070405045L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponentAndSameTimeZoneNoTimeMilliseconds() {
        String dateString = "2015-01-15T12:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070405000L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponentAndSameTimeZoneNoTimeSeconds() {
        String dateString = "2015-01-15T12:30Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070405000L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponentAndSameTimeZoneNoTimeMinutes() {
        String dateString = "2015-01-15T12Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070400000L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testParseValidDateWithTimeComponentAndSameTimeZoneNoTime() {
        String dateString = "2015-01-15Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        assertEquals(new Date(1420070400000L), date);
        assertEquals(dateString.length(), pos.getIndex());
    }

    @Test
    public void testFormatAndParseUTCDate() {
        String dateString = "2015-01-15T12:30:45.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals(dateString, formattedDate);
    }

    @Test
    public void testFormatAndParseDateWithTimeZoneOffset() {
        String dateString = "2015-01-15T12:30:45.000+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(dateString, formattedDate);
    }

    @Test
    public void testFormatAndParseDateWithEmptyTimeZone() {
        String dateString = "2015-01-15T12:30:45.000";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, pos);
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals(dateString, formattedDate);
    }

    @Test
    public void testParseValidDateWithYearOutOfRange() {
        String dateString = "0000-12-31";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateString, pos);
            fail("expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals("Failed to parse date ['0000-12-31']: (class java.text.ParseException)", e.getMessage());
        }
    }

    @Test
    public void testParseValidDateWithMonthOutOfRange() {
        String dateString = "1970-00-31";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateString, pos);
            fail("expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals("Failed to parse date ['1970-00-31']: (class java.text.ParseException)", e.getMessage());
        }
    }

    @Test
    public void testParseValidDateWithDayOutOfRange() {
        String dateString = "1970-12-32";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateString, pos);
            fail("expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals("Failed to parse date ['1970-12-32']: (class java.text.ParseException)", e.getMessage());
        }
    }

    @Test
    public void testParseValidDateWithInvalidFormat() {
        String dateString = "1970-12-31T12:30:45:001";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateString, pos);
            fail("expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals("Failed to parse date ['1970-12-31T12:30:45:001']: (class java.text.ParseException)", e.getMessage());
        }
    }

    @Test
    public void testParseValidDateWithInvalidTimeZoneIndicator() {
        String dateString = "2015-01-15T12:30:45.000Invalid";
        ParsePosition pos = new ParsePosition(0);
        try {
            ISO8601Utils.parse(dateString, pos);
            fail("expected ParseException");
        } catch (ParseException e) {
            // expected
            assertEquals("Failed to parse date ['2015-01-15T12:30:45.000Invalid']: (class java.text.ParseException)", e.getMessage());
        }
    }
}