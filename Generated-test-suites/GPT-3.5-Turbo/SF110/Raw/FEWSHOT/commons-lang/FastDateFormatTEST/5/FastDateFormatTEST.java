package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat formatter = FastDateFormat.getInstance();
        assertEquals(FastDateFormat.class, formatter.getClass());
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals("yyyy-MM-dd", formatter.getPattern());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals("yyyy-MM-dd", formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals("yyyy-MM-dd", formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals("yyyy-MM-dd", formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat formatter = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat formatter = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat formatter = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat formatter = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat formatter = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat formatter = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat formatter = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat formatter = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(FastDateFormat.class, formatter.getClass());
        assertEquals(FastDateFormat.DEFAULT_PATTERN, formatter.getPattern());
        assertEquals("GMT", formatter.getTimeZone().getID());
        assertEquals(Locale.US, formatter.getLocale());
    }
    
    @Test
    public void testFormatObject() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        String result = formatter.format(new Date(123456789L), buffer, position).toString();
        
        assertEquals("1973-11-29", result);
        assertEquals(10, position.getBeginIndex());
        assertEquals(20, position.getEndIndex());
    }
    
    @Test
    public void testFormatMillis() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = formatter.format(123456789L);
        assertEquals("1973-11-29", result);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = formatter.format(new Date(123456789L));
        assertEquals("1973-11-29", result);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = formatter.format(Calendar.getInstance());
        assertEquals("2022-02-18", result);
    }
    
    @Test
    public void testFormatMillisWithStringBuffer() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = formatter.format(123456789L, buffer);
        assertEquals("1973-11-29", result.toString());
    }
    
    @Test
    public void testFormatDateWithStringBuffer() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = formatter.format(new Date(123456789L), buffer);
        assertEquals("1973-11-29", result.toString());
    }
    
    @Test
    public void testFormatCalendarWithStringBuffer() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = formatter.format(Calendar.getInstance(), buffer);
        assertEquals("2022-02-18", result.toString());
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat parser = FastDateFormat.getInstance("yyyy-MM-dd");
        Date result = parser.parse("1973-11-29");
        assertEquals(new Date(123456789L), result);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat parser = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Date result = parser.parse("1973-11-29", position);
        assertEquals(new Date(123456789L), result);
        assertEquals(0, position.getIndex());
        assertEquals(10, position.getErrorIndex());
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat parser = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Object result = parser.parseObject("1973-11-29", position);
        assertEquals(new Date(123456789L), result);
        assertEquals(0, position.getIndex());
        assertEquals(10, position.getErrorIndex());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(10, formatter.getMaxLengthEstimate());
    }
    
    @Test
    public void testEquals() {
        FastDateFormat formatter1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat formatter2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(true, formatter1.equals(formatter2));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(1740359238, formatter.hashCode());
    }
    
    @Test
    public void testToString() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("FastDateFormat[yyyy-MM-dd,en_US,GMT]", formatter.toString());
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = formatter.applyRules(Calendar.getInstance(), buffer);
        assertEquals("", result.toString());
    }
}