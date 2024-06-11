import static org.junit.Assert.*;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.BaseCompilerTest;
import com.google.javascript.jscomp.testing.CheckLevel;
import com.google.javascript.jscomp.testing.CompilationTestFactory;
import com.google.javascript.jscomp.testing.ProcessingTest;
import com.google.javascript.jscomp.testing.regions.Region;

import java.util.List;

public class LightweightMessageFormatterTest extends BaseCompilerTest {

  private static final String CLASS_NAME = LightweightMessageFormatter.class.getName();

  /**
   * Test constructor without source information.
   */
  @ProcessingTest
  public void testLightweightMessageFormatterWithoutSource() {
    SourceExcerptProvider source = null;
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("TestError", null, null, 1, 1, "TestErrorMessage",
        null, null);
    String message = formatter.formatError(error);
    String expected = "ERROR - TestErrorMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test constructor with source information.
   */
  @ProcessingTest
  public void testLightweightMessageFormatterWithSource() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TestError", "TestSource.js", 1, 1, "TestErrorMessage",
        null, null);
    String message = formatter.formatError(error);
    String expected = "TestSource.js:1: ERROR - TestErrorMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test constructor with source information and excerpt.
   */
  @ProcessingTest
  public void testLightweightMessageFormatterWithCustomExcerpt() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source,
        SourceExcerpt.LINE);
    JSError error = new JSError("TestError", "TestSource.js", 1, 1, "TestErrorMessage",
        null, null);
    String message = formatter.formatError(error);
    String expected = "TestSource.js:1: ERROR - TestErrorMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test formatError method with null error.
   */
  @ProcessingTest
  public void testFormatErrorWithNullError() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String message = formatter.formatError(null);
    String expected = "";
    assertEquals(expected, message);
  }

  /**
   * Test formatWarning method.
   */
  @ProcessingTest
  public void testFormatWarning() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("TestWarning", null, null, 1, 1, "TestWarningMessage",
        null, null);
    String message = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarningMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test format method with null error and without source information.
   */
  @ProcessingTest
  public void testFormatWithNullErrorWithoutSource() {
    SourceExcerptProvider source = null;
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = null;
    String message = formatter.format(error, false);
    String expected = "";
    assertEquals(expected, message);
  }

  /**
   * Test format method with null error and with source information.
   */
  @ProcessingTest
  public void testFormatWithNullErrorWithSource() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = null;
    String message = formatter.format(error, false);
    String expected = "";
    assertEquals(expected, message);
  }

  /**
   * Test format method with error and without source information.
   */
  @ProcessingTest
  public void testFormatWithErrorWithoutSource() {
    SourceExcerptProvider source = null;
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("TestError", null, null, 1, 1, "TestErrorMessage",
        null, null);
    String message = formatter.format(error, false).trim();
    String expected = "ERROR - TestErrorMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test format method with error and with source information.
   */
  @ProcessingTest
  public void testFormatWithErrorWithSource() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TestError", "TestSource.js", 1, 1,
        "TestErrorMessage", null, null);
    String message = formatter.format(error, false).trim();
    String expected = "TestSource.js:1: ERROR - TestErrorMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test format method with warning and without source information.
   */
  @ProcessingTest
  public void testFormatWithWarningWithoutSource() {
    SourceExcerptProvider source = null;
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError("TestWarning", null, null, 1, 1,
        "TestWarningMessage", null, null);
    String message = formatter.format(warning, true).trim();
    String expected = "WARNING - TestWarningMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test format method with warning and with source information.
   */
  @ProcessingTest
  public void testFormatWithWarningWithSource() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("TestWarning", "TestSource.js", 1, 1,
        "TestWarningMessage", null, null);
    String message = formatter.format(warning, true).trim();
    String expected = "TestSource.js:1: WARNING - TestWarningMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test format method with error and region.
   */
  @ProcessingTest
  public void testFormatWithErrorAndRegion() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TestError", "TestSource.js", 1, 1, "TestErrorMessage",
        null, new Region("TestSource.js", 1, 1, 10, 5));
    String message = formatter.format(error, false).trim();
    String expected = "TestSource.js:1: ERROR - TestErrorMessage\n" +
                      "  | 1:5 TestErrorMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test format method with warning and region.
   */
  @ProcessingTest
  public void testFormatWithWarningAndRegion() {
    SourceExcerptProvider source = createSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("TestWarning", "TestSource.js", 1, 1,
        "TestWarningMessage", null, new Region("TestSource.js", 1, 1, 10, 5));
    String message = formatter.format(warning, true).trim();
    String expected = "TestSource.js:1: WARNING - TestWarningMessage\n" +
                      "  | 1:5 TestWarningMessage\n";
    assertEquals(expected, message);
  }

  /**
   * Test LineNumberingFormatter.formatLine method.
   */
  @ProcessingTest
  public void testFormatLine() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String line = "if (foo) {\nalert('bar');\n}";
    int lineNumber = 1;
    String message = formatter.formatLine(line, lineNumber);
    String expected = "  " + lineNumber + "| " + line + "\n";
    assertEquals(expected, message);
  }

  /**
   * Test LineNumberingFormatter.formatRegion method with empty region.
   */
  @ProcessingTest
  public void testFormatRegionWithEmptyRegion() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String code = "";
    Region region = null;
    String message = formatter.formatRegion(region);
    assertEquals(message, null);
  }

  /**
   * Test LineNumberingFormatter.formatRegion method with null code.
   */
  @ProcessingTest
  public void testFormatRegionWithNullCode() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region("TestSource.js", 1, 1, 10, 5);
    String message = formatter.formatRegion(region);
    assertEquals(message, null);
  }

  /**
   * Test LineNumberingFormatter.formatRegion method.
   */
  @ProcessingTest
  public void testFormatRegion() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String code = "if (foo) {\nalert('bar');\n}";
    Region region = new Region("TestSource.js", 1, 1, 10, 5);
    String message = formatter.formatRegion(region).trim();
    String expected = "  " + region.getBeginningLineNumber() + "| " +
                      code.substring(0, code.indexOf("\n")) + "\n" +
                      "  " + (region.getEndingLineNumber() + 1) + "| |\n" +
                      code.substring(code.indexOf("\n")).trim();
    assertEquals(expected, message);
  }

  /**
   * Test Preconditions.checkNotNull method in the constructor.
   */
  @ProcessingTest
  public void testPreconditionsCheckNotNull() {
    try {
      new LightweightMessageFormatter(null);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  private SourceExcerptProvider createSourceExcerptProvider() {
    return new CompilationTestFactory(getCompilerOptions())
        .getSourceExcerptProvider();
  }
}