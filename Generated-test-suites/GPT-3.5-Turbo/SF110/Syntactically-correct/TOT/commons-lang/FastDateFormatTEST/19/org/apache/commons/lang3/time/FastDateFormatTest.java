package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.ENGLISH);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getTimeZone("GMT"));
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.ENGLISH);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("GMT"));
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL, Locale.ENGLISH);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

        assertNotNull(instance);
    }
    
    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer resultBuffer = instance.format(new Date(), buffer, position);
        
        assertNotNull(resultBuffer);
        assertEquals(19, resultBuffer.length());
    }
    
    @Test
    public void testFormat_long() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        String result = instance.format(1617877513000L);
        
        assertNotNull(result);
        assertEquals("2021-04-08 14:52:45", result);
    }
    
    @Test
    public void testFormat_Date() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        
        String result = instance.format(date);
        
        assertNotNull(result);
        assertEquals(19, result.length());
    }
    
    @Test
    public void testFormat_Calendar() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        
        String result = instance.format(calendar);
        
        assertNotNull(result);
        assertEquals(19, result.length());
    }
    
    @Test
    public void testFormat_long_StringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer resultBuffer = instance.format(1617877513000L, buffer);
        
        assertNotNull(resultBuffer);
        assertEquals(19, resultBuffer.length());
    }
    
    @Test
    public void testFormat_Date_StringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer resultBuffer = instance.format(date, buffer);
        
        assertNotNull(resultBuffer);
        assertEquals(19, resultBuffer.length());
    }
    
    @Test
    public void testFormat_Calendar_StringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer resultBuffer = instance.format(calendar, buffer);
        
        assertNotNull(resultBuffer);
        assertEquals(19, resultBuffer.length());
    }
    
    @Test
    public void testParse_String() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String dateString = "2021-04-08 14:52:45";
        
        Date result = instance.parse(dateString);
        
        assertNotNull(result);
    }
    
    @Test
    public void testParse_String_ParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String dateString = "2021-04-08 14:52:45";
        ParsePosition position = new ParsePosition(0);
        
        Date result = instance.parse(dateString, position);
        
        assertNotNull(result);
        assertEquals(19, position.getIndex());
    }
    
    @Test
    public void testParseObject_String_ParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String dateString = "2021-04-08 14:52:45";
        ParsePosition position = new ParsePosition(0);
        
        Object result = instance.parseObject(dateString, position);
        
        assertNotNull(result);
        assertEquals(19, position.getIndex());
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        String pattern = instance.getPattern();
        
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd HH:mm:ss", pattern);
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        TimeZone timeZone = instance.getTimeZone();
        
        assertNotNull(timeZone);
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        Locale locale = instance.getLocale();
        
        assertNotNull(locale);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        int maxLength = instance.getMaxLengthEstimate();
        
        assertTrue(maxLength > 0);
    }
    
    @Test
    public void testEquals_Object() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        boolean result = instance.equals(other);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_Object_NotEqual() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
        
        boolean result = instance.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        int hashCode = instance.hashCode();
        
        assertTrue(hashCode != 0);
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        
        String toString = instance.toString();
        
        assertNotNull(toString);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer resultBuffer = instance.applyRules(calendar, buffer);
        
        assertNotNull(resultBuffer);
    }
}