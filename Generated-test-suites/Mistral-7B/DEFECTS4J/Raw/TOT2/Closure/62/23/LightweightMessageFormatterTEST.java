import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TopLevelTestingTestCase;

public class LightweightMessageFormatterTest extends TopLevelTestingTestCase {
  @Test
  public void testFormatError() {
    // Given
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public String getSource(String unitName, int lineNumber) {
        return "Test Source";
      }
    };
    LightweightMessageFormatter lmf = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    JSError error = new JSError("TestError", "TestFile.java", 1, 1, "TestMessage");

    // When
    String result = lmf.formatError(error);

    // Then
    assertEquals(
        "TestFile.java:1: ERROR - Error - TestMessage\nTest Source\n^\n", result);
  }

  @Test
  public void testFormatWarning() {
    // Given
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public String getSource(String unitName, int lineNumber) {
        return "Test Source";
      }
    };
    LightweightMessageFormatter lmf = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    JSError warning = new JSError("TestWarning", "TestFile.java", 1, 1, "TestMessage");

    // When
    String result = lmf.formatWarning(warning);

    // Then
    assertEquals(
        "TestFile.java:1: WARNING - Warning - TestMessage\nTest Source\n^\n", result);
  }

  @Test
  public void testFormatErrorWithSource() {
    // Given
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public String getSource(String unitName, int lineNumber) {
        return "Test Source\nif (x > y) {\n  z = 1;\n}";
      }
    };
    LightweightMessageFormatter lmf = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    JSError error = new JSError("TestError", "TestFile.java", 2, 5, "TestMessage");

    // When
    String result = lmf.formatError(error);

    // Then
    assertEquals(
        "TestFile.java:2: ERROR - Error - TestMessage\nif (x > y) {\n  z = 1;\n}\n  ^^\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    // Given
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public String getSource(String unitName, int lineNumber) {
        return "Test Source\nif (x > y) {\n  z = 1;\n}";
      }
    };
    LightweightMessageFormatter lmf = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    JSError warning = new JSError("TestWarning", "TestFile.java", 2, 5, "TestMessage");

    // When
    String result = lmf.formatWarning(warning);

    // Then
    assertEquals(
        "TestFile.java:2: WARNING - Warning - TestMessage\nif (x > y) {\n  z = 1;\n}\n  ^^\n", result);
  }

  @Test
  public void testFormatErrorNullSource() {
    // Given
    LightweightMessageFormatter lmf = new LightweightMessageFormatter();

    JSError error = new JSError("TestError", "TestFile.java", 1, 1, "TestMessage");

    // When
    String result = lmf.formatError(error);

    // Then
    assertEquals("TestError - Error - TestMessage\n", result);
  }

  @Test
  public void testFormatWarningNullSource() {
    // Given
    LightweightMessageFormatter lmf = new LightweightMessageFormatter();

    JSError warning = new JSError("TestWarning", "TestFile.java", 1, 1, "TestMessage");

    // When
    String result = lmf.formatWarning(warning);

    // Then
    assertEquals("TestWarning - Warning - TestMessage\n", result);
  }
}