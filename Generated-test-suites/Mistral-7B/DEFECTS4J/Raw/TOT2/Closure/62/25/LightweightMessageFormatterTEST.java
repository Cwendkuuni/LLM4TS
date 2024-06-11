package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestsForJSError;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber,
          SourceExcerpt excerpt) {
        return "Test Source";
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider, LIGHTWEIGHT_MESSAGE_FORMATTER.LINE);

    JSError error = new JSError("Test Error", "Test File.java", 1, 10,
        "Test Description");
    String result = formatter.formatError(error);

    String expected = "Test File:1:10 Test Error - ERROR: Test Description\n" +
        "Test Source\n" +
        " ^";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber,
          SourceExcerpt excerpt) {
        return "Test Source";
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider, LIGHTWEIGHT_MESSAGE_FORMATTER.LINE);

    JSError warning = new JSError("Test Warning", "Test File.java", 1, 10,
        "Test Description");
    String result = formatter.formatWarning(warning);

    String expected = "Test File:1:10 WARNING: Test Description\n" +
        "Test Source\n";

    assertEquals(expected, result);
  }
}