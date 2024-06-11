import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.Check;
import com.google.javascript.jscomp.testutil.TestSourceFile;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider sourceExpert;
  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    sourceExpert = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(sourceExpert, LINE);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Error message", null, 1, null, 1);
    String expected = "ERROR - Error message\n";
    when(sourceExpert.getSource(anyString())).thenReturn(null);
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithSource() {
    String source = "function foo() {\n  bar();\n}";
    TestSourceFile testSourceFile = new TestSourceFile("Test.js", source);
    SourceExcerptProvider sourceExpert = mock(SourceExcerptProvider.class);
    when(sourceExpert.getSource(eq("Test.js"))).thenReturn(source);

    JSError error = new JSError("Error message", "Test.js", 1, null, 1);
    String expected = "Test.js:1: ERROR - Error message\nfunction foo() {\n  bar();\n}";
    formatter = new LightweightMessageFormatter(sourceExpert, LINE);
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Warning message", null, 1, null, 1);
    String expected = "WARNING - Warning message\n";
    when(sourceExpert.getSource(anyString())).thenReturn(null);
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    String source = "function foo() {\n  bar();\n}";
    TestSourceFile testSourceFile = new TestSourceFile("Test.js", source);
    SourceExcerptProvider sourceExpert = mock(SourceExcerptProvider.class);
    when(sourceExpert.getSource(eq("Test.js"))).thenReturn(source);

    JSError warning = new JSError("Warning message", "Test.js", 1, null, 1);
    String expected = "Test.js:1: WARNING - Warning message\nfunction foo() {\n  bar();\n}";
    formatter = new LightweightMessageFormatter(sourceExpert, LINE);
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithCharno() {
    String source = "function foo() {\n  bar();\n}";
    TestSourceFile testSourceFile = new TestSourceFile("Test.js", source);
    SourceExcerptProvider sourceExpert = mock(SourceExcerptProvider.class);
    when(sourceExpert.getSource(eq("Test.js"))).thenReturn(source);

    JSError error = new JSError("Error message", "Test.js", 1, 5, 1);
    String expected = "Test.js:1: ERROR - Error message\nfunction foo() {\n  bar();\n  \n    ^";
    formatter = new LightweightMessageFormatter(sourceExpert, LINE);
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithCharno() {
    String source = "function foo() {\n  bar();\n}";
    TestSourceFile testSourceFile = new TestSourceFile("Test.js", source);
    SourceExcerptProvider sourceExpert = mock(SourceExcerptProvider.class);
    when(sourceExpert.getSource(eq("Test.js"))).thenReturn(source);

    JSError warning = new JSError("Warning message", "Test.js", 1, 5, 1);
    String expected = "Test.js:1: WARNING - Warning message\nfunction foo() {\n  bar();\n  \n    ^";
    formatter = new LightweightMessageFormatter(sourceExpert, LINE);
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithRegion() {
    String source = "function foo() {\n  if (x > 10) {\n    bar();\n  } else {\n    baz();\n  }\n}";
    TestSourceFile testSourceFile = new TestSourceFile("Test.js", source);
    SourceExcerptProvider sourceExpert = mock(SourceExcerptProvider.class);
    when(sourceExpert.getSource(eq("Test.js"))).thenReturn(source);

    JSError error = new JSError("Error message", "Test.js", 1, 11, 2);
    String expected = "Test.js:1: ERROR - Error message\nfunction foo() {\n  if (x > 10) {\n    bar();\n  } else {\n    baz();\n  }\n  ^---^";
    formatter = new LightweightMessageFormatter(sourceExpert, REGION);
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithRegion() {
    String source = "function foo() {\n  if (x > 10) {\n    bar();\n  } else {\n    baz();\n  }\n}";
    TestSourceFile testSourceFile = new TestSourceFile("Test.js", source);
    SourceExcerptProvider sourceExpert = mock(SourceExcerptProvider.class);
    when(sourceExpert.getSource(eq("Test.js"))).thenReturn(source);

    JSError warning = new JSError("Warning message", "Test.js", 1, 11, 2);
    String expected = "Test.js:1: WARNING - Warning message\nfunction foo() {\n  if (x > 10) {\n    bar();\n  } else {\n    baz();\n  }\n  ^---^";
    formatter = new LightweightMessageFormatter(sourceExpert, REGION);
    assertEquals(expected, formatter.formatWarning(warning));
  }
}