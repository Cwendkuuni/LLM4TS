import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSError.Level;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestingUtils;
import java.util.Arrays;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterDefaultConstructor() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertEquals("ERROR -  - \n", formatter.formatError(createError()));
    assertEquals("WARNING -  - \n", formatter.formatWarning(createWarning()));
  }

  @Test
  public void testLightweightMessageFormatterWithSourceConstructor() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    assertEquals("file:someFile.js:10:11 ERROR - someFunction:10:11 - CHECK_TYPE\n",
        formatter.formatError(createErrorWithSource("someFile.js", 10, 11)));
    assertEquals("file:someFile.js:10:11 WARNING - someFunction:10:11 - CHECK_TYPE\n",
        formatter.formatWarning(createWarningWithSource("someFile.js", 10, 11)));
  }

  @Test
  public void testWithoutSourceConstructor() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertEquals("ERROR -  - \n", formatter.formatError(createError()));
    assertEquals("WARNING -  - \n", formatter.formatWarning(createWarning()));
  }

  @Test(expected = NullPointerException.class)
  public void testNullSourceProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
  }

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String sourceExcerpt = "function someFunction() {\n" +
        "  if (foo) {\n" +
        "    bar();\n" +
        "  }\n" +
        "}";
    when(source.getSourceExcerpt(eq("someFile.js"), anyInt(), anyInt()))
        .thenReturn(sourceExcerpt);

    JSError error = createErrorWithSource("someFile.js", 1, 1);
    String expected = "someFile.js:1:1 ERROR - someFunction:1:1 - CHECK_TYPE\n" +
        "  function someFunction() {\n" +
        "    if (foo) {\n" +
        "      bar();\n" +
        "    }\n" +
        "  }\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String sourceExcerpt = "function someFunction() {\n" +
        "  if (foo) {\n" +
        "    bar();\n" +
        "  }\n" +
        "}";
    when(source.getSourceExcerpt(eq("someFile.js"), anyInt(), anyInt()))
        .thenReturn(sourceExcerpt);

    JSError warning = createWarningWithSource("someFile.js", 1, 1);
    String expected = "someFile.js:1:1 WARNING - someFunction:1:1 - CHECK_TYPE\n" +
        "  function someFunction() {\n" +
        "    if (foo) {\n" +
        "      bar();\n" +
        "    }\n" +
        "  }\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  private static JSError createError() {
    return new JSError(Level.ERROR, null, "CHECK_TYPE", null, 1, 1);
  }

  private static JSError createWarning() {
    return new JSError(Level.WARNING, null, "CHECK_TYPE", null, 1, 1);
  }

  private static JSError createErrorWithSource(String fileName, int lineNumber,
      int charno) {
    JSError error = new JSError(Level.ERROR, null, "CHECK_TYPE", null, lineNumber,
        charno);
    error.sourceName = fileName;
    return error;
  }

  private static JSError createWarningWithSource(String fileName, int lineNumber,
      int charno) {
    JSError warning = new JSError(Level.WARNING, null, "CHECK_TYPE", null, lineNumber,
        charno);
    warning.sourceName = fileName;
    return warning;
  }
}