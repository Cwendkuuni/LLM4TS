import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.without.LineNumberingFormatter;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider mockSourceExcerptProvider() {
    SourceExcerptProvider mockSource = mock(SourceExcerptProvider.class);
    return mockSource;
  }

  @Test
  public void testFormatErrorWithSource() {
    String sourceCode = "function foo() {\n" +
        "  bar();\n" +
        "}";

    SourceExcerptProvider sourceExpert = mockSourceExcerptProvider();
    ExcerptFormatter excerptFormatter = new LineNumberingFormatter();
    SourceExcerpt excerpt = new SourceExcerpt("test.js", sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExpert, excerpt);

    JSError error = new JSError("TypeError", "bar is not a function", 0,
        "test.js", 1, 5);
    String expected = "test.js:1:5 TypeError - bar is not a function\n" +
        "  function foo() {\n" +
        "  bar();\n" +
        "  }\n";

    when(sourceExpert.get(eq(error.sourceName), anyInt(), anyInt(), eq(excerptFormatter)))
        .thenReturn(excerpt.get(0, sourceCode.length()));

    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TypeError", "bar is not a function", 0);
    String expected = "TypeError - bar is not a function";

    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithSource() {
    String sourceCode = "function foo() {\n" +
        "  alert(bar);\n" +
        "}";

    SourceExcerptProvider sourceExpert = mockSourceExcerptProvider();
    ExcerptFormatter excerptFormatter = new LineNumberingFormatter();
    SourceExcerpt excerpt = new SourceExcerpt("test.js", sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExpert, excerpt);

    JSError warning = new JSError("DeprecationWarning", "alert is deprecated", 0,
        "test.js", 1, 5);
    String expected = "test.js:1:5 DeprecationWarning - alert is deprecated\n" +
        "  function foo() {\n" +
        "    alert(bar);\n" +
        "  }\n";

    when(sourceExpert.get(eq(warning.sourceName), anyInt(), anyInt(), eq(excerptFormatter)))
        .thenReturn(excerpt.get(0, sourceCode.length()));

    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("DeprecationWarning", "alert is deprecated", 0);
    String expected = "DeprecationWarning - alert is deprecated";

    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }
}