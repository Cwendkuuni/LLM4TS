import org.junit.Assert;
import org.junit.Test;

public class DurationFormatUtilsTest {

  @Test
  public void testFormatDurationHMS() {
    long durationMillis = 3700000;
    String expected = "1:01:40.000";
    String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatDurationISO() {
    long durationMillis = 3700000;
    String expected = "PT1H1M40.000S";
    String actual = DurationFormatUtils.formatDurationISO(durationMillis);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatDuration() {
    long durationMillis = 3700000;
    String format = "H:mm:ss.SSS";
    String expected = "1:01:40.000";
    String actual = DurationFormatUtils.formatDuration(durationMillis, format);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatDurationWithPad() {
    long durationMillis = 3700000;
    String format = "H:mm:ss.SSS";
    boolean padWithZeros = true;
    String expected = "01:01:40.000";
    String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatDurationWords() {
    long durationMillis = 3700000;
    boolean suppressLeadingZeroElements = false;
    boolean suppressTrailingZeroElements = false;
    String expected = " 0 days 1 hours 1 minutes 40 seconds";
    String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatPeriodISO() {
    long startMillis = 0;
    long endMillis = 3700000;
    String expected = "P1970Y1M1DT1H1M40.000S";
    String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatPeriod() {
    long startMillis = 0;
    long endMillis = 3700000;
    String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
    String expected = "P1970Y1M1DT1H1M40.000S";
    String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatPeriodWithPad() {
    long startMillis = 0;
    long endMillis = 3700000;
    String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
    boolean padWithZeros = true;
    String expected = "P1970Y001M001DT001H001M040.000S";
    String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testLexx() {
    String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
    DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx(format);
    DurationFormatUtils.Token[] expectedTokens = {new DurationFormatUtils.Token('P'), new DurationFormatUtils.Token("yyyy", 1), new DurationFormatUtils.Token('Y'), new DurationFormatUtils.Token("MM", 1), new DurationFormatUtils.Token('M'), new DurationFormatUtils.Token("dd", 1), new DurationFormatUtils.Token('D'), new DurationFormatUtils.Token("HH", 1), new DurationFormatUtils.Token('H'), new DurationFormatUtils.Token("mm", 1), new DurationFormatUtils.Token('m'), new DurationFormatUtils.Token("ss", 1), new DurationFormatUtils.Token('s'), new DurationFormatUtils.Token("SSS", 1), new DurationFormatUtils.Token('S')};
    Assert.assertArrayEquals(expectedTokens, tokens);
  }

  @Test
  public void testPaddedValue() {
    long value = 5;
    boolean padWithZeros = true;
    int count = 2;
    String expected = "05";
    String actual = DurationFormatUtils.paddedValue(value, padWithZeros, count);
    Assert.assertEquals(expected, actual);
  }
}