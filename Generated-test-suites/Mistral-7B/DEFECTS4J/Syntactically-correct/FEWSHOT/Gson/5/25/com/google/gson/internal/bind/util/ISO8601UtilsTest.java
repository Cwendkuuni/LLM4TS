package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_NullDate() {
        assertNull(ISO8601Utils.format(null));
    }

    @Test
    public void testFormat_UTC_NoMillis() {
        Date date = new GregorianCalendar(2023, Calendar.FEBRUARY, 14).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2023-02-14T00:00:00Z", formatted);
    }

    @Test
    public void testFormat_UTC_Millis() {
        Date date = new GregorianCalendar(2023, Calendar.FEBRUARY, 14, 15, 30, 12).getTime();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2023-02-14T15:30:12.012Z", formatted);
    }

    @Test
    public void testFormat_Offset_NoMillis() {
        Date date = new GregorianCalendar(2023, Calendar.FEBRUARY, 14).getTime();
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2023-02-14T07:00:00-08:00", formatted);
    }

    @Test
    public void testFormat_Offset_Millis() {
        Date date = new GregorianCalendar(2023, Calendar.FEBRUARY, 14, 15, 30, 12).getTime();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2023-02-14T20:30:12.012-05:00", formatted);
    }

    @Test
    public void testParse_ValidDate() throws ParseException {
        Date date = ISO8601Utils.parse("2023-02-14T15:30:12.012Z", new ParsePosition(0));
        GregorianCalendar expected = new GregorianCalendar(2023, Calendar.FEBRUARY, 14, 15, 30, 12);
        assertEquals(expected.getTime(), date);
    }

    @Test
    public void testParse_InvalidDate_MissingYears() throws ParseException {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("--02-14T15:30:12.012Z", new ParsePosition(0)));
    }

    @Test
    public void testParse_InvalidDate_MissingMonths() throws ParseException {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-0--14T15:30:12.012Z", new ParsePosition(0)));
    }

    @Test
    public void testParse_InvalidDate_MissingDays() throws ParseException {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-02--14T15:30:12.012Z", new ParsePosition(0)));
    }

    @Test
    public void testParse_InvalidDate_MissingTime() throws ParseException {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-02-14Z", new ParsePosition(0)));
    }

    @Test
    public void testParse_InvalidDate_MissingTimeZone() throws ParseException {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-02-14T15:30:12.012", new ParsePosition(0)));
    }

    @Test
    public void testParse_InvalidDate_MalformedTimeZone() throws ParseException {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-02-14T15:30:12.012X", new ParsePosition(0)));
    }

    @Test
    public void testPadInt() {
        StringBuilder buffer = new StringBuilder();
        ISO8601Utils.padInt(buffer, -1, 3);
        assertSame(buffer.toString(), "---");

        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, 0, 3);
        assertSame(buffer.toString(), "000");

        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, 1, 3);
        assertSame(buffer.toString(), "001");

        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, 123, 3);
        assertSame(buffer.toString(), "123");

        buffer.setLength(0);
        ISO8601Utils.padInt(buffer, 12345, 6);
        assertSame(buffer.toString(), "123450");
    }

    @Test
    public void testIndexOfNonDigit() {
        String string = "123abc456";
        int index = ISO8601Utils.indexOfNonDigit(string, 0);
        assertEquals(3, index);

        index = ISO8601Utils.indexOfNonDigit(string, 3);
        assertEquals(3, index);

        index = ISO8601Utils.indexOfNonDigit(string, 5);
        assertEquals(5, index);

        string = "123";
        index = ISO8601Utils.indexOfNonDigit(string, 2);
        assertFalse(index > 0);

        string = "";
        index = ISO8601Utils.indexOfNonDigit(string, 0);
        assertFalse(index > 0);
    }

}