package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.REGION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  @Test
  public void testFormatErrorNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Test Error", 1, "test.js:10: error message");
    assertEquals(
        "error: Test Error - error message\n", formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new DummySourceExcerptProvider(), LINE);
    JSError error = new JSError("Test Error", "test.js", 10, "test.js:10: error message");
    assertEquals(
        "test.js:10: error: Test Error - error message\n", formatter.formatError(error));
  }

  @Test
  public void testFormatWarningNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Test Warning", 1, "test.js:10: warning message");
    assertEquals(
        "warning: Test Warning - warning message\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new DummySourceExcerptProvider(), LINE);
    JSError warning = new JSError("Test Warning", "test.js", 10, "test.js:10: warning message");
    assertEquals(
        "test.js:10: warning: Test Warning - warning message\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatRegionNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    Region region = new Region("test.js", 9, 10, 11);
    assertNull(formatter.getSource().get(region));
    assertNull(formatter.formatRegion(region));
  }

  @Test
  public void testFormatRegionWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new DummySourceExcerptProvider(), REGION);
    Region region = new Region("test.js", 9, 10, 11);
    String source = "if (foo) {\n  alert('bar');\n}";
    Preconditions.checkState(formatter.getSource() != null);
    formatter.getSource().put(region, source);
    assertEquals(
        " 1| if (foo)\n 2|   alert('bar');\n", formatter.formatRegion(region));
  }
}

class DummySourceExcerptProvider implements SourceExcerptProvider {
  @Override
  public SourceExcerpt get(Region region) {
    return null;
  }
}