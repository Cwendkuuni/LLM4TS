import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.BaseCompilerTestCase;
import com.google.javascript.jscomp.testing.GeneratedCodeTestUtil;
import com.google.javascript.jscomp.testing.SourceFile;

import java.util.List;

public class LightweightMessageFormatterTest extends BaseCompilerTestCase {

  private static final String CLASS_NAME = LightweightMessageFormatter.class.getName();

  @SuppressWarnings("unchecked")
  private static final Class<?>[] DEPENDENCIES = new Class<?>[] {
      SourceExcerptProvider.class,
      com.google.common.base.Preconditions.class,
      com.google.common.base.Strings.class,
      com.google.javascript.jscomp.JSError.class,
      com.google.javascript.jscomp.Region.class,
      com.google.javascript.jscomp.SourceFile.class,
      com.google.javascript.jscomp.testing.GeneratedCodeTestUtil.class
  };

  @SuppressWarnings("unchecked")
  private static final Class<?>[] TEST_FILES = new Class<?>[] {
      SourceFile.class
  };

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider();
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = JSError.syntaxError("test", "Test error message", 1, 1);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test error message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    JSError error = JSError.syntaxError("test", "Test error message", file, 1, 1);
    String result = formatter.formatError(error);
    String expected = "test:1: Test error message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = JSError.warning("test", "Test warning message", 1, 1);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test warning message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    JSError warning = JSError.warning("test", "Test warning message", file, 1, 1);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test warning message\ntest:1:\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithRegion() {
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    JSError error = JSError.syntaxError("test", "Test error message", file, 1, 2);
    Region region = new Region(file, 1, 1, 1, 4);
    String result = formatter.formatError(error, region);
    String expected = "test:1:2: Test error message\n 1:| ";
    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorNullError() {
    formatter.formatError(null);
  }

  @Test
  public void testFormatErrorEmptyDescription() {
    JSError error = JSError.syntaxError("test", "", 1, 1);
    String result = formatter.formatError(error);
    String expected = "ERROR -\n";
    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorNullSource() {
    JSError error = JSError.syntaxError("test", "Test error message", null, 1, 1);
    formatter.formatError(error);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorNegativeLineNumber() {
    JSError error = JSError.syntaxError("test", "Test error message", null, -1, 1);
    formatter.formatError(error);
  }

  @Test
  public void testFormatErrorExcerptTooLong() {
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    String longSource = new String(new char[1024 * 1024]);
    JSError error = JSError.syntaxError("test", "Test error message", file, 1, 1);
    formatter.formatError(error, longSource);
  }

  @Test
  public void testFormatWarningEmptyDescription() {
    JSError warning = JSError.warning("test", "", 1, 1);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING -\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningNullSource() {
    JSError warning = JSError.warning("test", "Test warning message", null, 1, 1);
    formatter.formatWarning(warning);
  }

  @Test
  public void testFormatWarningNegativeLineNumber() {
    JSError warning = JSError.warning("test", "Test warning message", null, -1, 1);
    formatter.formatWarning(warning);
  }

  @Test
  public void testLineNumberingFormatterFormatLine() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String line = "if (foo) {";
    int lineNumber = 1;
    String result = formatter.formatLine(line, lineNumber);
    String expected = "  " + lineNumber + "| ";
    assertEquals(expected, result);
  }

  @Test
  public void testLineNumberingFormatterFormatRegion() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String code = "if (foo) {\n alert('bar');\n}";
    Region region = new Region(null, 1, 1, 1, code.length());
    String result = formatter.formatRegion(region);
    String expected = " 1| if (foo) {\n  2|   alert('bar');\n 3| }";
    assertEquals(expected, result);
  }

  @Test
  public void testLightweightMessageFormatterWithoutSource() {
    formatter = LightweightMessageFormatter.withoutSource();
    JSError error = JSError.syntaxError("test", "Test error message", null, 1, 1);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test error message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testLightweightMessageFormatterWithSource() {
    formatter = new LightweightMessageFormatter(null);
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    JSError error = JSError.syntaxError("test", "Test error message", file, 1, 1);
    String result = formatter.formatError(error);
    String expected = "test:1: Test error message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testLightweightMessageFormatterWithSourceExcerpt() {
    formatter = new LightweightMessageFormatter(null, LINE);
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    JSError error = JSError.syntaxError("test", "Test error message", file, 1, 2);
    String result = formatter.formatError(error);
    String expected = "test:1:2: Test error message\n 1:| ";
    assertEquals(expected, result);
  }

  @Test
  public void testLightweightMessageFormatterWithRegion() {
    formatter = new LightweightMessageFormatter(null, LINE);
    SourceFile file = getSourceFile("LightweightMessageFormatterTest.java");
    JSError error = JSError.syntaxError("test", "Test error message", file, 1, 2);
    Region region = new Region(file, 1, 1, 1, 5);
    String result = formatter.formatError(error, region);
    String expected = "test:1: Test error message\n 1:\n   1:| if (foo) {\n  2:|   alert('bar');\n 3:| }";
    assertEquals(expected, result);
  }

  @Test
  public void testLineNumberingFormatterFormatLongSource() {
    String longSource = new String(new char[1024 * 1024]);
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(null, 1, 1, 1, longSource.length());
    String result = formatter.formatRegion(region);
    assertTrue(result.isEmpty());
  }
}