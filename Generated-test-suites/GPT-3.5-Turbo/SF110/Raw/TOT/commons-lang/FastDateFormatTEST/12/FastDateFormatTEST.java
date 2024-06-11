import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FastDateFormatTest {
  
  @Test
  public void testGetInstance() {
    FastDateFormat format = FastDateFormat.getInstance();
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetInstanceWithPattern() {
    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetInstanceWithPatternAndTimeZone() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetInstanceWithPatternAndLocale() {
    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetInstanceWithPatternTimeZoneAndLocale() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, Locale.US);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetDateInstance() {
    FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetDateInstanceWithLocale() {
    FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetDateInstanceWithTimeZone() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetDateInstanceWithTimeZoneAndLocale() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, Locale.US);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetTimeInstance() {
    FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetTimeInstanceWithLocale() {
    FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZone() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZoneAndLocale() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, Locale.US);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetDateTimeInstance() {
    FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetDateTimeInstanceWithLocale() {
    FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, Locale.US);
    
    assertNotNull(format);
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZone() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, timeZone);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, timeZone, Locale.US);
    
    assertNotNull(format);
    assertEquals(timeZone, format.getTimeZone());
  }
  
  @Test
  public void testFormatForObject() {
    FastDateFormat format = FastDateFormat.getInstance();
    String result = format.format(123);
    
    assertNotNull(result);
  }
  
  @Test
  public void testFormatForMillis() {
    FastDateFormat format = FastDateFormat.getInstance();
    String result = format.format(1616601600000L); // 2021-03-25 00:00:00
    
    assertNotNull(result);
    assertEquals("2021-03-25 00:00:00", result);
  }
  
  @Test
  public void testFormatForDate() {
    FastDateFormat format = FastDateFormat.getInstance();
    Date date = new Date(1616601600000L); // 2021-03-25 00:00:00
    String result = format.format(date);
    
    assertNotNull(result);
    assertEquals("2021-03-25 00:00:00", result);
  }
  
  @Test
  public void testFormatForCalendar() {
    FastDateFormat format = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2021, 2, 25, 0, 0, 0); // 2021-03-25 00:00:00
    String result = format.format(calendar);
    
    assertNotNull(result);
    assertEquals("2021-03-25 00:00:00", result);
  }
  
  @Test
  public void testFormatForMillisAndBuffer() {
    FastDateFormat format = FastDateFormat.getInstance();
    StringBuffer buffer = new StringBuffer();
    StringBuffer result = format.format(1616601600000L, buffer); // 2021-03-25 00:00:00
    
    assertNotNull(result);
    assertEquals("2021-03-25 00:00:00", result.toString());
  }
  
  @Test
  public void testFormatForDateAndBuffer() {
    FastDateFormat format = FastDateFormat.getInstance();
    Date date = new Date(1616601600000L); // 2021-03-25 00:00:00
    StringBuffer buffer = new StringBuffer();
    StringBuffer result = format.format(date, buffer);
    
    assertNotNull(result);
    assertEquals("2021-03-25 00:00:00", result.toString());
  }
  
  @Test
  public void testFormatForCalendarAndBuffer() {
    FastDateFormat format = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2021, 2, 25, 0, 0, 0); // 2021-03-25 00:00:00
    StringBuffer buffer = new StringBuffer();
    StringBuffer result = format.format(calendar, buffer);
    
    assertNotNull(result);
    assertEquals("2021-03-25 00:00:00", result.toString());
  }
  
  @Test
  public void testParse() throws ParseException {
    FastDateFormat format = FastDateFormat.getInstance();
    Date result = format.parse("2021-03-25 00:00:00");
    
    assertNotNull(result);
  }
  
  @Test
  public void testParseWithParsePosition() {
    FastDateFormat format = FastDateFormat.getInstance();
    ParsePosition position = new ParsePosition(0);
    Date result = format.parse("2021-03-25 00:00:00", position);
    
    assertNotNull(result);
  }
  
  @Test
  public void testParseObject() {
    FastDateFormat format = FastDateFormat.getInstance();
    ParsePosition position = new ParsePosition(0);
    Object result = format.parseObject("2021-03-25 00:00:00", position);
    
    assertNotNull(result);
  }
  
  @Test
  public void testGetPattern() {
    FastDateFormat format = FastDateFormat.getInstance();
    String pattern = format.getPattern();
    
    assertNotNull(pattern);
    assertEquals("yyyy-MM-dd HH:mm:ss", pattern);
  }
  
  @Test
  public void testGetTimeZone() {
    FastDateFormat format = FastDateFormat.getInstance();
    TimeZone timeZone = format.getTimeZone();
    
    assertNotNull(timeZone);
  }
  
  @Test
  public void testGetLocale() {
    FastDateFormat format = FastDateFormat.getInstance();
    Locale locale = format.getLocale();
    
    assertNotNull(locale);
  }
  
  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat format = FastDateFormat.getInstance();
    int maxLengthEstimate = format.getMaxLengthEstimate();
    
    assertTrue(maxLengthEstimate > 0);
  }
  
  @Test
  public void testEquals() {
    FastDateFormat format1 = FastDateFormat.getInstance();
    FastDateFormat format2 = FastDateFormat.getInstance();
    
    assertEquals(format1, format2);
  }
  
  @Test
  public void testHashCode() {
    FastDateFormat format = FastDateFormat.getInstance();
    int hashCode = format.hashCode();
    
    assertTrue(hashCode > 0);
  }
  
  @Test
  public void testToString() {
    FastDateFormat format = FastDateFormat.getInstance();
    String result = format.toString();
    
    assertNotNull(result);
    assertTrue(result.contains("FastDateFormat["));
  }
  
  @Test
  public void testApplyRules() {
    FastDateFormat format = FastDateFormat.getInstance();
    Calendar calendar = Calendar.getInstance();
    StringBuffer buffer = new StringBuffer();
    StringBuffer result = format.applyRules(calendar, buffer);
    
    assertNotNull(result);
  }
  
}