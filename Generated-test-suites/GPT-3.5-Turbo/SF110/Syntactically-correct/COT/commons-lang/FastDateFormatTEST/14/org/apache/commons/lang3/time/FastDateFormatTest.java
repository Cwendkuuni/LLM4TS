import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.FastDateFormat;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithStringAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithStringAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithStringTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), buf, pos);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(0L);
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
        StringBuffer result = instance.format(0L, buf);
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
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse("2020-01-01");
        assertNotNull(result);
    }

    @Test
    public void testParseStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse("2020-01-01", pos);
        assertNotNull(result);
    }

    @Test
    public void testParseObjectStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject("2020-01-01", pos);
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
    public void testEquals() {
        FastDateFormat instance = FastDateFormat.getInstance();
        boolean result = instance.equals(instance);
        assertTrue(result);
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
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.applyRules(Calendar.getInstance(), buf);
        assertEquals(buf, result);
    }
}