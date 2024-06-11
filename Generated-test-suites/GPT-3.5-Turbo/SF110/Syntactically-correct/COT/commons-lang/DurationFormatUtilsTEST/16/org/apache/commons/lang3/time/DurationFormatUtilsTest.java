import org.junit.Test;

public class DurationFormatUtilsTest {
  
  // Test formatDurationHMS method
  @Test
  public void testFormatDurationHMS() {
    long durationMillis = 1000;
    String expected = "0:00:01.000";
    String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
    assertEquals(expected, actual);
  }
  
  // Test formatDurationISO method
  @Test
  public void testFormatDurationISO() {
    long durationMillis = 1000;
    String expected = "P1970Y1M1DT0H0M1.000S";
    String actual = DurationFormatUtils.formatDurationISO(durationMillis);
    assertEquals(expected, actual);
  }
  
  // Test formatDuration method
  @Test
  public void testFormatDuration() {
    long durationMillis = 1000;
    String format = "H:mm:ss.SSS";
    String expected = "0:01:00.000";
    String actual = DurationFormatUtils.formatDuration(durationMillis, format);
    assertEquals(expected, actual);
  }
  
  // Test formatDuration method with padWithZeros set to false
  @Test
  public void testFormatDurationWithoutPadding() {
    long durationMillis = 1000;
    String format = "H:mm:ss.SSS";
    String expected = "0:1:0.0";
    String actual = DurationFormatUtils.formatDuration(durationMillis, format, false);
    assertEquals(expected, actual);
  }
  
  // Test formatDurationWords method
  @Test
  public void testFormatDurationWords() {
    long durationMillis = 1000;
    boolean suppressLeadingZeroElements = false;
    boolean suppressTrailingZeroElements = false;
    String expected = "0 days 0 hours 0 minutes 1 seconds";
    String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
    assertEquals(expected, actual);
  }
  
  // Test formatPeriodISO method
  @Test
  public void testFormatPeriodISO() {
    long startMillis = 0;
    long endMillis = 1000;
    String expected = "P1970Y1M1DT0H0M1.000S";
    String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
    assertEquals(expected, actual);
  }
  
  // Test formatPeriod method
  @Test
  public void testFormatPeriod() {
    long startMillis = 0;
    long endMillis = 1000;
    String format = "H:mm:ss.SSS";
    String expected = "0:0:1.000";
    String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    assertEquals(expected, actual);
  }
  
  // Test formatPeriod method with padWithZeros set to false
  @Test
  public void testFormatPeriodWithoutPadding() {
    long startMillis = 0;
    long endMillis = 1000;
    String format = "H:mm:ss.SSS";
    String expected = "0:0:1.0";
    String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false);
    assertEquals(expected, actual);
  }
}