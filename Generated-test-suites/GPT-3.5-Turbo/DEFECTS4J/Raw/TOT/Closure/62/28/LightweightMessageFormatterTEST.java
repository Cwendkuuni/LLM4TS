import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithoutSource() {
    // Arrange
    JSError error = new JSError("Error Description");
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    // Act
    String formattedError = formatter.formatError(error);

    // Assert
    assertEquals("ERROR - Error Description\n", formattedError);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    // Arrange
    JSError warning = new JSError("Warning Description");
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    // Act
    String formattedWarning = formatter.formatWarning(warning);

    // Assert
    assertEquals("WARNING - Warning Description\n", formattedWarning);
  }

  @Test
  public void testFormatErrorWithSourceExcerpt() {
    // Arrange
    String sourceName = "example.js";
    int lineNumber = 10;
    String sourceExcerpt = "function test() {\n  return 'test';\n}";
    JSError error = new JSError(sourceName, lineNumber, "Error Description");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      public SourceExcerpt get(SourceExcerpt sourceExcerpt, String sourceName) {
        return sourceExcerpt;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String formattedError = formatter.formatError(error);

    // Assert
    String expected = sourceName + ":" + lineNumber + ": ERROR - Error Description\n" +
        sourceExcerpt + "\n       ^\n";
    assertEquals(expected, formattedError);
  }

  @Test
  public void testFormatWarningWithSourceExcerpt() {
    // Arrange
    String sourceName = "example.js";
    int lineNumber = 10;
    String sourceExcerpt = "function test() {\n  return 'test';\n}";
    JSError warning = new JSError(sourceName, lineNumber, "Warning Description");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      public SourceExcerpt get(SourceExcerpt sourceExcerpt, String sourceName) {
        return sourceExcerpt;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String formattedWarning = formatter.formatWarning(warning);

    // Assert
    String expected = sourceName + ":" + lineNumber + ": WARNING - Warning Description\n" +
        sourceExcerpt + "\n       ^\n";
    assertEquals(expected, formattedWarning);
  }

}