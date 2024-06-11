import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.text.*;

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
    public void testGetInstanceWithPatternAndTimezone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternTimezoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimezone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimezoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimezone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimezoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimezone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimezoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), buf, pos);
        assertTrue(result instanceof StringBuffer);
    }
    
    @Test
    public void testFormatMillis() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(System.currentTimeMillis());
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
    public void testFormatMillisWithBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(System.currentTimeMillis(), buf);
        assertTrue(result instanceof StringBuffer);
    }
    
    @Test
    public void testFormatDateWithBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(new Date(), buf);
        assertTrue(result instanceof StringBuffer);
    }
    
    @Test
    public void testFormatCalendarWithBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.format(Calendar.getInstance(), buf);
        assertTrue(result instanceof StringBuffer);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse("2022-01-01");
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse("2022-01-01", pos);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject("2022-01-01", pos);
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
        int estimate = instance.getMaxLengthEstimate();
        assertTrue(estimate >= 0);
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
        assertTrue(hashCode >= 0);
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String str = instance.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = instance.applyRules(Calendar.getInstance(), buf);
        assertTrue(result instanceof StringBuffer);
    }
}