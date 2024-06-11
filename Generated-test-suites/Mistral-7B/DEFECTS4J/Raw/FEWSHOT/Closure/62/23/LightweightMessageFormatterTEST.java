package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TestError", "Test description", 1, 1, 1);
    String result = formatter.formatError(error);
    String expected = String.format("%s - TestError: Test description%n", CheckLevel.ERROR.getName());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    String source = "function test() {\n" +
        "  return 1;\n" +
        "}";

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source) {
        return new SourceExcerpt(source, 1, source.length());
      }
    }, LINE);
    JSError error = new JSError("TestError", "Test description", 1, 1, 1);
    String result = formatter.formatError(error);
    String expected = String.format("%s:1: %s - TestError: Test description%n%s%n", CheckLevel.ERROR.getName(), source.substring(0, 60), source.substring(60));
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("TestWarning", "Test description", 1, 1, 1);
    String result = formatter.formatWarning(warning);
    String expected = String.format("%s - TestWarning: Test description%n", CheckLevel.WARNING.getName());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    String source = "function test() {\n" +
        "  return 1;\n" +
        "}";

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, String sourceName, int lineNumber) {
        return new SourceExcerpt(source, lineNumber, lineNumber + source.length());
      }
    }, LINE);
    JSError warning = new JSError("TestWarning", "Test description", "test.js", 1, 1, 1);
    String result = formatter.formatWarning(warning);
    String expected = String.format("%s:1: test.js: %s - TestWarning: Test description%n%s%n", CheckLevel.WARNING.getName(), source.substring(0, 60));
    assertEquals(expected, result);
  }

  @Test
  public void testFormatRegion() {
    String source = "function test() {\n" +
        "  if (foo) {\n" +
        "    alert('bar');\n" +
        "  } else {\n" +
        "    baz();\n" +
        "  }\n" +
        "}";

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String source, String sourceName, int lineNumber, int columnNumber) {
        return new SourceExcerpt(source, lineNumber, columnNumber + source.length());
      }
    }, LINE);
    Region region = new Region(0, 0, 4, 1);
    String result = formatter.formatRegion(region, source);
    String expected = String.format("%s:1:%n  %s:1:1| %s%n  %s:1:4| %s%n  %s:1:5| %s%n  %s:1:6| %s%n  %s:1:7| %s%n  %s:1:8| %s%n", " ", "function test() {", "  ", "    ", "if (foo) {", "      ", "alert('bar');", "    } else {", "      ", "baz();", "  }", "}");
    assertEquals(expected, result);
  }
}