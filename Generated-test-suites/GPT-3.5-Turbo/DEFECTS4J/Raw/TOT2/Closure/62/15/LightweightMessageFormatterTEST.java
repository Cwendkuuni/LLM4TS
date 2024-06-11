import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Test formatting an error message
    JSError error = new JSError("error", "Test error message", "sourceName", 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    // Test formatting a warning message
    JSError warning = new JSError("warning", "Test warning message", "sourceName", 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning message\n", formattedWarning);
  }

  @Test
  public void testFormatErrorNullSource() {
    // Test formatting an error message with null source
    JSError error = new JSError("error", "Test error message", null, 0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarningNullSource() {
    // Test formatting a warning message with null source
    JSError warning = new JSError("warning", "Test warning message", null, 0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning message\n", formattedWarning);
  }

  @Test
  public void testFormatErrorEmptyExcerpt() {
    // Test formatting an error message with empty excerpt
    JSError error = new JSError("error", "Test error message", "sourceName", 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      public String get(SourceExcerpt excerpt) {
        return "";
      }
    });
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarningEmptyExcerpt() {
    // Test formatting a warning message with empty excerpt
    JSError warning = new JSError("warning", "Test warning message", "sourceName", 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      public String get(SourceExcerpt excerpt) {
        return "";
      }
    });
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning message\n", formattedWarning);
  }

}