
package org.apache.commons.lang3.time;

import org.junit.Test;

import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void getInstance_noArgs_returnsInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
    }

    @Test
    public void getInstance_withPattern_returnsInstance() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(format);
    }
    
    @Test
    public void getInstance_withPatternAndTimeZone_returnsInstance() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
    }
    
    @Test
    public void getInstance_withPatternAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getInstance_withPatternTimeZoneAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getDateInstance_withStyle_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(format);
    }
    
    @Test
    public void getDateInstance_withStyleAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getDateInstance_withStyleAndTimeZone_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
    }
    
    @Test
    public void getDateInstance_withStyleTimeZoneAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getTimeInstance_withStyle_returnsInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(format);
    }
    
    @Test
    public void getTimeInstance_withStyleAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getTimeInstance_withStyleAndTimeZone_returnsInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
    }
    
    @Test
    public void getTimeInstance_withStyleTimeZoneAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getDateTimeInstance_withDateStyleAndTimeStyle_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertNotNull(format);
    }
    
    @Test
    public void getDateTimeInstance_withDateStyleAndTimeStyleAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, Locale.US);
        assertNotNull(format);
    }
    
    @Test
    public void getDateTimeInstance_withDateStyleAndTimeStyleAndTimeZone_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
    }
    
    @Test
    public void getDateTimeInstance_withDateStyleAndTimeStyleTimeZoneAndLocale_returnsInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }

    @Test
    public void format_withObjectAndStringBufferAndFieldPosition_returnsStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(new Date(), buffer, position);
        assertTrue(buffer.length() > 0);
    }
    
    @Test
    public void format_withMillis_returnsFormattedString() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = format.format(System.currentTimeMillis());
        assertNotNull(result);
    }
    
    @Test
    public void format_withDate_returnsFormattedString() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = format.format(new Date());
        assertNotNull(result);
    }
    
    @Test
    public void format_withCalendar_returnsFormattedString() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = format.format(Calendar.getInstance());
        assertNotNull(result);
    }
    
    @Test
    public void format_withMillisAndStringBuffer_returnsStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(System.currentTimeMillis(), buffer);
        assertTrue(buffer.length() > 0);
    }
    
    @Test
    public void format_withDateAndStringBuffer_returnsStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(new Date(), buffer);
        assertTrue(buffer.length() > 0);
    }
    
    @Test
    public void format_withCalendarAndStringBuffer_returnsStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(Calendar.getInstance(), buffer);
        assertTrue(buffer.length() > 0);
    }
    
    @Test
    public void parse_withString_returnsDate() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Date result = format.parse("2020-01-01");
        assertNotNull(result);
    }
    
    @Test
    public void parse_withStringAndParsePosition_returnDate() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Date result = format.parse("2020-01-01", position);
        assertNotNull(result);
    }
    
    @Test
    public void parseObject_withStringAndParsePosition_returnsObject() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Object result = format.parseObject("2020-01-01", position);
        assertNotNull(result);
    }
    
    @Test
    public void getPattern_returnsPattern() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String pattern = format.getPattern();
        assertNotNull(pattern);
    }
    
    @Test
    public void getTimeZone_returnsTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        TimeZone timeZone = format.getTimeZone();
        assertNotNull(timeZone);
    }
    
    @Test
    public void getLocale_returnsLocale() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Locale locale = format.getLocale();
        assertNotNull(locale);
    }
    
    @Test
    public void getMaxLengthEstimate_returnsMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        int maxLength = format.getMaxLengthEstimate();
        assertTrue(maxLength > 0);
    }
    
    @Test
    public void equals_withEqualObject_returnsTrue() {
        FastDateFormat format1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(format1.equals(format2));
    }
    
    @Test
    public void equals_withDifferentObject_returnsFalse() {
        FastDateFormat format1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertFalse(format1.equals(format2));
    }
    
    @Test
    public void hashCode_withEqualObject_returnsEqualHashCode() {
        FastDateFormat format1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(format1.hashCode(), format2.hashCode());
    }
    
    @Test
    public void toString_returnsStringRepresentation() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = format.toString();
        assertNotNull(result);
    }
}

