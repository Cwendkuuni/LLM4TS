import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceString() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceStringTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceStringLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceStringTimeZoneLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceInt() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceIntLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceIntTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateInstanceIntTimeZoneLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceInt() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceIntLocale() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceIntTimeZone() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testGetTimeInstanceIntTimeZoneLocale() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstance() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstanceLocale() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstanceTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testGetDateTimeInstanceTimeZoneLocale() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testFormatObjectStringBufferFieldPosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buffer = new StringBuffer();
    FieldPosition position = new FieldPosition(0);
    instance.format(new Date(), buffer, position);
    assertNotNull(buffer);
  }
  
  @Test
  public void testFormatLong() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(System.currentTimeMillis());
    assertNotNull(result);
  }
  
  @Test
  public void testFormatDate() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(new Date());
    assertNotNull(result);
  }
  
  @Test
  public void testFormatCalendar() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(Calendar.getInstance());
    assertNotNull(result);
  }
  
  @Test
  public void testFormatLongStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buffer = new StringBuffer();
    instance.format(System.currentTimeMillis(), buffer);
    assertNotNull(buffer);
  }
  
  @Test
  public void testFormatDateStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buffer = new StringBuffer();
    instance.format(new Date(), buffer);
    assertNotNull(buffer);
  }
  
  @Test
  public void testFormatCalendarStringBuffer() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buffer = new StringBuffer();
    instance.format(Calendar.getInstance(), buffer);
    assertNotNull(buffer);
  }
  
  @Test
  public void testParseString() throws ParseException {
    FastDateFormat instance = FastDateFormat.getInstance();
    Date result = instance.parse("2022-01-01");
    assertNotNull(result);
  }
  
  @Test
  public void testParseStringParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    ParsePosition position = new ParsePosition(0);
    Date result = instance.parse("2022-01-01", position);
    assertNotNull(result);
  }
  
  @Test
  public void testParseObjectStringParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    ParsePosition position = new ParsePosition(0);
    Object result = instance.parseObject("2022-01-01", position);
    assertNotNull(result);
  }
  
  @Test
  public void testGetPattern() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String pattern = instance.getPattern();
    assertNotNull(pattern);
  }
  
  @Test
  public void testGetTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance();
    TimeZone timeZone = instance.getTimeZone();
    assertNotNull(timeZone);
  }
  
  @Test
  public void testGetLocale() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Locale locale = instance.getLocale();
    assertNotNull(locale);
  }
  
  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat instance = FastDateFormat.getInstance();
    int estimate = instance.getMaxLengthEstimate();
    assertTrue(estimate >= 0);
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
    int hashCode = instance.hashCode();
    assertNotNull(hashCode);
  }
  
  @Test
  public void testToString() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String str = instance.toString();
    assertNotNull(str);
  }
  
  @Test
  public void testApplyRules() {
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer buffer = new StringBuffer();
    instance.applyRules(Calendar.getInstance(), buffer);
    assertNotNull(buffer);
  }
}