import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError_sourceWithExcerpt_shouldFormatError() {
    // Arrange
    JSError error = new JSError("Error message");
    error.setSourceName("test.js");
    error.setLineNumber(10);
    error.setDescription("An error occurred");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      public String getSource(String sourceName) {
        return "function test() {\n  console.log('Test');\n}";
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - An error occurred\n" +
        "function test() {\n" +
        "  console.log('Test');\n" +
        "}\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatError_sourceWithoutExcerpt_shouldFormatError() {
    // Arrange
    JSError error = new JSError("Error message");
    error.setSourceName("test.js");
    error.setLineNumber(10);
    error.setDescription("An error occurred");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      public String getSource(String sourceName) {
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - An error occurred\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning_sourceWithExcerpt_shouldFormatWarning() {
    // Arrange
    JSError warning = new JSError("Warning message");
    warning.setSourceName("test.js");
    warning.setLineNumber(5);
    warning.setDescription("A warning occurred");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      public String getSource(String sourceName) {
        return "function test() {\n  console.log('Test');\n}";
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    String expected = "test.js:5: WARNING - A warning occurred\n" +
        "function test() {\n" +
        "  console.log('Test');\n" +
        "}\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning_sourceWithoutExcerpt_shouldFormatWarning() {
    // Arrange
    JSError warning = new JSError("Warning message");
    warning.setSourceName("test.js");
    warning.setLineNumber(5);
    warning.setDescription("A warning occurred");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      public String getSource(String sourceName) {
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    String expected = "test.js:5: WARNING - A warning occurred\n";
    assertEquals(expected, result);
  }

}