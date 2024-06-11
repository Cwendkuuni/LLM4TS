import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetInstance_String() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetInstance_String_TimeZone() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetInstance_String_Locale() {
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetInstance_String_TimeZone_Locale() {
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateInstance() {
    int style = FastDateFormat.SHORT;
    FastDateFormat instance = FastDateFormat.getDateInstance(style);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateInstance_style_Locale() {
    int style = FastDateFormat.SHORT;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateInstance_style_TimeZone() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateInstance_style_TimeZone_Locale() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateInstance(style, timeZone, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetTimeInstance() {
    int style = FastDateFormat.SHORT;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetTimeInstance_style_Locale() {
    int style = FastDateFormat.SHORT;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetTimeInstance_style_TimeZone() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetTimeInstance_style_TimeZone_Locale() {
    int style = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateTimeInstance() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.SHORT;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateTimeInstance_dateStyle_timeStyle_Locale() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.SHORT;
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateTimeInstance_dateStyle_timeStyle_TimeZone() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testGetDateTimeInstance_dateStyle_timeStyle_TimeZone_Locale() {
    int dateStyle = FastDateFormat.SHORT;
    int timeStyle = FastDateFormat.SHORT;
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Locale locale = Locale.US;
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    assertEquals(FastDateFormat.class, instance.getClass());
  }

  @Test
  public void testFormat_Object_StringBuffer_FieldPosition() {
    Object obj = new Date();
    FieldPosition pos = new FieldPosition(0);
    StringBuffer buffer = new StringBuffer();
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer result = instance.format(obj, buffer, pos);
    assertEquals(buffer, result);
  }

  @Test
  public void testFormat_long() {
    long millis = 0L;
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(millis);
    assertEquals(String.class, result.getClass());
  }

  @Test
  public void testFormat_Date() {
    Date date = new Date();
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(date);
    assertEquals(String.class, result.getClass());
  }

  @Test
  public void testFormat_Calendar() {
    Calendar calendar = Calendar.getInstance();
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.format(calendar);
    assertEquals(String.class, result.getClass());
  }

  @Test
  public void testFormat_long_StringBuffer() {
    long millis = 0L;
    StringBuffer buf = new StringBuffer();
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer result = instance.format(millis, buf);
    assertEquals(buf, result);
  }

  @Test
  public void testFormat_Date_StringBuffer() {
    Date date = new Date();
    StringBuffer buf = new StringBuffer();
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer result = instance.format(date, buf);
    assertEquals(buf, result);
  }

  @Test
  public void testFormat_Calendar_StringBuffer() {
    Calendar calendar = Calendar.getInstance();
    StringBuffer buf = new StringBuffer();
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer result = instance.format(calendar, buf);
    assertEquals(buf, result);
  }

  @Test
  public void testParse_String() throws ParseException {
    String source = "2021-01-01";
    FastDateFormat instance = FastDateFormat.getInstance();
    Date result = instance.parse(source);
    assertEquals(Date.class, result.getClass());
  }

  @Test
  public void testParse_String_ParsePosition() {
    String source = "2021-01-01";
    ParsePosition pos = new ParsePosition(0);
    FastDateFormat instance = FastDateFormat.getInstance();
    Date result = instance.parse(source, pos);
    assertEquals(Date.class, result.getClass());
  }

  @Test
  public void testParseObject_String_ParsePosition() {
    String source = "2021-01-01";
    ParsePosition pos = new ParsePosition(0);
    FastDateFormat instance = FastDateFormat.getInstance();
    Object result = instance.parseObject(source, pos);
    assertEquals(Object.class, result.getClass());
  }

  @Test
  public void testGetPattern() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.getPattern();
    assertEquals(String.class, result.getClass());
  }

  @Test
  public void testGetTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance();
    TimeZone result = instance.getTimeZone();
    assertEquals(TimeZone.class, result.getClass());
  }

  @Test
  public void testGetLocale() {
    FastDateFormat instance = FastDateFormat.getInstance();
    Locale result = instance.getLocale();
    assertEquals(Locale.class, result.getClass());
  }

  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat instance = FastDateFormat.getInstance();
    int result = instance.getMaxLengthEstimate();
    assertEquals(int.class, result.getClass());
  }

  @Test
  public void testEquals() {
    Object obj = new FastDateFormat();
    FastDateFormat instance = FastDateFormat.getInstance();
    boolean result = instance.equals(obj);
    assertEquals(boolean.class, result.getClass());
  }

  @Test
  public void testHashCode() {
    FastDateFormat instance = FastDateFormat.getInstance();
    int result = instance.hashCode();
    assertEquals(int.class, result.getClass());
  }

  @Test
  public void testToString() {
    FastDateFormat instance = FastDateFormat.getInstance();
    String result = instance.toString();
    assertEquals(String.class, result.getClass());
  }

  @Test
  public void testApplyRules() {
    Calendar calendar = Calendar.getInstance();
    StringBuffer buf = new StringBuffer();
    FastDateFormat instance = FastDateFormat.getInstance();
    StringBuffer result = instance.applyRules(calendar, buf);
    assertEquals(buf, result);
  }
}