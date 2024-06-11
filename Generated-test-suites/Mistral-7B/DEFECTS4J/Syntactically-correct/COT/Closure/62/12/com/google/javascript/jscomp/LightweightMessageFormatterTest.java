import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.CheckLevel;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    ExcerptFormatter excerptFormatter = new LineNumberingFormatter();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        10,
        20,
        "Test message");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(excerptFormatter)))
        .thenReturn("test code");

    String result = formatter.formatError(error);

    String expected = "test.js:1: Test message\n" +
                      "test code\n" +
                      "^\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    JSError error = new JSError(
        "TypeError",
        null,
        1,
        10,
        20,
        "Test message");

    String result = formatter.formatError(error);

    String expected = "TypeError - Test message\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        10,
        20,
        "Test warning");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatWarning(warning);

    String expected = "test.js:1: WARNING - Test warning\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    JSError warning = new JSError(
        "DeprecationWarning",
        null,
        1,
        10,
        20,
        "Test warning");

    String result = formatter.formatWarning(warning);

    String expected = "DeprecationWarning - Test warning\n";

    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullSourceExcerptProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        10,
        20,
        "Test message");

    String result = formatter.formatError(error);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatWarningWithNullSourceExcerptProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        10,
        20,
        "Test warning");

    String result = formatter.formatWarning(warning);
  }

  @Test
  public void testFormatErrorWithEmptySourceExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        10,
        20,
        "Test message");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("");

    String result = formatter.formatError(error);

    String expected = "TypeError - Test message\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithEmptySourceExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        10,
        20,
        "Test warning");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("");

    String result = formatter.formatWarning(warning);

    String expected = "DeprecationWarning - Test warning\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNullSourceName() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        null,
        1,
        10,
        20,
        "Test message");

    when(source.getSourceExcerpt(eq(null), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("");

    String result = formatter.formatError(error);

    String expected = "TypeError - Test message\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithNullSourceName() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        null,
        1,
        10,
        20,
        "Test warning");

    when(source.getSourceExcerpt(eq(null), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("");

    String result = formatter.formatWarning(warning);

    String expected = "DeprecationWarning - Test warning\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNegativeLineNumber() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        -1,
        20,
        "Test message");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatError(error);

    String expected = "test.js:<not a number>: Test message\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithNegativeLineNumber() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        -1,
        20,
        "Test warning");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatWarning(warning);

    String expected = "test.js:<not a number>: WARNING - Test warning\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithZeroLineNumber() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        0,
        20,
        "Test message");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatError(error);

    String expected = "test.js:0: Test message\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithZeroLineNumber() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        0,
        20,
        "Test warning");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatWarning(warning);

    String expected = "test.js:0: WARNING - Test warning\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithEmptyDescription() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        10,
        20,
        "");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatError(error);

    String expected = "test.js:1: TypeError\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithEmptyDescription() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        10,
        20,
        "");

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatWarning(warning);

    String expected = "test.js:1: WARNING\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNullDescription() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError error = new JSError(
        "TypeError",
        "test.js",
        1,
        10,
        20,
        null);

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatError(error);

    String expected = "test.js:1: TypeError\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithNullDescription() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    JSError warning = new JSError(
        "DeprecationWarning",
        "test.js",
        1,
        10,
        20,
        null);

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");

    String result = formatter.formatWarning(warning);

    String expected = "test.js:1: WARNING\n" +
                      "test code\n";

    assertEquals(expected, result);
  }

  private void assertNotNull(Object obj) {
    if (obj == null) {
      throw new AssertionError();
    }
  }
}