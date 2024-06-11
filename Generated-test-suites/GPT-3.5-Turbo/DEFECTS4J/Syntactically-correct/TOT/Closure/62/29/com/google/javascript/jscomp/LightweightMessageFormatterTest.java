package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }

  @Test
  public void testFormatError_WithSourceNameAndLineNumber() {
    JSError error = new JSError("This is an error", "test.js", 10);
    String expected = "test.js:10: ERROR - This is an error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatError_WithoutSourceNameAndLineNumber() {
    JSError error = new JSError("This is an error");
    String expected = "ERROR - This is an error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning_WithSourceNameAndLineNumber() {
    JSError warning = new JSError("This is a warning", "test.js", 5);
    String expected = "test.js:5: WARNING - This is a warning\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning_WithoutSourceNameAndLineNumber() {
    JSError warning = new JSError("This is a warning");
    String expected = "WARNING - This is a warning\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormat_WithoutSourceExcerpt() {
    JSError error = new JSError("This is an error", "test.js", 10);
    String expected = "test.js:10: ERROR - This is an error\n";
    String actual = formatter.format(error, false);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormat_WithSourceExcerpt() {
    JSError error = new JSError("This is an error", "test.js", 10);
    SourceExcerptProvider provider = new MockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(provider);
    String expected = "test.js:10: ERROR - This is an error\n";
    expected += "This is a test excerpt\n";
    expected += "^";
    String actual = formatter.format(error, false);
    assertEquals(expected, actual);
  }

  @Test
  public void testLineNumberingFormatter_FormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("test", 1, 3, "Line 1\nLine 2\nLine 3");
    String expected = "  1| Line 1\n  2| Line 2\n  3| Line 3\n";
    String actual = formatter.formatRegion(region);
    assertEquals(expected, actual);
  }

  @Test
  public void testLineNumberingFormatter_FormatRegion_EmptyRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("test", 1, 1, "");
    String actual = formatter.formatRegion(region);
    assertNull(actual);
  }

  @Test
  public void testLineNumberingFormatter_FormatRegion_NullRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String actual = formatter.formatRegion(null);
    assertNull(actual);
  }

  class MockSourceExcerptProvider implements SourceExcerptProvider {

    public String getSourceLine(String sourceName, int lineNumber) {
      return "This is a test excerpt";
    }

    public String getSource(int lineNumber) {
      return null;
    }

  }
}