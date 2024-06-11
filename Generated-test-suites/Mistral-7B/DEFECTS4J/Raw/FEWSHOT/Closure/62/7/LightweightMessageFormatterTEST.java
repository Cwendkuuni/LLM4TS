package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithSource() {
    String errorDescription = "Invalid syntax";
    JSError error = new JSError("Source.js", 10, 20, errorDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new FakeSourceExcerptProvider("Source.js"), LINE);
    String result = formatter.formatError(error);

    assertEquals("Source.js:10:20 ERROR - Invalid syntax\n" +
        "Source.js:10:20:\n", result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    String errorDescription = "Invalid syntax";
    JSError error = new JSError("", -1, -1, errorDescription, null);

    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String result = formatter.formatError(error);

    assertEquals("ERROR - Invalid syntax\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    String warningDescription = "Use of deprecated feature";
    JSError warning = new JSError("Source.js", 15, 30, warningDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new FakeSourceExcerptProvider("Source.js"), LINE);
    String result = formatter.formatWarning(warning);

    assertEquals("Source.js:15:30 WARNING - Use of deprecated feature\n", result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    String warningDescription = "Use of deprecated feature";
    JSError warning = new JSError("", -1, -1, warningDescription, null);

    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - Use of deprecated feature\n", result);
  }

  @Test
  public void testFormatErrorWithRegion() {
    String errorDescription = "Invalid syntax";
    JSError error = new JSError("Source.js", 25, 45, errorDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new FakeSourceExcerptProvider("Source.js"), REGION);

    StringBuilder sourceCode = new StringBuilder("function foo() {\n" +
        "  bar = 1;\n" +
        "  if (baz) {\n" +
        "    qux(); // error line\n" +
        "  }\n" +
        "}");

    String result = formatter.formatError(error);

    assertEquals("Source.js:26:51 ERROR - Invalid syntax\n" +
        "Source.js:\n" +
        "24 | function foo() {\n" +
        "25 |   bar = 1;\n" +
        "26 |   if (baz) {\n" +
        " > 27 |     qux(); // error line\n" +
        "28 |   }\n" +
        "29 | }\n", result);
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    String errorDescription = "Invalid syntax";
    JSError error = new JSError("", -1, -1, errorDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new FakeSourceExcerptProvider(""), LINE);
    String result = formatter.formatError(error);

    assertEquals("ERROR - Invalid syntax\n", result);
  }

  @Test
  public void testFormatWarningWithEmptySource() {
    String warningDescription = "Use of deprecated feature";
    JSError warning = new JSError("", -1, -1, warningDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new FakeSourceExcerptProvider(""), LINE);
    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - Use of deprecated feature\n", result);
  }

  @Test
  public void testFormatErrorWithNullSource() {
    String errorDescription = "Invalid syntax";
    JSError error = new JSError("", -1, -1, errorDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null, LINE);
    String result = formatter.formatError(error);

    assertNull(result);
  }

  @Test
  public void testFormatWarningWithNullSource() {
    String warningDescription = "Use of deprecated feature";
    JSError warning = new JSError("", -1, -1, warningDescription, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null, LINE);
    String result = formatter.formatWarning(warning);

    assertNull(result);
  }

  private static class FakeSourceExcerptProvider implements SourceExcerptProvider {
    private String sourceName;

    public FakeSourceExcerptProvider(String sourceName) {
      this.sourceName = sourceName;
    }

    @Override
    public SourceExcerpt getSource(JSError error) {
      return new SourceExcerpt() {
        @Override
        public String getSourceName() {
          return sourceName;
        }

        @Override
        public int getLength() {
          return 0;
        }

        @Override
        public char charAt(int index) {
          throw new UnsupportedOperationException();
        }

        @Override
        public String getSubSequence(int start, int end) {
          throw new UnsupportedOperationException();
        }
      };
    }
  }
}