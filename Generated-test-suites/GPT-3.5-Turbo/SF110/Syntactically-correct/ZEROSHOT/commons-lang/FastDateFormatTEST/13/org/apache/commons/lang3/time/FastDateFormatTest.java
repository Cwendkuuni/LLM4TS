import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        assertEquals("yyyy-MM-dd", instance.getPattern());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale);
        assertNotNull(instance);
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone, locale);
        assertNotNull(instance);
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("FastDateFormat[yyyy-MM-dd,DEFAULT," + TimeZone.getDefault().getID() + "]", instance.toString());
    }
    
    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testFormat() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1);
        String expected = "2022-01-01";
        assertEquals(expected, instance.format(calendar));
    }
    
    @Test
    public void testFormatWithMillis() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        long millis = new Date().getTime();
        String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(millis));
        assertEquals(expected, instance.format(millis));
    }
    
    @Test
    public void testFormatWithDate() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        assertEquals(expected, instance.format(date));
    }
    
    @Test
    public void testFormatWithStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buf = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1);
        String expected = "2022-01-01";
        assertEquals(buf.append(expected), instance.format(calendar, buf));
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String source = "2022-01-01";
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse(source);
        assertEquals(expected, instance.parse(source));
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse(source, pos);
        assertEquals(expected, instance.parse(source, pos));
    }
    
    @Test
    public void testParseObject() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse(source, pos);
        assertEquals(expected, instance.parseObject(source, pos));
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", instance.getPattern());
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(TimeZone.getDefault(), instance.getTimeZone());
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(Locale.getDefault(), instance.getLocale());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(10, instance.getMaxLengthEstimate());
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1);
        StringBuffer buf = new StringBuffer();
        buf.append("2022-01-01");
        assertEquals(buf, instance.applyRules(calendar, buf));
    }
}