package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static com.google.javascript.jscomp.LightweightMessageFormatter.Region;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Lines;
import com.google.javascript.jscomp.util.Strings;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, "SomeError", 1, 1, "description");
    String message = formatter.formatError(error);
    assertEquals("SomeError - description\n", message);

    error.sourceName = "Test.java";
    error.lineNumber = 2;
    message = formatter.formatError(error);
    assertEquals("Test.java:2: SomeError - description\n", message);
  }

  @Test
  public void testLightweightMessageFormatterWithSource() {
    String code = "function foo() {\n" +
        "  if (x) {\n" +
        "    alert('bar');\n" +
        "  }\n" +
        "}";

    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource(String sourceName, int lineNumber) {
        return new SourceExcerpt(Lines.lines(code), LINE);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError(source, "Test.java", 2, 3, "description");
    String message = formatter.formatError(error);
    assertEquals("Test.java:2: SomeError - description\n" + code.substring(0, 3), message);

    error.lineNumber = 4;
    message = formatter.formatError(error);
    assertEquals("Test.java:2: SomeError - description\n" + code.substring(0, 3) + "^\n" + code.substring(3), message);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = new JSError(null, "SomeWarning", 1, 1, "description");
    String message = formatter.formatWarning(warning);
    assertEquals("Warning - description\n", message);
  }

  @Test
  public void testFormatRegion() {
    String code = "function foo() {\n" +
        "  if (x) {\n" +
        "    alert('bar');\n" +
        "  }\n" +
        "}";

    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource(String sourceName, int lineNumber) {
        return new SourceExcerpt(Lines.lines(code), LINE);
      }

      @Override
      public Region getRegion(String sourceName, int beginLineNumber, int endLineNumber) {
        return new Region(sourceName, beginLineNumber, endLineNumber, LINE);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    Region region = new Region("Test.java", 2, 3);
    String message = formatter.formatRegion(region);
    assertEquals("  Test.java:2: | function foo() {\n" +
        "  Test.java:2: |   if (x) {\n" +
        "  Test.java:2: |     alert('bar');\n" +
        "  Test.java:3: |   }\n" +
        "  Test.java:3: | }\n", message);
  }
}

class Region {
  private final String sourceName;
  private final int beginningLineNumber;
  private final int endingLineNumber;
  private final ExcerptFormatter excerptFormatter;

  public Region(String sourceName, int beginningLineNumber, int endingLineNumber, ExcerptFormatter excerptFormatter) {
    this.sourceName = sourceName;
    this.beginningLineNumber = beginningLineNumber;
    this.endingLineNumber = endingLineNumber;
    this.excerptFormatter = excerptFormatter;
  }

  public String getSourceExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource(String sourceName, int lineNumber) {
        return new SourceExcerpt(Lines.lines(getCode()), LINE);
      }
    };
    return excerptFormatter.formatRegion(new Region(sourceName, beginningLineNumber, endingLineNumber), source);
  }
}

class TestSourceExcerptProvider implements SourceExcerptProvider {
  @Override
  public SourceExcerpt getSource(String sourceName, int lineNumber) {
    return new SourceExcerpt("Test".getBytes(), LINE);
  }
}