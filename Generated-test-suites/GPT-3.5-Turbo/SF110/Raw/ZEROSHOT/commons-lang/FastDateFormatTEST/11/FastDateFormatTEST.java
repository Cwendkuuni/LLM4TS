package org.apache.commons.lang3.time;

import org.junit.*;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        int style = 1;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        int style = 1;
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        int style = 1;
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        int style = 1;
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        int style = 1;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        int style = 1;
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        int style = 1;
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        int style = 1;
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = 1;
        int timeStyle = 1;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        int dateStyle = 1;
        int timeStyle = 1;
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        int dateStyle = 1;
        int timeStyle = 1;
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        int dateStyle = 1;
        int timeStyle = 1;
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNotNull(result);
    }

    @Test
    public void testFormatLong() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(millis);
        assertNotNull(result);
    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(date);
        assertNotNull(result);
    }

    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
    }

    @Test
    public void testFormatLongStringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(millis, buf);
        assertNotNull(result);
    }

    @Test
    public void testFormatDateStringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(date, buf);
        assertNotNull(result);
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(calendar, buf);
        assertNotNull(result);
    }

    @Test
    public void testParseString() throws ParseException {
        String source = "2021-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse(source);
        assertNotNull(result);
    }

    @Test
    public void testParseStringParsePosition() {
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }

    @Test
    public void testParseObject() {
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Object result = instance.parseObject(source, pos);
        assertNotNull(result);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.getPattern();
        assertNotNull(result);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        TimeZone result = instance.getTimeZone();
        assertNotNull(result);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Locale result = instance.getLocale();
        assertNotNull(result);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int result = instance.getMaxLengthEstimate();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        Object obj = new Object();
        FastDateFormat instance = FastDateFormat.getInstance();
        boolean result = instance.equals(obj);
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int result = instance.hashCode();
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.toString();
        assertNotNull(result);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.applyRules(calendar, buf);
        assertNotNull(result);
    }
}