package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.testing.BaseTest;
import java.util.Arrays;
import org.junit.Test;

public class LightweightMessageFormatterTest extends BaseTest {
  private final SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);

  @Test
  public void testFormatErrorWithSource() {
    String sourceCode = "function foo() {\n" +
            "  var x = 1;\n" +
            "  if (x < 1) {\n" +
            "    throw new Error('x should be greater than 0');\n" +
            "  }\n" +
            "}";
    when(sourceProvider.getSource(JSError.SourceType.NORMAL, "MyFile.js")).thenReturn(sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    JSError error = new JSError(
        "MyFile.js",
        3,
        3,
        "x should be greater than 0",
        new SourcePosition(3, 3));

    String expectedResult = "MyFile.js:3: x should be greater than 0\n" +
            "  var x = 1;\n" +
            "  if (x < 1) {\n" +
            "    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
    assertEquals(expectedResult, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error message");

    String expectedResult = "ERROR - Error message\n";
    assertEquals(expectedResult, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    JSError warning = new JSError(
        "MyFile.js",
        3,
        3,
        "Possible unused variable: x",
        new SourcePosition(3, 3));

    String expectedResult = "MyFile.js:3: WARNING - Possible unused variable: x\n" +
            "  var x = 1;\n";
    assertEquals(expectedResult, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Warning message");

    String expectedResult = "WARNING - Warning message\n";
    assertEquals(expectedResult, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithRegion() {
    String sourceCode = "function foo() {\n" +
            "  var x = 1;\n" +
            "  if (x < 1) {\n" +
            "    throw new Error('x should be greater than 0');\n" +
            "  }\n" +
            "}";
    when(sourceProvider.getSource(JSError.SourceType.NORMAL, "MyFile.js"))
        .thenReturn(sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    JSError error = new JSError(
        "MyFile.js",
        3,
        3,
        "x should be greater than 0",
        new SourcePosition(3, 3),
        Arrays.asList(new SourcePosition(3, 3), new SourcePosition(3, 5)),
        Arrays.asList(3));

    String expectedResult = "MyFile.js:3: ERROR - x should be greater than 0\n" +
            "  var x = 1;\n" +
            "  if (x < 1) {\n" +
            "    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
    assertEquals(expectedResult, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithMalformedSourceRegion() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    JSError error = new JSError(
        "MyFile.js",
        3,
        3,
        "x should be greater than 0",
        new SourcePosition(3, 3),
        Arrays.asList(new SourcePosition(3, 3), new SourcePosition(3, 5)),
        Arrays.asList(3, 4));

    assertNull(formatter.formatError(error));
  }
}