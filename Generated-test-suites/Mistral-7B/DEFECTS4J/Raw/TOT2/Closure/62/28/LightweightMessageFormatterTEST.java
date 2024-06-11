import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new DummySourceExcerptProvider(), FORMAT);
    String source = "function foo() {"
        + "if (x > 5) {"
        + "  y = 10;"
        + "}";
    JSError error = new JSError("TestError", "x", 3, "invalid", source, 4);
    String expected = "3| if (x > 5) {"
        + "  y = 10;"
        + "}\n"
        + "4: ERROR - TestError - invalid\n"
        + "  ^^^^";

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("TestError", "x", 3, "invalid");
    String expected = "3: ERROR - TestError - invalid";

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new DummySourceExcerptProvider(), FORMAT);
    String source = "function foo() {"
        + "if (x > 5) {"
        + "  y = 10;"
        + "}";
    JSError warning = new JSError("TestWarning", "x", 3, "deprecate", source, 4);
    String expected = "3| if (x > 5) {"
        + "  y = 10;"
        + "}\n"
        + "4: WARNING - TestWarning - deprecate";

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError("TestWarning", "x", 3, "deprecate");
    String expected = "3: WARNING - TestWarning - deprecate";

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }
}