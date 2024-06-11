import static org.junit.Assert.*;
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
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = new Locale("en", "US");
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
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = new Locale("en", "US");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = 1000000000;
        String result = instance.format(millis);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        String result = instance.format(date);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String result = instance.format(calendar);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        long millis = 1000000000;
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(millis, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(date, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(calendar, buf);
        assertNotNull(result);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-01-01";
        Date result = instance.parse(source);
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse(source, pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String source = "2021-01-01";
        ParsePosition pos = new ParsePosition(0);
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
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        boolean result = instance1.equals(instance2);
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
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.applyRules(calendar, buf);
        assertNotNull(result);
    }
}