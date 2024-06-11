package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        Date date = new Date(0L);
        String formatDate = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", formatDate);

        date.setTime(1234567890123L);
        formatDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals("2009-11-19T15:26:30.123Z", formatDate);

        date.setTime(1234567890123L);
        formatDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2009-11-19T15:26:30.123Z", formatDate);
    }

    @Test
    public void testParse() throws ParseException {
        ParsePosition pos = new ParsePosition(0);

        String dateString1 = "1970-01-01T00:00:00Z";
        Date date1 = ISO8601Utils.parse(dateString1, pos);
        assertEquals("Thu Jan 01 00:00:00 UTC 1970", date1.toString());
        assertEquals(0, pos.getIndex());

        String dateString2 = "2009-11-19T15:26:30.123Z";
        Date date2 = ISO8601Utils.parse(dateString2, pos);
        assertEquals("Thu Nov 19 15:26:30.123 UTC 2009", date2.toString());
        assertEquals(27, pos.getIndex());

        String dateString3 = "2009-11-19T15:26:30+01:00";
        Date date3 = ISO8601Utils.parse(dateString3, pos);
        assertEquals("Fri Nov 20 16:26:30 CET 2009", date3.toString());
        assertEquals(26, pos.getIndex());

        String dateString4 = "2009-11-19T15:26:30-05:00";
        Date date4 = ISO8601Utils.parse(dateString4, pos);
        assertEquals("Wed Nov 18 10:26:30 EST 2009", date4.toString());
        assertEquals(27, pos.getIndex());

        String dateString5 = "2009-11-19T15:26:30.123-05:00";
        Date date5 = ISO8601Utils.parse(dateString5, pos);
        assertEquals("Wed Nov 18 10:26:30.123 EST 2009", date5.toString());
        assertEquals(32, pos.getIndex());

        String dateString6 = "2009-11-19T15:26:30.123+01:00";
        Date date6 = ISO8601Utils.parse(dateString6, pos);
        assertEquals("Fri Nov 20 16:26:30.123 CET 2009", date6.toString());
        assertEquals(27, pos.getIndex());

        String dateString7 = "2009-11-19T15:26:30.123+01:30";
        Date date7 = ISO8601Utils.parse(dateString7, pos);
        assertEquals("Fri Nov 20 16:26:30.123 CET 2009", date7.toString());
        assertEquals(27, pos.getIndex());

        String dateString8 = "2009-11-19T15:26:30.123-01:30";
        Date date8 = ISO8601Utils.parse(dateString8, pos);
        assertEquals("Fri Nov 20 15:55:30.123 CET 2009", date8.toString());
        assertEquals(27, pos.getIndex());
    }

    @Test
    public void testParseInvalid() throws ParseException {
        ParsePosition pos = new ParsePosition(0);

        String dateString1 = "2009-11-19T15:26:30Zabc";
        try {
            ISO8601Utils.parse(dateString1, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Text 'abc' could not be parsed at index 13", e.getMessage());
            assertEquals(20, pos.getIndex());
        }

        String dateString2 = "2009-11-19T15:26:30Z--";
        try {
            ISO8601Utils.parse(dateString2, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected character ('-') found at index 19", e.getMessage());
            assertEquals(19, pos.getIndex());
        }

        String dateString3 = "2009-11-19T15:26:30.123Z--";
        try {
            ISO8601Utils.parse(dateString3, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected character ('-') found at index 23", e.getMessage());
            assertEquals(23, pos.getIndex());
        }

        String dateString4 = "2009-11-19T15:26:30.123+";
        try {
            ISO8601Utils.parse(dateString4, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator '+' at index 18", e.getMessage());
            assertEquals(18, pos.getIndex());
        }

        String dateString5 = "2009-11-19T15:26:30.123+1:";
        try {
            ISO8601Utils.parse(dateString5, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 19", e.getMessage());
            assertEquals(19, pos.getIndex());
        }

        String dateString6 = "2009-11-19T15:26:30.123+1:30";
        try {
            ISO8601Utils.parse(dateString6, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 20", e.getMessage());
            assertEquals(20, pos.getIndex());
        }

        String dateString7 = "2009-11-19T15:26:30.123+13:";
        try {
            ISO8601Utils.parse(dateString7, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 21", e.getMessage());
            assertEquals(21, pos.getIndex());
        }

        String dateString8 = "2009-11-19T15:26:30.123+13:60";
        try {
            ISO8601Utils.parse(dateString8, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 23", e.getMessage());
            assertEquals(23, pos.getIndex());
        }

        String dateString9 = "2009-11-19T15:26:30.123+13:60:";
        try {
            ISO8601Utils.parse(dateString9, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 25", e.getMessage());
            assertEquals(25, pos.getIndex());
        }

        String dateString10 = "2009-11-19T15:26:30.123+13:60:60";
        try {
            ISO8601Utils.parse(dateString10, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 27", e.getMessage());
            assertEquals(27, pos.getIndex());
        }

        String dateString11 = "2009-11-19T15:26:30.123+13:60:60:";
        try {
            ISO8601Utils.parse(dateString11, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 30", e.getMessage());
            assertEquals(30, pos.getIndex());
        }

        String dateString12 = "2009-11-19T15:26:30.123+13:60:60:60";
        try {
            ISO8601Utils.parse(dateString12, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid time zone indicator ':' at index 33", e.getMessage());
            assertEquals(33, pos.getIndex());
        }
    }

    @Test
    public void testCheckOffset() {
        String dateString1 = "2009-11-19T15:26:30+01:30";
        assertTrue(ISO8601Utils.checkOffset(dateString1, 0, '+'));
        assertTrue(ISO8601Utils.checkOffset(dateString1, 1, ':'));
        assertFalse(ISO8601Utils.checkOffset(dateString1, 2, ':'));
        assertFalse(ISO8601Utils.checkOffset(dateString1, 3, ':'));
        assertFalse(ISO8601Utils.checkOffset(dateString1, 4, ':'));
        assertFalse(ISO8601Utils.checkOffset(dateString1, 5, ':'));
        assertFalse(ISO8601Utils.checkOffset(dateString1, 6, 'Z'));
        assertFalse(ISO8601Utils.checkOffset(dateString1, 7, 'Z'));

        String dateString2 = "2009-11-19T15:26:30Z";
        assertFalse(ISO8601Utils.checkOffset(dateString2, 0, 'T'));
        assertFalse(ISO8601Utils.checkOffset(dateString2, 1, 'T'));
        assertFalse(ISO8601Utils.checkOffset(dateString2, 2, '-'));
        assertFalse(ISO8601Utils.checkOffset(dateString2, 3, '-'));
        assertTrue(ISO8601Utils.checkOffset(dateString2, 4, 'Z'));
        assertTrue(ISO8601Utils.checkOffset(dateString2, 5, 'Z'));
    }

    @Test
    public void testParseInt() throws Exception {
        int value = ISO8601Utils.parseInt("123", 0, 3);
        assertEquals(123, value);

        value = ISO8601Utils.parseInt("12345", 0, 5);
        assertEquals(12345, value);

        value = ISO8601Utils.parseInt("12345", 1, 5);
        assertEquals(12345, value);

        try {
            ISO8601Utils.parseInt("123", 2, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertEquals("123", e.getValue().toString());
            assertEquals(2, e.getErrorOffset());
        }

        try {
            ISO8601Utils.parseInt("12345", 5, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertEquals("12345", e.getValue().toString());
            assertEquals(5, e.getErrorOffset());
        }

        try {
            ISO8601Utils.parseInt("12345", 0, 2);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertEquals("12345", e.getValue().toString());
            assertEquals(0, e.getErrorOffset());
        }
    }

    @Test
    public void testPadInt() throws Exception {
        StringBuilder buffer = new StringBuilder(5);
        ISO8601Utils.padInt(buffer, 123, 5);
        assertEquals("00123", buffer.toString());

        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, 12345, 5);
        assertEquals("012345", buffer.toString());

        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, 12345, 3);
        assertEquals("123", buffer.toString());
    }

    @Test
    public void testIndexOfNonDigit() throws Exception {
        assertEquals(5, ISO8601Utils.indexOfNonDigit("12345".toCharArray(), 0));
        assertEquals(6, ISO8601Utils.indexOfNonDigit("12345".toCharArray(), 1));
        assertEquals(6, ISO8601Utils.indexOfNonDigit("12345".toCharArray(), 2));
        assertEquals(6, ISO8601Utils.indexOfNonDigit("12345".toCharArray(), 3));
        assertEquals(6, ISO8601Utils.indexOfNonDigit("12345".toCharArray(), 4));
        assertEquals(-1, ISO8601Utils.indexOfNonDigit("12345".toCharArray(), 5));
    }
}