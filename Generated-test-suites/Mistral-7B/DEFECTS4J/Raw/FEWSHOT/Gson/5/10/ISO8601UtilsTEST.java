package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDateWithUTC() {
        Date date = new Date();
        String expected = ISO8601Utils.format(date, false, ISO8601Utils.TIMEZONE_UTC);
        String actual = ISO8601Utils.format(date, false, ISO8601Utils.TIMEZONE_UTC);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithTimeZone() {
        Date date = new Date();
        String expected = ISO8601Utils.format(date, false, ZoneId.systemDefault().toZoneId());
        String actual = ISO8601Utils.format(date, false, ZoneId.systemDefault().toZoneId());
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new Date(System.currentTimeMillis() + 100);
        String expected = "2023-03-15T10:15:23.100Z";
        String actual = ISO8601Utils.format(date, true, ISO8601Utils.TIMEZONE_UTC);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDateWithInvalidTimeZone() {
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.format(new Date(), false, null));
    }

    @Test
    public void testParseDate() {
        String dateString = "2023-03-15T10:15:23.100Z";
        Date expected = new Date(0L);
        expected.setTime(1678891523100L);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(pos.getIndex(), dateString.length());
    }

    @Test
    public void testParseDateInvalidFormat() {
        String dateString = "2023-03-15A10:15:23.100Z";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
    }

    @Test
    public void testParseDateWithNullInput() {
        ParsePosition pos = new ParsePosition(0);
        assertNull(ISO8601Utils.parse(null, pos));
    }

    @Test
    public void testParseDateWithEmptyInput() {
        ParsePosition pos = new ParsePosition(0);
        assertNull(ISO8601Utils.parse("", pos));
    }

    @Test
    public void testParseDateWithTZOffset() {
        String dateString = "2023-03-15T10:15:23+01:00";
        Date expected = new Date(0L);
        expected.setTime(1678867123000L);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(pos.getIndex(), dateString.length());
    }

    @Test
    public void testParseDateWithNegativeTZOffset() {
        String dateString = "2023-03-15T10:15:23-01:00";
        Date expected = new Date(0L);
        expected.setTime(1678870323000L);
        ParsePosition pos = new ParsePosition(0);
        Date actual = ISO8601Utils.parse(dateString, pos);
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(pos.getIndex(), dateString.length());
    }

    @Test
    public void testParseDateWithZoneID() {
        String dateString = "2023-03-15T10:15:23.100+01:00";
        ZonedDateTime expected = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1678867123100L), ZoneId.of("Europe/Paris"));
        Date actual = ISO8601Utils.parse(dateString, new ParsePosition(0)).toInstant().toDate();
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getMonthValue(), actual.getMonth() + 1);
        assertEquals(expected.getDayOfMonth(), actual.getDate());
        assertEquals(expected.getHour(), actual.getHours());
        assertEquals(expected.getMinute(), actual.getMinutes());
        assertEquals(expected.getSecond(), actual.getMinutes());
        assertEquals(expected.getNano(), actual.getTime());
    }

    @Test
    public void testParseDatesWithList() throws ParseException {
        List<String> dateStrings = Arrays.asList("2023-03-15T10:15:23Z", "2023-03-15T10:15:23+01:00", "2023-03-15T10:15:23-01:00");
        List<Date> dates = Arrays.asList(new Date(1678891523000L), new Date(1678867123000L), new Date(1678870323000L));
        List<ParsePosition> positions = new ArrayList<>();
        for (int i = 0; i < dateStrings.size(); i++) {
            ParsePosition pos = new ParsePosition(0);
            Date date = ISO8601Utils.parse(dateStrings.get(i), pos);
            assertEquals(dates.get(i).getTime(), date.getTime());
            assertEquals(pos.getIndex(), dateStrings.get(i).length());
            positions.add(pos);
        }
        assertEquals(positions.size(), dateStrings.size());
    }
}