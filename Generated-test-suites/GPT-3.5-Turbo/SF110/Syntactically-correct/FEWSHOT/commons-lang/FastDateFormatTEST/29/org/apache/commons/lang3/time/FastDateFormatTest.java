package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(format);
        assertEquals("yyyy-MM-dd", format.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(format);
        assertEquals("yyyy-MM-dd", format.getPattern());
        assertEquals(timeZone, format.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(format);
        assertEquals("yyyy-MM-dd", format.getPattern());
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(format);
        assertEquals("yyyy-MM-dd", format.getPattern());
        assertEquals(timeZone, format.getTimeZone());
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getDateStyle());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getDateStyle());
        assertEquals(TimeZone.getTimeZone("GMT"), format.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithStyleAndTimeZoneAndLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getDateStyle());
        assertEquals(TimeZone.getTimeZone("GMT"), format.getTimeZone());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getTimeStyle());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("GMT"), format.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithStyleAndTimeZoneAndLocale() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("GMT"), format.getTimeZone());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithStyleAndLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getDateStyle());
        assertEquals(FastDateFormat.SHORT, format.getTimeStyle());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithStyleAndTimeZone() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getDateStyle());
        assertEquals(FastDateFormat.SHORT, format.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("GMT"), format.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithStyleAndTimeZoneAndLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
        assertEquals(FastDateFormat.MEDIUM, format.getDateStyle());
        assertEquals(FastDateFormat.SHORT, format.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("GMT"), format.getTimeZone());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testFormatObject() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(new Object(), buffer, null);
        assertNotNull(buffer.toString());
    }

    @Test
    public void testFormatLong() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String formattedDate = format.format(System.currentTimeMillis());
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatDate() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String formattedDate = format.format(new Date(0));
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatCalendar() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String formattedDate = format.format(Calendar.getInstance());
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatLongAndStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(System.currentTimeMillis(), buffer);
        assertNotNull(buffer.toString());
    }

    @Test
    public void testFormatDateAndStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(new Date(0), buffer);
        assertNotNull(buffer.toString());
    }

    @Test
    public void testFormatCalendarAndStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        format.format(Calendar.getInstance(), buffer);
        assertNotNull(buffer.toString());
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Date date = format.parse("2021-01-01");
        assertNotNull(date);
    }

    @Test
    public void testParseWithParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Date date = format.parse("2021-01-01", position);
        assertNotNull(date);
    }

    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Object obj = format.parseObject("2021-01-01", position);
        assertNotNull(obj);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", format.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertEquals(timeZone, format.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(10, format.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(format.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(format.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.applyRules(calendar, buffer);
        assertNotNull(buffer.toString());
    }
}