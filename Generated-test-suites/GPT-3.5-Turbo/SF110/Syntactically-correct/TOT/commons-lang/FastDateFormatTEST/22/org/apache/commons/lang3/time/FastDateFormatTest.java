import org.junit.*;
import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Assert.assertNotNull(instance);
    // Add more test cases here
  }

  @Test
  public void testGetInstanceWithPattern() {
    String pattern = "yyyy-MM-dd";
    FastDateFormat instance = FastDateFormat.getInstance(pattern);
    Assert.assertEquals(pattern, instance.getPattern());
    // Add more test cases here
  }

  @Test
  public void testGetInstanceWithPatternAndTimeZone() {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
    Assert.assertEquals(pattern, instance.getPattern());
    Assert.assertEquals(timeZone, instance.getTimeZone());
    // Add more test cases here
  }

  @Test
  public void testGetInstanceWithPatternAndLocale() {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
    Assert.assertEquals(pattern, instance.getPattern());
    Assert.assertEquals(locale, instance.getLocale());
    // Add more test cases here
  }

  @Test
  public void testGetInstanceWithPatternTimeZoneAndLocale() {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
    Assert.assertEquals(pattern, instance.getPattern());
    Assert.assertEquals(timeZone, instance.getTimeZone());
    Assert.assertEquals(locale, instance.getLocale());
    // Add more test cases here
  }

  // Add more test cases for other methods

}