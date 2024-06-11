import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }

  @Test
  public void testFormatError() {
    // Typical use case
    JSError error = new JSError("source.js", 10, CheckLevel.ERROR, "Test error");
    String formattedError = formatter.formatError(error);
    assertEquals("source.js:10: ERROR - Test error\n", formattedError);

    // Edge case: Error with no source name and line number
    error = new JSError(null, -1, CheckLevel.ERROR, "Test error");
    formattedError = formatter.formatError(error);
    assertEquals("ERROR - Test error\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    // Typical use case
    JSError warning = new JSError("source.js", 5, CheckLevel.WARNING, "Test warning");
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("source.js:5: WARNING - Test warning\n", formattedWarning);

    // Error scenario: Warning with no source name
    warning = new JSError(null, 8, CheckLevel.WARNING, "Test warning");
    formattedWarning = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning\n", formattedWarning);
  }

  @Test
  public void testWithoutSource() {
    // Typical use case
    LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatterWithoutSource);

    // Edge case: Check that the source provider is null
    assertNull(formatterWithoutSource.getSource());
  }

}