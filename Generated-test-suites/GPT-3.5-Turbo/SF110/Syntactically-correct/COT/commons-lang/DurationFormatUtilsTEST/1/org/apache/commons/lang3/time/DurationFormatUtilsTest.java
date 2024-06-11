import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

  @Test
  public void testFormatDurationHMS() {
    assertEquals("1:10:30.500", DurationFormatUtils.formatDurationHMS(4230500L));
  }
  
  @Test
  public void testFormatDurationISO() {
    assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0L));
  }
  
  @Test
  public void testFormatDuration() {
    assertEquals("0:01:15.050", DurationFormatUtils.formatDuration(75050L, "H:mm:ss.SSS"));
  }
  
  @Test
  public void testFormatDurationPadWithZeros() {
    assertEquals("00:00:15.050", DurationFormatUtils.formatDuration(15050L, "HH:mm:ss.SSS", true));
  }
  
  @Test
  public void testFormatDurationWords() {
    assertEquals("2 days 1 hour 23 minutes 5 seconds", DurationFormatUtils.formatDurationWords(184985000L, false, false));
  }
  
  @Test
  public void testFormatDurationWordsSuppressLeadingZeroElements() {
    assertEquals("2 days 1 hour 23 minutes 5 seconds", DurationFormatUtils.formatDurationWords(184985000L, true, false));
  }
  
  @Test
  public void testFormatDurationWordsSuppressTrailingZeroElements() {
    assertEquals("2 days 1 hour 23 minutes 5 seconds", DurationFormatUtils.formatDurationWords(184985000L, false, true));
  }
  
  @Test
  public void testFormatPeriodISO() {
    assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0L, TimeZone.getDefault()));
  }
  
  @Test
  public void testFormatPeriod() {
    assertEquals("1:01:25:05.050", DurationFormatUtils.formatPeriod(9005050L, TimeZone.getDefault()));
  }
  
  @Test
  public void testFormatPeriodPadWithZeros() {
    assertEquals("01:01:25:05.050", DurationFormatUtils.formatPeriod(9005050L, "HH:mm:ss:SSS", true, TimeZone.getDefault()));
  }
  
  @Test
  public void testFormatPeriodWithTimeZone() {
    assertEquals("1:01:25:05.050", DurationFormatUtils.formatPeriod(9005050L, "H:mm:ss.SSS", true, TimeZone.getTimeZone("GMT")));
  }

}