import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

  @Test
  public void testGetInstance_noArgs_returnsInstanceFromCache() {
    FastDateFormat instance1 = FastDateFormat.getInstance();
    FastDateFormat instance2 = FastDateFormat.getInstance();
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetInstance_String_returnsInstanceFromCache() {
    FastDateFormat instance1 = FastDateFormat.getInstance("yyyy/MM/dd");
    FastDateFormat instance2 = FastDateFormat.getInstance("yyyy/MM/dd");
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetInstance_StringTimeZoneLocale_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getInstance("yyyy/MM/dd", timeZone, locale);
    FastDateFormat instance2 = FastDateFormat.getInstance("yyyy/MM/dd", timeZone, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetInstance_StringTimeZone_returnsDifferentInstance() {
    TimeZone timeZone1 = TimeZone.getTimeZone("GMT");
    TimeZone timeZone2 = TimeZone.getTimeZone("BST");
    
    FastDateFormat instance1 = FastDateFormat.getInstance("yyyy/MM/dd", timeZone1);
    FastDateFormat instance2 = FastDateFormat.getInstance("yyyy/MM/dd", timeZone2);
    
    assertNotSame(instance1, instance2);
  }
  
  @Test
  public void testGetInstance_StringLocale_returnsDifferentInstance() {
    Locale locale1 = Locale.US;
    Locale locale2 = Locale.UK;
    
    FastDateFormat instance1 = FastDateFormat.getInstance("yyyy/MM/dd", locale1);
    FastDateFormat instance2 = FastDateFormat.getInstance("yyyy/MM/dd", locale2);
    
    assertNotSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateInstance_style_returnsInstanceFromCache() {
    FastDateFormat instance1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
    FastDateFormat instance2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateInstance_styleLocale_returnsInstanceFromCache() {
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
    FastDateFormat instance2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateInstance_styleTimeZone_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    
    FastDateFormat instance1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
    FastDateFormat instance2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateInstance_styleTimeZoneLocale_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
    FastDateFormat instance2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetTimeInstance_style_returnsInstanceFromCache() {
    FastDateFormat instance1 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
    FastDateFormat instance2 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetTimeInstance_styleLocale_returnsInstanceFromCache() {
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
    FastDateFormat instance2 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetTimeInstance_styleTimeZone_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    
    FastDateFormat instance1 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
    FastDateFormat instance2 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetTimeInstance_styleTimeZoneLocale_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
    FastDateFormat instance2 = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateTimeInstance_dateStyleTimeStyle_returnsInstanceFromCache() {
    FastDateFormat instance1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
    FastDateFormat instance2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateTimeInstance_dateStyleTimeStyleLocale_returnsInstanceFromCache() {
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
    FastDateFormat instance2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateTimeInstance_dateStyleTimeStyleTimeZone_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    
    FastDateFormat instance1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
    FastDateFormat instance2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testGetDateTimeInstance_dateStyleTimeStyleTimeZoneLocale_returnsInstanceFromCache() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    
    FastDateFormat instance1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
    FastDateFormat instance2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
    
    assertSame(instance1, instance2);
  }
  
  @Test
  public void testFormat_ObjectStringBufferFieldPosition() {
    Object obj = new Date();
    StringBuffer buffer = new StringBuffer();
    FieldPosition position = new FieldPosition(0);
    
    FastDateFormat format = FastDateFormat.getInstance();
    StringBuffer result = format.format(obj, buffer, position);
    
    assertNotNull(result);
    assertEquals(buffer, result);
  }
  
  @Test
  public void testFormat_long_returnsFormattedDateString() {
    long millis = System.currentTimeMillis();
    
    FastDateFormat format = FastDateFormat.getInstance();
    String result = format.format(millis);
    
    assertNotNull(result);
  }
  
  @Test
  public void testFormat_Date_returnsFormattedDateString() {
    Date date = new Date();
    
    FastDateFormat format = FastDateFormat.getInstance();
    String result = format.format(date);
    
    assertNotNull(result);
  }
  
  @Test
  public void testFormat_Calendar_returnsFormattedDateString() {
    Calendar calendar = Calendar.getInstance();
    
    FastDateFormat format = FastDateFormat.getInstance();
    String result = format.format(calendar);
    
    assertNotNull(result);
  }
  
  @Test
  public void testFormat_longStringBuffer_returnsFormattedDateString() {
    long millis = System.currentTimeMillis();
    StringBuffer buffer = new StringBuffer();
    
    FastDateFormat format = FastDateFormat.getInstance();
    StringBuffer result = format.format(millis, buffer);
    
    assertNotNull(result);
    assertEquals(buffer, result);
  }
  
  @Test
  public void testFormat_DateStringBuffer_returnsFormattedDateString() {
    Date date = new Date();
    StringBuffer buffer = new StringBuffer();
    
    FastDateFormat format = FastDateFormat.getInstance();
    StringBuffer result = format.format(date, buffer);
    
    assertNotNull(result);
    assertEquals(buffer, result);
  }
  
  @Test
  public void testFormat_CalendarStringBuffer_returnsFormattedDateString() {
    Calendar calendar = Calendar.getInstance();
    StringBuffer buffer = new StringBuffer();
    
    FastDateFormat format = FastDateFormat.getInstance();
    StringBuffer result = format.format(calendar, buffer);
    
    assertNotNull(result);
    assertEquals(buffer, result);
  }
  
  @Test
  public void testParse_String_returnsParsedDate() throws ParseException {
    String source = "2019/12/31";
    
    FastDateFormat format = FastDateFormat.getInstance();
    Date result = format.parse(source);
    
    assertNotNull(result);
  }
  
  @Test
  public void testParse_StringParsePosition_returnsParsedDate() {
    String source = "2019/12/31";
    ParsePosition position = new ParsePosition(0);
    
    FastDateFormat format = FastDateFormat.getInstance();
    Date result = format.parse(source, position);
    
    assertNotNull(result);
  }
  
  @Test
  public void testParseObject_StringParsePosition_returnsParsedObject() {
    String source = "2019/12/31";
    ParsePosition position = new ParsePosition(0);
    
    FastDateFormat format = FastDateFormat.getInstance();
    Object result = format.parseObject(source, position);
    
    assertNotNull(result);
    assertTrue(result instanceof Date);
  }
  
  @Test
  public void testGetPattern_returnsPatternString() {
    FastDateFormat format = FastDateFormat.getInstance("yyyy/MM/dd");
    String pattern = format.getPattern();
    
    assertEquals("yyyy/MM/dd", pattern);
  }
  
  @Test
  public void testGetTimeZone_returnsTimeZone() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    
    FastDateFormat format = FastDateFormat.getInstance("yyyy/MM/dd", timeZone);
    TimeZone result = format.getTimeZone();
    
    assertEquals(timeZone, result);
  }
  
  @Test
  public void testGetLocale_returnsLocale() {
    Locale locale = Locale.US;
    
    FastDateFormat format = FastDateFormat.getInstance("yyyy/MM/dd", locale);
    Locale result = format.getLocale();
    
    assertEquals(locale, result);
  }
  
  @Test
  public void testGetMaxLengthEstimate_returnsEstimatedMaxLength() {
    FastDateFormat format = FastDateFormat.getInstance();
    int maxLength = format.getMaxLengthEstimate();
    
    assertTrue(maxLength > 0);
  }
  
  @Test
  public void testEquals_sameFormat_returnsTrue() {
    FastDateFormat format1 = FastDateFormat.getInstance("yyyy/MM/dd");
    FastDateFormat format2 = FastDateFormat.getInstance("yyyy/MM/dd");
    
    assertTrue(format1.equals(format2));
  }
  
  @Test
  public void testEquals_differentFormat_returnsFalse() {
    FastDateFormat format1 = FastDateFormat.getInstance("yyyy/MM/dd");
    FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
    
    assertFalse(format1.equals(format2));
  }
  
  @Test
  public void testHashCode_sameFormat_returnsSameHashCode() {
    FastDateFormat format1 = FastDateFormat.getInstance("yyyy/MM/dd");
    FastDateFormat format2 = FastDateFormat.getInstance("yyyy/MM/dd");
    
    assertEquals(format1.hashCode(), format2.hashCode());
  }
  
  @Test
  public void testHashCode_differentFormat_returnsDifferentHashCode() {
    FastDateFormat format1 = FastDateFormat.getInstance("yyyy/MM/dd");
    FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
    
    assertNotEquals(format1.hashCode(), format2.hashCode());
  }
  
  @Test
  public void testToString_returnsFormattedString() {
    FastDateFormat format = FastDateFormat.getInstance("yyyy/MM/dd");
    String result = format.toString();
    
    assertNotNull(result);
    assertTrue(result.contains("yyyy/MM/dd"));
  }
  
  @Test
  public void testApplyRules_CalendarStringBuffer_returnsFormattedString() {
    Calendar calendar = Calendar.getInstance();
    StringBuffer buffer = new StringBuffer();
    
    FastDateFormat format = FastDateFormat.getInstance();
    StringBuffer result = format.applyRules(calendar, buffer);
    
    assertNotNull(result);
    assertEquals(buffer, result);
  }
}