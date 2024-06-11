import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

public class FastDateFormatTest {
  
  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetInstanceWithStringPattern() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetInstanceWithStringPatternAndTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetInstanceWithStringPatternAndLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetInstanceWithStringPatternAndTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateInstanceWithStyle() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateInstanceWithStyleAndLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateInstanceWithStyleAndTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateInstanceWithStyleAndTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetTimeInstanceWithStyle() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetTimeInstanceWithStyleAndLocale() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetTimeInstanceWithStyleAndTimeZone() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetTimeInstanceWithStyleAndTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndLocale() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithDateStyleAndTimeStyleAndTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotEquals(null, instance);
  }
  
  @Test
  public void testFormatWithObjectAndStringBufferAndFieldPosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(new Object(), buf, null);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testFormatWithLong() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(1580457600000L);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testFormatWithDate() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(new Date());
    assertNotEquals(null, result);
  }
  
  @Test
  public void testFormatWithCalendar() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(Calendar.getInstance());
    assertNotEquals(null, result);
  }
  
  @Test
  public void testFormatWithLongAndStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(1580457600000L, buf);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testFormatWithDateAndStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(new Date(), buf);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testFormatWithCalendarAndStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buf = new StringBuffer();
    StringBuffer result = instance.format(Calendar.getInstance(), buf);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testParse() throws ParseException {
    FastDateFormat instance = FastDateFormat.getInstance();
    Date result = instance.parse("2020-02-01");
    assertNotEquals(null, result);
  }
  
  @Test
  public void testParseWithParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    ParsePosition pos = new ParsePosition(0);
    Date result = instance.parse("2020-02-01", pos);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testParseObjectWithParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    ParsePosition pos = new ParsePosition(0);
    Object result = instance.parseObject("2020-02-01", pos);
    assertNotEquals(null, result);
  }
  
  @Test
  public void testGetPattern() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String pattern = instance.getPattern();
    assertEquals("yyyy-MM-dd", pattern);
  }
  
  @Test
  public void testGetTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance();
    TimeZone timeZone = instance.getTimeZone();
    assertNotEquals(null, timeZone);
  }
  
  @Test
  public void testGetLocale() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Locale locale = instance.getLocale();
    assertNotEquals(null, locale);
  }
  
  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat instance = FastDateFormat.getInstance();
    int maxLength = instance.getMaxLengthEstimate();
    assertNotEquals(0, maxLength);
  }
  
  @Test
  public void testEquals() {
    FastDateFormat instance1 = FastDateFormat.getInstance();
    FastDateFormat instance2 = FastDateFormat.getInstance();
    assertEquals(true, instance1.equals(instance2));
  }
  
  @Test
  public void testHashCode() {
    FastDateFormat instance = FastDateFormat.getInstance();
    int hashCode = instance.hashCode();
    assertNotEquals(0, hashCode);
  }
  
  @Test
  public void testToString() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.toString();
    assertNotEquals(null, result);
  }
}