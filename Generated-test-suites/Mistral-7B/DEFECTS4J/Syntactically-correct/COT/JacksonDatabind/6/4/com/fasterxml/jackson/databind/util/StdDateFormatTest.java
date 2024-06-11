package com.example.stdDateFormat;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for StdDateFormat.
 */
public class StdDateFormatTest {

    

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat blueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(blueprintISO8601Format);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", blueprintISO8601Format.toPattern());
    }

    @Test(expected = DeprecatedException.class)
    public void testGetBlueprintISO8601FormatDeprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(Locale.US, TimeZone.getTimeZone("UTC"));
        assertNotNull(iso8601Format);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", iso8601Format.toPattern());
    }

    @Test(expected = NullPointerException.class)
    public void testGetISO8601FormatNullLocale() {
        StdDateFormat.getISO8601Format(null, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetISO8601FormatNullTimeZone() {
        StdDateFormat.getISO8601Format(Locale.US, null);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/London"), Locale.US);
        stdDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat iso8601Format = stdDateFormat.getISO8601Format();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", iso8601Format.toPattern());
        assertNotSame(TimeZone.getTimeZone("Europe/London"), iso8601Format.getTimeZone());
        assertSame(TimeZone.getTimeZone("UTC"), iso8601Format.getTimeZone());
    }

    @Test
    public void testParse() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-12-31T23:59:59.999Z";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-12-31T23:59:59.999ZZ";
        stdDateFormat.parse(dateString);
        fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testParseMissingFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2022-12-31";
        stdDateFormat.parse(dateString);
        fail("Expected ParseException");
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date, new StringBuffer(), new FieldPosition(0));
        assertNotNull(formattedDate);
        assertTrue(formattedDate.length() > 0);
    }

    
}