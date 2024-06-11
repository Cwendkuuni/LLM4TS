package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.BeforeClass;
import org.junit.Test;

public class FastDateFormatTest {
    
    private static FastDateFormat fastDateFormat;
    
    @BeforeClass
    public static void setup() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);
    }
    
    @Test
    public void testGetInstance() {
        assertNotNull(FastDateFormat.getInstance());
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd"));
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT")));
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", Locale.US));
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US));
    }
    
    @Test
    public void testGetDateInstance() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.SHORT));
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.US));
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT")));
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US));
    }
    
    @Test
    public void testGetTimeInstance() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.SHORT));
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US));
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT")));
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US));
    }
    
    @Test
    public void testGetDateTimeInstance() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT));
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, Locale.US));
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT")));
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US));
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        StringBuffer sb = new StringBuffer();
        fastDateFormat.format(new Object(), sb, null);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testFormatLong() {
        String result = fastDateFormat.format((long) 1612345678901L);
        assertEquals("2021-02-03 14:14:38", result);
    }
    
    @Test
    public void testFormatDate() {
        Date date = new Date(1612345678901L);
        String result = fastDateFormat.format(date);
        assertEquals("2021-02-03 14:14:38", result);
    }
    
    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
        String result = fastDateFormat.format(calendar);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        StringBuffer sb = new StringBuffer();
        fastDateFormat.format((long) 1612345678901L, sb);
        assertEquals("2021-02-03 14:14:38", sb.toString());
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        Date date = new Date(1612345678901L);
        StringBuffer sb = new StringBuffer();
        fastDateFormat.format(date, sb);
        assertEquals("2021-02-03 14:14:38", sb.toString());
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
        StringBuffer sb = new StringBuffer();
        fastDateFormat.format(calendar, sb);
        assertNotNull(sb.toString());
    }
    
    @Test
    public void testParse() throws ParseException {
        Date result = fastDateFormat.parse("2021-02-03 14:14:38");
        assertEquals(1612345678000L, result.getTime());
    }
    
    @Test
    public void testParseWithParsePosition() {
        ParsePosition pos = new ParsePosition(0);
        Date result = fastDateFormat.parse("2021-02-03 14:14:38", pos);
        assertEquals(1612345678000L, result.getTime());
    }
    
    @Test
    public void testParseObject() {
        ParsePosition pos = new ParsePosition(0);
        Object result = fastDateFormat.parseObject("2021-02-03 14:14:38", pos);
        assertNotNull(result);
    }
    
    @Test
    public void testGetPattern() {
        assertEquals("yyyy-MM-dd HH:mm:ss", fastDateFormat.getPattern());
    }
    
    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getTimeZone("GMT"), fastDateFormat.getTimeZone());
    }
    
    @Test
    public void testGetLocale() {
        assertEquals(Locale.US, fastDateFormat.getLocale());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        assertEquals(19, fastDateFormat.getMaxLengthEstimate());
    }
    
    @Test
    public void testEquals() {
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(true, fastDateFormat.equals(other));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(fastDateFormat.hashCode(), other.hashCode());
    }
    
    @Test
    public void testToString() {
        String result = fastDateFormat.toString();
        assertEquals("FastDateFormat[yyyy-MM-dd HH:mm:ss,en_US,GMT]", result);
    }
    
    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
        StringBuffer sb = new StringBuffer();
        fastDateFormat.applyRules(calendar, sb);
        assertNotNull(sb.toString());
    }
}