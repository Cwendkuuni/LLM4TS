import org.junit.Assert;
import org.junit.Test;

public class DurationFormatUtilsTest {
  
  @Test
  public void testFormatDurationHMS() {
    long durationMillis = 1000;
    String expected = "0:00:01.000";
    String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDurationISO() {
    long durationMillis = 2000;
    String expected = "P1970Y1M0DT0H0M2.000S";
    String actual = DurationFormatUtils.formatDurationISO(durationMillis);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatDuration() {
    long durationMillis = 3000;
    String format = "H:mm:ss.SSS";
    String expected = "0:00:03.000";
    String actual = DurationFormatUtils.formatDuration(durationMillis, format);
    Assert.assertEquals(expected, actual);
  }
  
  // Add more test cases for formatDuration() method
  
  
  @Test
  public void testFormatDurationWords() {
    long durationMillis = 4000;
    boolean suppressLeadingZeroElements = true;
    boolean suppressTrailingZeroElements = true;
    String expected = "4 seconds";
    String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
    Assert.assertEquals(expected, actual);
  }
  
  // Add more test cases for formatDurationWords() method
  
  
  @Test
  public void testFormatPeriodISO() {
    long startMillis = 1000;
    long endMillis = 2000;
    String expected = "P1970Y1M0DT0H0M1.000S";
    String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatPeriod() {
    long startMillis = 2000;
    long endMillis = 3000;
    String format = "H:mm:ss.SSS";
    String expected = "0:00:01.000";
    String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    Assert.assertEquals(expected, actual);
  }
  
  // Add more test cases for formatPeriod() method
  
}