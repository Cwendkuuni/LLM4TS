import static com.google.javascript.jscomp.CheckLevel.ERROR;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.JSError;
import com.google.javascript.jscomp.test.TestSourceFileProvider;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link LightweightMessageFormatter} class.
 *
 */
public class LightweightMessageFormatterTest {

  private SourceFile sourceFile;
  private SourceExcerptProvider sourceProvider;

  @Before
  public void setup() {
    sourceFile = new SourceFile("Test.java", "package test;\n" +
        "public class Test {\n" +
        "  public static void main(String[] args) {\n" +
        "    // invalid int\n" +
        "    int x = 1 / 0;\n" +
        "  }\n" +
        "}");
    sourceProvider = new TestSourceFileProvider(ImmutableList.of(sourceFile));
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError(sourceFile, "Test.java", 3, 10,
        "Invalid operation: /", CheckLevel.ERROR);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider);
    String message = formatter.formatError(error);
    String expectedMessage = "3:10 ERROR - Invalid operation: /\n" +
        "  int x = 1 / 0;\n";
    assertEquals(expectedMessage, message);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError(sourceFile, "Test.java", 3, 10,
        "Unused variable: x", CheckLevel.WARNING);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider);
    String message = formatter.formatWarning(warning);
    String expectedMessage = "3:10 WARNING - Unused variable: x\n";
    assertEquals(expectedMessage, message);
  }

  @Test
  public void testFormatWithoutSource() {
    JSError error = new JSError(null, null, 0, 0, "Invalid operation: /", ERROR);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String message = formatter.formatError(error);
    String expectedMessage = "ERROR - Invalid operation: /\n";
    assertEquals(expectedMessage, message);
  }

  @Test
  public void testFormatLineNumberingFormatter() {
    JSError error = new JSError(sourceFile, "Test.java", 3, 7,
        "Unreachable code", ERROR);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider);
    String message = formatter.formatError(error);
    String expectedMessage = "3:7 ERROR - ERROR - Unreachable code\n" +
        "  int x = 1 / 0;\n" +
        "  // Unreachable code\n";
    assertEquals(expectedMessage, message);
  }
}