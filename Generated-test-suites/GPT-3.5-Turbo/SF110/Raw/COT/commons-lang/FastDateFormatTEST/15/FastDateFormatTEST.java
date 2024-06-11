import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {
  
  FastDateFormat fastDateFormat;

@Before
public void setUp() {
  fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
}

@Test
public void testFormatObject() {
  String result = fastDateFormat.format(new Date());
  assertNotNull(result);
}

@Test
public void testFormatLong() {
  String result = fastDateFormat.format(1234567890L);
  assertNotNull(result);
}

@Test
public void testFormatDate() {
  Date date = new Date();
  String result = fastDateFormat.format(date);
  assertNotNull(result);
}

@Test
public void testFormatCalendar() {
  Calendar calendar = Calendar.getInstance();
  String result = fastDateFormat.format(calendar);
  assertNotNull(result);
}

@Test
public void testFormatLongStringBuffer() {
  StringBuffer buffer = new StringBuffer();
  fastDateFormat.format(1234567890L, buffer);
  assertTrue(buffer.length() > 0);
}

@Test
public void testFormatDateStringBuffer() {
  Date date = new Date();
  StringBuffer buffer = new StringBuffer();
  fastDateFormat.format(date, buffer);
  assertTrue(buffer.length() > 0);
}

@Test
public void testFormatCalendarStringBuffer() {
  Calendar calendar = Calendar.getInstance();
  StringBuffer buffer = new StringBuffer();
  fastDateFormat.format(calendar, buffer);
  assertTrue(buffer.length() > 0);
}

@Test
public void testParse() throws ParseException {
  String source = "2022-01-01 12:00:00";
  Date result = fastDateFormat.parse(source);
  assertNotNull(result);
}

@Test
public void testParseStringParsePosition() {
  String source = "2022-01-01 12:00:00";
  ParsePosition pos = new ParsePosition(0);
  Date result = fastDateFormat.parse(source, pos);
  assertNotNull(result);
}

@Test
public void testParseObject() {
  String source = "2022-01-01 12:00:00";
  ParsePosition pos = new ParsePosition(0);
  Object result = fastDateFormat.parseObject(source, pos);
  assertNotNull(result);
}

@Test
public void testGetPattern() {
  String pattern = fastDateFormat.getPattern();
  assertNotNull(pattern);
}

@Test
public void testGetTimeZone() {
  TimeZone timeZone = fastDateFormat.getTimeZone();
  assertNotNull(timeZone);
}

@Test
public void testGetLocale() {
  Locale locale = fastDateFormat.getLocale();
  assertNotNull(locale);
}

@Test
public void testGetMaxLengthEstimate() {
  int maxLength = fastDateFormat.getMaxLengthEstimate();
  assertTrue(maxLength >= 0);
}

@Test
public void testEquals() {
  FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
  boolean result = fastDateFormat.equals(other);
  assertTrue(result);
}

@Test
public void testHashCode() {
  int hashCode = fastDateFormat.hashCode();
  assertNotNull(hashCode);
}

@Test
public void testToString() {
  String stringValue = fastDateFormat.toString();
  assertNotNull(stringValue);
}

}