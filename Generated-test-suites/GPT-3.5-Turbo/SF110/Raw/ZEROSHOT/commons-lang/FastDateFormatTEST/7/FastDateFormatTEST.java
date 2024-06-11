package org.apache.commons.lang3.time;

import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat.getInstance();
        // Add assertions here
    }
    
    @Test
    public void testGetInstanceWithString() {
        FastDateFormat.getInstance("yyyy-MM-dd");
        // Add assertions here
    }
    
    @Test
    public void testGetInstanceWithStringAndTimeZone() {
        FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetInstanceWithStringAndLocale() {
        FastDateFormat.getInstance("yyyy-MM-dd", Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetInstanceWithStringAndTimeZoneAndLocale() {
        FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL);
        // Add assertions here
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        // Add assertions here
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        // Add assertions here
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault());
        // Add assertions here
    }
    
    @Test
    public void testFormatWithObjectStringBufferFieldPosition() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(new Date(), buffer, position);
        // Add assertions here
    }
    
    @Test
    public void testFormatWithLong() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.format(System.currentTimeMillis());
        // Add assertions here
    }
    
    @Test
    public void testFormatWithDate() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.format(new Date());
        // Add assertions here
    }
    
    @Test
    public void testFormatWithCalendar() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.format(Calendar.getInstance());
        // Add assertions here
    }
    
    @Test
    public void testFormatWithLongAndStringBuffer() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        StringBuffer buffer = new StringBuffer();
        format.format(System.currentTimeMillis(), buffer);
        // Add assertions here
    }
    
    @Test
    public void testFormatWithDateAndStringBuffer() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        StringBuffer buffer = new StringBuffer();
        format.format(new Date(), buffer);
        // Add assertions here
    }
    
    @Test
    public void testFormatWithCalendarAndStringBuffer() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        StringBuffer buffer = new StringBuffer();
        format.format(Calendar.getInstance(), buffer);
        // Add assertions here
    }
    
    @Test
    public void testParseWithString() throws ParseException {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.parse("2021-12-31");
        // Add assertions here
    }
    
    @Test
    public void testParseWithStringAndParsePosition() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        ParsePosition position = new ParsePosition(0);
        format.parse("2021-12-31", position);
        // Add assertions here
    }
    
    @Test
    public void testParseObjectWithStringAndParsePosition() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        ParsePosition position = new ParsePosition(0);
        format.parseObject("2021-12-31", position);
        // Add assertions here
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.getPattern();
        // Add assertions here
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.getTimeZone();
        // Add assertions here
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.getLocale();
        // Add assertions here
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.getMaxLengthEstimate();
        // Add assertions here
    }
    
    @Test
    public void testEqualsWithObject() {
        FastDateFormat format1 = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        FastDateFormat format2 = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format1.equals(format2);
        // Add assertions here
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.hashCode();
        // Add assertions here
    }
    
    @Test
    public void testToString() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        format.toString();
        // Add assertions here
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat format = new FastDateFormat("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.applyRules(calendar, buffer);
        // Add assertions here
    }
}