package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testFormatWithObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(1);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        String result = instance.format(millis);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        String result = instance.format(date);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithLongAndStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(millis, buf);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithDateAndStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(date, buf);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithCalendarAndStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(calendar, buf);
        assertNotNull(result);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2022-01-01 12:00:00";
        Date result = instance.parse(source);
        assertNotNull(result);
    }

    @Test
    public void testParseWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2022-01-01 12:00:00";
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }

    @Test
    public void testParseObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2022-01-01 12:00:00";
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
    public void testEqualsWithObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Object obj = new Object();
        boolean result = instance.equals(obj);
        assertFalse(result);
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
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.applyRules(calendar, buf);
        assertNotNull(result);
    }

}