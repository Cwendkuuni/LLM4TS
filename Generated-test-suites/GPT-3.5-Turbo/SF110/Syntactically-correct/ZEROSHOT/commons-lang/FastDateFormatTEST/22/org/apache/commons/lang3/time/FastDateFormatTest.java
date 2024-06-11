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
    String pattern = "yyyy-MM-dd";
    FastDateFormat instance = FastDateFormat.getInstance(pattern);
    assertNotNull(instance);
    assertEquals(pattern, instance.getPattern());
  }
  
  @Test
  public void testGetInstanceWithPatternAndTimeZone() {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
    assertNotNull(instance);
    assertEquals(pattern, instance.getPattern());
    assertEquals(timeZone, instance.getTimeZone());
  }
  
  @Test
  public void testGetInstanceWithPatternAndLocale() {
    String pattern = "yyyy-MM-dd";
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
    assertNotNull(instance);
    assertEquals(pattern, instance.getPattern());
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
    assertNotNull(instance);
    assertEquals(pattern, instance.getPattern());
    assertEquals(timeZone, instance.getTimeZone());
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetDateInstance() {
    int style = FastDateFormat.LONG;
    FastDateFormat instance = FastDateFormat.getDateInstance(style);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceWithLocale() {
    int style = FastDateFormat.LONG;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
    assertNotNull(instance);
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetDateInstanceWithTimeZone() {
    int style = FastDateFormat.LONG;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
    assertNotNull(instance);
    assertEquals(timeZone, instance.getTimeZone());
  }
  
  @Test
  public void testGetDateInstanceWithTimeZoneAndLocale() {
    int style = FastDateFormat.LONG;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
    assertNotNull(instance);
    assertEquals(timeZone, instance.getTimeZone());
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetTimeInstance() {
    int style = FastDateFormat.LONG;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceWithLocale() {
    int style = FastDateFormat.LONG;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
    assertNotNull(instance);
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZone() {
    int style = FastDateFormat.LONG;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
    assertNotNull(instance);
    assertEquals(timeZone, instance.getTimeZone());
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZoneAndLocale() {
    int style = FastDateFormat.LONG;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
    assertNotNull(instance);
    assertEquals(timeZone, instance.getTimeZone());
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetDateTimeInstance() {
    int dateStyle = FastDateFormat.LONG;
    int timeStyle = FastDateFormat.SHORT;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithLocale() {
    int dateStyle = FastDateFormat.LONG;
    int timeStyle = FastDateFormat.SHORT;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    assertNotNull(instance);
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZone() {
    int dateStyle = FastDateFormat.LONG;
    int timeStyle = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
    assertNotNull(instance);
    assertEquals(timeZone, instance.getTimeZone());
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
    int dateStyle = FastDateFormat.LONG;
    int timeStyle = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    assertNotNull(instance);
    assertEquals(timeZone, instance.getTimeZone());
    assertEquals(locale, instance.getLocale());
  }
  
  @Test
  public void testFormatObjectStringBufferFieldPosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buf = new StringBuffer();
    FieldPosition pos = new FieldPosition(0);
    StringBuffer result = instance.format(new Object(), buf, pos);
    assertNotNull(result);
    assertEquals(buf, result);
    assertEquals(pos, result);
  }
  
  @Test
  public void testFormatLong() {
    FastDateFormat instance = FastDateFormat.getInstance();
    long millis = System.currentTimeMillis();
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
    long millis = System.currentTimeMillis();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(millis, buf);
    assertNotNull(result);
    assertEquals(buf, result);
  }
  
  @Test
  public void testFormatDateStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Date date = new Date();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(date, buf);
    assertNotNull(result);
    assertEquals(buf, result);
  }
  
  @Test
  public void testFormatCalendarStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(calendar, buf);
    assertNotNull(result);
    assertEquals(buf, result);
  }
  
  @Test
  public void testParseString() throws ParseException {
    FastDateFormat instance = FastDateFormat.getInstance();
    String source = "2021-01-01";
    Date result = instance.parse(source);
    assertNotNull(result);
  }
  
  @Test
  public void testParseStringParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String source = "2021-01-01";
    ParsePosition pos = new ParsePosition(0);
    Date result = instance.parse(source, pos);
    assertNotNull(result);
    assertEquals(0, pos.getIndex());
  }
  
  @Test
  public void testParseObjectParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String source = "2021-01-01";
    ParsePosition pos = new ParsePosition(0);
    Object result = instance.parseObject(source, pos);
    assertNotNull(result);
    assertEquals(0, pos.getIndex());
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
    assertTrue(instance1.equals(instance2));
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
    assertEquals(buf, result);
  }
}