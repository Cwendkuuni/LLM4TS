import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.Checker;
import com.google.javascript.jscomp.parsing.config.ParsingConfig;
import com.google.javascript.jscomp.parsing.parser.JavaScriptParser;
import com.google.javascript.jscomp.parsing.util.Source;
import com.google.javascript.jscomp.util.CodeChunk;
import com.google.javascript.jscomp.util.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider sourceProvider;
  private Checker checker;
  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    sourceProvider = mock(SourceExcerptProvider.class);
    checker = mock(Checker.class);
    when(sourceProvider.getSource(anyString(), anyInt())).thenReturn(
        new SourceFile("test.js", new CodeChunk("function test() {}")));
    formatter = new LightweightMessageFormatter(sourceProvider);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("Test Error", "test.js", 1, 1, null);
    String result = formatter.formatError(error);
    assertEquals("ERROR - Test Error: test.js:1: Malformed node\n", result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Warning", "test.js", 1, 1, null);
    String result = formatter.formatWarning(warning);
    assertEquals("WARNING - Test Warning: test.js:1: Test Warning\n", result);
  }

  @Test
  public void testFormatRegion() {
    Region region = new Region(new Source("test.js", 1), 1, 1, 1, 5);
    String result = formatter.formatRegion(region);
    assertEquals("1| function test() {\n   ^^^\n  5| }\n", result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError("Test Error", null, 1, 1, null);
    formatter = new LightweightMessageFormatter(sourceProvider);
    String result = formatter.formatError(error);
    assertEquals("Test Error:\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError("Test Warning", null, 1, 1, null);
    formatter = new LightweightMessageFormatter(sourceProvider);
    String result = formatter.formatWarning(warning);
    assertEquals("Test Warning:\n", result);
  }

  @Test
  public void testFormatErrorWithRegion() {
    JSError error = new JSError("Test Error", null, 1, 1, null);
    Region region = new Region(new Source("test.js", 1), 1, 1, 1, 5);
    formatter = new LightweightMessageFormatter(sourceProvider);
    String result = formatter.formatError(error, region);
    assertEquals("ERROR - Test Error: test.js:1:1: Malformed node\n  1| function test() {\n   ^^\n  5| }\n", result);
  }

  @Test
  public void testFormatWarningWithRegionSource() {
    JSError warning = new JSError("Test Warning", "test.js", 1, 1, null);
    Region region = new Region(new SourceFile("test.js", new CodeChunk("function test() {}")), 1,
        1, 1, 5);
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public Iterable<SourceExcerpt> getSourceExcerpts(SourceFile source) {
        return ImmutableList.of(new SourceExcerpt(LINE, "test.js", 1,
            excerptFormatter::formatLine));
      }
    }, LINE);
    String result = formatter.formatWarning(warning, region);
    assertEquals("WARNING - Test Warning: test.js:1:1: Test Warning\n  1| function test() {\n   ^^\n  5| }\n", result);
  }

  @Test
  public void testFormatErrorWithRegionAndInvalidSource() {
    JSError error = new JSError("Test Error", null, 1, 1, null);
    Region region = new Region(new SourceFile("test.js", new CodeChunk("function test() {}")), 1,
        1, 1, 5);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatError(error, region);
    assertEquals("ERROR - Test Error: test.js:1: Malformed node\n", result);
  }

  @Test
  public void testFormatWarningWithRegionAndInvalidSource() {
    JSError warning = new JSError("Test Warning", null, 1, 1, null);
    Region region = new Region(new SourceFile("test.js", new CodeChunk("function test() {}")), 1,
        1, 1, 5);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatWarning(warning, region);
    assertEquals("WARNING - Test Warning: test.js:1: Test Warning\n", result);
  }

  @Test
  public void testFormatErrorWithRegionAndEmptySource() {
    JSError error = new JSError("Test Error", null, 1, 1, null);
    Region region = new Region(new SourceFile("test.js", new CodeChunk("")), 1, 1, 1, 5);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatError(error, region);
    assertEquals("ERROR - Test Error:\n", result);
  }

  @Test
  public void testFormatWarningWithRegionAndEmptySource() {
    JSError warning = new JSError("Test Warning", null, 1, 1, null);
    Region region = new Region(new SourceFile("test.js", new CodeChunk("")), 1, 1, 1, 5);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatWarning(warning, region);
    assertEquals("WARNING - Test Warning:\n", result);
  }

  @Test
  public void testFormatErrorWithRegionAndNoSource() {
    JSError error = new JSError("Test Error", null, 1, 1, null);
    Region region = new Region(new Source("test.js", 1), 1, 1, 1, 5);
    formatter = new LightweightMessageFormatter(null, LINE);
    String result = formatter.formatError(error, region);
    assertEquals("Test Error:\n", result);
  }

  @Test
  public void testFormatWarningWithRegionAndNoSource() {
    JSError warning = new JSError("Test Warning", null, 1, 1, null);
    Region region = new Region(new Source("test.js", 1), 1, 1, 1, 5);
    formatter = new LightweightMessageFormatter(null, LINE);
    String result = formatter.formatWarning(warning, region);
    assertEquals("Test Warning:\n", result);
  }
}