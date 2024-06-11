import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        // Mock implementation
        return null;
      }
    });
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("Test error", CheckLevel.ERROR);
    error.sourceName = "test.js";
    error.lineNumber = 5;
    String expected = "test.js:5: ERROR - Test error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test warning", CheckLevel.WARNING);
    warning.sourceName = "test.js";
    warning.lineNumber = 10;
    String expected = "test.js:10: WARNING - Test warning\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }
  
}