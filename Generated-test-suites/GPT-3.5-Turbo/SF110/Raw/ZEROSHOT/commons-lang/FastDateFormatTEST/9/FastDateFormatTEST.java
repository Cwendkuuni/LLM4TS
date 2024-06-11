import org.junit.Assert;
import org.junit.Test;

public class FastDateFormatTest {

  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetInstanceWithPattern() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetInstanceWithPatternAndTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT+0"));
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetInstanceWithPatternAndLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.getDefault());
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetInstanceWithPatternTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT+0"), Locale.getDefault());
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetDateInstance() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
    Assert.assertNotNull(instance);
  }
  
  // Additional test cases for other getDateInstance methods
  @Test
  public void testGetDateInstanceWithLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.getDefault());
    Assert.assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceWithTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+0"));
    Assert.assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceWithTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+0"), Locale.getDefault());
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetTimeInstance() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
    Assert.assertNotNull(instance);
  }
  
  // Additional test cases for other getTimeInstance methods
  @Test
  public void testGetTimeInstanceWithLocale() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.getDefault());
    Assert.assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZone() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+0"));
    Assert.assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceWithTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+0"), Locale.getDefault());
    Assert.assertNotNull(instance);
  }

  @Test
  public void testGetDateTimeInstance() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
    Assert.assertNotNull(instance);
  }
  
  // Additional test cases for other getDateTimeInstance methods
  @Test
  public void testGetDateTimeInstanceWithLocale() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.getDefault());
    Assert.assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT+0"));
    Assert.assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT+0"), Locale.getDefault());
    Assert.assertNotNull(instance);
  }

  // Additional test cases for format methods
  @Test
  public void testFormatWithObject() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    StringBuffer buf = new StringBuffer();
    instance.format(new Date(), buf, null);
    Assert.assertNotEquals("", buf.toString());
  }

  @Test
  public void testFormatWithMillis() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    String formattedDate = instance.format(System.currentTimeMillis());
    Assert.assertNotEquals("", formattedDate);
  }

  @Test
  public void testFormatWithDate() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    String formattedDate = instance.format(new Date());
    Assert.assertNotEquals("", formattedDate);
  }

  @Test
  public void testFormatWithCalendar() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    String formattedDate = instance.format(Calendar.getInstance());
    Assert.assertNotEquals("", formattedDate);
  }

  @Test
  public void testFormatWithMillisAndBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    StringBuffer buf = new StringBuffer();
    instance.format(System.currentTimeMillis(), buf);
    Assert.assertNotEquals("", buf.toString());
  }

  @Test
  public void testFormatWithDateAndBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    StringBuffer buf = new StringBuffer();
    instance.format(new Date(), buf);
    Assert.assertNotEquals("", buf.toString());
  }

  @Test
  public void testFormatWithCalendarAndBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    StringBuffer buf = new StringBuffer();
    instance.format(Calendar.getInstance(), buf);
    Assert.assertNotEquals("", buf.toString());
  }

  // Additional test cases for parse methods
  @Test
  public void testParse() throws ParseException {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    Date date = instance.parse("2021-01-01");
    Assert.assertNotNull(date);
  }

  @Test
  public void testParseWithParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    ParsePosition pos = new ParsePosition(0);
    Date date = instance.parse("2021-01-01", pos);
    Assert.assertNotNull(date);
    Assert.assertEquals(10, pos.getIndex());
  }

  @Test
  public void testParseObject() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    ParsePosition pos = new ParsePosition(0);
    Object result = instance.parseObject("2021-01-01", pos);
    Assert.assertNotNull(result);
    Assert.assertEquals(10, pos.getIndex());
  }

  // Additional test cases for getter methods
  @Test
  public void testGetPattern() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    String pattern = instance.getPattern();
    Assert.assertEquals("yyyy-MM-dd", pattern);
  }

  @Test
  public void testGetTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    TimeZone timeZone = instance.getTimeZone();
    Assert.assertEquals(TimeZone.getDefault(), timeZone);
  }

  @Test
  public void testGetLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    Locale locale = instance.getLocale();
    Assert.assertEquals(Locale.getDefault(), locale);
  }

  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    int maxLengthEstimate = instance.getMaxLengthEstimate();
    Assert.assertTrue(maxLengthEstimate > 0);
  }

  @Test
  public void testEquals() {
    FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
    FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
    Assert.assertEquals(instance1, instance2);
  }

  @Test
  public void testHashCode() {
    FastDateFormat instance1 = FastDateFormat.getInstance("yyyy-MM-dd");
    FastDateFormat instance2 = FastDateFormat.getInstance("yyyy-MM-dd");
    Assert.assertEquals(instance1.hashCode(), instance2.hashCode());
  }

  @Test
  public void testToString() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    String str = instance.toString();
    Assert.assertEquals("FastDateFormat[yyyy-MM-dd," + Locale.getDefault() + "," + TimeZone.getDefault().getID() + "]", str);
  }
}