import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.CodeSnippetTestLoader;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LightweightMessageFormatterTest {
  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider source;
  private ExcerptFormatter excerptFormatter;

  @Parameters(name = "{index}: testCase-{0}")
  public static List<Object[]> testCases() {
    return CodeSnippetTestLoader.loadTestCases(
        LightweightMessageFormatterTest.class, "testCases.csv");
  }

  public LightweightMessageFormatterTest(SourceExcerptProvider source,
      ExcerptFormatter excerptFormatter) {
    this.source = source;
    this.excerptFormatter = excerptFormatter;
    formatter = new LightweightMessageFormatter(source, excerptFormatter);
  }

  @Test
  public void testWithoutSource() {
    JSError error = new JSError("Test Error", null, null, null, null, null,
        null, null, null);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testWithSource() {
    String code = "function foo() {\n" + "  if (x > 5) {\n" + "    y = 10;\n"
        + "  }\n}";
    SourceExcerptProvider source = new TestSourceExcerptProvider(code);
    JSError error = new JSError("Test Error", "test.js", 3, null, null, null,
        null, null, null);
    String result = formatter.formatError(error);
    String expected = "test.js:3: ERROR - Test Error\n" + "      if (x > 5) {\n"
        + "          ^";
    assertEquals(expected, result);
  }

  @Test
  public void testWarning() {
    JSError warning = new JSError("Test Warning", null, null, null, null, null,
        null, null, null);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test Warning\n";
    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    formatter.formatError(new JSError("Test Error", null, null, null, null, null,
        null, null, null));
  }

  @Test
  public void testExcerptFormatter() {
    String code = "function foo() {\n" + "  if (x > 5) {\n" + "    y = 10;\n"
        + "  }\n}";
    SourceExcerptProvider source = new TestSourceExcerptProvider(code);
    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, excerptFormatter);
    JSError error = new JSError("Test Error", "test.js", 3, null, null, null,
        null, null, null);
    String result = formatter.formatError(error);
    String expected = "test.js:3: ERROR - Test Error\n" + "      if (x > 5) {\n"
        + "          ^";
    assertEquals(expected, result);
  }
}

class TestSourceExcerptProvider implements SourceExcerptProvider {
  private final String code;

  public TestSourceExcerptProvider(String code) {
    this.code = code;
  }

  @Override
  public String getSourceName() {
    return "test.js";
  }

  @Override
  public String getSourceContent(String name, int lineNumber) {
    return code;
  }

  @Override
  public SourceExcerpt getSourceExcerpt(String name, int lineNumber,
      ExcerptFormatter excerptFormatter) {
    return new SourceExcerpt() {
      @Override
      public String getSourceName() {
        return getSourceName();
      }

      @Override
      public String getSourceContent() {
        return code;
      }

      @Override
      public String format(ExcerptFormatter excerptFormatter) {
        return excerptFormatter.formatRegion(new Region() {
          @Override
          public int getBeginningLineNumber() {
            return 1;
          }

          @Override
          public int getEndingLineNumber() {
            return 4;
          }

          @Override
          public String getSourceExcerpt() {
            return code;
          }
        });
      }
    };
  }
}