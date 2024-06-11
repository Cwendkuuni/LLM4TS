import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    JSError error = new JSError("Test error", "test.js", 10, CheckLevel.ERROR);
    String expected = "test.js:10: ERROR - Test error\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    JSError warning = new JSError("Test warning", "test.js", 5, CheckLevel.WARNING);
    String expected = "test.js:5: WARNING - Test warning\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Test error", "test.js", 10, CheckLevel.ERROR);
    String expected = "ERROR - Test error\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Test error", "test.js", 10, CheckLevel.ERROR);
    String expected = "ERROR - Test error\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatRegionWithoutCode() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("", 1, 1);
    assertEquals(null, formatter.formatRegion(region));
  }
}