import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {
    
    // Test cases for getInstance() method
    
    // Test case for getInstance() with default parameters
    @Test
    public void testGetInstanceDefault() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    // Test case for getInstance(String pattern)
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }
    
    // Test case for getInstance(String pattern, TimeZone timeZone)
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
    }
    
    // Test case for getInstance(String pattern, Locale locale)
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(instance);
    }
    
    // Test case for getInstance(String pattern, TimeZone timeZone, Locale locale)
    @Test
    public void testGetInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(instance);
    }
    
    // Test cases for getDateInstance() methods
    
    // Test case for getDateInstance(int style)
    @Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    // Test case for getDateInstance(int style, Locale locale)
    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(instance);
    }
    
    // Test case for getDateInstance(int style, TimeZone timeZone)
    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
    }
    
    // Test case for getDateInstance(int style, TimeZone timeZone, Locale locale)
    @Test
    public void testGetDateInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
    }
    
    // Test cases for getTimeInstance() methods
    
    // Test case for getTimeInstance(int style)
    @Test
    public void testGetTimeInstanceWithStyle() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    // Test case for getTimeInstance(int style, Locale locale)
    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        assertNotNull(instance);
    }
    
    // Test case for getTimeInstance(int style, TimeZone timeZone)
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
    }
    
    // Test case for getTimeInstance(int style, TimeZone timeZone, Locale locale)
    @Test
    public void testGetTimeInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
    }
    
    // Test cases for getDateTimeInstance() methods
    
    // Test case for getDateTimeInstance(int dateStyle, int timeStyle)
    @Test
    public void testGetDateTimeInstanceWithStyles() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT);
        assertNotNull(instance);
    }
    
    // Test case for getDateTimeInstance(int dateStyle, int timeStyle, Locale locale)
    @Test
    public void testGetDateTimeInstanceWithStylesAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, locale);
        assertNotNull(instance);
    }
    
    // Test case for getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone)
    @Test
    public void testGetDateTimeInstanceWithStylesAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
    }
    
    // Test case for getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale)
    @Test
    public void testGetDateTimeInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
    }
    
    // Test case for format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    @Test
    public void testFormatWithObjectAndFieldPosition() {
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(obj, toAppendTo, pos));
    }
    
    // Test case for format(long millis)
    @Test
    public void testFormatWithMillis() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(millis));
    }
    
    // Test case for format(Date date)
    @Test
    public void testFormatWithDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(date));
    }
    
    // Test case for format(Calendar calendar)
    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(calendar));
    }
    
    // Test case for format(long millis, StringBuffer buf)
    @Test
    public void testFormatWithMillisAndBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(millis, buf));
    }
    
    // Test case for format(Date date, StringBuffer buf)
    @Test
    public void testFormatWithDateAndBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(date, buf));
    }
    
    // Test case for format(Calendar calendar, StringBuffer buf)
    @Test
    public void testFormatWithCalendarAndBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.format(calendar, buf));
    }
    
    // Test case for parse(String source)
    @Test
    public void testParse() throws ParseException {
        String source = "2021-10-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.parse(source));
    }
    
    // Test case for parse(String source, ParsePosition pos)
    @Test
    public void testParseWithParsePosition() {
        String source = "2021-10-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.parse(source, pos));
    }
    
    // Test case for parseObject(String source, ParsePosition pos)
    @Test
    public void testParseObjectWithParsePosition() {
        String source = "2021-10-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.parseObject(source, pos));
    }
    
    // Test case for getPattern()
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.getPattern());
    }
    
    // Test case for getTimeZone()
    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.getTimeZone());
    }
    
    // Test case for getLocale()
    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.getLocale());
    }
    
    // Test case for getMaxLengthEstimate()
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertTrue(instance.getMaxLengthEstimate() >= 0);
    }
    
    // Test case for equals(Object obj)
    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertTrue(instance1.equals(instance2));
    }
    
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.hashCode());
    }
    
    // Test case for toString()
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.toString());
    }
    
    // Test case for applyRules(Calendar calendar, StringBuffer buf)
    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance.applyRules(calendar, buf));
    }
}