package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.BaseCompilerTestCase;

/**
 * Tests for {@link LightweightMessageFormatter}.
 *
 */
public class LightweightMessageFormatterTest extends BaseCompilerTestCase {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider());
    JSError error = JSError.make("TestError", "file.js", 1, 1, null, null,
        "Error message", null);
    String actual = formatter.formatError(error);
    String expected = "ERROR - Error message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider());
    JSError warning = JSError.make("TestWarning", "file.js", 1, 1, null, null,
        "Warning message", null);
    String actual = formatter.formatWarning(warning);
    String expected = "WARNING - Warning message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider(), SourceExcerpt.LINE);
    JSError error = JSError.make("TestError", "file.js", 1, 1, "function test() {}",
        null, null, "Error message", null);
    String actual = formatter.formatError(error);
    String expected = "file.js:1: function test() {^\nERROR - Error message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithRegion() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider(), SourceExcerpt.REGION);
    String code = "function test() {\n" + "  if (foo) {\n" + "    alert('bar');\n"
        + "  }\n}";
    JSError error = JSError.make("TestError", "file.js", 1, 1, null, null,
        "Error message", new Region(code, 1, 5, 1, 13));
    String actual = formatter.formatError(error);
    String expected = "1| function test() {\n" + "  ^^^^\n" + "  if (foo) {\n"
        + "    ^^\n" + "    alert('bar');\n" + "  }\n" + "}\n" + "ERROR - Error message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithOutOfRangeRegion() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider(), SourceExcerpt.REGION);
    String code = "function test() {\n" + "  if (foo) {\n" + "    alert('bar');\n"
        + "  }\n}";
    JSError error = JSError.make("TestError", "file.js", 1, 1, null, null,
        "Error message", new Region(code, 1, 5, 1, 12));
    String actual = formatter.formatError(error);
    // nothing should be printed
    assertEquals("", actual);
  }

  @Test
  public void testFormatWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = JSError.make("TestError", null, 1, 1, null, null,
        "Error message", null);
    String actual = formatter.formatError(error);
    String expected = "ERROR - Error message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider());
    String actual = formatter.formatError(null);
    assertEquals("", actual);
  }

  @Test
  public void testFormatWithNullWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new StubSourceExcerptProvider());
    String actual = formatter.formatWarning(null);
    assertEquals("", actual);
  }

  private static class StubSourceExcerptProvider implements SourceExcerptProvider {
    @Override
    public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
      return SourceExcerpt.fromCode("function test() {}", sourceName, lineNumber);
    }

    @Override
    public SourceExcerpt getSourceExcerpt(Region region) {
      return region;
    }

    @Override
    public String getSourceCode(String sourceName) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void checkValid() {
      // no-op
    }
  }

}