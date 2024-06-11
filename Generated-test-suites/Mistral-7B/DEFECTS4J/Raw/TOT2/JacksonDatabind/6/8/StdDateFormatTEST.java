package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test for {@link StdDateFormat} class. This class tests all public methods
 * including their signatures, generates basic JUnit 4 test cases for each
 * method, identifies potential edge cases and exception handling scenarios,
 * and generates JUnit 4 test cases for the identified edge cases and exceptions.
 */
public class StdDateFormatTest {

    // Basic test cases for public methods

    /**
     * Test for getInstance() method.
     */
    @Test
    public void testGetInstance() {
        StdDateFormat formatter = StdDateFormat.getInstance();
        assertEquals(StdDateFormat.class, formatter.getClass());
    }

    /**
     * Test for getInstance(TimeZone tz) method.
     */
    @Test
    public void testGetInstanceTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");
        StdDateFormat formatter = StdDateFormat.getInstance(tz);
        assertEquals(StdDateFormat.class, formatter.getClass());
        assertEquals(tz, formatter.getTimeZone());
    }

    /**
     * Test for getInstance(TimeZone tz, Locale locale) method.
     */
    @Test
    public void testGetInstanceTimeZoneLocale() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.JAPAN;
        StdDateFormat formatter = StdDateFormat.getInstance(tz, locale);
        assertEquals(StdDateFormat.class, formatter.getClass());
        assertEquals(tz, formatter.getTimeZone());
        assertEquals(locale, formatter.getLocale());
    }

    /**
     * Test for setTimeZone(TimeZone tz) method.
     */
    @Test
    public void testSetTimeZone() {
        StdDateFormat formatter = new StdDateFormat();
        TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");
        formatter.setTimeZone(tz);
        assertEquals(tz, formatter.getTimeZone());
    }

    /**
     * Test for setLocale(Locale locale) method.
     */
    @Test
    public void testSetLocale() {
        StdDateFormat formatter = new StdDateFormat();
        Locale locale = Locale.JAPAN;
        formatter.setLocale(locale);
        assertEquals(locale, formatter.getLocale());
    }

    /**
     * Test for clone() method.
     */
    @Test
    public void testClone() {
        StdDateFormat formatter = new StdDateFormat();
        StdDateFormat clonedFormatter = formatter.clone();
        assertEquals(formatter.getClass(), clonedFormatter.getClass());
        assertNotSame(formatter, clonedFormatter);
        assertEquals(formatter.getTimeZone(), clonedFormatter.getTimeZone());
        assertEquals(formatter.getLocale(), clonedFormatter.getLocale());
    }

    /**
     * Test for parse(String dateString) method.
     */
    @Test
    public void testParse() {
        StdDateFormat formatter = StdDateFormat.getInstance();
        Date date;

        // Basic test cases
        date = formatter.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672499191230")));

        date = formatter.parse("2022-12-31T23:59:59.123-06:00");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672435591230") - 3600000 * 6));

        // Test for edge cases
        date = formatter.parse("2022-12-31T23:59:59.123+02:30");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672532791230") + 2 * 3600000));

        date = formatter.parse("2022-12-31T23:59:59.123+14:30");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672675991230") + 14 * 3600000));

        date = formatter.parse("2022-12-31T23:59:59.123+24:00");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672719191230") + 24 * 3600000));

        // Test for error cases
        assertThrows(ParseException.class, () -> formatter.parse("2022-12-31T23:59:59.123"));
        assertThrows(ParseException.class, () -> formatter.parse("2022-12-31T23:59:59.123abc"));
        assertThrows(NullPointerException.class, () -> formatter.parse(null));
    }

    /**
     * Test for format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) method.
     */
    @Test
    public void testFormat() {
        StdDateFormat formatter = StdDateFormat.getInstance();
        String expectedFormat;

        // Basic test cases
        Date date = new Date(Long.parseLong("1672499191230"));
        StringBuffer buffer = new StringBuffer(32);

        formatter.format(date, buffer, new FieldPosition(0, 0));
        assertEquals("Invalid date format", expectedFormat = "Sat Dec 31 22:59:59 GMT 2022", buffer.toString());

        // Test for edge cases
        date.setTime(date.getTime() - 3600000 * 6);
        formatter = StdDateFormat.getInstance(TimeZone.getTimeZone("Asia/Kolkata"), Locale.SIMPLIFIED_CHINESE);
        buffer.setLength(0);
        formatter.format(date, buffer, new FieldPosition(0, 0));
        expectedFormat = "星期六 12月31日 21:59:59 中国标准时 2022";
        assertEquals("Invalid date format", expectedFormat, buffer.toString());

        date.setTime(date.getTime() + 3600000 * 6);
        formatter = StdDateFormat.getInstance(TimeZone.getTimeZone("Asia/Kolkata"), Locale.ITALIAN);
        buffer.setLength(0);
        formatter.format(date, buffer, new FieldPosition(0, 0));
        expectedFormat = "dom. giu. 31 2022 23:59:59 ora legale italiana";
        assertEquals("Invalid date format", expectedFormat, buffer.toString());

        // Test for error cases
        date = new Date(Long.parseLong("1672499191230"));
        buffer.setLength(0);
        formatter.format(null, buffer, new FieldPosition(0, 0));
        assertEquals("", buffer.toString());
    }

    // Edge cases and exception handling scenarios

    /**
     * Test for parse method with empty string.
     */
    @Test
    public void testParseEmptyString() {
        StdDateFormat formatter = StdDateFormat.getInstance();
        assertNull("Expected null", formatter.parse(""));
    }

    /**
     * Test for parse method with various invalid formats.
     */
    @Test
    public void testParseInvalidFormats() {
        StdDateFormat formatter = StdDateFormat.getInstance();
        assertThrows(ParseException.class, () -> formatter.parse("InvalidFormat"));
        assertThrows(ParseException.class, () -> formatter.parse("12345"));
        assertThrows(ParseException.class, () -> formatter.parse("12345T"));
        assertThrows(ParseException.class, () -> formatter.parse("T12345"));
        assertThrows(ParseException.class, () -> formatter.parse("T12345Z"));
        assertThrows(ParseException.class, () -> formatter.parse("T12345+12:00"));
        assertThrows(ParseException.class, () -> formatter.parse("T12345-12:00"));
    }

    /**
     * Test for parse method with various timezones.
     */
    @Test
    public void testParseDifferentTimezones() {
        StdDateFormat formatter = StdDateFormat.getInstance(TimeZone.getTimeZone("America/Los_Angeles"), Locale.GERMAN);
        Date date;

        date = formatter.parse("2022-12-31T23:59:59.123-08:00");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672427391230") - 3600000 * 8));

        date = formatter.parse("2022-12-31T23:59:59.123+08:00");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672543591230") + 3600000 * 8));

        date = formatter.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672499191230")));
    }

    /**
     * Test for parse method with various locales.
     */
    @Test
    public void testParseDifferentLocales() {
        StdDateFormat formatter = StdDateFormat.getInstance(TimeZone.getTimeZone("Europe/London"), Locale.SIMPLIFIED_CHINESE);
        Date date;

        date = formatter.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672499191230")));

        date = formatter.parse("星期六, 12月31日 23:59:59 英国标准时");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672499191230")));

        date = formatter.parse("Sat Dec 31 23:59:59 GMT 2022");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672499191230")));

        date = formatter.parse("星期六 12月31日 23:59:59 中国标准时 2022");
        assertEquals("Invalid date", date, new Date(Long.parseLong("1672499191230")));
    }
}