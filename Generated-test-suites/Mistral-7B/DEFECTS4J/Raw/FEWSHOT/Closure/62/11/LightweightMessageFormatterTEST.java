package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.ExcerptFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Strings;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithNullSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error message", 1, 1, "sourceName", 1);
    String result = formatter.formatError(error);
    assertEquals("Error - Error message\n", result);
  }

  @Test
  public void testFormatErrorWithValidSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return new SourceExcerpt("sourceName", "sourceCode") {
              @Override
              public String getSourceCode() {
                return "sourceCode";
              }
            };
          }
        }, LINE);
    JSError error = new JSError("Error message", 1, 1, "sourceName", 1);
    String result = formatter.formatError(error);
    assertEquals("sourceName:1: Error - Error message\nsourceCode:\n^", result);
  }

  @Test
  public void testFormatWarningWithNullSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Warning message", 1, 1, "sourceName", 1);
    String result = formatter.formatWarning(warning);
    assertEquals("Warning - Warning message\n", result);
  }

  @Test
  public void testFormatWarningWithValidSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return new SourceExcerpt("sourceName", "sourceCode") {
              @Override
              public String getSourceCode() {
                return "sourceCode";
              }
            };
          }
        }, LINE);
    JSError warning = new JSError("Warning message", 1, 1, "sourceName", 1);
    String result = formatter.formatWarning(warning);
    assertEquals("sourceName:1: Warning - Warning message\nsourceCode:\n^", result);
  }

  @Test
  public void testFormatErrorWithNullExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return null;
          }
        }, null);
    JSError error = new JSError("Error message", 1, 1, "sourceName", 1);
    String result = formatter.formatError(error);
    assertNull(result);
  }

  @Test
  public void testFormatErrorWithEmptyExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return new SourceExcerpt("sourceName", "") {
            };
          }
        }, LINE);
    JSError error = new JSError("Error message", 1, 1, "sourceName", 1);
    String result = formatter.formatError(error);
    assertEquals("Error - Error message\n", result);
  }

  @Test
  public void testFormatErrorWithSourceRegion() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return null;
          }

          @Override
          public Region getSourceRegion(String sourceFile, int lineNumber) {
            return new Region("sourceName", new int[] { 0, 10 },
                new int[] { 0, 20 }, false);
          }
        }, LINE);
    JSError error = new JSError("Error message", 1, 1, "sourceName", 1);
    String result = formatter.formatError(error);
    assertEquals("sourceName:1: Error - Error message\nsourceCode:\n ^\n", result);
  }

  @Test
  public void testFormatWarningWithSourceRegion() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return null;
          }

          @Override
          public Region getSourceRegion(String sourceFile, int lineNumber) {
            return new Region("sourceName", new int[] { 0, 10 },
                new int[] { 0, 20 }, false);
          }
        }, LINE);
    JSError warning = new JSError("Warning message", 1, 1, "sourceName", 1);
    String result = formatter.formatWarning(warning);
    assertEquals("sourceName:1: Warning - Warning message\nsourceCode:\n ^\n", result);
  }

  @Test
  public void testFormatErrorWithSourceRegionAndCharno() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return null;
          }

          @Override
          public Region getSourceRegion(String sourceFile, int lineNumber) {
            return new Region("sourceName", new int[] { 0, 10 },
                new int[] { 0, 20 }, false);
          }
        }, LINE);
    JSError error = new JSError("Error message", 1, 5, "sourceName", 1);
    String result = formatter.formatError(error);
    assertEquals("sourceName:1: Error - Error message\nsourceCode:\n  ^\n", result);
  }

  @Test
  public void testFormatWarningWithSourceRegionAndCharno() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource() {
            return null;
          }

          @Override
          public Region getSourceRegion(String sourceFile, int lineNumber) {
            return new Region("sourceName", new int[] { 0, 10 },
                new int[] { 0, 20 }, false);
          }
        }, LINE);
    JSError warning = new JSError("Warning message", 1, 5, "sourceName", 1);
    String result = formatter.formatWarning(warning);
    assertEquals("sourceName:1: Warning - Warning message\nsourceCode:\n  ^\n", result);
  }
}