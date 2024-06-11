package com.google.javascript.jscomp.test.unit.messaging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.test.unit.messaging.testutils.TestSourceExcerptProvider;

import org.junit.Test;

/**
 * Tests for {@link LightweightMessageFormatter}.
 */
public class LightweightMessageFormatterTest {

  private SourceExcerptProvider source = mock(SourceExcerptProvider.class);

  private LightweightMessageFormatter formatter =
      new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

  @Test
  public void testErrorMessageWithSource() {
    when(source.getExcerpt(TestSourceExcerptProvider.TEST_FILE, 1, 10, null))
        .thenReturn("function test() {\n  return 10;\n}");

    JSError error = new JSError("Test Error", "test.js", 1, 10, "Test Error", null);

    String message = formatter.formatError(error);
    assertEquals("ERROR - Test Error: function test() {\n  return 10;\n}^", message);
  }

  @Test
  public void testErrorMessageWithoutSource() {
    LightweightMessageFormatter formatterWithoutSource =
        LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Test Error", null, 1, 10, "Test Error", null);

    String message = formatterWithoutSource.formatError(error);
    assertEquals("ERROR - Test Error\n", message);
  }

  @Test
  public void testWarningMessageWithSource() {
    when(source.getExcerpt(TestSourceExcerptProvider.TEST_FILE, 1, 5, null))
        .thenReturn("function test() {\n  // Warning comment\n  return 5;\n}");

    JSError warning = new JSError("Test Warning", "test.js", 1, 5, "Test Warning", null);

    String message = formatter.formatWarning(warning);
    assertEquals("WARNING - Test Warning: function test() {\n  // Warning comment\n}^", message);
  }

  @Test
  public void testWarningMessageWithoutSource() {
    LightweightMessageFormatter formatterWithoutSource =
        LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Test Warning", null, 1, 5, "Test Warning", null);

    String message = formatterWithoutSource.formatWarning(warning);
    assertEquals("WARNING - Test Warning\n", message);
  }

  @Test
  public void testErrorMessageWithRegion() {
    when(source.getExcerpt(TestSourceExcerptProvider.TEST_FILE, 2, 3, null))
        .thenReturn("function test() {\n  if (condition) {\n    return 3;\n  } else {\n" +
            "    return 4;\n  }\n}");

    JSError error = new JSError("Test Error", "test.js", 2, 5, "Test Error", null);

    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

    String message = formatter.formatError(error);

    assertEquals("2| if (condition) {\n" +
        "  3|   return 3;\n" +
        "}^", message);
  }

  @Test
  public void testErrorMessageWithLongSourceLine() {
    when(source.getExcerpt(TestSourceExcerptProvider.TEST_FILE, 1, 100, null))
        .thenReturn("function test() {\n  var veryLongLine = 'Test long string of characters longer than 80 columns with spaces'\n" +
            "return 100;\n}");

    JSError error = new JSError("Test Error", "test.js", 1, 100, "Test Error", null);

    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

    String message = formatter.formatError(error);

    assertEquals("ERROR - Test Error: function test() {\n  var veryLongLine = 'Test long string of characters longer than 80 columns with spaces'\n}^", message);
  }
}