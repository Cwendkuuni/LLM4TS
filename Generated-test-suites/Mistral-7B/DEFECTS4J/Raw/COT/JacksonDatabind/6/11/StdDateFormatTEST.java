package com.example.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testInstances() {
        StdDateFormat instance = StdDateFormat.instance;
        assertNotNull(instance);
        assertEquals(StdDateFormat.class, instance.getClass());
        assertEquals(Locale.US, instance.getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), instance.getTimeZone());
        StdDateFormat newInstance = new StdDateFormat();
        assertEquals(instance.getLocale(), newInstance.getLocale());
        assertEquals(instance.getTimeZone(), newInstance.getTimeZone());
        assertNotSame(instance, newInstance);
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat instance = StdDateFormat.instance;
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat newInstance = new StdDateFormat(timeZone, Locale.US);
        assertNotSame(instance, newInstance);
        assertEquals(timeZone, newInstance.getTimeZone());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat instance = StdDateFormat.instance;
        Locale locale = Locale.CHINA;
        StdDateFormat newInstance = new StdDateFormat(instance.getTimeZone(), locale);
        assertNotSame(instance, newInstance);
        assertEquals(locale, newInstance.getLocale());
    }

    @Test
    public void testClone() {
        StdDateFormat instance = StdDateFormat.instance;
        StdDateFormat clone = instance.clone();
        assertNotSame(instance, clone);
        assertEquals(instance.getLocale(), clone.getLocale());
        assertEquals(instance.getTimeZone(), clone.getTimeZone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetTimeZone() {
        StdDateFormat instance = StdDateFormat.instance;
        instance.setTimeZone(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        String invalidDate = "invalidDate";
        try {
            dateFormat.parse(invalidDate);
            fail();
        } catch (ParseException expected) {
        }
    }

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2023-03-01T14:30:59.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(dateString), parsedDate);
    }

    @Test
    public void testParseISO8601Z() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2023-03-01T14:30:59.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString), parsedDate);
    }

    @Test
    public void testParseISO8601Plain() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2023-03-01";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(dateString), parsedDate);
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "Thu, 01 Mar 2023 14:30:59 GMT";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
        assertEquals(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(dateString), parsedDate);
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = dateFormat.format(date, new StringBuffer(), null);
        assertNotNull(formattedDate);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date), formattedDate);
    }

}