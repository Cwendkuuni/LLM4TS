import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.JSError;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(null);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("Test error", "test.js", 10, CheckLevel.ERROR);
    String result = formatter.formatError(error);
    String expected = "test.js:10: ERROR - Test error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test warning", "test.js", 5, CheckLevel.WARNING);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:5: WARNING - Test warning\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSourceExcerpt() {
    JSError error = new JSError("Test error without source", null, 0, CheckLevel.ERROR);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test error without source\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSourceExcerpt() {
    JSError warning = new JSError("Test warning without source", null, 0, CheckLevel.WARNING);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test warning without source\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSourceExcerpt() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("Test error with source", "test.js", 10, CheckLevel.ERROR);
    String result = formatter.formatError(error);
    String expected = "test.js:10: ERROR - Test error with source\n  function foo() {\n    alert('bar');\n  }\n  ^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSourceExcerpt() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("Test warning with source", "test.js", 5, CheckLevel.WARNING);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:5: WARNING - Test warning with source\n  if (foo) {\n    alert('bar');\n  }\n  ^\n";
    assertEquals(expected, result);
  }

  private class TestSourceExcerptProvider implements SourceExcerptProvider {

    public String getSourceExcerpt(
        String sourceName, int lineNumber, ExcerptFormatter formatter) {
      switch (sourceName) {
        case "test.js":
          switch (lineNumber) {
            case 10:
              return "function foo() {\n  alert('bar');\n}";
            case 5:
              return "if (foo) {\n  alert('bar');\n}";
          }
      }
      return null;
    }
  }
}