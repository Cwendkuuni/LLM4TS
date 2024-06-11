import static org.junit.Assert.*;

import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(new Object(), buf, pos);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(Date.UTC(2000, 0, 1, 0, 0, 0));
        assertNotNull(result);
    }

    @Test
    public void testFormatDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(new Date());
        assertNotNull(result);
    }

    @Test
    public void testFormatCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(Calendar.getInstance());
        assertNotNull(result);
    }

    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(Date.UTC(2000, 0, 1, 0, 0, 0), buf);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(new Date(), buf);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(Calendar.getInstance(), buf);
        assertEquals(buf, result);
    }

    @Test
    public void testParseString() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse("2000-01-01");
        assertNotNull(result);
    }

    @Test
    public void testParseStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse("2000-01-01", pos);
        assertNotNull(result);
    }

    @Test
    public void testParseObjectStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject("2000-01-01", pos);
        assertNotNull(result);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = instance.getPattern();
        assertEquals("yyyy-MM-dd", result);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        TimeZone result = instance.getTimeZone();
        assertEquals(TimeZone.getTimeZone("GMT"), result);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        Locale result = instance.getLocale();
        assertEquals(Locale.US, result);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        int result = instance.getMaxLengthEstimate();
        assertTrue(result > 0);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US, TimeZone.getTimeZone("GMT"));
        String result = instance.toString();
        assertNotNull(result);
    }

    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.applyRules(Calendar.getInstance(), buf);
        assertEquals(buf, result);
    }
}