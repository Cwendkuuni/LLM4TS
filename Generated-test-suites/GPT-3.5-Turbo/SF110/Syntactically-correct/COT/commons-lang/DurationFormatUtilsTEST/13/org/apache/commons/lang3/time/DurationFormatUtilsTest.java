
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DurationFormatUtilsTest {
  
  @Test
  public void testFormatDurationHMS() {
    long duration = 3600000; // 1 hour in milliseconds
    String expected = "1:00:00.000";
    String actual = DurationFormatUtils.formatDurationHMS(duration);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDurationISO() {
    long duration = 86400000; // 1 day in milliseconds
    String expected = "P1970Y1M1DT0H0M0.0S";
    String actual = DurationFormatUtils.formatDurationISO(duration);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDuration() {
    long duration = 60000; // 1 minute in milliseconds
    String format = "mm:ss";
    String expected = "01:00";
    String actual = DurationFormatUtils.formatDuration(duration, format);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDurationPadWithZeros() {
    long duration = 3600000; // 1 hour in milliseconds
    String format = "H:m:s";
    String expected = "1:0:0";
    String actual = DurationFormatUtils.formatDuration(duration, format, true);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDurationWords() {
    long duration = 86400000; // 1 day in milliseconds
    boolean suppressLeadingZeroElements = false;
    boolean suppressTrailingZeroElements = false;
    String expected = " 1 days 0 hours 0 minutes 0 seconds";
    String actual = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatPeriodISO() {
    long startMillis = 0;
    long endMillis = 3600000; // 1 hour in milliseconds
    String expected = "P1970Y1M1DT1H0M0.0S";
    String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatPeriodPadWithZeros() {
    long startMillis = 0;
    long endMillis = 60000; // 1 minute in milliseconds
    String format = "H:mm";
    boolean padWithZeros = true;
    String expected = "0:01";
    String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, TimeZone.getDefault());
    assertEquals(expected, actual);
  }
}
