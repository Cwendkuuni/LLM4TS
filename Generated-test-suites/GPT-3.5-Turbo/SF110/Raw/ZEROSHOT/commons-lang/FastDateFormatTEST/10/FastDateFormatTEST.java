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
    public void testGetInstanceWithStringPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringPatternAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
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
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithStyleTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        assertNotNull(instance);
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
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithStyleTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleAndTimeZone() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getDefault();
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatWithObjectStringBufferAndFieldPosition() {
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithLong() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(millis);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(date);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithLongAndStringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(millis, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithDateAndStringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(date, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithCalendarAndStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.format(calendar, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithString() throws ParseException {
        String source = "2022-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse(source);
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithStringAndParsePosition() {
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithStringAndParsePosition() {
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
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
    public void testEquals() {
        Object obj = new Object();
        FastDateFormat instance = FastDateFormat.getInstance();
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
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.applyRules(calendar, buf);
        assertNotNull(result);
    }
}