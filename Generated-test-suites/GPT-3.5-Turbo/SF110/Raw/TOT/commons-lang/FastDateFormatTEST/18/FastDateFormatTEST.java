package org.apache.commons.lang3.time;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, timeZone);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, timeZone, locale);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testFormatObject() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(date);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFormatLong() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(millis);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(date);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(calendar);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFormatLongStringBuffer() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(millis, buf);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFormatDateStringBuffer() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(date, buf);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(calendar, buf);
        Assert.assertNotNull(result);
    }

    @Test
    public void testParse() throws ParseException {
        String source = "2020-01-01";
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Date result = instance.parse(source);
        Assert.assertNotNull(result);
    }

    @Test
    public void testParseWithParsePosition() {
        String source = "2020-01-01";
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse(source, pos);
        Assert.assertNotNull(result);
    }

    @Test
    public void testParseObject() {
        String source = "2020-01-01";
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject(source, pos);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String pattern = instance.getPattern();
        Assert.assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        TimeZone timeZone = instance.getTimeZone();
        Assert.assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Locale locale = instance.getLocale();
        Assert.assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        Assert.assertNotNull(maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        boolean result = instance1.equals(instance2);
        Assert.assertTrue(result);
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int hashCode = instance.hashCode();
        Assert.assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.toString();
        Assert.assertNotNull(result);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.applyRules(calendar, new StringBuffer());
        Assert.assertNotNull(result);
    }

}