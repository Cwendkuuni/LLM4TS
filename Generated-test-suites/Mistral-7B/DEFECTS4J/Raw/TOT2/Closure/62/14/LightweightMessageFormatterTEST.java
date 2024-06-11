
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;

public class LightweightMessageFormatterTest {

  @Test
  public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertNotNull(formatter);
  }

  @Test
  public void testConstructorWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    assertNotNull(formatter);
    verify(source, times(1)).getExcerpt(anyString(), anyInt(), anyInt(), eq(LINE));
  }

  @Test
  public void testConstructorWithExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getExcerpt(eq("test.java"), anyInt(), anyInt(), eq(LINE)))
        .thenReturn("test code");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    assertNotNull(formatter);
    assertEquals("test code\n", formatter.format(mock(JSError.class), false).trim());
  }

  @Test
  public void testStaticWithoutSourceConstructor() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }

  @Test
  public void testFormatErrorWithNullSourceNameAndLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = mock(JSError.class);
    String result = formatter.formatError(error);
    assertNull(result);
  }

  @Test
  public void testFormatErrorWithNullSourceNameButNotNullLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = mock(JSError.class);
    when(error.lineNumber).thenReturn(42);
    String result = formatter.formatError(error);
    assertEquals("42: ERROR - Error\n", result);
  }

  @Test
  public void testFormatErrorWithNegativeLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = mock(JSError.class);
    when(error.lineNumber).thenReturn(-1);
    String result = formatter.formatError(error);
    assertNull(result);
  }

  @Test
  public void testFormatErrorWithNullDescription() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = mock(JSError.class);
    String result = formatter.formatError(error);
    assertEquals(": ERROR - Error\n", result);
  }

  @Test
  public void testFormatErrorWithNullSourceExcerptProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = mock(JSError.class);
    when(error.sourceName).thenReturn("test.java");
    when(error.lineNumber).thenReturn(42);
    String result = formatter.formatError(error);
    assertEquals("test.java:42: ERROR - Error\n", result);
  }

  @Test
  public void testFormatErrorWithExceptionFromSourceExcerptProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = mock(JSError.class);
    when(error.sourceName).thenReturn("test.java");
    when(error.lineNumber).thenReturn(42);
    Throwable exception = new RuntimeException("Test exception");
    when(mock(SourceExcerptProvider.class).getExcerpt(eq("test.java"), anyInt(), anyInt(), eq(LINE)))
        .thenThrow(exception);
    String result = formatter.formatError(error);
    assertEquals("test.java:42: ERROR - Error\n", result);
  }

  @Test
  public void testFormatWarningWithNullSourceNameAndLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = mock(JSError.class);
    String result = formatter.formatWarning(warning);
    assertNull(result);
  }

  @Test
  public void testFormatWarningWithNullSourceNameButNotNullLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = mock(JSError.class);
    when(warning.lineNumber).thenReturn(42);
    String result = formatter.formatWarning(warning);
    assertEquals("42: WARNING - Warning\n", result);
  }

  @Test
  public void testFormatWarningWithNegativeLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = mock(JSError.class);
    when(warning.lineNumber).thenReturn(-1);
    String result = formatter.formatWarning(warning);
    assertNull(result);
  }

  @Test
  public void testFormatWarningWithNullDescription() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = mock(JSError.class);
    String result = formatter.formatWarning(warning);
    assertEquals(": WARNING - Warning\n", result);
  }

  @Test
  public void testFormatWarningWithNullSourceExcerptProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = mock(JSError.class);
    when(error.sourceName).thenReturn("test.java");
    when(error.lineNumber).thenReturn(42);
    String result = formatter.formatWarning(warning);
    assertEquals("test.java:42: WARNING - Warning\n", result);
  }

  @Test
  public void testFormatWarningWithExceptionFromSourceExcerptProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = mock(JSError.class);
    when(error.sourceName).thenReturn("test.java");
    when(error.lineNumber).thenReturn(42);
    Throwable exception = new RuntimeException("Test exception");
    when(mock(SourceExcerptProvider.class).getExcerpt(eq("test.java"), anyInt(), anyInt(), eq(LINE)))
        .thenThrow(exception);
    String result = formatter.formatWarning(warning);
    assertEquals("test.java:42: WARNING - Warning\n", result);
  }
}
