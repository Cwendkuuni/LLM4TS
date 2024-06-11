package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.test.util.TestSourceFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LightweightMessageFormatterTest {

  private static final String CLASS_NAME = "com.google.javascript.jscomp.LightweightMessageFormatter";

  @org.junit.Test
  public void testFormatErrorWithSource() {
    LightweightMessageFormatter formatter = createFormatterWithSource();
    JSError error = createError("example.js", 1, 1, "message");

    String actual = formatter.formatError(error);
    String expected = "example.js:1: ERROR - message\n"
        + "1:^";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = createFormatterWithoutSource();
    JSError error = createError("example.js", 1, 1, "message");

    String actual = formatter.formatError(error);
    String expected = "ERROR - message\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = createFormatterWithSource();
    JSError warning = createWarning("example.js", 1, 1, "message");

    String actual = formatter.formatWarning(warning);
    String expected = "example.js:1: WARNING - message\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = createFormatterWithoutSource();
    JSError warning = createWarning("example.js", 1, 1, "message");

    String actual = formatter.formatWarning(warning);
    String expected = "WARNING - message\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithRegion() {
    LightweightMessageFormatter formatter = createFormatterWithSource();
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getRegion(anyString(), anyInt(), anyInt())).thenReturn(
        createRegion("example.js", 1, 1, 4, "if (foo) {\n   alert('bar');\n}"));

    JSError error = createError("example.js", 1, 2, "message");

    String actual = formatter.formatError(error, source);
    String expected = "example.js:1: ERROR - message\n"
        + "1:2: ^ ";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithRegion() {
    LightweightMessageFormatter formatter = createFormatterWithSource();
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getRegion(anyString(), anyInt(), anyInt())).thenReturn(
        createRegion("example.js", 1, 1, 4, "if (foo) {\n   alert('bar');\n}"));

    JSError warning = createWarning("example.js", 1, 2, "message");

    String actual = formatter.formatWarning(warning, source);
    String expected = "example.js:1: WARNING - message\n"
        + "1:2: ^ ";
    assertEquals(expected, actual);
  }

  private LightweightMessageFormatter createFormatterWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    return new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
  }

  private LightweightMessageFormatter createFormatterWithoutSource() {
    return new LightweightMessageFormatter();
  }

  private JSError createError(String fileName, int lineNumber, int charno,
      String description) {
    return new JSError(fileName, lineNumber, charno, CheckLevel.ERROR,
        description);
  }

  private JSError createWarning(String fileName, int lineNumber, int charno,
      String description) {
    return new JSError(fileName, lineNumber, charno, CheckLevel.WARNING,
        description);
  }

  private Region createRegion(String fileName, int beginningLineNumber,
      int beginningColumn, int endingLineNumber, String code) {
    return new Region(new File(fileName), beginningLineNumber,
        beginningColumn, endingLineNumber, code);
  }
}