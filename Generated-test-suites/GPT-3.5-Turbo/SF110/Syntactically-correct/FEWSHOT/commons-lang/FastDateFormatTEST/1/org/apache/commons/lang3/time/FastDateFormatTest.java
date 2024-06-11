package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, timeZone);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, timeZone, locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(2020, 0, 1);
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(FastDateFormat.YEAR_FIELD);
        dateFormat.format(calendar, buf, pos);
        assertEquals("2020", buf.toString());
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        long millis = 1577836800000L;
        String formattedDate = dateFormat.format(millis);
        assertEquals("2020-01-01", formattedDate);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Date date = new Date(1577836800000L);
        String formattedDate = dateFormat.format(date);
        assertEquals("2020-01-01", formattedDate);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(2020, 0, 1);
        String formattedDate = dateFormat.format(calendar);
        assertEquals("2020-01-01", formattedDate);
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        long millis = 1577836800000L;
        StringBuffer buf = new StringBuffer();
        dateFormat.format(millis, buf);
        assertEquals("2020-01-01", buf.toString());
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Date date = new Date(1577836800000L);
        StringBuffer buf = new StringBuffer();
        dateFormat.format(date, buf);
        assertEquals("2020-01-01", buf.toString());
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(2020, 0, 1);
        StringBuffer buf = new StringBuffer();
        dateFormat.format(calendar, buf);
        assertEquals("2020-01-01", buf.toString());
    }
    
    @Test
    public void testParseString() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        String dateString = "2020-01-01";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
    }
    
    @Test
    public void testParseStringParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        String dateString = "2020-01-01";
        ParsePosition pos = newParsePosition();
        Date parsedDate = dateFormat.parse(dateString, pos);
        assertNotNull(parsedDate);
    }
    
    @Test
    public void testParseObjectParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        String dateString = "2020-01-01";
        ParsePosition pos = newParsePosition();
        Date parsedDate = (Date) dateFormat.parseObject(dateString, pos);
        assertNotNull(parsedDate);
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        String pattern = dateFormat.getPattern();
        assertEquals("yyyy-MM-dd", pattern);
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        TimeZone timeZone = dateFormat.getTimeZone();
        assertNotNull(timeZone);
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Locale locale = dateFormat.getLocale();
        assertNotNull(locale);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        int maxLength = dateFormat.getMaxLengthEstimate();
        assertEquals(10, maxLength);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat dateFormat2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(dateFormat1.equals(dateFormat2));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        int hashCode = dateFormat.hashCode();
        // no exact value can be checked here
    }
    
    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        String toString = dateFormat.toString();
        assertNotNull(toString);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = dateFormat.applyRules(calendar, buf);
        assertNotNull(result);
    }
    
    private ParsePosition newParsePosition() {
        return new ParsePosition(0);
    }
    
}