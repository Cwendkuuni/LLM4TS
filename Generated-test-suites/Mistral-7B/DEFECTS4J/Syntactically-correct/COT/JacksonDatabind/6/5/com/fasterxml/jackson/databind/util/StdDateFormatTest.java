package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testGetBlueprintISO8601Format() {
        assertNull(StdDateFormat.getBlueprintISO8601Format());
    }

    @Test
    public void testGetISO8601Format() {
        TimeZone timeZone = ZoneId.systemDefault().toZone();
        Locale locale = Locale.US;
        DateFormat dateFormat = StdDateFormat.getISO8601Format(timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateFormat.toPattern());
    }

    @Test
    public void testParseDateWithISO8601Format() throws ParseException {
        TimeZone timeZone = ZoneId.systemDefault().toZone();
        Locale locale = Locale.US;
        DateFormat dateFormat = StdDateFormat.getISO8601Format(timeZone, locale);
        String dateString = "2022-03-01T12:34:56.789Z";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        String dateString = "InvalidDateFormat";
        DateFormat dateFormat = StdDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        dateFormat.parse(dateString, parsePosition);
        fail("Expected ParseException but got: " + dateFormat.parse(dateString));
    }

    @Test
    public void testFormatDateWithISO8601Format() {
        TimeZone timeZone = ZoneId.systemDefault().toZone();
        Locale locale = Locale.US;
        DateFormat dateFormat = StdDateFormat.getISO8601Format(timeZone, locale);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        assertTrue(formattedDate.matches("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{3}Z$"));
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = new StdDateFormat();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertNotNull(clonedDateFormat);
        assertNotSame(dateFormat, clonedDateFormat);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone timeZone = ZoneId.systemDefault().toZone();
        dateFormat.setTimeZone(timeZone);
        assertSame(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testSetLocale() {
        StdDateFormat dateFormat = new StdDateFormat();
        Locale locale = Locale.US;
        dateFormat.setLocale(locale);
        assertSame(locale, dateFormat.getLocale());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetTimeZoneAfterInstanceCreation() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setTimeZone(null);
        assertThrows(UnsupportedOperationException.class, () -> dateFormat.setTimeZone(ZoneId.systemDefault().toZone()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetLocaleAfterInstanceCreation() {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.setLocale(Locale.US);
        assertThrows(UnsupportedOperationException.class, () -> dateFormat.setLocale(Locale.JAPAN));
    }
}