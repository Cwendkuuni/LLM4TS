import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.CodeSnippetTestHelper;
import com.google.javascript.jscomp.testing.TestSourceExcerptProvider;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  private static final ExcerptFormatter excerptFormatter =
      new LightweightMessageFormatter.LineNumberingFormatter();

  private SourceExcerptProvider sourceProvider = new TestSourceExcerptProvider();

  private LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);

  @Test
  public void testEmptyConstructor() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertEquals("", formatter.formatError(null));
  }

  @Test
  public void testWithoutSourceConstructor() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, null, 0, "Test error", null);
    assertEquals("Error - TEST ERROR\n", formatter.formatError(error));
  }

  @Test
  public void testWithSourceConstructor() {
    String code = "function foo() {\n" +
        "  bar();\n" +
        "}";
    SourceExcerpt excerpt = SourceExcerptProvider.lineExcerpt(code);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    JSError error = new JSError("test.js", 3, 0, "Test error", 2);
    String expected = "test.js:3:2 - ERROR - TEST ERROR\n" +
        "  bar();\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatError() {
    String code = "function foo() {\n" +
        "  bar();\n" +
        "}";
    SourceExcerpt excerpt = SourceExcerptProvider.lineExcerpt(code);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    JSError error = new JSError("test.js", 3, 2, "Test error", 5);
    String expected = "test.js:3:2 - ERROR - Test error\n" +
        "  bar();\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    String code = "function foo() {\n" +
        "  bar();\n" +
        "}";
    SourceExcerpt excerpt = SourceExcerptProvider.lineExcerpt(code);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    JSError warning = new JSError("test.js", 3, 2, "Test warning", 5);
    String expected = "test.js:3:2 - WARNING - Test warning\n" +
        "  bar();\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    formatter.formatError(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    formatter.formatWarning(null);
  }

  @Test
  public void testFormatRegion() {
    String code = "function foo() {\n" +
        "  if (foo) {\n" +
        "    alert('bar');\n" +
        "  } else {\n" +
        "    baz();\n" +
        "  }\n" +
        "}";
    SourceExcerpt excerpt = SourceExcerptProvider.regionExcerpt(code, 1, 2, 4);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    JSError error = new JSError("test.js", 3, 2, "Test error", 5);
    String expected = "test.js:\n" +
        "  1:2 - ERROR - Test error\n" +
        "  if (foo) {\n" +
        "    alert('bar');\n" +
        "  }\n";
    assertEquals(expected, formatter.formatError(error));
  }
}