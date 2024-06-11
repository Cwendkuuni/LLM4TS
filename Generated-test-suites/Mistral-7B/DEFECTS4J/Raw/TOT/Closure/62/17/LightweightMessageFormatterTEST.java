import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.TestingSourceExcerptProvider;
import com.google.javascript.jscomp.common.CodeAnalyzer;
import com.google.javascript.jscomp.common.CompilationTestRunner;
import com.google.javascript.jscomp.common.ErrorReporter;
import com.google.javascript.jscomp.parser.js.JsParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LightweightMessageFormatterTest {

  private static final String SOURCE =
      "function foo() {" +
      "  bar();" +
      "}";

  private static final String SOURCE_WITH_ERROR =
      "function foo() {" +
      "  bar();" +
      "  alert('XSS');" +
      "}";

  private static final String SOURCE_WITH_WARNING =
      "function foo() {" +
      "  if (typeof bar === 'undefined') {" +
      "    console.warn('bar is not defined');" +
      "  }" +
      "  alert('Hello World');" +
      "}";

  private static final String SOURCE_WITH_REGION =
      "function foo(x) {" +
      "  if (x < 10) {" +
      "    alert('x is less than 10');" +
      "  } else if (x > 20) {" +
      "    alert('x is greater than 20');" +
      "  }" +
      "}";

  private static final int LINE_NUMBER = 2;

  @org.junit.Test
  public void testErrorMessageWithSource() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_ERROR, null, null, null);

    JSError error = errorReporter.getError(0);

    String expectedMessage =
        "SOURCE_NAME:test.js:LINE_NUMBER: " +
        ERROR + " - " +
        "XSS" +
        "\n" +
        "  function foo() {" +
        "    bar();" +
        "    alert('XSS');" +
        "  }" +
        "                   ^";

    assertEquals(expectedMessage, formatter.formatError(error));
  }

  @org.junit.Test
  public void testWarningMessageWithSource() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_WARNING, null, null, null);

    JSError warning = errorReporter.getWarning(0);

    String expectedMessage =
        "SOURCE_NAME:test.js:LINE_NUMBER: " +
        WARNING + " - " +
        "bar is not defined";

    assertEquals(expectedMessage, formatter.formatWarning(warning));
  }

  @org.junit.Test
  public void testErrorMessageWithoutSource() throws IOException {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_ERROR, null, null, null);

    JSError error = errorReporter.getError(0);

    String expectedMessage = "ERROR - XSS";

    assertEquals(expectedMessage, formatter.formatError(error));
  }

  @org.junit.Test
  public void testWarningMessageWithoutSource() throws IOException {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_WARNING, null, null, null);

    JSError warning = errorReporter.getWarning(0);

    String expectedMessage = "WARNING - bar is not defined";

    assertEquals(expectedMessage, formatter.formatWarning(warning));
  }

  @org.junit.Test
  public void testRegionMessageWithSource() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_REGION, null, null, null);

    JSError error = errorReporter.getError(0);

    String expectedMessage =
        "SOURCE_NAME:test.js:LINE_NUMBER:\n" +
        "  if (x < 10) {\n" +
        "\n" +
        "    ^--------^" +
        "\n" +
        "  alert('x is less than 10');" +
        "\n" +
        "}";

    assertEquals(expectedMessage, formatter.formatError(error));
  }

  @org.junit.Test
  public void testErrorMessageWithSourceAndMultipleErrors() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE, null, null, null);

    List<JSError> errors = Arrays.asList(
        errorReporter.getError(0),
        errorReporter.getError(1)
    );

    for (JSError error : errors) {
      String expectedMessage =
          "SOURCE_NAME:test.js:LINE_NUMBER: " +
          ERROR + " - " +
          "Unexpected token";

      assertEquals(expectedMessage, formatter.formatError(error));
    }
  }

  @org.junit.Test
  public void testWarningMessageWithSourceAndMultipleWarnings() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_WARNING, null, null, null);

    List<JSError> warnings = Arrays.asList(
        errorReporter.getWarning(0),
        errorReporter.getWarning(1)
    );

    for (JSError warning : warnings) {
      String expectedMessage =
          "SOURCE_NAME:test.js:LINE_NUMBER: " +
          WARNING + " - " +
          "bar is not defined";

      assertEquals(expectedMessage, formatter.formatWarning(warning));
    }
  }

  @org.junit.Test
  public void testErrorMessageWithSourceAndRegion() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_REGION, null, null, null);

    JSError error = errorReporter.getError(0);

    String expectedMessage =
        "SOURCE_NAME:test.js:LINE_NUMBER:\n" +
        "  if (x < 10) {\n" +
        "    ^--------^" +
        "\n" +
        "    alert('x is less than 10');" +
        "\n" +
        "  } else if (x > 20) {" +
        "\n" +
        "    alert('x is greater than 20');" +
        "\n" +
        "  }";

    assertEquals(expectedMessage, formatter.formatError(error));
  }

  @org.junit.Test
  public void testErrorMessageWithSourceAndRegionAndMultipleErrors() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE, null, null, null);

    List<JSError> errors = Arrays.asList(
        errorReporter.getError(0),
        errorReporter.getError(1)
    );

    for (JSError error : errors) {
      String expectedMessage =
          "SOURCE_NAME:test.js:LINE_NUMBER:\n" +
          "  function foo() {" +
          "\n" +
          "    ^--------^" +
          "\n" +
          "    bar();" +
          "\n" +
          "  }\n" +
          "  ^^^^" +
          "\n" +
          "  alert('XSS');" +
          "\n" +
          "  ^^^^^^";

      assertEquals(expectedMessage, formatter.formatError(error));
    }
  }

  @org.junit.Test
  public void testErrorMessageWithSourceAndRegionAndMultipleErrorsAndLongLines() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(
        "function foo() {" +
        "  bar();" +
        "  alert('XSS');" +
        "}",
        null,
        null,
        null
    );

    List<JSError> errors = Arrays.asList(
        errorReporter.getError(0),
        errorReporter.getError(1)
    );

    for (JSError error : errors) {
      String expectedMessage =
          "SOURCE_NAME:test.js:LINE_NUMBER:\n" +
          "function foo() {" +
          "\n" +
          "  ^--------^" +
          "\n" +
          "  bar();" +
          "\n" +
          "  ^^^^" +
          "\n" +
          "  alert('XSS');" +
          "\n" +
          "  ^^^^^^";

      assertEquals(expectedMessage, formatter.formatError(error));
    }
  }

  @org.junit.Test
  public void testWarningMessageWithSourceAndRegionAndMultipleWarnings() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(SOURCE_WITH_WARNING, null, null, null);

    List<JSError> warnings = Arrays.asList(
        errorReporter.getWarning(0),
        errorReporter.getWarning(1)
    );

    for (JSError warning : warnings) {
      String expectedMessage =
          "SOURCE_NAME:test.js:LINE_NUMBER:\n" +
          "function foo() {" +
          "\n" +
          "  if (typeof bar === 'undefined') {" +
          "\n" +
          "    ^------------------------^" +
          "\n" +
          "    console.warn('bar is not defined');" +
          "\n" +
          "  }" +
          "\n" +
          "  alert('Hello World');" +
          "\n" +
          "  ^------------------------^";

      assertEquals(expectedMessage, formatter.formatWarning(warning));
    }
  }

  @org.junit.Test
  public void testErrorMessageWithSourceAndRegionAndMultipleErrorsAndLongLines2() throws IOException {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    ErrorReporter errorReporter = new ErrorReporter();
    JsParser parser = new JsParser(null);
    CodeAnalyzer analyzer = new CodeAnalyzer(null, parser, errorReporter, null, null, null, null, null, formatter, null);

    analyzer.process(
        "function foo() {" +
        "  bar();" +
        "  alert('XSS');" +
        "}",
        null,
        null,
        null
    );

    List<JSError> errors = Arrays.asList(
        errorReporter.getError(0),
        errorReporter.getError(1)
    );

    for (JSError error : errors) {
      String expectedMessage =
          "SOURCE_NAME:test.js:LINE_NUMBER:\n" +
          "function foo() {" +
          "\n" +
          "  ^--------^" +
          "\n" +
          "  bar();" +
          "\n" +
          "  ^^^^" +
          "\n" +
          "  alert('XSS');" +
          "\n" +
          "  ^^^^^^";

      assertEquals(expectedMessage, formatter.formatError(error));
    }
  }
}