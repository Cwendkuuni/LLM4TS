package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.ExcerptFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Strings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider mockSourceProvider;
  private LightweightMessageFormatter lightweightMessageFormatter;

  @Before
  public void setUp() {
    mockSourceProvider = mock(SourceExcerptProvider.class);
    lightweightMessageFormatter = new LightweightMessageFormatter(mockSourceProvider);
  }

  @Test
  public void testFormatErrorWithSource() {
    String sourceCode = "function foo() {\n" +
        "  alert('Hello, World!');\n" +
        "}";

    SourceExcerpt mockExcerpt = new SourceExcerpt("foo.js", LINE, sourceCode);
    ArgumentCaptor<SourceExcerpt> sourceExcerptArgumentCaptor = ArgumentCaptor.forClass(SourceExcerpt.class);

    when(mockSourceProvider.getExcerpt(mockSourceProvider, "foo.js", 5, LINE))
        .thenReturn(mockExcerpt);

    JSError error = new JSError("Expected identifier, but 'alert' was found.", 5, "foo.js", 5);

    String expectedMessage = "foo.js:5: EXpected identifier, but 'alert' was found.\n" +
        "  alert('Hello, World!');\n";

    String actualMessage = lightweightMessageFormatter.formatError(error);

    assertEquals(expectedMessage, actualMessage);
    assertEquals(mockExcerpt, error.getSourceExcerpt());
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Expected identifier, but 'alert' was found.", null, null, 0);

    String expectedMessage = "Expected identifier, but 'alert' was found.\n";

    String actualMessage = lightweightMessageFormatter.withoutSource().formatError(error);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testFormatWarningWithSource() {
    String sourceCode = "function foo() {\n" +
        "  alert('Hello, World!');\n" +
        "}";

    SourceExcerpt mockExcerpt = new SourceExcerpt("foo.js", LINE, sourceCode);
    when(mockSourceProvider.getExcerpt(mockSourceProvider, "foo.js", 5, LINE))
        .thenReturn(mockExcerpt);

    JSError warning = new JSError("Possible unused variable 'foo'.", 5, "foo.js", 5);

    String expectedMessage = "foo.js:5: WARNING - Possible unused variable 'foo'.\n" +
        "  alert('Hello, World!');\n";

    String actualMessage = lightweightMessageFormatter.formatWarning(warning);

    assertEquals(expectedMessage, actualMessage);
    assertEquals(mockExcerpt, warning.getSourceExcerpt());
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Possible unused variable 'foo'.", null, null, 0);

    String expectedMessage = "WARNING - Possible unused variable 'foo'.";

    String actualMessage = lightweightMessageFormatter.withoutSource().formatWarning(warning);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testFormatErrorWithNullSourceExcerpt() {
    JSError error = new JSError("Expected identifier, but 'alert' was found.", null, null, 0);

    String expectedMessage = "Expected identifier, but 'alert' was found.\n";

    String actualMessage = lightweightMessageFormatter.formatError(error);

    assertEquals(expectedMessage, actualMessage);
    assertNull(error.getSourceExcerpt());
  }

  @Test
  public void testFormatWarningWithNullSourceExcerpt() {
    JSError warning = new JSError("Possible unused variable 'foo'.", null, null, 0);

    String expectedMessage = "WARNING - Possible unused variable 'foo'.";

    String actualMessage = lightweightMessageFormatter.formatWarning(warning);

    assertEquals(expectedMessage, actualMessage);
    assertNull(warning.getSourceExcerpt());
  }

  @Test
  public void testLineNumberingFormatterFormatLine() {
    ExcerptFormatter lineNumberingFormatter = new LineNumberingFormatter();

    String line = "function foo() {\n" +
        "  alert('Hello, World!');\n" +
        "}";

    int lineNumber = 3;

    String expectedMessage = "  ";

    String actualMessage = lineNumberingFormatter.formatLine(line, lineNumber);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testLineNumberingFormatterFormatRegion() {
    ExcerptFormatter lineNumberingFormatter = new LineNumberingFormatter();

    String code = "function foo() {\n" +
        "  alert('Hello, World!');\n" +
        "}";

    Region region = new Region(code.substring(0, code.indexOf("\n", 0)), null, 1, 1);

    String expectedMessage = " 1| function foo() {\n";

    String actualMessage = lineNumberingFormatter.formatRegion(region);

    assertEquals(expectedMessage, actualMessage);
  }
}

class Region {
  private int beginningLineNumber;
  private SourceExcerpt sourceExcerpt;

  public Region(String code, SourceExcerpt sourceExcerpt, int beginningLineNumber, int endingLineNumber) {
    this.sourceExcerpt = sourceExcerpt;
    this.beginningLineNumber = beginningLineNumber;
  }

  public String getSourceExcerpt() {
    return sourceExcerpt != null ? sourceExcerpt.getSource() : "";
  }

  public int getBeginningLineNumber() {
    return beginningLineNumber;
  }

  public int getEndingLineNumber() {
    return beginningLineNumber;
  }
}

class LineNumberingFormatter implements ExcerptFormatter {
  public static final String LINE = "LINE";

  public String formatLine(String line, int lineNumber) {
    return "  " + lineNumber + "| " + line;
  }

  public String formatRegion(Region region) {
    if (region == null) {
      return null;
    }

    String code = region.getSourceExcerpt();
    if (code.length() == 0) {
      return null;
    }

    if (region.getEndingLineNumber() <= region.getBeginningLineNumber()) {
      throw new IllegalStateException("Invalid region.");
    }

    StringBuilder builder = new StringBuilder();

    int start = 0;
    int end = code.indexOf('\n', start);
    int lineNumber = region.getBeginningLineNumber();

    while (start >= 0) {
      if (end < 0) {
        builder.append(code.substring(start));
        start = -1;
      } else {
        builder.append(formatLine(code.substring(start, end), lineNumber));
        builder.append('\n');
        start = code.indexOf('\n', end + 1);
        lineNumber++;
      }
    }

    return builder.toString();
  }
}