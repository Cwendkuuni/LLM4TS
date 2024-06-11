import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import com.google.common.base.Strings;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TopLevelTestingCompiler;
import com.google.javascript.jscomp.testing.testcase.CompilationTestBase;

import java.util.Arrays;

public class LightweightMessageFormatterTest extends CompilationTestBase {

  private static final String CLASS_NAME = LightweightMessageFormatter.class.getName();
  private static final String FORMATTER_NAME = "lightweight";

  // Basic test cases for each method

  @org.junit.Test
  public void testConstructors() {
    LightweightMessageFormatter lwFormatter1 = new LightweightMessageFormatter();
    LightweightMessageFormatter lwFormatter2 = new LightweightMessageFormatter(null);
    LightweightMessageFormatter lwFormatter3 = new LightweightMessageFormatter(null, LINE);
    LightweightMessageFormatter lwFormatter4 = new LightweightMessageFormatter(
        new TestSourceExcerptProvider(), LINE);

    assertNotNull(lwFormatter1);
    assertNull(lwFormatter1.getSource());
    assertNull(lwFormatter1.getExcerpt());

    assertNotNull(lwFormatter2);
    assertNull(lwFormatter2.getSource());
    assertSame(LINE, lwFormatter2.getExcerpt());

    assertNotNull(lwFormatter3);
    assertNull(lwFormatter3.getSource());
    assertSame(LINE, lwFormatter3.getExcerpt());

    assertNotNull(lwFormatter4);
    assertNotNull(lwFormatter4.getSource());
    assertSame(LINE, lwFormatter4.getExcerpt());
  }

  @org.junit.Test
  public void testFormatError() {
    String errorMessage = "Expected identifier, but '{' found.";
    JSError error = new JSError(1, "file.js", 1, null, errorMessage, null);
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String expected = String.format(
        "%s%s: %s - ERROR : %s%s",
        "file.js:", 1, getLevelName(CheckLevel.ERROR), errorMessage, System.lineSeparator());
    String actual = formatter.formatError(error);

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarning() {
    String warningMessage = "Use of the experimental 'foo' feature.";
    JSError warning = new JSError(1, "file.js", 1, null, null, warningMessage);
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String expected = String.format(
        "%s%s: %s - WARNING : %s%s",
        "file.js:", 1, getLevelName(CheckLevel.WARNING), warningMessage, System.lineSeparator());
    String actual = formatter.formatWarning(warning);

    assertEquals(expected, actual);
  }

  // Test cases for edge cases and exceptions

  @org.junit.Test(expected = NullPointerException.class)
  public void testFormatErrorNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    formatter.formatError(null);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testFormatWarningNullWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    formatter.formatWarning(null);
  }

  @org.junit.Test
  public void testFormatErrorWithSource() {
    String errorMessage = "Expected identifier, but '{' found.";
    String sourceCode = "function foo() {\n" +
        "  if (foo) {\n" +
        "    // some code\n" +
        "  }";
    JSError error = new JSError(1, "file.js", 3, null, errorMessage, null);
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SOURCE_EXCERPT);

    SourceExcerpt excerpt = source.getSourceExcerpt("file.js", 3, SOURCE_EXCERPT);
    String expected = String.format(
        "%s%d:%s: %s%s",
        "file.js:", 3, getLevelName(CheckLevel.ERROR), errorMessage, System.lineSeparator() + excerpt);
    String actual = formatter.formatError(error);

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSource() {
    String warningMessage = "Use of the experimental 'foo' feature.";
    String sourceCode = "function foo() {\n" +
        "  experimentalFeature();\n" +
        "}";
    JSError warning = new JSError(1, "file.js", 1, null, null, warningMessage);
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SOURCE_EXCERPT);

    SourceExcerpt excerpt = source.getSourceExcerpt("file.js", 1, SOURCE_EXCERPT);
    String expected = String.format(
        "%s%d:%s: %s%s",
        "file.js:", 1, getLevelName(CheckLevel.WARNING), warningMessage, System.lineSeparator() + excerpt);
    String actual = formatter.formatWarning(warning);

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithRegion() {
    String errorMessage = "Unexpected token ')'";
    JSError error = new JSError(1, "file.js", 1, null, errorMessage, null);
    Region region = new Region("file.js", 1, 5, 1, 10);
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, REGION);

    String expected = String.format(
        "%s%d:%d:%s - %s : %s%s",
        "file.js:", 1, 5, getLevelName(CheckLevel.ERROR), errorMessage, System.lineSeparator() +
            source.getSourceExcerpt("file.js", 1, REGION).substring(4));
    String actual = formatter.formatError(error, region);

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithRegion() {
    String warningMessage = "Use of the experimental 'foo' feature";
    JSError warning = new JSError(1, "file.js", 1, null, null, warningMessage);
    Region region = new Region("file.js", 1, 5, 1, 10);
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, REGION);

    String expected = String.format(
        "%s%d:%d:%s - %s : %s%s",
        "file.js:", 1, 5, getLevelName(CheckLevel.WARNING), warningMessage, System.lineSeparator() +
            source.getSourceExcerpt("file.js", 1, REGION).substring(4));
    String actual = formatter.formatWarning(warning, region);

    assertEquals(expected, actual);
  }

  private String getLevelName(CheckLevel level) {
    return level == CheckLevel.ERROR ? "ERROR" : "WARNING";
  }

  private static class TestSourceExcerptProvider implements SourceExcerptProvider {
    @Override
    public SourceExcerpt getSourceExcerpt(String fileName, int lineNumber, SourceExcerpt excerpt) {
      return Arrays.asList(new SourceExcerpt(fileName, lineNumber, getSourceCode(fileName, lineNumber), excerpt)).get(0);
    }

    private String getSourceCode(String fileName, int lineNumber) {
      switch (fileName) {
        case "file.js":
          switch (lineNumber) {
            case 1:
              return "function foo() {\n" +
                  "  experimentalFeature();\n" +
                  "}";
            case 3:
              return "function foo() {\n" +
                  "  if (foo) {\n" +
                  "    // some code\n" +
                  "  }";
            default:
              throw new AssertionError("Unexpected line number: " + lineNumber);
          }
        default:
          throw new AssertionError("Unexpected file name: " + fileName);
      }
    }
  }

  // Merge all the test cases into a complete JUnit 4 test file
}