import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.common.base.Preconditions;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String sourceCode = "function foo() {\n" +
        "  bar(); // should fail\n" +
        "}";
    when(source.getSourceExcerpt(SourceExcerpt.LINE, "Test.java", 1, null)).thenReturn(sourceCode);

    JSError error = new JSError("TypeError", "Test.java", 1, 10, "undefined is not a function", null);
    String expectedErrorMessage = "1:10 TypeError - undefined is not a function\n" +
        "  function foo() {\n" +
        "    bar(); // should fail\n" +
        "  }\n";

    String result = formatter.formatError(error);
    assertEquals(expectedErrorMessage, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    JSError error = new JSError("TypeError", null, 1, 10, "undefined is not a function", null);
    String expectedErrorMessage = "1: TypeError - undefined is not a function\n";

    String result = formatter.formatError(error);
    assertEquals(expectedErrorMessage, result);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String sourceCode = "function foo() {\n" +
        "  var x = y;\n" +
        "}";
    when(source.getSourceExcerpt(SourceExcerpt.LINE, "Test.java", 1, null)).thenReturn(sourceCode);

    JSError warning = new JSError("DeprecationWarning", "Test.java", 1, 5, "Using 'var' keyword is discouraged.", null);
    String expectedWarningMessage = "1:5 DeprecationWarning - Using 'var' keyword is discouraged.\n" +
        "  function foo() {\n" +
        "    var x = y;\n" +
        "  }\n";

    String result = formatter.formatWarning(warning);
    assertEquals(expectedWarningMessage, result);
  }

  @Test
  public void testFormatRegion() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, REGION);

    String sourceCode = "function foo() {\n" +
        "  if (x > y) {\n" +
        "    // do something\n" +
        "  } else {\n" +
        "    throw new Error('Error message');\n" +
        "  }\n" +
        "}";
    Region region = new Region(null, 1, 1, 1, 1, 1, null);
    when(source.getSourceExcerpt(REGION, "Test.java", 1, null)).thenReturn(sourceCode);

    JSError error = new JSError("TypeError", "Test.java", 1, 10, "undefined is not a function", null);
    String expectedErrorMessage = "1:1-5 1:10 TypeError - undefined is not a function\n" +
        "function foo() {\n" +
        "  if (x > y) {\n" +
        "    // do something\n" +
        "  } else {\n" +
        "    throw new Error('Error message');// should fail\n" +
        "  }\n" +
        "}\n";

    String result = formatter.formatError(error);
    assertEquals(expectedErrorMessage, result);
  }

  @Test
  public void testFormatRegionWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    JSError error = new JSError("TypeError", null, 1, 10, "undefined is not a function", null);
    String expectedErrorMessage = "1: TypeError - undefined is not a function\n";

    String result = formatter.formatError(error);
    assertEquals(expectedErrorMessage, result);
  }
}