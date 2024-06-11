import static org.junit.Assert.*;
import org.junit.Test;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstance_String() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd HH:mm:ss", instance.getPattern());
    }

    @Test
    public void testGetInstance_String_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", timeZone);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd HH:mm:ss", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetInstance_String_Locale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", locale);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd HH:mm:ss", instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetInstance_String_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", timeZone, locale);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd HH:mm:ss", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance_Locale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateInstance_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateInstance_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance_Locale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetTimeInstance_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetTimeInstance_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance_Locale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstance_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstance_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), buffer, fieldPosition);
        assertNotNull(result);
    }

    @Test
    public void testFormat_long() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(System.currentTimeMillis()));
    }

    @Test
    public void testFormat_Date() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(new Date()));
    }

    @Test
    public void testFormat_Calendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(Calendar.getInstance()));
    }

    @Test
    public void testFormat_long_StringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.format(System.currentTimeMillis(), buffer));
    }

    @Test
    public void testFormat_Date_StringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.format(new Date(), buffer));
    }

    @Test
    public void testFormat_Calendar_StringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.format(Calendar.getInstance(), buffer));
    }

    @Test
    public void testParse_String() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.parse("2019-01-01 12:00:00"));
    }

    @Test
    public void testParse_String_ParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        assertNotNull(instance.parse("2019-01-01 12:00:00", pos));
    }

    @Test
    public void testParseObject_String_ParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        assertNotNull(instance.parseObject("2019-01-01 12:00:00", pos));
    }

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertEquals(0, instance.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(instance.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.applyRules(Calendar.getInstance(), buffer));
    }

}