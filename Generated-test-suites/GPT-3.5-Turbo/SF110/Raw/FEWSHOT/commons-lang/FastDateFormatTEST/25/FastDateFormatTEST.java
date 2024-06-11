package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

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
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyle() {
        int style = FastDateFormat.MEDIUM;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        int style = FastDateFormat.MEDIUM;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleTimeZoneAndLocale() {
        int style = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyle() {
        int style = FastDateFormat.MEDIUM;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        int style = FastDateFormat.MEDIUM;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleTimeZoneAndLocale() {
        int style = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
        int dateStyle = FastDateFormat.MEDIUM;
        int timeStyle = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleAndLocale() {
        int dateStyle = FastDateFormat.MEDIUM;
        int timeStyle = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleAndTimeZone() {
        int dateStyle = FastDateFormat.MEDIUM;
        int timeStyle = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.MEDIUM;
        int timeStyle = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNull(result);
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = 1000L;
        String result = instance.format(millis);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        String result = instance.format(date);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = 1000L;
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(millis, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(date, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(calendar, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2020-01-01";
        Date result = instance.parse(source);
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
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
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        boolean result = instance1.equals(instance2);
        assertTrue(result);
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
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.applyRules(calendar, buf);
        assertNotNull(result);
    }
}