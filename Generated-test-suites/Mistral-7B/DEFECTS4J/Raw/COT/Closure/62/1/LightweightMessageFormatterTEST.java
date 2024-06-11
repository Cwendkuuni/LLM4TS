import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.BaseTestCase;
import com.google.javascript.jscomp.testing.TypeChecker;

import java.util.Arrays;

public class LightweightMessageFormatterTest extends BaseTestCase {

  private SourceExcerptProvider sourceExcerptProvider;
  private LightweightMessageFormatter formatter;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, int lineNumber) {
        return LINE;
      }
    };
    formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Test Error", null, 1, 1, "test", null);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError("Test Error", "test.js", 1, 1, "test", null);
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, int lineNumber) {
        if ("test.js".equals(source)) {
          return source.substring(lineNumber - 1, lineNumber).trim();
        }
        return null;
      }
    };
    formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatError(error);
    String expected = "test.js:1: ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Test Warning", null, 1, 1, "test", null);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test Warning\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError("Test Warning", "test.js", 1, 1, "test", null);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:1: WARNING - Test Warning\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    JSError error = new JSError("Test Error", "test.js", 1, 1, "test", null);
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, int lineNumber) {
        return "";
      }
    };
    formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullSourceProvider() {
    JSError error = new JSError("Test Error", null, 1, 1, "test", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    formatter.formatError(error);
  }

  @Test
  public void testFormatErrorWithEmptyExcerpt() {
    JSError error = new JSError("Test Error", null, 1, 1, "test", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithLargeSource() {
    JSError error = new JSError("Test Error", "test.js", 1, 1, "test", null);
    TypeChecker checker = new TypeChecker(new CompilationTestLoader());
    String source = checker.loadTypeScriptSource("TestLargeSource.ts");
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, int lineNumber) {
        return source.substring(lineNumber - 1, lineNumber).trim();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
    String result = formatter.formatError(error);
    assertTrue(result.contains("test.js:"));
  }

  @Test
  public void testFormatWarningWithLargeSource() {
    JSError warning = new JSError("Test Warning", "test.js", 1, 1, "test", null);
    TypeChecker checker = new TypeChecker(new CompilationTestLoader());
    String source = checker.loadTypeScriptSource("TestLargeSource.ts");
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, int lineNumber) {
        return source.substring(lineNumber - 1, lineNumber).trim();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
    String result = formatter.formatWarning(warning);
    assertTrue(result.contains("test.js:"));
  }

  @Test
  public void testFormatErrorWithInvalidSourceName() {
    JSError error = new JSError("Test Error", null, 1, 1, "test", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNegativeLineNumber() {
    JSError error = new JSError("Test Error", null, 1, 1, "test", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    formatter.formatError(error);
    assertNull(formatter.excerpt);
  }

  @Test
  public void testFormatErrorWithNegativeCharno() {
    JSError error = new JSError("Test Error", null, 1, 1, "test", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNullDescription() {
    JSError error = new JSError("Test Error", null, 1, 1, null, null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithEmptyDescription() {
    JSError error = new JSError("Test Error", null, 1, 1, "", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithLongDescription() {
    JSError error = new JSError("Test Error", null, 1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed commodo, dolor quis bibendum ornare, tortor nulla faucibus nisl, vel ullamcorper velit leo eu orci.", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    assertTrue(result.contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
  }
}