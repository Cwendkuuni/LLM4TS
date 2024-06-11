package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt.LINE;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider mockSourceProvider;

  @Before
  public void setup() {
    mockSourceProvider = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(mockSourceProvider, LINE);
  }

  @Test
  public void testConstructorWithoutSource() {
    // Test code for the constructor without source
  }

  @Test
  public void testConstructorWithSource() {
    // Test code for the constructor with source
  }

  @Test
  public void testConstructorWithCustomExcerpt() {
    // Test code for the constructor with custom excerpt
  }

  @Test
  public void testWithoutSource() {
    // Test code for the withoutSource method
  }

  @Test
  public void testFormatError() {
    // Test code for the formatError method
  }

  @Test
  public void testFormatWarning() {
    // Test code for the formatWarning method
  }

  @Test
  public void testGetSourceReturnsNull() {
    when(mockSourceProvider.getSource(anyString(), anyInt(), anyInt()))
        .thenReturn(null);
    assertEquals("null", formatter.formatError(new JSError("", 0, 0)));
  }

  @Test
  public void testFormatEmptyDescriptionError() {
    JSError error = new JSError("", "Empty description error", 0, 0);
    assertEquals("Empty description error\n", formatter.formatError(error));
  }

  @Test
  public void testFormatEmptyDescriptionWarning() {
    JSError warning = new JSError("", "Empty description warning", 0, 0);
    assertEquals("Empty description warning\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorExcerptEndOfLine() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    String sourceCode = "class MyClass { func() { foo(); } }";
    when(source.getSource(eq("MyClass.js"), anyInt(), anyInt()))
        .thenReturn(sourceCode);
    JSError error = new JSError("MyClass.js", 1, 5);
    assertEquals("MyClass.js:1:5 - ERROR - unexpected token: foo\nfoo();\n",
        formatter.formatError(error));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorInvalidSourceName() {
    formatter.formatError(new JSError("", 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorInvalidLineNumber() {
    formatter.formatError(new JSError("", 0, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorInvalidCheckLevel() {
    formatter.formatError(new JSError("", 0, 0), CheckLevel.FATAL);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWarningInvalidSourceName() {
    formatter.formatWarning(new JSError("", 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWarningInvalidLineNumber() {
    formatter.formatWarning(new JSError("", 0, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWarningInvalidCheckLevel() {
    formatter.formatWarning(new JSError("", 0, 0), CheckLevel.FATAL);
  }

  @Test
  public void testSourceExcerptProviderIsNull() {
    when(mockSourceProvider.getSource(anyString(), anyInt(), anyInt()))
        .thenReturn(null);
    formatter = new LightweightMessageFormatter(mockSourceProvider, LINE);
    JSError error = new JSError("", 0, 0);
    assertEquals("null", formatter.formatError(error));
  }
}