
package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithStringPattern() {
        FastDateFormat patternFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("2019-01-01", patternFormat.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetInstanceWithStringPatternAndTimeZone() {
        FastDateFormat timeZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertEquals("2019-01-01", timeZoneFormat.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetInstanceWithStringPatternAndLocale() {
        FastDateFormat localeFormat = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertEquals("2019-01-01", localeFormat.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetInstanceWithStringPatternAndTimeZoneAndLocale() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals("2019-01-01", format.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateInstance = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(dateInstance);
        assertEquals("1/1/19", dateInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat dateInstance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.US);
        assertNotNull(dateInstance);
        assertEquals("1/1/19", dateInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat dateInstance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateInstance);
        assertEquals("1/1/19", dateInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateInstance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateInstance);
        assertEquals("1/1/19", dateInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeInstance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(timeInstance);
        assertEquals("12:00 AM", timeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat timeInstance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
        assertNotNull(timeInstance);
        assertEquals("12:00 AM", timeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat timeInstance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(timeInstance);
        assertEquals("12:00 AM", timeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat timeInstance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(timeInstance);
        assertEquals("12:00 AM", timeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeInstance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertNotNull(dateTimeInstance);
        assertEquals("1/1/19 12:00 AM", dateTimeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat dateTimeInstance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, Locale.US);
        assertNotNull(dateTimeInstance);
        assertEquals("1/1/19 12:00 AM", dateTimeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat dateTimeInstance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateTimeInstance);
        assertEquals("1/1/19 12:00 AM", dateTimeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateTimeInstance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateTimeInstance);
        assertEquals("1/1/19 12:00 AM", dateTimeInstance.format(new Date(1546300800000L)));
    }

    @Test
    public void testFormatWithObjectAndStringBufferAndFieldPosition() {
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(DateFormat.YEAR_FIELD);
        StringBuffer formattedBuffer = fastDateFormat.format(new Date(1546300800000L), buffer, position);
        assertNotNull(formattedBuffer);
        assertEquals("Jan 1, 2019", formattedBuffer.toString());
    }

    @Test
    public void testFormatWithMillis() {
        String formattedDate = fastDateFormat.format(1546300800000L);
        assertNotNull(formattedDate);
        assertEquals("Jan 1, 2019", formattedDate);
    }

    @Test
    public void testFormatWithDate() {
        String formattedDate = fastDateFormat.format(new Date(1546300800000L));
        assertNotNull(formattedDate);
        assertEquals("Jan 1, 2019", formattedDate);
    }

    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(2019, Calendar.JANUARY, 1);
        String formattedDate = fastDateFormat.format(calendar);
        assertNotNull(formattedDate);
        assertEquals("Jan 1, 2019", formattedDate);
    }

    @Test
    public void testFormatWithMillisAndStringBuffer() {
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = fastDateFormat.format(1546300800000L, buffer);
        assertNotNull(formattedBuffer);
        assertEquals("Jan 1, 2019", formattedBuffer.toString());
    }

    @Test
    public void testFormatWithDateAndStringBuffer() {
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = fastDateFormat.format(new Date(1546300800000L), buffer);
        assertNotNull(formattedBuffer);
        assertEquals("Jan 1, 2019", formattedBuffer.toString());
    }

    @Test
    public void testFormatWithCalendarAndStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(2019, Calendar.JANUARY, 1);
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = fastDateFormat.format(calendar, buffer);
        assertNotNull(formattedBuffer);
        assertEquals("Jan 1, 2019", formattedBuffer.toString());
    }

    @Test
    public void testParseWithString() throws Exception {
        Date parsedDate = fastDateFormat.parse("Jan 1, 2019");
        assertNotNull(parsedDate);
        assertEquals(1546300800000L, parsedDate.getTime());
    }

    @Test
    public void testParseWithStringAndParsePosition() {
        ParsePosition position = new ParsePosition(0);
        Date parsedDate = fastDateFormat.parse("Jan 1, 2019", position);
        assertNotNull(parsedDate);
        assertEquals(1546300800000L, parsedDate.getTime());
        assertEquals(11, position.getIndex());
    }

    @Test
    public void testParseObjectWithStringAndParsePosition() {
        ParsePosition position = new ParsePosition(0);
        Object parsedObject = fastDateFormat.parseObject("Jan 1, 2019", position);
        assertTrue(parsedObject instanceof Date);
        Date parsedDate = (Date) parsedObject;
        assertEquals(1546300800000L, parsedDate.getTime());
        assertEquals(11, position.getIndex());
    }

    @Test
    public void testGetPattern() {
        assertEquals("MMM d, yyyy", fastDateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), fastDateFormat.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), fastDateFormat.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        assertEquals(14, fastDateFormat.getMaxLengthEstimate());
    }

    @Test
    public void testEqualsWithSameInstance() {
        assertEquals(fastDateFormat, fastDateFormat);
    }

    @Test
    public void testEqualsWithEqualInstances() {
        FastDateFormat equalFormat = FastDateFormat.getInstance();
        assertEquals(fastDateFormat, equalFormat);
    }

    @Test
    public void testEqualsWithDifferentInstance() {
        DateFormat differentFormat = new SimpleDateFormat();
        assertNotEquals(fastDateFormat, differentFormat);
    }

    @Test
    public void testHashCode() {
        int hashCode = fastDateFormat.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        assertEquals("FastDateFormat[MMM d, yyyy," + TimeZone.getDefault().getID() + "]", fastDateFormat.toString());
    }

    @Test
    public void testApplyRulesWithCalendarAndStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(2019, Calendar.JANUARY, 1);
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = fastDateFormat.applyRules(calendar, buffer);
        assertNotNull(formattedBuffer);
        assertEquals("Jan 1, 2019", formattedBuffer.toString());
    }

}
