package com.example.iso8601utils; // replace with the actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeNotNull;

import java.text.ParseException;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    // Test utility method to create a Date object for testing
    private static Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.set(year, month - 1, day, hour, minute, second, millisecond);
        return calendar.getTime();
    }

    // Test formatting methods

    @Test
    public void testFormatDate_UTC() throws Exception {
        Date date = createDate(2022, 1, 1, 12, 30, 45, 678, TimeZone.getTimeZone("UTC"));
        String formatted = ISO8601Utils.format(date);
        assertEquals("2022-01-01T12:30:45.067Z", formatted);
    }

    @Test
    public void testFormatDate_CustomTimeZone() throws Exception {
        Date date = createDate(2022, 1, 1, 12, 30, 45, 678, TimeZone.getTimeZone("America/New_York"));
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2022-01-01T17:30:45.067-05:00", formatted);
    }

    // Test parsing methods

    @Test
    public void testParseDate_ValidISO8601Format() throws Exception {
        String isoDate = "2022-01-01T12:30:45.067Z";
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
        assertEquals(createDate(2022, 1, 1, 12, 30, 45, 678, TimeZone.getTimeZone("UTC")), parsedDate);
    }

    @Test
    public void testParseDate_MissingTimeZoneIndicator() throws Exception {
        String isoDate = "2022-01-01";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidTimeZoneIndicator() throws Exception {
        String isoDate = "2022-01-01T12:30:45.067Invalid";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_MissingTimeComponent() throws Exception {
        String isoDate = "2022-01-01T12:30";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_MissingMillisecondsComponent() throws Exception {
        String isoDate = "2022-01-01T12:30:45Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_NegativeYear() throws Exception {
        String isoDate = "-0001-12-31T12:30:45.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidYear() throws Exception {
        String isoDate = "9999-12-31T12:30:45.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidMonth() throws Exception {
        String isoDate = "2022-00-31T12:30:45.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidDay() throws Exception {
        String isoDate = "2022-13-31T12:30:45.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidHour() throws Exception {
        String isoDate = "2022-01-01T24:30:45.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidMinute() throws Exception {
        String isoDate = "2022-01-01T12:60:45.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidSecond() throws Exception {
        String isoDate = "2022-01-01T12:30:60.067Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidMillisecond() throws Exception {
        String isoDate = "2022-01-01T12:30:45.10001Z";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidTimeZoneIndicator_OffsetFormat() throws Exception {
        String isoDate = "2022-01-01T12:30:45.067+01:30";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_InvalidTimeZoneIndicator_IDFormat() throws Exception {
        String isoDate = "2022-01-01T12:30:45.067America/New_York";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_EmptyString() throws Exception {
        String isoDate = "";
        ParsePosition parsePosition = new ParsePosition(isoDate.length());
        assertNull(ISO8601Utils.parse(isoDate, parsePosition));
    }

    @Test
    public void testParseDate_NullString() throws Exception {
        String isoDate = null;
        ParsePosition parsePosition = new ParsePosition(0);
        assertNull(ISO8601Utils.parse(isoDate, parsePosition));
    }
}