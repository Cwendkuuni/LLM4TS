import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.TestSourceFile;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  private static final String SOURCE = "class MyClass {" +
      "  foo() {" +
      "    if (bar) {" +
      "      alert('Hello World');" +
      "    }" +
      "  }" +
      "  bar = 42;" +
      "}";

  @Test
  public void testErrorMessageWithSource() {
    SourceExcerptProvider source = new TestSourceFile(SOURCE).getSourceProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TypeError", "use of undefined variable 'bar'",
        SOURCE, 4, 10);
    String message = formatter.formatError(error);
    String expected = "MyClass:4:10 ERROR - ERROR - use of undefined variable 'bar'\n" +
        "  foo() {" +
        "    if (bar) {" +
        "      alert('Hello World');" +
        "    }" +
        "  }" +
        "  bar = 42;\n";
    assertEquals(expected, message);
  }

  @Test
  public void testErrorMessageWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TypeError", "use of undefined variable 'bar'",
        null, 4, 10);
    String message = formatter.formatError(error);
    String expected = "ERROR - ERROR - use of undefined variable 'bar'\n";
    assertEquals(expected, message);
  }

  @Test
  public void testWarningMessageWithSource() {
    SourceExcerptProvider source = new TestSourceFile(SOURCE).getSourceProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("DeprecationWarning", "Deprecated feature",
        SOURCE, 4, 5);
    String message = formatter.formatWarning(warning);
    String expected = "MyClass:4:5 WARNING - WARNING - Deprecated feature\n" +
        "  foo() {" +
        "    if (bar) {" +
        "      alert('Hello World');" +
        "    }" +
        "  }" +
        "  bar = 42;\n";
    assertEquals(expected, message);
  }

  @Test
  public void testWarningMessageWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("DeprecationWarning", "Deprecated feature",
        null, 4, 5);
    String message = formatter.formatWarning(warning);
    String expected = "WARNING - WARNING - Deprecated feature\n";
    assertEquals(expected, message);
  }

  @Test
  public void testRegionFormat() {
    SourceExcerptProvider source = new TestSourceFile(SOURCE).getSourceProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    Region region = new Region("MyClass.java", 2, 10, 2, 20);
    String message = formatter.formatRegion(region);
    String expected = " 2| if (bar) {" +
        " 3|   alert('Hello World');" +
        " 4| }";
    assertEquals(expected, message);
  }
}