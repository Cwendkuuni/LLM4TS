package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class StdDateFormatTest {

    private StdDateFormat stdDateFormat;

    @Before
    public void setup() {
        stdDateFormat = new StdDateFormat();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(StdDateFormat.instance);
    }

    @Test
    public void testGetBlueprintFormatISO8601() {
        assertNotNull(StdDateFormat.DATE_FORMAT_ISO8601);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBlueprintFormatISO8601_modify() {
        StdDateFormat.DATE_FORMAT_ISO8601.setTimeZone(null);
    }

    @Test
    public void testGetBlueprintFormatRFC1123() {
        assertNotNull(StdDateFormat.DATE_FORMAT_RFC1123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBlueprintFormatRFC1123_modify() {
        StdDateFormat.DATE_FORMAT_RFC1123.setTimeZone(null);
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format(TimeZone.getDefault(), Locale.getDefault());
        assertNotNull(dateFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetISO8601Format_modify() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format(TimeZone.getDefault(), Locale.getDefault());
        dateFormat.setTimeZone(null);
    }

    @Test
    public void testParseISO8601() throws ParseException {
        String dateString = "2023-03-23T12:34:56.123Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = StdDateFormat.getInstance().parse(dateString, parsePosition);
        assertEquals(dateString, StdDateFormat.getInstance().format(date));
        assertEquals(dateString, StdDateFormat.getISO8601Format(TimeZone.getDefault(), Locale.getDefault()).format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601_invalid() throws ParseException {
        String dateString = "2023-03-23T12:34:56.123ABC";
        ParsePosition parsePosition = new ParsePosition(0);
        StdDateFormat.getInstance().parse(dateString, parsePosition);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        String dateString = "Thu, 23 Mar 2023 12:34:56 GMT";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = StdDateFormat.getInstance().parse(dateString, parsePosition);
        assertEquals(dateString, StdDateFormat.getInstance().format(date));
        assertEquals(dateString, StdDateFormat.getRFC1123Format(TimeZone.getDefault(), Locale.getDefault()).format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123_invalid() throws ParseException {
        String dateString = "Thu, 23 Mar 2023 12:34:56 ABC";
        ParsePosition parsePosition = new ParsePosition(0);
        StdDateFormat.getInstance().parse(dateString, parsePosition);
    }

    @Test
    public void testFormatISO8601() {
        Date date = new Date();
        String dateString = StdDateFormat.getInstance().format(date);
        assertEquals(dateString, StdDateFormat.getInstance().format(date));
        assertEquals(dateString, StdDateFormat.getISO8601Format(TimeZone.getDefault(), Locale.getDefault()).format(date));
    }

    @Test
    public void testFormatRFC1123() {
        Date date = new Date();
        String dateString = StdDateFormat.getInstance().format(date);
        assertEquals(dateString, StdDateFormat.getInstance().format(date));
        assertEquals(dateString, StdDateFormat.getRFC1123Format(TimeZone.getDefault(), Locale.getDefault()).format(date));
    }

    @Test
    public void testSetTimeZone() {
        DateFormat dateFormat = StdDateFormat.getInstance();
        assertNull(dateFormat.getTimeZone());

        dateFormat = StdDateFormat.getInstance().withTimeZone(TimeZone.getTimeZone("UTC+1"));
        assertNotNull(dateFormat.getTimeZone());
        assertEquals("UTC+1", dateFormat.getTimeZone().getID());
    }

    @Test
    public void testSetLocale() {
        DateFormat dateFormat = StdDateFormat.getInstance();
        assertNotNull(dateFormat.getLocale());
        assertEquals(Locale.getDefault(), dateFormat.getLocale());

        dateFormat = StdDateFormat.getInstance().withLocale(new Locale("en", "US"));
        assertNotNull(dateFormat.getLocale());
        assertEquals("en_US", dateFormat.getLocale().toString());
    }

    @Test
    public void testClone() {
        DateFormat dateFormat = StdDateFormat.getInstance();
        DateFormat clone = dateFormat.clone();

        assertNotNull(clone);
        assertEquals(dateFormat.getClass(), clone.getClass());
        assertNull(clone.getTimeZone());
        assertNull(clone.getLocale());
    }
}