package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatWithObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(FastDateFormat.FULL);
        
        StringBuffer result = instance.format(new Object(), sb, fp);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithMillis() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(1234567890L);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(new Date());
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(Calendar.getInstance());
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithMillisAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer sb = new StringBuffer();
        StringBuffer result = instance.format(1234567890L, sb);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithDateAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer sb = new StringBuffer();
        StringBuffer result = instance.format(new Date(), sb);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithCalendarAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer sb = new StringBuffer();
        StringBuffer result = instance.format(Calendar.getInstance(), sb);
        assertNotNull(result);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse("2021-01-01");
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithPos() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse("2021-01-01", pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject("2021-01-01", pos);
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
        assertEquals(0, result);
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
        StringBuffer sb = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        StringBuffer result = instance.applyRules(calendar, sb);
        assertNotNull(result);
    }
}