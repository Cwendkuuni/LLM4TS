package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestFactory;
import com.google.javascript.jscomp.testing.TestSourceFile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for {@link LightweightMessageFormatter}.
 */
public class LightweightMessageFormatterTest {

  private static final SourceExcerptProvider SOURCE_PROVIDER =
      new TestSourceFile();

  @Test
  public void testCreateWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = newJSError("Error Message", 1, 1);
    String result = formatter.formatError(error);
    assertEquals("ERROR - Error Message\n", result);
  }

  @Test
  public void testCreateWithSource() {
    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    JSError error = newJSError("Error Message", new TestSourceFile("Test.js"), 1, 1);
    String result = formatter.formatError(error);
    assertEquals("Test.js:1: ERROR - Error Message\n", result);
  }

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    JSError error = newJSError("Error Message", new TestSourceFile("Test.js"), 1, 1);
    String result = formatter.formatError(error);
    assertEquals("Test.js:1: ERROR - Error Message\n", result);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    JSError warning = newJSError("Warning Message", new TestSourceFile("Test.js"), 1, 1);
    String result = formatter.formatWarning(warning);
    assertEquals("Test.js:1: WARNING - Warning Message\n", result);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError error = newJSError("Error Message", 1, 1);
    formatter.formatError(error);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatWarningWithNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError warning = newJSError("Warning Message", 1, 1);
    formatter.formatWarning(warning);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    formatter.formatError(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWarningWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    formatter.formatWarning(null);
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    JSError error = newJSError("Error Message", new TestSourceFile("Test.js"), 1, 1);
    String result = formatter.formatError(error);
    assertEquals("ERROR - Error Message\n", result);
  }

  @Test
  public void testLineNumberingFormatterFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "function foo() {}";
    int lineNumber = 1;
    String result = formatter.formatLine(line, lineNumber);
    assertEquals("  " + lineNumber + "| ", result);
  }

  @Test
  public void testLineNumberingFormatterFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String code = "function foo() {\n  alert('bar');\n}";
    Region region = new Region(1, 1, 1, 4);
    String result = formatter.formatRegion(region);
    List<String> expected = new ArrayList<>();
    expected.add("  " + 1 + "| function foo() {\n");
    expected.add("    " + 1 + "|   alert('bar');\n");
    expected.add("  " + 1 + "| }");
    for (String expectedLine : expected) {
      assertEquals(expectedLine, formatter.formatLine(code, region.getBeginningLineNumber()));
    }
  }

  @Test
  public void testFormatErrorWithLongSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(SOURCE_PROVIDER, LightweightMessageFormatter.LINE);
    JSError error =
        newJSError("Error Message", new TestSourceFile("Test.js", "long source string"), 1, 1);
    String result = formatter.formatError(error);
    assertEquals("Test.js:1: ERROR - Error Message\nlong source string\n", result);
  }

  private JSError newJSError(String description, SourceExcerpt source, int lineNumber, int charno) {
    JSError error = new JSError(new JSError.Location(source, lineNumber, charno));
    error.description = description;
    return error;
  }

  private JSError newJSError(String description, String sourceName, int lineNumber, int charno) {
    Preconditions.checkNotNull(sourceName);
    JSError error = new JSError(new JSError.Location(new TestSourceFile(sourceName), lineNumber, charno));
    error.description = description;
    return error;
  }

  // Test source file for testing purposes
  private static class TestSourceFile implements SourceExcerptProvider {

    private final String name;
    private final String code;

    public TestSourceFile() {
      this(null, "");
    }

    public TestSourceFile(String name) {
      this(name, "");
    }

    public TestSourceFile(String name, String code) {
      this.name = name;
      this.code = code;
    }

    @Override
    public SourceExcerpt getSourceExcerpt(String name, int lineNumber, int charno,
        ExcerptFormatter formatter) {
      return new SourceExcerpt(name, formatter.formatRegion(new Region(lineNumber, charno, lineNumber, charno + code.length())), code);
    }

    @Override
    public SourceExcerpt getSourceExcerpt(String name, int lineNumber, int charno) {
      return getSourceExcerpt(name, lineNumber, charno, new LineNumberingFormatter());
    }
  }

  // Region for testing purposes
  private static class Region {

    private final int beginningLineNumber;
    private final int charno;
    private final int endingLineNumber;

    public Region(int beginningLineNumber, int charno, int endingLineNumber, int length) {
      this.beginningLineNumber = beginningLineNumber;
      this.charno = charno;
      this.endingLineNumber = endingLineNumber;
    }

    public int getBeginningLineNumber() {
      return beginningLineNumber;
    }

    public int length() {
      return endingLineNumber - beginningLineNumber + 1;
    }
  }
}