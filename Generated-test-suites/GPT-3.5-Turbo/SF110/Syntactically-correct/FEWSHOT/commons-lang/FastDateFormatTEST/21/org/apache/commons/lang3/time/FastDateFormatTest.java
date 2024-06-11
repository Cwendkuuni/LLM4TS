package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FastDateFormatTest {
    @Test
    public void testFormat_Object() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPos = new FieldPosition(0);
        
        assertEquals("2021-01-01T00:00:00", format.format(new Date(1609459200000L), buffer, fieldPos));
    }
    
    @Test
    public void testFormat_Long() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals("2021-01-01T00:00:00", format.format(1609459200000L));
    }
    
    @Test
    public void testFormat_Date() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals("2021-01-01T00:00:00", format.format(new Date(1609459200000L)));
    }
    
    @Test
    public void testFormat_Calendar() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 0, 1, 0, 0, 0);
        
        assertEquals("2021-01-01T00:00:00", format.format(calendar));
    }
    
    @Test
    public void testFormat_Long_Buffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        
        assertEquals("2021-01-01T00:00:00", format.format(1609459200000L, buffer));
    }
    
    @Test
    public void testFormat_Date_Buffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        
        assertEquals("2021-01-01T00:00:00", format.format(new Date(1609459200000L), buffer));
    }
    
    @Test
    public void testFormat_Calendar_Buffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 0, 1, 0, 0, 0);
        StringBuffer buffer = new StringBuffer();
        
        assertEquals("2021-01-01T00:00:00", format.format(calendar, buffer));
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals(new Date(1609459200000L), format.parse("2021-01-01T00:00:00"));
    }
    
    @Test
    public void testParse_Position() {
        FastDateFormat format = FastDateFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        
        assertEquals(new Date(1609459200000L), format.parse("2021-01-01T00:00:00", position));
    }
    
    @Test
    public void testParseObject_Position() {
        FastDateFormat format = FastDateFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        
        assertEquals(new Date(1609459200000L), format.parseObject("2021-01-01T00:00:00", position));
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", format.getPattern());
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals(TimeZone.getDefault(), format.getTimeZone());
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals(Locale.getDefault(), format.getLocale());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals(23, format.getMaxLengthEstimate());
    }
    
    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        
        assertEquals(true, format1.equals(format2));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals(format.hashCode(), format.hashCode());
    }
    
    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();
        
        assertEquals("FastDateFormat[yyyy-MM-dd'T'HH:mm:ss," + Locale.getDefault() + "," + TimeZone.getDefault().getID() + "]", format.toString());
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 0, 1, 0, 0, 0);
        StringBuffer buffer = new StringBuffer();
        
        assertEquals(new StringBuffer("2021-01-01T00:00:00"), format.applyRules(calendar, buffer));
    }
}