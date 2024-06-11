import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

import com.google.common.base.Strings;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertEquals("", formatter.getSource());
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("TestError", "TestErrorDescription", "TestSource",
        1, 1, "TestMessage", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null);
    String result = formatter.formatError(error);
    String expected = "ERROR - TestErrorDescription\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        "TestSource", 1, 1, "TestMessage", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarningDescription\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError("TestError", "TestErrorDescription", "TestSource",
        1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, LINE);
    String result = formatter.formatError(error);
    String expected = "TestSource:1: - ERROR - TestErrorDescription\n";
    String sourceExcerpt = "TestSource code";
    assertEquals(expected, result);
    verify(source).getSourceExcerpt(eq(TestSource.class), anyString(), anyInt(),
        eq(LINE));
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, LINE);
    String result = formatter.formatWarning(warning);
    String expected = "TestSource:1: - WARNING - TestWarningDescription\n";
    String sourceExcerpt = "TestSource code";
    assertEquals(expected, result);
    verify(source).getSourceExcerpt(eq(TestSource.class), anyString(), anyInt(),
        eq(LINE));
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorNullSourceProvider() {
    JSError error = new JSError("TestError", "TestErrorDescription", null, 1, 1,
        "TestMessage", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null, LINE);
    formatter.formatError(error);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatWarningNullSourceProvider() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        null, 1, 1, "TestMessage", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null, LINE);
    formatter.formatWarning(warning);
  }

  @Test
  public void testFormatErrorWithRegion() {
    JSError error = new JSError("TestError", "TestErrorDescription", "TestSource",
        1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatError(error);
    String expected = "TestSource:1: |   1: if (foo) {\n"
        + "TestSource:2:   alert('bar');\n" + "TestSource:3: }\n"
        + "ERROR - TestErrorDescription\n";
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithRegion() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatWarning(warning);
    String expected = "TestSource:1: |   1: if (foo) {\n"
        + "TestSource:2:   alert('bar');\n" + "TestSource:3: }\n"
        + "WARNING - TestWarningDescription\n";
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithEmptySourceRegion() {
    JSError error = new JSError("TestError", "TestErrorDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatError(error);
    String expected = "TestSource:1: - ERROR - TestErrorDescription\n";
    String sourceExcerpt = "";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithEmptySourceRegion() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatWarning(warning);
    String expected = "TestSource:1: - WARNING - TestWarningDescription\n";
    String sourceExcerpt = "";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithIncompleteSourceRegion() {
    JSError error = new JSError("TestError", "TestErrorDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatError(error);
    String expected = "TestSource:1: - ERROR - TestErrorDescription\n";
    String sourceExcerpt = "if (foo) {\n";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithIncompleteSourceRegion() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatWarning(warning);
    String expected = "TestSource:1: - WARNING - TestWarningDescription\n";
    String sourceExcerpt = "if (foo) {\n";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSourceRegionEndingWithNewline() {
    JSError error = new JSError("TestError", "TestErrorDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatError(error);
    String expected = "TestSource:1: - ERROR - TestErrorDescription\n"
        + "TestSource:1: |\n";
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSourceRegionEndingWithNewline() {
    JSError warning = new JSError("TestWarning", "TestWarningDescription",
        "TestSource", 1, 1, "TestMessage", null);
    SourceExcerptProvider source = mockSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, new LineNumberingFormatter());
    String result = formatter.formatWarning(warning);
    String expected = "TestSource:1: - WARNING - TestWarningDescription\n"
        + "TestSource:1: |\n";
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    verify(source).getRegion(eq(TestSource.class), eq(1), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any());
    assertEquals(expected, result);
  }

  private SourceExcerptProvider mockSourceExcerptProvider() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getRegion(any(Class.class), anyInt(), anyInt(), anyInt(),
        eq(1), anyInt(), anyInt(), any()))
        .thenReturn(new Region("TestSource", "TestSource code", 1, 1, 1, 1));
    return source;
  }
}