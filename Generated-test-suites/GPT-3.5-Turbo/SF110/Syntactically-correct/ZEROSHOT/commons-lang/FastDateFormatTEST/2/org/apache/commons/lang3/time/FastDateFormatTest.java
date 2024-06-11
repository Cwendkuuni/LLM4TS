import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {
  
  @Test
  public void testGetInstance() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    assertNotNull(fastDateFormat);
  }
  
  @Test
  public void testGetInstanceWithStringPattern() {
    String pattern = "dd-MM-yyyy";
    FastDateFormat fastDateFormat = FastDateFormat.getInstance(pattern);
    assertNotNull(fastDateFormat);
    assertEquals(pattern, fastDateFormat.getPattern());
  }
  
  @Test
  public void testGetInstanceWithStringPatternAndTimeZone() {
    String pattern = "dd-MM-yyyy";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat fastDateFormat = FastDateFormat.getInstance(pattern, timeZone);
    assertNotNull(fastDateFormat);
    assertEquals(pattern, fastDateFormat.getPattern());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
  }
  
  @Test
  public void testGetInstanceWithStringPatternAndLocale() {
    String pattern = "dd-MM-yyyy";
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getInstance(pattern, locale);
    assertNotNull(fastDateFormat);
    assertEquals(pattern, fastDateFormat.getPattern());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetInstanceWithStringPatternAndTimeZoneAndLocale() {
    String pattern = "dd-MM-yyyy";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getInstance(pattern, timeZone, locale);
    assertNotNull(fastDateFormat);
    assertEquals(pattern, fastDateFormat.getPattern());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetDateInstance() {
    int style = FastDateFormat.SHORT;
    FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(style);
    assertNotNull(fastDateFormat);
  }
  
  @Test
  public void testGetDateInstanceWithLocale() {
    int style = FastDateFormat.SHORT;
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(style, locale);
    assertNotNull(fastDateFormat);
    assertEquals(style, fastDateFormat.getDateFormat().getDateStyle());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetDateInstanceWithTimeZone() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(style, timeZone);
    assertNotNull(fastDateFormat);
    assertEquals(style, fastDateFormat.getDateFormat().getDateStyle());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
  }
  
  @Test
  public void testGetDateInstanceWithTimeZoneAndLocale() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(style, timeZone, locale);
    assertNotNull(fastDateFormat);
    assertEquals(style, fastDateFormat.getDateFormat().getDateStyle());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetTimeInstance() {
    int style = FastDateFormat.SHORT;
    FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(style);
    assertNotNull(fastDateFormat);
  }
  
  @Test
  public void testGetTimeInstanceWithLocale() {
    int style = FastDateFormat.SHORT;
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(style, locale);
    assertNotNull(fastDateFormat);
    assertEquals(style, fastDateFormat.getDateFormat().getTimeStyle());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZone() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(style, timeZone);
    assertNotNull(fastDateFormat);
    assertEquals(style, fastDateFormat.getDateFormat().getTimeStyle());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZoneAndLocale() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(style, timeZone, locale);
    assertNotNull(fastDateFormat);
    assertEquals(style, fastDateFormat.getDateFormat().getTimeStyle());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetDateTimeInstance() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.MEDIUM;
    FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
    assertNotNull(fastDateFormat);
  }
  
  @Test
  public void testGetDateTimeInstanceWithLocale() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.MEDIUM;
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    assertNotNull(fastDateFormat);
    assertEquals(dateStyle, fastDateFormat.getDateFormat().getDateStyle());
    assertEquals(timeStyle, fastDateFormat.getDateFormat().getTimeStyle());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZone() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.MEDIUM;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
    assertNotNull(fastDateFormat);
    assertEquals(dateStyle, fastDateFormat.getDateFormat().getDateStyle());
    assertEquals(timeStyle, fastDateFormat.getDateFormat().getTimeStyle());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.MEDIUM;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.ENGLISH;
    FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    assertNotNull(fastDateFormat);
    assertEquals(dateStyle, fastDateFormat.getDateFormat().getDateStyle());
    assertEquals(timeStyle, fastDateFormat.getDateFormat().getTimeStyle());
    assertEquals(timeZone, fastDateFormat.getTimeZone());
    assertEquals(locale, fastDateFormat.getLocale());
  }
  
  @Test
  public void testFormatObjectStringBufferFieldPosition() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Object obj = new Object();
    StringBuffer toAppendTo = new StringBuffer();
    FieldPosition pos = new FieldPosition(0);
    StringBuffer result = fastDateFormat.format(obj, toAppendTo, pos);
    assertEquals(toAppendTo, result);
  }
  
  @Test
  public void testFormatWithMillis() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    long millis = System.currentTimeMillis();
    String result = fastDateFormat.format(millis);
    assertNotNull(result);
  }
  
  @Test
  public void testFormatWithDate() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Date date = new Date();
    String result = fastDateFormat.format(date);
    assertNotNull(result);
  }
  
  @Test
  public void testFormatWithCalendar() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    String result = fastDateFormat.format(calendar);
    assertNotNull(result);
  }
  
  @Test
  public void testFormatWithMillisAndBuffer() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    long millis = System.currentTimeMillis();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = fastDateFormat.format(millis, buf);
    assertEquals(buf, result);
  }
  
  @Test
  public void testFormatWithDateAndBuffer() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Date date = new Date();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = fastDateFormat.format(date, buf);
    assertEquals(buf, result);
  }
  
  @Test
  public void testFormatWithCalendarAndBuffer() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = fastDateFormat.format(calendar, buf);
    assertEquals(buf, result);
  }
  
  @Test(expected = ParseException.class)
  public void testParseException() throws ParseException {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    String source = "invalid";
    fastDateFormat.parse(source);
  }
  
  @Test
  public void testParseWithParsePosition() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    String source = "valid";
    ParsePosition pos = new ParsePosition(0);
    Date result = fastDateFormat.parse(source, pos);
    assertNotNull(result);
  }
  
  @Test
  public void testParseObjectWithParsePosition() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    String source = "valid";
    ParsePosition pos = new ParsePosition(0);
    Object result = fastDateFormat.parseObject(source, pos);
    assertNotNull(result);
  }
  
  @Test
  public void testGetPattern() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    String pattern = fastDateFormat.getPattern();
    assertNotNull(pattern);
  }
  
  @Test
  public void testGetTimeZone() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    TimeZone timeZone = fastDateFormat.getTimeZone();
    assertNotNull(timeZone);
  }
  
  @Test
  public void testGetLocale() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Locale locale = fastDateFormat.getLocale();
    assertNotNull(locale);
  }
  
  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
    assertNotNull(maxLengthEstimate);
  }
  
  @Test
  public void testEquals() {
    FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
    FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
    boolean result = fastDateFormat1.equals(fastDateFormat2);
    assertTrue(result);
  }
  
  @Test
  public void testHashCode() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    int hashCode = fastDateFormat.hashCode();
    assertNotNull(hashCode);
  }
  
  @Test
  public void testToString() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    String toString = fastDateFormat.toString();
    assertNotNull(toString);
  }
  
  @Test
  public void testApplyRules() {
    FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = fastDateFormat.applyRules(calendar, buf);
    assertEquals(buf, result);
  }
}