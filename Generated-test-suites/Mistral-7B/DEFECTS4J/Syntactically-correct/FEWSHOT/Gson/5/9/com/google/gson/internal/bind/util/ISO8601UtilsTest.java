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
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class ISO8601UtilsTest {

    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

    private ISO8601Utils iso8601Utils;

    @Before
    public void setup() {
        iso8601Utils = new ISO8601Utils();
    }

    @Test
    public void testFormat() {
        Date date = new Date(1556374400000L); // 2019-02-01 00:00:00 in UTC

        // Default format with no milliseconds and no timezone
        String formatted = iso8601Utils.format(date);
        assertEquals("2019-02-01", formatted);

        // Format with milliseconds and no timezone
        String formattedMillis = iso8601Utils.format(date, true);
        assertEquals("2019-02-01T00:00:00.000Z", formattedMillis);

        // Format with timezone UTC
        String formattedUTC = iso8601Utils.format(date, true, TIMEZONE_UTC);
        assertEquals("2019-02-01T00:00:00.000Z", formattedUTC);

        // Format with timezone other than UTC
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/London").toString());
        String formattedLondon = iso8601Utils.format(date, true, timeZone);
        assertEquals("2019-02-01T00:00:00.000+00:00", formattedLondon);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 00:00:00 GMT", parsedDate.toString());
        assertEquals(0, pos.getIndex());

        // Test invalid date string
        dateString = "2019-02-01T00:00:00.000X";
        pos = new ParsePosition(0);
        try {
            iso8601Utils.parse(dateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Text 'X' cannot be parsed at index 10", e.getMessage());
            assertEquals(10, pos.getIndex());
        }

        // Test parse with timezone UTC
        dateString = "2019-02-01T00:00:00.000Z";
        pos = new ParsePosition(0);
        Date parsedDateUTC = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 00:00:00 GMT", parsedDateUTC.toString());
        assertEquals(14, pos.getIndex());

        // Test parse with timezone Europe/London
        dateString = "2019-02-01T00:00:00.000+00:00";
        pos = new ParsePosition(0);
        Date parsedDateLondon = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 00:00:00 GMT+00:00", parsedDateLondon.toString());
        assertEquals(14, pos.getIndex());
    }

    @Test
    public void testParse_withInvalidTimeZone() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+00:00";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"));
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Text 'SimpleDateFormat' is not capable of parsing '+00:00' with class java.text.ParseException", e.getMessage());
            assertEquals(14, pos.getIndex());
        }
    }

    @Test
    public void testParse_withInvalidTimeZoneId() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+00:00";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"));
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone 'zzz' with class java.text.ParseException", e.getMessage());
            assertEquals(14, pos.getIndex());
        }
    }

    @Test
    public void testParse_withInvalidFormat() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000XXX";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Text 'X' cannot be parsed at index 12", e.getMessage());
            assertEquals(12, pos.getIndex());
        }
    }

    @Test
    public void testParse_withMissingTimeZone() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("No time zone indicator", e.getMessage());
            assertEquals(14, pos.getIndex());
        }
    }

    @Test
    public void testParse_withMalformedTimeZone() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+00:60";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone offset '60'", e.getMessage());
            assertEquals(14, pos.getIndex());
        }
    }

    @Test
    public void testParse_withNegativeOffset() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000-01:30";
        ParsePosition pos = new ParsePosition(0);

        Date parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 00:00:00 CET2019", parsedDate.toString());
        assertEquals(14, pos.getIndex());

        // Test with negative offset smaller than -1 hours
        dateString = "2019-02-01T00:00:00.000-23:60";
        pos = new ParsePosition(0);
        parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 19:36:00 CST2019", parsedDate.toString());
        assertEquals(14, pos.getIndex());
    }

    @Test
    public void testParse_withPositiveOffset() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+01:30";
        ParsePosition pos = new ParsePosition(0);

        Date parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 01:30:00 CET2019", parsedDate.toString());
        assertEquals(14, pos.getIndex());

        // Test with positive offset greater than 24 hours
        dateString = "2019-02-01T00:00:00.000+25:00";
        pos = new ParsePosition(0);
        parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Sat Feb 02 01:00:00 CET2019", parsedDate.toString());
        assertEquals(14, pos.getIndex());
    }

    @Test
    public void testParse_withOffsetWithoutColon() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+0130";
        ParsePosition pos = new ParsePosition(0);

        Date parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 02:30:00 CET2019", parsedDate.toString());
        assertEquals(14, pos.getIndex());
    }

    @Test
    public void testParse_withOffsetWithLeadingZero() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+01:00";
        ParsePosition pos = new ParsePosition(0);

        Date parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 01:00:00 CET2019", parsedDate.toString());
        assertEquals(14, pos.getIndex());
    }

    @Test
    public void testParse_withOffsetWithLeadingZeroAndSpace() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+01:00 ";
        ParsePosition pos = new ParsePosition(0);

        Date parsedDate = iso8601Utils.parse(dateString, pos);
        assertEquals("Fri Feb 01 01:00:00 CET2019", parsedDate.toString());
        assertEquals(15, pos.getIndex());
    }

    @Test
    public void testParse_withOffsetWithTrailingZero() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+01:00:00";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone offset '00:00' with class java.text.ParseException", e.getMessage());
            assertEquals(14, pos.getIndex());
        }
    }

    @Test
    public void testParse_withOffsetWithTrailingZeroAndSpace() throws ParseException {
        String dateString = "2019-02-01T00:00:00.000+01:00:00 ";
        ParsePosition pos = new ParsePosition(0);

        try {
            iso8601Utils.parse(dateString, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone offset '00:00' with class java.text.ParseException", e.getMessage());
            assertEquals(15, pos.getIndex());
        }
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();

        // Test zero padding
        iso8601Utils.padInt(buffer, 12, 3);
        assertEquals("12", buffer.toString());

        iso8601Utils.padInt(buffer, 1, 3);
        assertEquals("01", buffer.toString());

        iso8601Utils.padInt(buffer, 10, 5);
        assertEquals("00100", buffer.toString());

        // Test negative value
        iso8601Utils.padInt(buffer, -5, 3);
        assertEquals("--005", buffer.toString());

        // Test value larger than expected length
        iso8601Utils.padInt(buffer, 123456, 3);
        assertEquals("123456", buffer.toString());
    }
}