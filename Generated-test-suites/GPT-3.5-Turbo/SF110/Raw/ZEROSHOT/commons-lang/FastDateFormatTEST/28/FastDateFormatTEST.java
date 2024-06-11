import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithString() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringAndTimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        Object obj = new Date();
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(obj, buf, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat format = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        String result = format.format(millis);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat format = FastDateFormat.getInstance();
        Date date = new Date();
        String result = format.format(date);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String result = format.format(calendar);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = format.format(millis, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = format.format(date, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = format.format(calendar, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "2020-01-01";
        Date result = format.parse(source);
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date result = format.parse(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "2020-01-01";
        ParsePosition pos = new ParsePosition(0);
        Object result = format.parseObject(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance();
        String result = format.getPattern();
        assertNotNull(result);
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance();
        TimeZone result = format.getTimeZone();
        assertNotNull(result);
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance();
        Locale result = format.getLocale();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance();
        int result = format.getMaxLengthEstimate();
        assertNotNull(result);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        boolean result = format1.equals(format2);
        assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat format = FastDateFormat.getInstance();
        int result = format.hashCode();
        assertNotNull(result);
    }
    
    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();
        String result = format.toString();
        assertNotNull(result);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = format.applyRules(calendar, buf);
        assertNotNull(result);
    }
}