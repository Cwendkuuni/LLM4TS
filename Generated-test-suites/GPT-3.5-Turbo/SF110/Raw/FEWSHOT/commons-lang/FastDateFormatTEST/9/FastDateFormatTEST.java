package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testFormatWithObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String formattedDate = instance.format(calendar);
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testFormatWithMillis() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String formattedDate = instance.format(1234567890L);
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        String formattedDate = instance.format(date);
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testFormatWithCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String formattedDate = instance.format(calendar);
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testFormatWithMillisAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.format(1234567890L, buffer);
        assertNotNull(formattedBuffer);
        assertTrue(formattedBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testFormatWithDateAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.format(date, buffer);
        assertNotNull(formattedBuffer);
        assertTrue(formattedBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testFormatWithCalendarAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.format(calendar, buffer);
        assertNotNull(formattedBuffer);
        assertTrue(formattedBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-01-01";
        Date parsedDate = instance.parse(source);
        assertNotNull(parsedDate);
    }
    
    @Test
    public void testParseWithPos() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = instance.parse(source, pos);
        assertNotNull(parsedDate);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseObjectWithPos() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        Object parsedObject = instance.parseObject(source, pos);
        assertNotNull(parsedObject);
        assertTrue(parsedObject instanceof Date);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String pattern = instance.getPattern();
        assertEquals("yyyy-MM-dd", pattern);
    }
    
    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        TimeZone instanceTimeZone = instance.getTimeZone();
        assertEquals(timeZone, instanceTimeZone);
    }
    
    @Test
    public void testGetLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        Locale instanceLocale = instance.getLocale();
        assertEquals(locale, instanceLocale);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate > 0);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertTrue(instance.equals(instance));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertEquals(instance.hashCode(), instance.hashCode());
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String toString = instance.toString();
        assertNotNull(toString);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer resultBuffer = instance.applyRules(calendar, buffer);
        assertNotNull(resultBuffer);
        assertTrue(resultBuffer instanceof StringBuffer);
    }
}