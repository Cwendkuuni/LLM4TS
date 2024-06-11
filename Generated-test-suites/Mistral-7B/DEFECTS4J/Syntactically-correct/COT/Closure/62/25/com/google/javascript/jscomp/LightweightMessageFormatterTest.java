import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.checker.Checker;
import com.google.javascript.jscomp.formatter.LineNumberingFormatter;
import com.google.javascript.jscomp.testing.BaseCompilerTest;
import com.google.javascript.jscomp.testing.TestSourceFile;

import java.io.File;
import java.util.List;

public class LightweightMessageFormatterTest extends BaseCompilerTest {

  private static final String TEST_FILE = "LightweightMessageFormatterTest.js";
  private static final String SOURCE_PATH = "src/test/java/com/google/javascript/jscomp/";

  @org.junit.Test
  public void testFormatError() {
    String input = "function foo() { throw new Error('Test Error'); }";
    JSError error = checkCompilation(input);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(this.getSource(), LINE);
    String actual = formatter.formatError(error);
    String expected = "Error: Test Error: src/test/java/com/google/javascript/jscomp/LightweightMessageFormatterTest.js:1:9\n" +
        "   1| function foo() {\n" +
        "   2|   throw new Error('Test Error');\n" +
        "   3| }\n" +
        "Error: Test Error\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarning() {
    String input = "function foo() { alert('Test Warning'); }";
    Checker checker = newCheckerWithTypes();
    checker.checkSource(new TestSourceFile(TEST_FILE, input));
    JSError warning = checker.getWarnings().get(0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(this.getSource(), LINE);
    String actual = formatter.formatWarning(warning);
    String expected = "Warning: Useless use of alert\n" +
        "src/test/java/com/google/javascript/jscomp/LightweightMessageFormatterTest.js:1:1\n" +
        " 1| function foo() {\n" +
        " 1|   alert('Test Warning');\n" +
        " 2| }\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSourceMissing() {
    String input = "function foo() { throw new Error('Test Error'); }";
    JSError error = checkCompilation(input);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String actual = formatter.formatError(error);
    String expected = "Error: Test Error";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSourceMissing() {
    String input = "function foo() { alert('Test Warning'); }";
    Checker checker = newCheckerWithTypes();
    checker.checkSource(new TestSourceFile(TEST_FILE, input));
    JSError warning = checker.getWarnings().get(0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String actual = formatter.formatWarning(warning);
    String expected = "Warning: Useless use of alert";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithRegion() {
    String input = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (x > 10) {\n" +
        "    throw new Error('Test Error');\n" +
        "  }\n" +
        "}";
    JSError error = checkCompilation(input);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(this.getSource(), new Region(1, 0, 1, 14));
    String actual = formatter.formatError(error);
    String expected = "Error: Test Error\n" +
        "src/test/java/com/google/javascript/jscomp/LightweightMessageFormatterTest.js:1:10\n" +
        " 1| function foo() {\n" +
        " 2|   var x = 1;\n" +
        " 3|   if (x > 10) {\n" +
        "  > 4|     throw new Error('Test Error');\n" +
        " 5|   }\n" +
        " 6| }\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithRegion() {
    String input = "function foo() {\n" +
        "  var x = 1;\n" +
        "  alert('Test Warning');\n" +
        "}";
    Checker checker = newCheckerWithTypes();
    checker.checkSource(new TestSourceFile(TEST_FILE, input));
    JSError warning = checker.getWarnings().get(0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(this.getSource(), new Region(1, 0, 1, 15));
    String actual = formatter.formatWarning(warning);
    String expected = "Warning: Useless use of alert\n" +
        "src/test/java/com/google/javascript/jscomp/LightweightMessageFormatterTest.js:1:12\n" +
        " 1| function foo() {\n" +
        " 2|   var x = 1;\n" +
        "  >3|   alert('Test Warning');\n" +
        " 4| }\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSourceAndLineNumberMissing() {
    String input = "function foo() { throw new Error('Test Error'); }";
    JSError error = checkCompilation(input);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public List<SourceExcerpt> getExcerpts(File file) {
        return null;
      }
    }, LINE);
    String actual = formatter.formatError(error);
    String expected = "Error: Test Error";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSourceAndLineNumberMissing() {
    String input = "function foo() { alert('Test Warning'); }";
    Checker checker = newCheckerWithTypes();
    checker.checkSource(new TestSourceFile(TEST_FILE, input));
    JSError warning = checker.getWarnings().get(0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public List<SourceExcerpt> getExcerpts(File file) {
        return null;
      }
    }, LINE);
    String actual = formatter.formatWarning(warning);
    String expected = "Warning: Useless use of alert";
    assertEquals(expected, actual);
  }

  private SourceExcerptProvider getSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getExcerpts(eq(new File(SOFTWARE_UNDER_TEST_PATH + SOURCE_PATH + TEST_FILE)))).thenReturn(Arrays.asList(
        new SourceExcerpt(LINE, "function foo() {\n" +
            "  var x = 1;\n" +
            "}", SOURCE_PATH + TEST_FILE, 1, 0, 1, 3)
    ));
    return source;
  }
}