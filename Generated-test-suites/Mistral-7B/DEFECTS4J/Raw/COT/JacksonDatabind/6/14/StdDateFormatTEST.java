package com.example; // Replace this with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        assertThrows(Deprecated.class, () -> StdDateFormat.getBlueprintISO8601Format());
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat dateFormat = StdDateFormat.getISO8601Format(null, Locale.US);
        assertNotNull(dateFormat);
        
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        DateFormat dateFormatWithTimeZone = StdDateFormat.getISO8601Format(timeZone, Locale.US);
        assertNotNull(dateFormatWithTimeZone);

        Date date = new Date();
        String isoDateString = StdDateFormat.DATE_FORMAT_STR_ISO8601 + " " + date.toLocaleString();
        Date parsedDate = dateFormatWithTimeZone.parse(isoDateString);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetRFC1123Format() {
        assertThrows(Deprecated.class, () -> StdDateFormat.getRFC1123Format(null));
        
        DateFormat dateFormat = StdDateFormat.getRFC1123Format(null, Locale.US);
        assertNotNull(dateFormat);
        
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        DateFormat dateFormatWithTimeZone = StdDateFormat.getRFC1123Format(timeZone, Locale.US);
        assertNotNull(dateFormatWithTimeZone);

        Date date = new Date();
        String rfc1123DateString = "Wed, 17 Feb 2023 12:34:56 GMT";
        Date parsedDate = dateFormatWithTimeZone.parse(rfc1123DateString);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");

        stdDateFormat.setTimeZone(timeZone);
        DateFormat dateFormat = stdDateFormat.clone();
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParseSimpleDateString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2023-02-17";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test
    public void testParseISO8601DateString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "2023-02-17T12:34:56.123Z";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test
    public void testParseRFC1123DateString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateString = "Wed, 17 Feb 2023 12:34:56 GMT";
        Date date = stdDateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test
    public void testParseInvalidDateString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String invalidDateString = "invalidDate";
        assertThrows(ParseException.class, () -> stdDateFormat.parse(invalidDateString));
    }

    @Test
    public void testFormatDate() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date, new StringBuffer(), null);
        assertNotNull(formattedDate);
    }

    @Test
    public void testToString() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String toString = stdDateFormat.toString();
        assertNotNull(toString);
    }

    @Test
    public void testClone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        DateFormat clonedFormat = stdDateFormat.clone();
        assertEquals(stdDateFormat, clonedFormat);
    }
}