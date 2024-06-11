package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstanceWithoutPattern() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern);
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern, timeZone);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern, timeZone);
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern, locale);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern, locale);
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern, timeZone, locale);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern, timeZone, locale);
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetDateInstanceWithStyle() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyle() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.MEDIUM;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleAndLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.MEDIUM;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleAndTimeZone() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Object obj = new Object();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        
        StringBuffer result = instance.format(obj, buffer, pos);
        
        assertNotNull(result);
        assertEquals(buffer, result);
        assertNotEquals(pos, result);
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        
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
        long millis = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer result = instance.format(millis, buffer);
        
        assertNotNull(result);
        assertEquals(buffer, result);
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer result = instance.format(date, buffer);
        
        assertNotNull(result);
        assertEquals(buffer, result);
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer result = instance.format(calendar, buffer);
        
        assertNotNull(result);
        assertEquals(buffer, result);
    }
    
    @Test
    public void testParseString() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-09-14";
        
        Date result = instance.parse(source);
        
        assertNotNull(result);
    }
    
    @Test
    public void testParseStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-09-14";
        ParsePosition pos = new ParsePosition(0);
        
        Date result = instance.parse(source, pos);
        
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-09-14";
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
    public void testEqualsObject() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        FastDateFormat instance3 = FastDateFormat.getInstance("yyyy-MM-dd");
        
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        
        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
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
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer result = instance.applyRules(calendar, buffer);
        
        assertNotNull(result);
        assertEquals(buffer, result);
    }
    
}