import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
  
  @Test
  public void testFormatDurationHMS() {
    assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
    assertEquals("0:00:01.000", DurationFormatUtils.formatDurationHMS(1000));
    assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
  }
  
  @Test
  public void testFormatDurationISO() {
    assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
    assertEquals("P1970Y1M1DT0H0M1.000S", DurationFormatUtils.formatDurationISO(1000));
    assertEquals("P1970Y1M1DT1H0M0.000S", DurationFormatUtils.formatDurationISO(3600000));
  }
  
  @Test
  public void testFormatDuration() {
    assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
    assertEquals("1:00:00.000", DurationFormatUtils.formatDuration(3600000, "H:mm:ss.SSS"));
  }
  
  @Test
  public void testFormatDurationWords() {
    assertEquals("0 days 0 hours 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
    assertEquals("1 day 1 hour 1 minute 1 second", DurationFormatUtils.formatDurationWords(93781000, false, false));
  }
  
  @Test
  public void testFormatPeriodISO() {
    assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
    assertEquals("P1970Y1M1DT0H0M1.000S", DurationFormatUtils.formatPeriodISO(0, 1000));
    assertEquals("P1970Y1M1DT1H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 3600000));
  }
  
  @Test
  public void testFormatPeriod() {
    assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(0, 0, "H:mm:ss.SSS"));
    assertEquals("1:00:00.000", DurationFormatUtils.formatPeriod(0, 3600000, "H:mm:ss.SSS"));
  }
  
}