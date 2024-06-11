import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy/MM/dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy/MM/dd";
        String timeZoneId = "GMT";
        FastDateFormat instance = FastDateFormat.getInstance(pattern, TimeZone.getTimeZone(timeZoneId));
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZoneId, instance.getTimeZone().getID());
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy/MM/dd";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        String pattern = "yyyy/MM/dd";
        String timeZoneId = "GMT";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, TimeZone.getTimeZone(timeZoneId), locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZoneId, instance.getTimeZone().getID());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstanceWithStyle() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        String timeZoneId = "GMT";
        FastDateFormat instance = FastDateFormat.getDateInstance(style, TimeZone.getTimeZone(timeZoneId));
        assertNotNull(instance);
        assertEquals(timeZoneId, instance.getTimeZone().getID());
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        String timeZoneId = "GMT";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, TimeZone.getTimeZone(timeZoneId), locale);
        assertNotNull(instance);
        assertEquals(timeZoneId, instance.getTimeZone().getID());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetTimeInstanceWithStyle() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        String timeZoneId = "GMT";
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, TimeZone.getTimeZone(timeZoneId));
        assertNotNull(instance);
        assertEquals(timeZoneId, instance.getTimeZone().getID());
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        String timeZoneId = "GMT";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, TimeZone.getTimeZone(timeZoneId), locale);
        assertNotNull(instance);
        assertEquals(timeZoneId, instance.getTimeZone().getID());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndTimeZone() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        String timeZoneId = "GMT";
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, TimeZone.getTimeZone(timeZoneId));
        assertNotNull(instance);
        assertEquals(timeZoneId, instance.getTimeZone().getID());
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        String timeZoneId = "GMT";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, TimeZone.getTimeZone(timeZoneId), locale);
        assertNotNull(instance);
        assertEquals(timeZoneId, instance.getTimeZone().getID());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testFormatWithObjectAndStringBufferAndFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithMillis() {
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
    public void testFormatWithMillisAndStringBuffer() {
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
    public void testParseWithString() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021/01/01";
        Date result = instance.parse(source);
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithStringAndParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021/01/01";
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithStringAndParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021/01/01";
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String pattern = instance.getPattern();
        assertNotNull(pattern);
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        TimeZone timeZone = instance.getTimeZone();
        assertNotNull(timeZone);
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Locale locale = instance.getLocale();
        assertNotNull(locale);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int hashCode = instance.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String string = instance.toString();
        assertNotNull(string);
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