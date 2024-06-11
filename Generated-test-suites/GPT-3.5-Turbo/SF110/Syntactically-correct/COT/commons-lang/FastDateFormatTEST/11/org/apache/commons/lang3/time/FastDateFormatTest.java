import static org.junit.Assert.*;
import org.junit.Test;

public class FastDateFormatTest {

  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotNull(instance);
  }

  @Test
  public void testGetInstance_withPattern() {
    String pattern = "yyyy-MM-dd";
    FastDateFormat instance = FastDateFormat.getInstance(pattern);
    assertNotNull(instance);
  }

  @Test
  public void testGetInstance_withPatternAndTimeZone() {
    String pattern = "yyyy-MM-dd";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
    assertNotNull(instance);
  }

  @Test
  public void testGetInstance_withPatternAndLocale() {
    String pattern = "yyyy-MM-dd";
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetInstance_withPatternAndTimeZoneAndLocale() {
    String pattern = "yyyy-MM-dd";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateInstance() {
    int style = FastDateFormat.FULL;
    FastDateFormat instance = FastDateFormat.getDateInstance(style);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateInstance_withLocale() {
    int style = FastDateFormat.FULL;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateInstance_withTimeZone() {
    int style = FastDateFormat.FULL;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateInstance_withTimeZoneAndLocale() {
    int style = FastDateFormat.FULL;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetTimeInstance() {
    int style = FastDateFormat.FULL;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style);
    assertNotNull(instance);
  }

  @Test
  public void testGetTimeInstance_withLocale() {
    int style = FastDateFormat.FULL;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetTimeInstance_withTimeZone() {
    int style = FastDateFormat.FULL;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
    assertNotNull(instance);
  }

  @Test
  public void testGetTimeInstance_withTimeZoneAndLocale() {
    int style = FastDateFormat.FULL;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateTimeInstance() {
    int dateStyle = FastDateFormat.FULL;
    int timeStyle = FastDateFormat.FULL;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateTimeInstance_withLocale() {
    int dateStyle = FastDateFormat.FULL;
    int timeStyle = FastDateFormat.FULL;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateTimeInstance_withTimeZone() {
    int dateStyle = FastDateFormat.FULL;
    int timeStyle = FastDateFormat.FULL;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
    assertNotNull(instance);
  }

  @Test
  public void testGetDateTimeInstance_withTimeZoneAndLocale() {
    int dateStyle = FastDateFormat.FULL;
    int timeStyle = FastDateFormat.FULL;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    assertNotNull(instance);
  }

  @Test
  public void testFormat_Object_StringBuffer_FieldPosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer toAppendTo = new StringBuffer();
    FieldPosition pos = new FieldPosition(0);
    StringBuffer result = instance.format(null, toAppendTo, pos);
    assertEquals(result, toAppendTo);
  }

  @Test
  public void testFormat_long() {
    FastDateFormat instance = FastDateFormat.getInstance();
    long millis = System.currentTimeMillis();
    String result = instance.format(millis);
    assertNotNull(result);
  }

  @Test
  public void testFormat_Date() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Date date = new Date();
    String result = instance.format(date);
    assertNotNull(result);
  }

  @Test
  public void testFormat_Calendar() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    String result = instance.format(calendar);
    assertNotNull(result);
  }

  @Test
  public void testFormat_long_StringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    long millis = System.currentTimeMillis();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(millis, buf);
    assertEquals(result, buf);
  }

  @Test
  public void testFormat_Date_StringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Date date = new Date();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(date, buf);
    assertEquals(result, buf);
  }

  @Test
  public void testFormat_Calendar_StringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(calendar, buf);
    assertEquals(result, buf);
  }

  @Test
  public void testParse() throws ParseException {
    FastDateFormat instance = FastDateFormat.getInstance();
    String source = "2020-01-01";
    Date result = instance.parse(source);
    assertNotNull(result);
  }

  @Test
  public void testParse_withParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String source = "2020-01-01";
    ParsePosition pos = new ParsePosition(0);
    Date result = instance.parse(source, pos);
    assertNotNull(result);
  }

  @Test
  public void testParseObject_withParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String source = "2020-01-01";
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
    assertEquals(result, buf);
  }

}