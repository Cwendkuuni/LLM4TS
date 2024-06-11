
import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

  @Test
  public void format_Test() {
    Date date = new Date();
    String formattedDate = ISO8601Utils.format(date);
    assertNotNull(formattedDate);
    assertEquals(20, formattedDate.length());
  }

  @Test
  public void format_withMillis_Test() {
    Date date = new Date();
    String formattedDate = ISO8601Utils.format(date, true);
    assertNotNull(formattedDate);
    assertTrue(formattedDate.endsWith("Z"));
  }

  @Test
  public void format_withTimeZone_Test() {
    Date date = new Date();
    TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
    String formattedDate = ISO8601Utils.format(date, false, tz);
    assertNotNull(formattedDate);
    assertTrue(formattedDate.contains("+05:30"));
  }

  @Test
  public void parse_Test() throws ParseException {
    String dateString = "2021-09-29T10:00:00Z";
    ParsePosition pos = new ParsePosition(0);
    Date parsedDate = ISO8601Utils.parse(dateString, pos);
    assertNotNull(parsedDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parse_InvalidTimeZone_Test() throws ParseException {
    String dateString = "2021-09-29T10:00:00+05";
    ParsePosition pos = new ParsePosition(0);
    ISO8601Utils.parse(dateString, pos);
  }

  @Test(expected = ParseException.class)
  public void parse_InvalidDateFormat_Test() throws ParseException {
    String dateString = "20210929T100000Z";
    ParsePosition pos = new ParsePosition(0);
    ISO8601Utils.parse(dateString, pos);
  }

}
