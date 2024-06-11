import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class FastDateFormatTest {

  @Test
  public void testGetInstance() {
    FastDateFormat.getInstance();
  }

  @Test
  public void testGetInstanceWithPattern() {
    FastDateFormat.getInstance("yyyy-MM-dd");
  }

  @Test
  public void testGetInstanceWithPatternAndTimeZone() {
    FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
  }

  @Test
  public void testGetInstanceWithPatternAndLocale() {
    FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
  }

  @Test
  public void testGetInstanceWithPatternTimeZoneAndLocale() {
    FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
  }

  @Test
  public void testGetDateInstance() {
    FastDateFormat.getDateInstance(FastDateFormat.FULL);
  }

  @Test
  public void testGetDateInstanceWithLocale() {
    FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
  }

  @Test
  public void testGetDateInstanceWithTimeZone() {
    FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
  }

  @Test
  public void testGetDateInstanceWithTimeZoneAndLocale() {
    FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
  }

  @Test
  public void testGetTimeInstance() {
    FastDateFormat.getTimeInstance(FastDateFormat.FULL);
  }

  @Test
  public void testGetTimeInstanceWithLocale() {
    FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
  }

  @Test
  public void testGetTimeInstanceWithTimeZone() {
   FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
  }

  @Test
  public void testGetTimeInstanceWithTimeZoneAndLocale() {
   FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
  }

  @Test
  public void testGetDateTimeInstance() {
    FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
  }

  @Test
  public void testGetDateTimeInstanceWithLocale() {
    FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
  }

  @Test
  public void testGetDateTimeInstanceWithTimeZone() {
    FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
  }

  @Test
  public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
    FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
  }

  @Test
  public void testFormatObjectStringBufferFieldPosition() {
    FastDateFormat.getInstance().format(new Date(), new StringBuffer(), null);
  }

  @Test
  public void testFormatLong() {
    FastDateFormat.getInstance().format(System.currentTimeMillis());
  }

  @Test
  public void testFormatDate() {
    FastDateFormat.getInstance().format(new Date());
  }

  @Test
  public void testFormatCalendar() {
    FastDateFormat.getInstance().format(Calendar.getInstance());
  }

  @Test
  public void testFormatLongStringBuffer() {
    FastDateFormat.getInstance().format(System.currentTimeMillis(), new StringBuffer());
  }

  @Test
  public void testFormatDateStringBuffer() {
    FastDateFormat.getInstance().format(new Date(), new StringBuffer());
  }

  @Test
  public void testFormatCalendarStringBuffer() {
    FastDateFormat.getInstance().format(Calendar.getInstance(), new StringBuffer());
  }

  @Test
  public void testParse() throws ParseException {
    FastDateFormat.getInstance().parse("2022-01-01");
  }

  @Test
  public void testParseWithParsePosition() {
    FastDateFormat.getInstance().parse("2022-01-01", new ParsePosition(0));
  }

  @Test
  public void testParseObjectWithParsePosition() {
    FastDateFormat.getInstance().parseObject("2022-01-01", new ParsePosition(0));
  }

  @Test
  public void testGetPattern() {
    FastDateFormat.getInstance().getPattern();
  }

  @Test
  public void testGetTimeZone() {
    FastDateFormat.getInstance().getTimeZone();
  }

  @Test
  public void testGetLocale() {
    FastDateFormat.getInstance().getLocale();
  }

  @Test
  public void testGetMaxLengthEstimate() {
    FastDateFormat.getInstance().getMaxLengthEstimate();
  }

  @Test
  public void testEquals() {
    FastDateFormat.getInstance().equals(new FastDateFormat());
  }

  @Test
  public void testHashCode() {
    FastDateFormat.getInstance().hashCode();
  }

  @Test
  public void testToString() {
    FastDateFormat.getInstance().toString();
  }

}