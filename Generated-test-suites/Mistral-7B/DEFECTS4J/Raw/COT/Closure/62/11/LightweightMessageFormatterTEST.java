package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

/**
 * Test class for LightweightMessageFormatter
 */
public class LightweightMessageFormatterTest {

  @Test
  public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = newJSError(1, "TestFile.js", 3, "message");
    String result = formatter.formatError(error);
    assertEquals("ERROR - message\n", result);
  }

  @Test
  public void testConstructorWithSource() {
    SourceExcerptProvider sourceProvider = new TestSourceFile("TestFile.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError error = newJSError(1, "TestFile.js", 3, "message");
    String result = formatter.formatError(error);
    assertEquals("TestFile.js:3: ERROR - message\n", result);
  }

  @Test
  public void testFormatError() {
    SourceExcerptProvider sourceProvider = new TestSourceFile("TestFile.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError error = newJSError(1, "TestFile.js", 3, "message");
    String expected = "TestFile.js:3: ERROR - message\nTestFile.js:3:^\n";
    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider sourceProvider = new TestSourceFile("TestFile.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError warning = newJSError(1, "TestFile.js", 3, "message", CheckLevel.WARNING);
    String result = formatter.formatWarning(warning);
    assertEquals("TestFile.js:3: WARNING - message\n", result);
  }

  @Test(expected = NullPointerException.class)
  public void testNullSourceProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExcerpt() {
    SourceExcerptProvider sourceProvider = new TestSourceFile("TestFile.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, null);
  }

  private JSError newJSError(int lineNumber, String sourceName, int sourceChar, String message) {
    return new JSError(sourceName, lineNumber, sourceChar, message);
  }

  private List<JSError> newErrors(String... messages) {
    return ImmutableList.copyOf(ImmutableList.builder().addAll(messages).build());
  }

  private class TestSourceFile implements SourceExcerptProvider {
    private final String fileName;

    public TestSourceFile(String fileName) {
      this.fileName = fileName;
    }

    @Override
    public SourceExcerpt getSource(String name, int lineNumber, int start, int end) throws IOException {
      if (!fileName.equals(name)) {
        throw new IllegalArgumentException("Unexpected source name");
      }
      String code = "class Test {\n" +
          "  if (true) {\n" +
          "    alert('message');\n" +
          "  }\n}";
      return new SourceExcerpt(code, start, end);
    }

    @Override
    public String getSourceFileName(String name) {
      return fileName;
    }

    @Override
    public void close() throws IOException {}
  }
}