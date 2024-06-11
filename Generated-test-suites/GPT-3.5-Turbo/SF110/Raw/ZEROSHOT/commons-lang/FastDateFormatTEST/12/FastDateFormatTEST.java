package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getDefault(), Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getDefault(), Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG);
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, TimeZone.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, TimeZone.getDefault(), Locale.getDefault());
        assertEquals(FastDateFormat.class, instance.getClass());
    }

    @Test
    public void testFormatWithObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        Date date = new Date();
        assertEquals(buffer.getClass(), instance.format(date, buffer).getClass());
    }

    @Test
    public void testFormatWithMillis() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String expected = "2020-01-01";
        String actual = instance.format(1577836800000L);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String expected = "2020-01-01";
        Date date = new Date(1577836800000L);
        String actual = instance.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String expected = "2020-01-01";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 1);
        String actual = instance.format(calendar);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMillisAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        String expected = "2020-01-01";
        String actual = instance.format(1577836800000L, buffer).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDateAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        String expected = "2020-01-01";
        Date date = new Date(1577836800000L);
        String actual = instance.format(date, buffer).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCalendarAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        String expected = "2020-01-01";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 1);
        String actual = instance.format(calendar, buffer).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date expected = new Date(1577836800000L);
        Date actual = instance.parse("2020-01-01");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithPos() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1577836800000L);
        Date actual = instance.parse("2020-01-01", pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseObjectWithPos() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object expected = new Date(1577836800000L);
        Object actual = instance.parseObject("2020-01-01", pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String expected = "yyyy-MM-dd HH:mm:ss";
        String actual = instance.getPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        TimeZone expected = TimeZone.getDefault();
        TimeZone actual = instance.getTimeZone();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Locale expected = Locale.getDefault();
        Locale actual = instance.getLocale();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int expected = 40;
        int actual = instance.getMaxLengthEstimate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertEquals(true, instance1.equals(instance2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int expected = instance.toString().hashCode();
        int actual = instance.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String expected = "FastDateFormat["+instance.getPattern()+","+instance.getLocale()+","+instance.getTimeZone().getID()+"]";
        String actual = instance.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 1);
        StringBuffer expected = instance.format(calendar, buffer);
        StringBuffer actual = instance.applyRules(calendar, buffer);
        assertEquals(expected, actual);
    }

}