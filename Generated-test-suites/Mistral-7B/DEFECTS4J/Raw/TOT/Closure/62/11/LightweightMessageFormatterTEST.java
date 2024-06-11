import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.Check;
import com.google.javascript.jscomp.error.Errors;
import com.google.javascript.jscomp.parsing.config.ParserOptions;
import com.google.javascript.jscomp.parsing.config.ParserOptions.SourceMap;
import com.google.javascript.jscomp.parsing.config.ParserOptions.WarningLevel;
import com.google.javascript.jscomp.parsing.parser.ErrorReporter;
import com.google.javascript.jscomp.parsing.parser.JavaScriptParser;
import com.google.javascript.jscomp.parsing.util.SourceFile;
import com.google.javascript.jscomp.testing.TestingUtils;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider source;
  private ErrorReporter errorReporter;
  private JavaScriptParser parser;
  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    source = mock(SourceExcerptProvider.class);
    errorReporter = mock(ErrorReporter.class);
    parser = new JavaScriptParser(new ParserOptions());
    formatter = new LightweightMessageFormatter(source);
  }

  @Test
  public void testFormatError() {
    String code = "function foo() { alert('bar'); }";
    SourceFile sourceFile = TestingUtils.createSourceFile("Test.js", code);
    JSError error = new JSError("TypeError", sourceFile, 1, 7, 0, "invalid ','.");

    when(source.getSource(sourceFile)).thenReturn(code);

    String result = formatter.formatError(error);
    String expected = "TypeError: Test.js:1:7: - invalid ','.\n" +
        "1| function foo() {\n" +
        "  1|   alert('bar');\n" +
        "  1| }";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    String code = "function foo() { alert('bar'); }";
    SourceFile sourceFile = TestingUtils.createSourceFile("Test.js", code);
    JSError warning = new JSError(Errors.UNDEFINED, sourceFile, 1, 1, 0,
        "foo is not defined.");

    when(source.getSource(sourceFile)).thenReturn(code);

    String result = formatter.formatWarning(warning);
    String expected = "Warning: Test.js:1:1: - foo is not defined.\n" +
        "1| function foo() {\n" +
        "  1|   alert('bar');\n" +
        "  1| }";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("TypeError", null, 1, 7, 0, "invalid ','.");

    String result = formatter.formatError(error);
    String expected = "TypeError: - invalid ','.";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError(Errors.UNDEFINED, null, 1, 1, 0,
        "foo is not defined.");

    String result = formatter.formatWarning(warning);
    String expected = "Warning: - foo is not defined.";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithRegion() {
    String code = "function foo() { alert('bar'); } function bar() { return 42; }";
    SourceFile sourceFile = TestingUtils.createSourceFile("Test.js", code);
    JSError error = new JSError("TypeError", sourceFile, 2, 3, 0, "invalid ')'.");

    when(source.getSource(sourceFile)).thenReturn(code);

    ParserOptions options = new ParserOptions();
    options.setWarningLevel(WarningLevel.QUIET);
    options.setSourceMap(SourceMap.NONE);
    ErrorReporter reporter = new ErrorReporter();
    Check check = new Check(new Parser(options));

    check.visitSourceFile(sourceFile, parser, errorReporter);

    String expected = "TypeError: Test.js:2:3: - invalid ')'.\n" +
        "2| function bar() {\n" +
        "  2|   return 42;\n" +
        "  2| }";

    formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatError(error);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithRegion() {
    String code = "function foo() { alert('bar'); } function bar() { return 42; }";
    SourceFile sourceFile = TestingUtils.createSourceFile("Test.js", code);
    JSError warning = new JSError(Errors.UNDEFINED, sourceFile, 1, 1, 0,
        "foo is not defined.");

    when(source.getSource(sourceFile)).thenReturn(code);

    ParserOptions options = new ParserOptions();
    options.setWarningLevel(WarningLevel.QUIET);
    options.setSourceMap(SourceMap.NONE);
    ErrorReporter reporter = new ErrorReporter();
    Check check = new Check(new Parser(options));

    check.visitSourceFile(sourceFile, parser, errorReporter);

    String expected = "Warning: Test.js:1:1: - foo is not defined.\n" +
        "1| function foo() {\n" +
        "  1|   alert('bar');\n" +
        "  1| }";

    formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatWarning(warning);

    assertEquals(expected, result);
  }
}