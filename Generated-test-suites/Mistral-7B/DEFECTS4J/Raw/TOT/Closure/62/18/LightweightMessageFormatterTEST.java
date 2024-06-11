import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.CheckLevel;
import com.google.javascript.jscomp.parser.Parser;
import com.google.javascript.jscomp.parser.Source;

import java.io.IOException;
import java.util.List;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider source;

  @org.junit.Before
  public void setUp() {
    source = mock(SourceExcerptProvider.class);
  }

  @org.junit.Test
  public void testFormatError() throws IOException {
    Parser parser = new Parser();
    Source sourceCode = parser.parseJavaScript("testError",
        "function testError() { throw new Error('Test Error'); }");

    JSError error = new JSError("testError.js", 1, 1, "Test Error");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String expected = String.format("1: Error - ERROR: Test Error%n testError.js:1:1 %%",
        System.lineSeparator());
    when(source.getSourceExcerpt(error.sourceName, error.lineNumber)).thenReturn(expected);

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFormatWarning() throws IOException {
    Parser parser = new Parser();
    Source sourceCode = parser.parseJavaScript("testWarning",
        "function testWarning() { alert('Test Warning'); }");

    JSError warning = new JSError("testWarning.js", 1, 1, "Test Warning");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String expected = String.format("1: WARNING - WARNING: Test Warning%n testWarning.js:1:1 %%",
        System.lineSeparator());
    when(source.getSourceExcerpt(warning.sourceName, warning.lineNumber)).thenReturn(expected);

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Test Error", 1, 1, "Test Error");
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String expected = String.format("1: Error - ERROR: Test Error%n",
        System.lineSeparator());

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Test Warning", 1, 1, "Test Warning");
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String expected = String.format("1: WARNING - WARNING: Test Warning%n",
        System.lineSeparator());

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFormatErrorWithRegion() throws IOException {
    Parser parser = new Parser();
    Source sourceCode = parser.parseJavaScript("testErrorRegion",
        "function printError(message) { throw new Error(message); }");

    JSError error = new JSError("testErrorRegion.js", 2, 5, "Message contains error");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    List<Source> sources = List.of(sourceCode);
    when(source.getSourceForPosition(error.sourceName, error.lineNumber, error.columnNumber)).thenReturn(sources.get(0));
    when(source.getRegionForPosition(error.sourceName, error.lineNumber, error.columnNumber)).thenReturn(new Region(sources.get(0), 1, 1, 5, 10));

    String expected = String.format("2: Error - ERROR: Message contains error%n testErrorRegion.js:2:5 %%\n      printError(message) {^\n        throw new Error(message);\n      }%n",
        System.lineSeparator());

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFormatWarningWithRegion() throws IOException {
    Parser parser = new Parser();
    Source sourceCode = parser.parseJavaScript("testWarningRegion",
        "function printWarning(message) { alert(message); }");

    JSError warning = new JSError("testWarningRegion.js", 2, 5, "Message contains warning");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    List<Source> sources = List.of(sourceCode);
    when(source.getSourceForPosition(warning.sourceName, warning.lineNumber, warning.columnNumber)).thenReturn(sources.get(0));
    when(source.getRegionForPosition(warning.sourceName, warning.lineNumber, warning.columnNumber)).thenReturn(new Region(sources.get(0), 1, 1, 5, 10));

    String expected = String.format("2: WARNING - WARNING: Message contains warning%n testWarningRegion.js:2:5 %%\n      printWarning(message) {^\n        alert(message);\n      }%n",
        System.lineSeparator());

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }
}