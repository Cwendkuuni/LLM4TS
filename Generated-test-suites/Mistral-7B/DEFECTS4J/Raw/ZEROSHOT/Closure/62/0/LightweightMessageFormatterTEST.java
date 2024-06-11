package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.test.testtype.Region;
import com.google.javascript.jscomp.test.testtype.TestExcerptProvider;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatter() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("TestError", 1, 1, 1, "Test Description", 1);
    assertEquals("ERROR - Test Description\n", formatter.formatError(error));

    SourceExcerptProvider source = new TestExcerptProvider();
    LightweightMessageFormatter formatterWithSource = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    error = new JSError("TestError", 1, 1, 1, "Test Description", 1, 10);
    String sourceExcerpt = source.getSourceExcerpt("TestFile.js", 1, 1, 10);
    String expected = String.format("ERROR - Test Description\n%s", sourceExcerpt);
    assertEquals(expected, formatterWithSource.formatError(error));
  }

  @Test
  public void testLineNumberingFormatter() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("TestFile.js", Arrays.asList(1, 2, 3), 1, 10, 15);
    String code = "function TestFunction() {"
        + "  if (foo) {"
        + "    bar();"
        + "  }"
        + "  alert('Hello World');"
        + "}";
    assertEquals(
        " 1| function TestFunction() {"
        + " 2|   if (foo) {"
        + " 3|     bar();"
        + " 4|   }"
        + " 5|  alert('Hello World');"
        + " 6|\n", formatter.formatRegion(region, code));
  }
}

class TestExcerptProvider implements SourceExcerptProvider {
  @Override
  public String getSourceExcerpt(String sourceName, int startLine, int startColumn,
      int endColumn) {
    String source = "function TestFunction() {"
        + "  if (foo) {"
        + "    bar();"
        + "  }"
        + "  alert('Hello World');"
        + "}";
    int startIndex = source.lastIndexOf('\n', startLine - 1) + 1;
    int endIndex = startIndex + 10;
    return source.substring(startIndex, endIndex);
  }
}