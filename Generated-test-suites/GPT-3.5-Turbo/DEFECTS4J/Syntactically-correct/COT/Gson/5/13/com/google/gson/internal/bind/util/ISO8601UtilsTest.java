
import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

  @Test
  public void testFormat1() {
    Date date = new Date();
    String expected = ISO8601Utils.format(date, false, ISO8601Utils.TIMEZONE_UTC);
    String actual = ISO8601Utils.format(date);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormat2() {
    Date date = new Date();
    String expected = ISO8601Utils.format(date, true, ISO8601Utils.TIMEZONE_UTC);
    String actual = ISO8601Utils.format(date, true);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormat3() {
    Date date = new Date();
    String expected = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
    String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
    assertEquals(expected, actual);
  }

  @Test
  public void testParse() throws ParseException {
    String date = "2021-01-01";
    ParsePosition pos = new ParsePosition(0);
    Date expected = ISO8601Utils.parse(date, pos);
    Date actual = new Date(1609459200000L);
    assertEquals(expected, actual);
    assertEquals(pos.getIndex(), date.length());
  }

  @Test(expected = ParseException.class)
  public void testParseInvalidDate() throws ParseException {
    String date = "2021-01-39";
    ParsePosition pos = new ParsePosition(0);
    ISO8601Utils.parse(date, pos);
  }
}

