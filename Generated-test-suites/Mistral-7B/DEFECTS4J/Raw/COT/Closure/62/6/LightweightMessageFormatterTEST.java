import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.Checker$CompilationLevel;
import com.google.javascript.jscomp.check.Checker;
import com.google.javascript.jscomp.util.CodeChunk;
import com.google.javascript.jscomp.util.Source;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterWithNullSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error message", "File.js", 1, 1);
    String expected = "ERROR - Error message\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testLightweightMessageFormatterWithValidSource() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", 1, 1);
    when(sourceExcerptProvider.getSource("File.js")).thenReturn(new Source(
        "File.js", null, null, new CodeChunk("function foo() {", "}"), null));
    String expected = "File.js:1: ERROR - Error message\nfunction foo() {\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testLightweightMessageFormatterWithValidWarningSource() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError("Warning message", "File.js", 1, 1);
    when(sourceExcerptProvider.getSource("File.js")).thenReturn(new Source(
        "File.js", null, null, new CodeChunk("function foo() {", "}"), null));
    String expected = "File.js:1: WARNING - Warning message\nfunction foo() {\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithNullSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error message", "File.js", 1, 1);
    String expected = "ERROR - Error message\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", 1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource("File.js"))
        .thenReturn(new Source("File.js", null, null, new CodeChunk("function foo() {", "}"), null));
    when(sourceExcerptProvider.getSourceName("File.js")).thenReturn("File.js");
    String expected = "File.js:1: ERROR - Error message\nfunction foo() {\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithWarningSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError warning = new JSError("Warning message", "File.js", 1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource("File.js"))
        .thenReturn(new Source("File.js", null, null, new CodeChunk("function foo() {", "}"), null));
    when(sourceExcerptProvider.getSourceName("File.js"))
        .thenReturn("File.js");
    String expected = "File.js:1: WARNING - Warning message\nfunction foo() {\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithLongLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", Integer.MAX_VALUE, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource("File.js"))
        .thenReturn(new Source("File.js", null, null, new CodeChunk("function foo() {", "}"), null));
    when(sourceExcerptProvider.getSourceName("File.js"))
        .thenReturn("File.js");
    String expected = "File.js:" + Integer.MAX_VALUE + ": ERROR - Error message\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWithNegativeLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", -1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource("File.js"))
        .thenReturn(new Source("File.js", null, null, new CodeChunk("function foo() {", "}"), null));
    when(sourceExcerptProvider.getSourceName("File.js"))
        .thenReturn("File.js");
    formatter.formatError(error);
  }

  @Test
  public void testFormatWithEmptySourceExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", 1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt("File.js", 1, 1))
        .thenReturn(SourceExcerpt.EMPTY);
    String expected = "ERROR - Error message\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithSourceExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", 1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt("File.js", 1, 1))
        .thenReturn(new SourceExcerpt("File.js", "function foo() {\n  bar = 1; ", 1));
    String expected = "File.js:1: ERROR - Error message\nfunction foo() {\n  bar = 1;\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithErrorAndSourceExcerptEndOfLine() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", 1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt("File.js", 1, 1))
        .thenReturn(new SourceExcerpt("File.js", "function foo() {\n  bar = 1;\n  }", 1));
    String expected = "File.js:1: ERROR - Error message\nfunction foo() {\n  bar = 1;\n  }^\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithWarningAndSourceExcerptEndOfLine() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError warning = new JSError("Warning message", "File.js", 1, 1);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt("File.js", 1, 1))
        .thenReturn(new SourceExcerpt("File.js", "function foo() {\n  bar = 1;\n  }", 1));
    String expected = "File.js:1: WARNING - Warning message\nfunction foo() {\n  bar = 1;\n  }^\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithErrorAndSourceExcerptMiddleOfLine() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "File.js", 1, 5);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt("File.js", 1, 1))
        .thenReturn(new SourceExcerpt("File.js", "function foo() {\n  bar = 1 + x; ", 1));
    String expected = "File.js:1: ERROR - Error message\nfunction foo() {\n  bar = 1 + x;\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithWarningAndSourceExcerptMiddleOfLine() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        mock(SourceExcerptProvider.class), LightweightMessageFormatter.LINE);
    JSError warning = new JSError("Warning message", "File.js", 1, 5);
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt("File.js", 1, 1))
        .thenReturn(new SourceExcerpt("File.js", "function foo() {\n  bar = 1 + x; ", 1));
    String expected = "File.js:1: WARNING - Warning message\nfunction foo() {\n  bar = 1 + x;\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  // Add more test cases for additional methods and edge cases as needed.
}