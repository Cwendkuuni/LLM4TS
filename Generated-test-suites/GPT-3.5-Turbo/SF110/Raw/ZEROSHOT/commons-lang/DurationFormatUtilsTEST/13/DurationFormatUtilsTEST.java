import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

  @Test
  public void testFormatDurationHMS() {
    assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
    assertEquals("0:01:30.000", DurationFormatUtils.formatDurationHMS(90000));
    assertEquals("0:00:15.000", DurationFormatUtils.formatDurationHMS(15000));
  }
  
  @Test
  public void testFormatDurationISO() {
    assertEquals("P1970Y0M0DT2H30M0.000S", DurationFormatUtils.formatDurationISO(9000000));
    assertEquals("P0Y1M0DT0H30M10.000S", DurationFormatUtils.formatDurationISO(1810000));
    assertEquals("P0Y0M0DT0H0M30.500S", DurationFormatUtils.formatDurationISO(30500));
  }
  
  @Test
  public void testFormatDuration() {
    assertEquals("3600", DurationFormatUtils.formatDuration(3600000, "s"));
    assertEquals("01:30:00", DurationFormatUtils.formatDuration(5400000, "H:mm:ss"));
    assertEquals("0 days 00:00:15", DurationFormatUtils.formatDuration(15000, "d' days 'H:mm:ss"));
  }
  
  @Test
  public void testFormatDurationWords() {
    assertEquals("1 hour", DurationFormatUtils.formatDurationWords(3600000, true, true));
    assertEquals("1 hour 30 minutes", DurationFormatUtils.formatDurationWords(5400000, true, true));
    assertEquals("15 seconds", DurationFormatUtils.formatDurationWords(15000, true, true));
  }
  
  @Test
  public void testFormatPeriodISO() {
    assertEquals("P1970Y0M0DT2H30M0.000S", DurationFormatUtils.formatPeriodISO(0, 9000000));
    assertEquals("P0Y1M0DT0H30M10.000S", DurationFormatUtils.formatPeriodISO(0, 1810000));
    assertEquals("P0Y0M0DT0H0M30.500S", DurationFormatUtils.formatPeriodISO(0, 30500));
  }
  
  @Test
  public void testFormatPeriod() {
    assertEquals("3600", DurationFormatUtils.formatPeriod(0, 3600000, "s"));
    assertEquals("01:30:00", DurationFormatUtils.formatPeriod(0, 5400000, "H:mm:ss"));
    assertEquals("0 days 00:00:15", DurationFormatUtils.formatPeriod(0, 15000, "d' days 'H:mm:ss"));
  }
}