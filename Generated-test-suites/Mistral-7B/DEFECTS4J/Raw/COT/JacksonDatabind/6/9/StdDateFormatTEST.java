package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test cases for the StdDateFormat class.
 */
public class StdDateFormatTest {

    // -----------------------------------------------------------------------
    // Test methods for public methods

    /**
     * Test method for {@link StdDateFormat#getInstance()}.
     */
    @Test
    public void testGetInstance() {
        StdDateFormat instance = StdDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals(StdDateFormat.class, instance.getClass());
    }

    /**
     * Test method for {@link StdDateFormat#getBlueprintISO8601Format()}.
     */
    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat blueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(blueprintISO8601Format);
        assertEquals(SimpleDateFormat.class, blueprintISO8601Format.getClass());
    }

    /**
     * Test method for {@link StdDateFormat#getISO8601Format(TimeZone)}.
     */
    @Test
    public void testGetISO8601Format() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(timeZone);
        assertNotNull(iso8601Format);
        assertEquals(SimpleDateFormat.class, iso8601Format.getClass());
        assertEquals(timeZone, iso8601Format.getTimeZone());
        assertEquals(Locale.US, iso8601Format.getLocale());
    }

    /**
     * Test method for {@link StdDateFormat#getISO8601Format(TimeZone, Locale)}.
     */
    @Test
    public void testGetISO8601FormatWithLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Locale locale = Locale.FRENCH;
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(timeZone, locale);
        assertNotNull(iso8601Format);
        assertEquals(SimpleDateFormat.class, iso8601Format.getClass());
        assertEquals(timeZone, iso8601Format.getTimeZone());
        assertEquals(locale, iso8601Format.getLocale());
    }

    /**
     * Test method for {@link StdDateFormat#getRFC1123Format()}.
     */
    @Test
    public void testGetRFC1123Format() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format();
        assertNotNull(rfc1123Format);
        assertEquals(SimpleDateFormat.class, rfc1123Format.getClass());
    }

    /**
     * Test method for {@link StdDateFormat#getRFC1123Format(TimeZone)}.
     */
    @Test
    public void testGetRFC1123FormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(timeZone);
        assertNotNull(rfc1123Format);
        assertEquals(SimpleDateFormat.class, rfc1123Format.getClass());
        assertEquals(timeZone, rfc1123Format.getTimeZone());
        assertEquals(Locale.US, rfc1123Format.getLocale());
    }

    /**
     * Test method for {@link StdDateFormat#getRFC1123Format(TimeZone, Locale)}.
     */
    @Test
    public void testGetRFC1123FormatWithLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        Locale locale = Locale.FRENCH;
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(timeZone, locale);
        assertNotNull(rfc1123Format);
        assertEquals(SimpleDateFormat.class, rfc1123Format.getClass());
        assertEquals(timeZone, rfc1123Format.getTimeZone());
        assertEquals(locale, rfc1123Format.getLocale());
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)}.
     */
    @Test
    public void testParse() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        Date date = instance.parse("2022-12-31T23:59:59.999Z");
        assertNotNull(date);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)}.
     */
    @Test
    public void testParseWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = instance.parse("2022-12-31T23:59:59.999Z", parsePosition);
        assertNotNull(date);
        assertEquals(0, parsePosition.getIndex());
        assertEquals(parsePosition.getErrorIndex(), -1);
    }

    /**
     * Test method for {@link StdDateFormat#format(Date)}.
     */
    @Test
    public void testFormat() {
        DateFormat instance = StdDateFormat.getInstance();
        Date date = new Date();
        String formattedDate = instance.format(date);
        assertNotNull(formattedDate);
    }

    /**
     * Test method for {@link StdDateFormat#format(Date, StringBuffer)}.
     */
    @Test
    public void testFormatWithStringBuffer() {
        DateFormat instance = StdDateFormat.getInstance();
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        instance.format(date, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
    }

    // -----------------------------------------------------------------------
    // Test methods for edge cases and exception handling

    /**
     * Test method for {@link StdDateFormat#parse(String)} with empty string.
     */
    @Test(expected = ParseException.class)
    public void testParseEmptyString() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.parse("");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with null string.
     */
    @Test(expected = NullPointerException.class)
    public void testParseNullString() {
        DateFormat instance = StdDateFormat.getInstance();
        instance.parse(null);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with an invalid date string.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDateString() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.parse("InvalidDate");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with a date string that exceeds the maximum date value.
     */
    @Test(expected = ParseException.class)
    public void testParseMaxDate() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.setLenient(false);
        instance.parse("2999-12-31T23:59:59.999Z");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with a date string that is too short.
     */
    @Test(expected = ParseException.class)
    public void testParseTooShortDateString() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.parse("2022-12-");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with a date string with an invalid timezone.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidTimezone() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.parse("2022-12-31T23:59:59.999XYZ");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with a date string with an invalid date format.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.setLenient(false);
        instance.parse("InvalidFormat");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String)} with a date string that contains an invalid character.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidCharacter() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        instance.parse("2022-12-31T23:59:59.999%");
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with an empty string.
     */
    @Test(expected = ParseException.class)
    public void testParseEmptyStringWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.parse("", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with a null string.
     */
    @Test(expected = NullPointerException.class)
    public void testParseNullStringWithParsePosition() {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = null;
        instance.parse("", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with an invalid date string.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDateStringWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.parse("InvalidDate", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with a date string that exceeds the maximum date value.
     */
    @Test(expected = ParseException.class)
    public void testParseMaxDateWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.setLenient(false);
        instance.parse("2999-12-31T23:59:59.999Z", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with a date string that is too short.
     */
    @Test(expected = ParseException.class)
    public void testParseTooShortDateStringWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.parse("2022-12-", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with a date string with an invalid timezone.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidTimezoneWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.parse("2022-12-31T23:59:59.999XYZ", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with a date string with an invalid date format.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidFormatWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.setLenient(false);
        instance.parse("InvalidFormat", parsePosition);
    }

    /**
     * Test method for {@link StdDateFormat#parse(String, ParsePosition)} with a date string that contains an invalid character.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidCharacterWithParsePosition() throws ParseException {
        DateFormat instance = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        instance.parse("2022-12-31T23:59:59.999%", parsePosition);
    }
}