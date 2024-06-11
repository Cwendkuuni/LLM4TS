import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.test.AbstractMessageFormatterTest;
import com.google.javascript.jscomp.test.TestSourceExcerptProvider;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test file for the LightweightMessageFormatter class.
 *
 */
public class LightweightMessageFormatterTest extends AbstractMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider sourceExcerptProvider;

  @Before
  public void setUp() {
    sourceExcerptProvider = new TestSourceExcerptProvider();
    formatter = new LightweightMessageFormatter(sourceExcerptProvider);
  }

  @Test
  public void testCreateWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertEquals("", formatter.formatError(createError("")));
    assertEquals("", formatter.formatWarning(createWarning("")));
  }

  @Test
  public void testCreateWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider, LINE);
    String sourceCode = "function foo() {\n" +
                       "  bar();\n" +
                       "}";
    SourceExcerpt sourceExcerpt = new SourceExcerpt("test.js", sourceCode);
    when(sourceExcerptProvider.getSource(sourceExcerpt.getName()))
        .thenReturn(sourceCode);

    JSError error = createError("test.js", 1, "Expected identifier.", null);
    assertEquals(
        "ERROR - test.js:1: Expected identifier.\n" +
        "function foo() {\n" +
        "  ^-------^",
        formatter.formatError(error));

    JSError warning = createWarning("test.js", 1, "Possible unintended consequence.", null);
    assertEquals(
        "WARNING - test.js:1: Possible unintended consequence.\n" +
        "function foo() {\n" +
        "  ^--------^",
        formatter.formatWarning(warning));
  }

  @Test
  public void testCreateWithRegionSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider, REGION);
    String sourceCode = "function foo() {\n" +
                       "  bar();\n" +
                       "  baz();\n" +
                       "}";
    SourceExcerpt sourceExcerpt = new SourceExcerpt("test.js", sourceCode, 1, 3);
    when(sourceExcerptProvider.getSource(sourceExcerpt.getName()))
        .thenReturn(sourceCode);

    JSError error = createError("test.js", 1, "Expected identifier.", null);
    assertEquals(
        "ERROR - test.js:1:3: Expected identifier.\n" +
        "function foo() {\n" +
        "  bar();\n" +
        "  baz();\n" +
        "  ^^---^",
        formatter.formatError(error));

    JSError warning = createWarning("test.js", 2, "Possible unintended consequence.", null);
    assertEquals(
        "WARNING - test.js:2: Expected identifier.\n" +
        "baz();\n" +
        " ^----^",
        formatter.formatWarning(warning));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNullSource() {
    new LightweightMessageFormatter(null, LINE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNullExcerpt() {
    new LightweightMessageFormatter(sourceExcerptProvider, null);
  }

  @Test
  public void testFormatError() {
    String sourceCode = "function foo() {\n" +
                       "  bar();\n" +
                       "}";
    SourceExcerpt sourceExcerpt = new SourceExcerpt("test.js", sourceCode);
    when(sourceExcerptProvider.getSource(sourceExcerpt.getName()))
        .thenReturn(sourceCode);

    JSError error = createError("test.js", 1, "Expected identifier.", null);
    assertEquals(
        "ERROR - test.js:1: Expected identifier.\n" +
        "function foo() {\n" +
        "  ^-------^",
        formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    String sourceCode = "function foo() {\n" +
                       "  bar();\n" +
                       "}";
    SourceExcerpt sourceExcerpt = new SourceExcerpt("test.js", sourceCode);
    when(sourceExcerptProvider.getSource(sourceExcerpt.getName()))
        .thenReturn(sourceCode);

    JSError warning = createWarning("test.js", 1, "Possible unintended consequence.", null);
    assertEquals(
        "WARNING - test.js:1: Possible unintended consequence.\n" +
        "function foo() {\n" +
        "  ^--------^",
        formatter.formatWarning(warning));
  }

  private JSError createError(String sourceName, int lineNumber,
      String description, String source) {
    return new JSError(sourceName, lineNumber, description, null, CheckLevel.ERROR);
  }

  private JSError createWarning(String sourceName, int lineNumber,
      String description, String source) {
    return new JSError(sourceName, lineNumber, description, null, CheckLevel.WARNING);
  }
}

class TestSourceExcerptProvider implements SourceExcerptProvider {
  @Override
  public SourceExcerpt getSource(String name) {
    return new SourceExcerpt(name, "");
  }
}