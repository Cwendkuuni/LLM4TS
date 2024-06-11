import static org.junit.Assert.*;
import org.junit.*;
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
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);
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
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatWithObjectToAppendToAndFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), buffer, fieldPosition);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithMillis() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(System.currentTimeMillis());
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(new Date());
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String result = instance.format(Calendar.getInstance());
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithMillisAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(System.currentTimeMillis(), buffer);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithDateAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(new Date(), buffer);
        assertNotNull(result);
    }
    
    @Test
    public void testFormatWithCalendarAndBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(Calendar.getInstance(), buffer);
        assertNotNull(result);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date result = instance.parse("2020-01-01 12:00:00");
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        Date result = instance.parse("2020-01-01 12:00:00", parsePosition);
        assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        Object result = instance.parseObject("2020-01-01 12:00:00", parsePosition);
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
        assertTrue(maxLengthEstimate >= 0);
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        Object instance2 = new Object();
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testEqualsWithSameInstance() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        assertTrue(instance1.equals(instance1));
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
        String string = instance.toString();
        assertNotNull(string);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.applyRules(calendar, buffer);
        assertNotNull(result);
    }
}