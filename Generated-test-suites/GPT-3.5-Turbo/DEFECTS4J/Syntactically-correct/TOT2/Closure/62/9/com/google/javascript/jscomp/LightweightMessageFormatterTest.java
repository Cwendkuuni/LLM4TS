import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("test source", 123, "Test error", CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    assertEquals("test source:123: ERROR - Test error\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError("test source", 456, "Test warning", CheckLevel.WARNING);
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("test source:456: WARNING - Test warning\n", formattedWarning);
  }

  @Test
  public void testFormatWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, 0, "Test error without source", CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - Test error without source\n", formattedError);
  }

  @Test
  public void testFormatWithSourceExcerpt() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("test source", 789, "Test error with source", CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    assertEquals("test source:789: ERROR - Test error with source\n", formattedError);
  }

  // Additional test cases for edge cases and error scenarios could be added here

}

class TestSourceExcerptProvider implements SourceExcerptProvider {

  @Override
  public String get(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
    return "Test source excerpt";
  }

}