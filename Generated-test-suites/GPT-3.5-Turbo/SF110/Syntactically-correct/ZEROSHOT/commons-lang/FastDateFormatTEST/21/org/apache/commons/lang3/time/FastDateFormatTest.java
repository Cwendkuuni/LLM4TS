import org.junit.*;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    // Test getInstance()
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance);
    }

    // Test getInstance(String pattern)
    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        Assert.assertNotNull(instance);
        Assert.assertEquals(pattern, instance.getPattern());
    }

    // Test getInstance(String pattern, TimeZone timeZone)
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        Assert.assertNotNull(instance);
        Assert.assertEquals(pattern, instance.getPattern());
        Assert.assertEquals(timeZone, instance.getTimeZone());
    }

    // Test getInstance(String pattern, Locale locale)
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(pattern, instance.getPattern());
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getInstance(String pattern, TimeZone timeZone, Locale locale)
    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(pattern, instance.getPattern());
        Assert.assertEquals(timeZone, instance.getTimeZone());
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getDateInstance(int style)
    @Test
    public void testGetDateInstanceWithStyle() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        Assert.assertNotNull(instance);
        Assert.assertTrue(instance instanceof DatePrinter);
        Assert.assertTrue(instance instanceof DateParser);
    }

    // Test getDateInstance(int style, Locale locale)
    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getDateInstance(int style, TimeZone timeZone)
    @Test
    public void testGetDateInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
        Assert.assertNotNull(instance);
        Assert.assertEquals(timeZone, instance.getTimeZone());
    }

    // Test getDateInstance(int style, TimeZone timeZone, Locale locale)
    @Test
    public void testGetDateInstanceWithStyleAndTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(timeZone, instance.getTimeZone());
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getTimeInstance(int style)
    @Test
    public void testGetTimeInstanceWithStyle() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        Assert.assertNotNull(instance);
        Assert.assertTrue(instance instanceof DatePrinter);
        Assert.assertTrue(instance instanceof DateParser);
    }

    // Test getTimeInstance(int style, Locale locale)
    @Test
    public void testGetTimeInstanceWithStyleAndLocale() {
        int style = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getTimeInstance(int style, TimeZone timeZone)
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        Assert.assertNotNull(instance);
        Assert.assertEquals(timeZone, instance.getTimeZone());
    }

    // Test getTimeInstance(int style, TimeZone timeZone, Locale locale)
    @Test
    public void testGetTimeInstanceWithStyleAndTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(timeZone, instance.getTimeZone());
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getDateTimeInstance(int dateStyle, int timeStyle)
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        Assert.assertNotNull(instance);
        Assert.assertTrue(instance instanceof DatePrinter);
        Assert.assertTrue(instance instanceof DateParser);
    }

    // Test getDateTimeInstance(int dateStyle, int timeStyle, Locale locale)
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.SHORT;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone)
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndTimeZone() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
        Assert.assertNotNull(instance);
        Assert.assertEquals(timeZone, instance.getTimeZone());
    }

    // Test getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale)
    @Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.SHORT;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
        Assert.assertNotNull(instance);
        Assert.assertEquals(timeZone, instance.getTimeZone());
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    @Test
    public void testFormatWithObjectToAppendToAndFieldPosition() {
        Object obj = new Date();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertEquals(toAppendTo, instance.format(obj, toAppendTo, pos));
    }

    // Test format(long millis)
    @Test
    public void testFormatWithMillis() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.format(millis));
    }

    // Test format(Date date)
    @Test
    public void testFormatWithDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.format(date));
    }

    // Test format(Calendar calendar)
    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.format(calendar));
    }

    // Test format(long millis, StringBuffer buf)
    @Test
    public void testFormatWithMillisAndBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertEquals(buf, instance.format(millis, buf));
    }

    // Test format(Date date, StringBuffer buf)
    @Test
    public void testFormatWithDateAndBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertEquals(buf, instance.format(date, buf));
    }

    // Test format(Calendar calendar, StringBuffer buf)
    @Test
    public void testFormatWithCalendarAndBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertEquals(buf, instance.format(calendar, buf));
    }

    // Test parse(String source)
    @Test
    public void testParseWithStringSource() throws ParseException {
        String source = "2022-03-15";
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.parse(source));
    }

    // Test parse(String source, ParsePosition pos)
    @Test
    public void testParseWithStringSourceAndParsePosition() {
        String source = "2022-03-15";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.parse(source, pos));
    }

    // Test parseObject(String source, ParsePosition pos)
    @Test
    public void testParseObjectWithStringSourceAndParsePosition() {
        String source = "2022-03-15";
        ParsePosition pos = new ParsePosition(0);
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.parseObject(source, pos));
    }

    // Test getPattern()
    @Test
    public void testGetPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        Assert.assertEquals(pattern, instance.getPattern());
    }

    // Test getTimeZone()
    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        Assert.assertEquals(timeZone, instance.getTimeZone());
    }

    // Test getLocale()
    @Test
    public void testGetLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        Assert.assertEquals(locale, instance.getLocale());
    }

    // Test getMaxLengthEstimate()
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertTrue(instance.getMaxLengthEstimate() > 0);
    }

    // Test equals(Object obj)
    @Test
    public void testEqualsWithObject() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        Assert.assertEquals(instance1, instance2);
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance.hashCode());
    }

    // Test toString()
    @Test
    public void testToString() {
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        Assert.assertEquals("FastDateFormat[" + pattern + "," + locale + "," + timeZone.getID() + "]", instance.toString());
    }

    // Test applyRules(Calendar calendar, StringBuffer buf)
    @Test
    public void testApplyRulesWithCalendarAndBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertEquals(buf, instance.applyRules(calendar, buf));
    }
}