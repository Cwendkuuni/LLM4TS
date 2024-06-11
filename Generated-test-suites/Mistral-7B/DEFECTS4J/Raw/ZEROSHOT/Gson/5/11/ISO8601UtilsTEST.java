package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Test
    public void testFormatValidDate() {
        Date date = new Date(1592036900000L);
        String formatted = ISO8601Utils.format(date);
        assertEquals("2020-06-15T10:31:40Z", formatted);
    }

    @Test
    public void testFormatValidDateWithMillis() {
        Date date = new Date(1592036901234L);
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2020-06-15T10:31:40.234Z", formatted);
    }

    @Test
    public void testFormatValidDateWithCustomTimeZone() {
        Date date = new Date(1592036900000L);
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Etc/GMT-5"));
        assertEquals("2020-06-15T15:31:40-05:00", formatted);
    }

    @Test
    public void testParseValidDate() throws ParseException {
        String dateString = "2020-06-15T10:31:40Z";
        ParsePosition position = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, position);
        assertEquals(1592036900000L, parsedDate.getTime());
        assertEquals(0, position.getIndex());
    }

    @Test
    public void testParseValidDateWithMillis() throws ParseException {
        String dateString = "2020-06-15T10:31:40.123Z";
        ParsePosition position = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, position);
        assertEquals(1592036900123L, parsedDate.getTime());
        assertEquals(dateString.length(), position.getIndex());
    }

    @Test
    public void testParseValidDateWithCustomTimeZone() throws ParseException {
        String dateString = "2020-06-15T15:31:40-05:00";
        ParsePosition position = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, position);
        ZonedDateTime expected = ZonedDateTime.of(2020, 6, 15, 15, 31, 0, 0, ZoneId.of("America/New_York"));
        assertEquals(expected.toInstant().toEpochMilli(), parsedDate.getTime());
        assertEquals(dateString.length(), position.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithoutFormat() throws ParseException {
        String dateString = "2020-06-15";
        ParsePosition position = new ParsePosition(0);
        ISO8601Utils.parse(dateString, position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithMissingTimeComponent() throws ParseException {
        String dateString = "2020-06-15";
        ParsePosition position = new ParsePosition(0);
        ISO8601Utils.parse(dateString, position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithInvalidTimeComponent() throws ParseException {
        String dateString = "2020-06-15T25:60:00Z";
        ParsePosition position = new ParsePosition(0);
        ISO8601Utils.parse(dateString, position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithInvalidTimeZoneIndicator() throws ParseException {
        String dateString = "2020-06-15T10:31:40#Z";
        ParsePosition position = new ParsePosition(0);
        ISO8601Utils.parse(dateString, position);
    }

    @Test
    public void testFormatNullDate() {
        assertNull(ISO8601Utils.format(null));
    }

    @Test
    public void testParseNullString() {
        assertNull(ISO8601Utils.parse(null, new ParsePosition(0)));
    }

    @Test
    public void testParseEmptyString() throws ParseException {
        ParsePosition position = new ParsePosition(0);
        assertNull(ISO8601Utils.parse("", position));
        assertEquals(0, position.getIndex());
    }

    @Test
    public void testParseStringWithInvalidFormat() throws ParseException {
        String dateString = "2020-06-15T10:31:40:Z";
        ParsePosition position = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, position));
    }
}