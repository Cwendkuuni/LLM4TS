import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

  @Test
  public void testFormat() {
    Date date = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0).getTime();
    String expected = "2020-01-01T12:00:00Z";
    String actual = ISO8601Utils.format(date);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatMillis() {
    Date date = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0).getTime();
    String expected = "2020-01-01T12:00:00.000Z";
    String actual = ISO8601Utils.format(date, true);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithTimeZone() {
    Date date = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0).getTime();
    TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
    String expected = "2020-01-01T12:00:00-08:00";
    String actual = ISO8601Utils.format(date, false, timeZone);
    assertEquals(expected, actual);
  }

  @Test
  public void testParse() throws ParseException {
    String date = "2020-01-01T12:00:00Z";
    Date expected = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0).getTime();
    ParsePosition pos = new ParsePosition(0);
    Date actual = ISO8601Utils.parse(date, pos);
    assertEquals(expected, actual);
  }

  @Test
  public void testParseWithOffset() throws ParseException {
    String date = "2020-01-01T12:00:00+01:00";
    Date expected = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0).getTime();
    TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
    ParsePosition pos = new ParsePosition(0);
    Date actual = ISO8601Utils.parse(date, pos);
    assertEquals(expected, actual);
  }

  @Test(expected = ParseException.class)
  public void testParseInvalidDate() throws ParseException {
    String date = "2020-99-01T12:00:00Z";
    ParsePosition pos = new ParsePosition(0);
    ISO8601Utils.parse(date, pos);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseMissingTimeZone() throws ParseException {
    String date = "2020-01-01T12:00:00";
    ParsePosition pos = new ParsePosition(0);
    ISO8601Utils.parse(date, pos);
  }

  @Test
  public void testPaddingWithZeroes() {
    StringBuilder buffer = new StringBuilder();
    int value = 7;
    int length = 3;
    ISO8601Utils.padInt(buffer, value, length);
    String expected = "007";
    String actual = buffer.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testIndexOfNonDigit() {
    String string = "123456a789";
    int offset = 0;
    int expected = 6;
    int actual = ISO8601Utils.indexOfNonDigit(string, offset);
    assertEquals(expected, actual);
  }
}