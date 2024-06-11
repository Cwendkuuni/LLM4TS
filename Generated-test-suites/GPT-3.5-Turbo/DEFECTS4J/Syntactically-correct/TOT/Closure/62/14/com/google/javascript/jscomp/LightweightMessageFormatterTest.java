import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError_withSource() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, CheckLevel.ERROR);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderStub());

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("test.js:10: ERROR - error description\n", result);
  }

  @Test
  public void testFormatError_withoutSource() {
    // Arrange
    JSError error = new JSError("error description", null, 0, CheckLevel.ERROR);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("ERROR - error description\n", result);
  }

  @Test
  public void testFormatWarning_withSource() {
    // Arrange
    JSError warning = new JSError("warning description", "test.js", 10, CheckLevel.WARNING);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderStub());

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    assertEquals("test.js:10: WARNING - warning description\n", result);
  }

  @Test
  public void testFormatWarning_withoutSource() {
    // Arrange
    JSError warning = new JSError("warning description", null, 0, CheckLevel.WARNING);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    assertEquals("WARNING - warning description\n", result);
  }

  @Test
  public void testFormat_withSourceExcerpt() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, CheckLevel.ERROR);
    SourceExcerptProviderStub source = new SourceExcerptProviderStub();
    source.setExcerpt("var x = 5;\nalert(x);\n");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - error description\n" +
        "var x = 5;\n" +
        "        ^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_withoutSourceExcerpt() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, CheckLevel.ERROR);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderStub());

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("test.js:10: ERROR - error description\n", result);
  }

  @Test
  public void testFormat_withEmptySourceExcerpt() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, CheckLevel.ERROR);
    SourceExcerptProviderStub source = new SourceExcerptProviderStub();
    source.setExcerpt("");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("test.js:10: ERROR - error description\n", result);
  }

  @Test
  public void testFormat_withCharNoLessThanSourceExcerptLength() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, 15, CheckLevel.ERROR);
    SourceExcerptProviderStub source = new SourceExcerptProviderStub();
    source.setExcerpt("var x = 5;\nalert(x);\n");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - error description\n" +
        "var x = 5;\n" +
        "        ^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_withLeadingWhitespaceInSourceExcerpt() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, 6, CheckLevel.ERROR);
    SourceExcerptProviderStub source = new SourceExcerptProviderStub();
    source.setExcerpt("var x = 5;\n alert(x);\n");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - error description\n" +
        "var x = 5;\n" +
        "    ^\n";
    assertEquals(expected, result);
  }

  private class SourceExcerptProviderStub implements SourceExcerptProvider {

    private String excerpt;

    public void setExcerpt(String excerpt) {
      this.excerpt = excerpt;
    }

    @Override
    public String get(SourceExcerpt type, String sourceName, int lineNumber, ExcerptFormatter formatter) {
      return excerpt;
    }
  }
}