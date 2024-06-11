package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt.LINE;
import static com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt.REGION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.TestSourceFile;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterConstructors() {
    // Test default constructor
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNull(formatter.getSource());

    // Test constructor with SourceExcerptProvider
    SourceExcerptProvider sourceProvider = new TestSourceFile();
    LightweightMessageFormatter formatterWithSource =
        new LightweightMessageFormatter(sourceProvider);
    assertEquals(sourceProvider, formatterWithSource.getSource());

    // Test constructor with SourceExcerptProvider and LINE excerpt
    LightweightMessageFormatter formatterWithLineExcerpt =
        new LightweightMessageFormatter(sourceProvider, LINE);
    assertEquals(sourceProvider, formatterWithLineExcerpt.getSource());

    // Test constructor with SourceExcerptProvider and REGION excerpt
    LightweightMessageFormatter formatterWithRegionExcerpt =
        new LightweightMessageFormatter(sourceProvider, REGION);
    assertEquals(sourceProvider, formatterWithRegionExcerpt.getSource());
  }

  @Test
  public void testFormatErrorWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertNull(formatter.formatError(null));
  }

  @Test
  public void testFormatWarningWithNullWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertNull(formatter.formatWarning(null));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("error description", "file.js", 1, 1);
    assertEquals(
        "error: error description\n",
        formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithSource() {
    TestSourceFile sourceProvider = new TestSourceFile();
    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(sourceProvider);
    JSError error = new JSError("error description", "file.js", 1, 1);

    SourceExcerpt excerpt = sourceProvider.getExcerpt(LINE, "file.js", 1);
    assertEquals(
        "file.js:1: error: error description\n" + excerpt,
        formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("warning description", "file.js", 1, 1);
    assertEquals(
        "warning: warning description\n",
        formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    TestSourceFile sourceProvider = new TestSourceFile();
    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(sourceProvider);
    JSError warning = new JSError("warning description", "file.js", 1, 1);

    SourceExcerpt excerpt = sourceProvider.getExcerpt(LINE, "file.js", 1);
    assertEquals(
        "file.js:1: warning: warning description\n" + excerpt,
        formatter.formatWarning(warning));
  }
}

class TestSourceFile implements SourceExcerptProvider {
  @Override
  public SourceExcerpt getExcerpt(SourceExcerpt excerpt, String resource, int lineNumber, ExcerptFormatter formatter) {
    switch (excerpt) {
      case LINE:
        return "line " + lineNumber + "\n";
      case REGION:
        return "region starting at line 1 and ending at line 3\nline 1: if (foo)\nline 2: {\nline 3: alert('bar');\n";
      default:
        throw new AssertionError("Invalid excerpt: " + excerpt);
    }
  }
}