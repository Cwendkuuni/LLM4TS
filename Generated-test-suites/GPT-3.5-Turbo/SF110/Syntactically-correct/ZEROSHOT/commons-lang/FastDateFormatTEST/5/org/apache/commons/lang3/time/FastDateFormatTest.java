import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {
  
  private FastDateFormat format;
  
  @Before
  public void setup() {
    format = new FastDateFormat("dd-MM-yyyy", TimeZone.getDefault(), Locale.getDefault());
  }
  
  @Test
  public void testFormatObject() {
    StringBuffer expected = new StringBuffer("10-09-2023");
    StringBuffer actual = format.format(new Date(1234567890L), new StringBuffer(), null);
    assertEquals(expected.toString(), actual.toString());
  }
  
  @Test
  public void testFormatLong() {
    String expected = "10-09-2023";
    String actual = format.format(1234567890L);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDate() {
    String expected = "10-09-2023";
    String actual = format.format(new Date(1234567890L));
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatCalendar() {
    String expected = "10-09-2023";
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(1234567890L);
    String actual = format.format(calendar);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatLongStringBuffer() {
    StringBuffer expected = new StringBuffer("10-09-2023");
    StringBuffer actual = format.format(1234567890L, new StringBuffer());
    assertEquals(expected.toString(), actual.toString());
  }
  
  @Test
  public void testFormatDateStringBuffer() {
    StringBuffer expected = new StringBuffer("10-09-2023");
    StringBuffer actual = format.format(new Date(1234567890L), new StringBuffer());
    assertEquals(expected.toString(), actual.toString());
  }
  
  @Test
  public void testFormatCalendarStringBuffer() {
    StringBuffer expected = new StringBuffer("10-09-2023");
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(1234567890L);
    StringBuffer actual = format.format(calendar, new StringBuffer());
    assertEquals(expected.toString(), actual.toString());
  }
  
  @Test
  public void testParse() throws ParseException {
    String source = "10-09-2023";
    Date expected = new Date(1234567890L);
    Date actual = format.parse(source);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseWithParsePosition() {
    String source = "10-09-2023";
    Date expected = new Date(1234567890L);
    ParsePosition position = new ParsePosition(0);
    Date actual = format.parse(source, position);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseObject() {
    String source = "10-09-2023";
    Date expected = new Date(1234567890L);
    ParsePosition position = new ParsePosition(0);
    Object actual = format.parseObject(source, position);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetPattern() {
    String expected = "dd-MM-yyyy";
    String actual = format.getPattern();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetTimeZone() {
    TimeZone expected = TimeZone.getDefault();
    TimeZone actual = format.getTimeZone();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetLocale() {
    Locale expected = Locale.getDefault();
    Locale actual = format.getLocale();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetMaxLengthEstimate() {
    int expected = 10;
    int actual = format.getMaxLengthEstimate();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testEquals() {
    FastDateFormat other = new FastDateFormat("dd-MM-yyyy", TimeZone.getDefault(), Locale.getDefault());
    boolean actual = format.equals(other);
    assertTrue(actual);
  }
  
  @Test
  public void testHashCode() {
    FastDateFormat other = new FastDateFormat("dd-MM-yyyy", TimeZone.getDefault(), Locale.getDefault());
    int expected = format.hashCode();
    int actual = other.hashCode();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testToString() {
    String expected = "FastDateFormat[dd-MM-yyyy," + 
                      Locale.getDefault() + "," +
                      TimeZone.getDefault().getID() + "]";
    String actual = format.toString();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testApplyRules() {
    StringBuffer buf = new StringBuffer();
    Calendar calendar = Calendar.getInstance();
    StringBuffer expected = format.applyRules(calendar, buf);
    StringBuffer actual = format.applyRules(calendar, buf);
    assertEquals(expected, actual);
  }
}