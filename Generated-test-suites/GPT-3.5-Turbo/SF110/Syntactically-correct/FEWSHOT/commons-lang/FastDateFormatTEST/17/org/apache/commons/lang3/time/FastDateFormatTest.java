package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern);
        assertEquals(instance1.getPattern(), pattern);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern, timeZone);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern, timeZone);
        assertEquals(instance1.getTimeZone(), timeZone);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "dd/MM/yyyy";
        Locale locale = Locale.US;
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern, locale);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern, locale);
        assertEquals(instance1.getLocale(), locale);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern, timeZone, locale);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertEquals(instance1.getTimeZone(), timeZone);
        assertEquals(instance1.getLocale(), locale);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        FastDateFormat instance2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals(instance1, instance2);
    }

    // Add more test cases for getDateInstance()

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance1 = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        FastDateFormat instance2 = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertEquals(instance1, instance2);
    }

    // Add more test cases for getTimeInstance()

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat instance2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertEquals(instance1, instance2);
    }

    // Add more test cases for getDateTimeInstance()

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        // Write test cases to test the format() method with obj, toAppendTo, and pos parameters
    }

    @Test
    public void testFormatLong() {
        // Write test cases to test the format() method with millis parameter
    }

    @Test
    public void testFormatDate() {
        // Write test cases to test the format() method with date parameter
    }

    @Test
    public void testFormatCalendar() {
        // Write test cases to test the format() method with calendar parameter
    }

    @Test
    public void testFormatLongStringBuffer() {
        // Write test cases to test the format() method with millis and buf parameters
    }

    @Test
    public void testFormatDateStringBuffer() {
        // Write test cases to test the format() method with date and buf parameters
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        // Write test cases to test the format() method with calendar and buf parameters
    }

    @Test
    public void testParse() throws ParseException {
        String source = "31/12/2022";
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        Date expected = new SimpleDateFormat("dd/MM/yyyy").parse(source);
        Date actual = instance.parse(source);
        // Compare expected and actual dates using assertEquals()
    }

    @Test
    public void testParseStringParsePosition() {
        // Write test cases to test the parse() method with source and pos parameters
    }

    @Test
    public void testParseObjectStringParsePosition() {
        // Write test cases to test the parseObject() method with source and pos parameters
    }

    @Test
    public void testGetPattern() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertEquals(pattern, instance.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", timeZone);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", locale);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        assertEquals(0, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, "invalid");
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int hashCode = instance.hashCode();
        assertEquals(hashCode, instance.hashCode());
    }

    @Test
    public void testToString() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        String expected = "FastDateFormat[" + pattern + "," + locale + "," + timeZone.getID() + "]";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testApplyRules() {
        // Write test cases for applyRules() method
    }
}