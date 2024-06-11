import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
  
  @Test
  public void testFormatDurationHMS() {
    long durationMillis = 3765000;
    String expected = "1:02:45.000";
    String result = DurationFormatUtils.formatDurationHMS(durationMillis);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatDurationISO() {
    long durationMillis = 3765000;
    String expected = "PT1H2M45S";
    String result = DurationFormatUtils.formatDurationISO(durationMillis);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatDuration() {
    long durationMillis = 3765000;
    String format = "H:mm:ss.SSS";
    String expected = "1:02:45.000";
    String result = DurationFormatUtils.formatDuration(durationMillis, format);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatDuration_padWithZeros() {
    long durationMillis = 3765000;
    String format = "HH:mm:ss.SSS";
    String expected = "01:02:45.000";
    String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatDurationWords() {
    long durationMillis = 3765000;
    boolean suppressLeadingZeroElements = false;
    boolean suppressTrailingZeroElements = false;
    String expected = "0 days 1 hours 2 minutes 45 seconds";
    String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatPeriodISO() {
    long startMillis = 1000;
    long endMillis = 5000;
    String expected = "PT4S";
    String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatPeriod() {
    long startMillis = 1000;
    long endMillis = 4000;
    String format = "mm:ss";
    String expected = "03:00";
    String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatPeriod_padWithZeros() {
    long startMillis = 1000;
    long endMillis = 4000;
    String format = "HH:mm:ss";
    String expected = "00:03:00";
    String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
    assertEquals(expected, result);
  }
}