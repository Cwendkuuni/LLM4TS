package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, "Error message", 1, 1);
    String result = formatter.formatError(error);
    assertEquals("Error message\n", result);
  }

  @Test
  public void testFormatErrorWithSource() {
    String source = "function foo() {\n" +
        "  bar();\n" +
        "}";
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(SourceFile sourceFile) {
            return new SourceExcerpt(source, sourceFile.getOriginalName());
          }
        }, LINE);
    JSError error = new JSError(new SourceFile("Test.js", null), "Error message", 1, 5);
    String result = formatter.formatError(error);
    assertEquals("Test.js:1:5 Error - Error message\n" +
        "function foo() {\n" +
        "  bar();\n" +
        "}", result);
  }

  @Test
  public void testFormatWarningNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(null, "Warning message", 1, 1);
    String result = formatter.formatWarning(warning);
    assertEquals("Warning - Warning message\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    String source = "function foo() {\n" +
        "  bar();\n" +
        "}";
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(SourceFile sourceFile) {
            return new SourceExcerpt(source, sourceFile.getOriginalName());
          }
        }, LINE);
    JSError warning = new JSError(new SourceFile("Test.js", null), "Warning message", 1, 5);
    String result = formatter.formatWarning(warning);
    assertEquals("Test.js:1:5 Warning - Warning message\n" +
        "function foo() {\n" +
        "  bar();\n" +
        "}", result);
  }

  @Test
  public void testFormatEmptySourceExcerpt() {
    String source = "";
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(SourceFile sourceFile) {
            return new SourceExcerpt(source, sourceFile.getOriginalName());
          }
        }, LINE);
    JSError error = new JSError(new SourceFile("Test.js", null), "Error message", 1, 1);
    String result = formatter.formatError(error);
    assertEquals("Error message\n", result);
  }

  @Test
  public void testFormatNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = new JSError(null, "Error message", 1, 1);
    String result = formatter.formatError(error);
    assertEquals("Error message\n", result);
  }

  @Test
  public void testFormatRegion() {
    String source = "function foo() {\n" +
        "  if (x > 10) {\n" +
        "    bar();\n" +
        "  } else {\n" +
        "    baz();\n" +
        "  }\n" +
        "}";
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(SourceFile sourceFile) {
            return new SourceExcerpt(source, sourceFile.getOriginalName());
          }
        }, LINE);
    Region region = new Region(new SourceFile("Test.js", null), 3, 5, 3, 15);
    String result = formatter.formatRegion(region);
    assertEquals("   3| if (x > 10) {\n" +
        "   4|   bar();\n" +
        "   5| }\n" +
        "   6| else {\n" +
        "   7|   baz();\n" +
        "   8| }\n", result);
  }

  @Test
  public void testFormatRegionNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    Region region = new Region(null, 3, 5, 3, 15);
    String result = formatter.formatRegion(region);
    assertNull(result);
  }
}