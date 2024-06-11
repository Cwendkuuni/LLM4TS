import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestHelper;
import com.google.javascript.jscomp.testing.TestSourceFile;
import java.util.Arrays;
import java.util.List;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = createError("test.js", 1, "SyntaxError", "invalid syntax");
    String result = formatter.formatError(error);
    assertEquals(
        "ERROR - SyntaxError - invalid syntax\n", result);
  }

  @Test
  public void testLightweightMessageFormatterWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = createError("test.js", 1, "SyntaxError", "invalid syntax");
    when(source.getSourceExcerpt(eq("test.js"), eq(1), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn("invalid syntax");

    String result = formatter.formatError(error);
    assertEquals(
        "test.js:1: SYntaxError - invalid syntax\ninvalid syntax^\n", result);
  }

  @Test
  public void testLightweightMessageFormatterWithRegion() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = createError("test.js", 1, "SyntaxError", "invalid syntax");
    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn("if (foo) { \n   alert('bar'); \n}");

    String result = formatter.formatError(error);
    assertEquals(
        "test.js:1: SYntaxError - invalid syntax\nif (foo) {\n   alert('bar'); \n}\n^\n", result);
  }

  @Test
  public void testLightweightMessageFormatterWithWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = createWarning("test.js", 1, "Deprecated", "deprecated method");
    String result = formatter.formatWarning(warning);
    assertEquals(
        "WARNING - Deprecated - deprecated method\n", result);
  }

  @Test
  public void testLineNumberingFormatter() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

    JSError error = createError("test.js", 1, "SyntaxError", "invalid syntax");
    String source =
        "function test() {\n"
            + "  if (foo) {\n"
            + "    alert('bar');\n"
            + "  }\n"
            + "}\n";

    when(source.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenAnswer(i -> source.substring(i.getArgument(0), i.getArgument(2)));

    String result = formatter.formatError(error);
    String expected =
        "test.js:1: SYntaxError - invalid syntax\n"
            + "function test() {\n"
            + "  if (foo) {\n"
            + "    alert('bar');\n"
            + "  }\n"
            + "}\n"
            + "^\n";
    assertEquals(expected, result);
  }

  private JSError createError(String fileName, int lineNumber, String errorType, String errorMessage) {
    return new JSError(
        fileName, lineNumber, errorType, errorMessage, null, null, null, null, null);
  }

  private JSError createWarning(String fileName, int lineNumber, String errorType, String errorMessage) {
    return new JSError(
        fileName, lineNumber, errorType, errorMessage, null, null, null, null, CheckLevel.WARNING);
  }

  @Before
  public void setUp() {
    CodeSnippetTestHelper.setUp();
  }

  @After
  public void tearDown() {
    CodeSnippetTestHelper.tearDown();
  }
}