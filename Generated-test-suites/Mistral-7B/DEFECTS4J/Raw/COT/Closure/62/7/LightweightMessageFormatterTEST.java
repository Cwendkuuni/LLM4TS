import static com.google.javascript.jscomp.CheckLevel.ERROR;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestsForNestedClasses;
import com.google.javascript.jscomp.testing.util.SourceFile;

import java.io.IOException;
import java.util.List;

public class LightweightMessageFormatterTest extends TestsForNestedClasses {

  @SuppressWarnings("unchecked")
  private SourceExcerptProvider sourceExcerptProviderMock = mock(SourceExcerptProvider.class);

  private LightweightMessageFormatter lmf;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    lmf = new LightweightMessageFormatter(sourceExcerptProviderMock, LINE);
  }

  @org.junit.Test
  public void testEmptySourceExcerpt() {
    // Test case for when sourceExcerpt is empty
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 0, excerptFormatter)).thenReturn("");

    JSError error = new JSError("testError", "", 0, 0);
    String expected = "ERROR  - testError\n";
    String actual = lmf.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorNoSource() {
    // Test case for when error has no source
    JSError error = new JSError("testError", "", 0, 0);
    String expected = "ERROR  - testError\n";
    String actual = lmf.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSource() throws IOException {
    // Test case for when error has a source
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSource(AnyString.class)).thenReturn(sourceFile);

    JSError error = new JSError("testError", "Test.js", 3, 11);
    String expected = "Test.js:3:11 ERROR  - testError\n" +
        "    x = x + 1;\n";
    String actual = lmf.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningNoSource() {
    // Test case for when warning has no source
    JSError warning = new JSError("testWarning", "", 0, 0);
    String expected = "WARNING  - testWarning\n";
    String actual = lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSource() throws IOException {
    // Test case for when warning has a source
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "    if (x > 10) {\n" +
        "      console.log('Warning: x is greater than 10');\n" +
        "    }\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSource(AnyString.class)).thenReturn(sourceFile);

    JSError warning = new JSError("testWarning", "Test.js", 4, 0);
    String expected = "Test.js:4:0 WARNING  - Warning: x is greater than 10\n";
    String actual = lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithEmptyExcerpt() {
    // Test case for when excerpt is empty
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 0, excerptFormatter)).thenReturn("");

    JSError error = new JSError("testError", "Test.js", 3, 11);
    String expected = "Test.js:3:11 ERROR  - testError\n";
    String actual = lmf.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSourceExcerpt() throws IOException {
    // Test case for when excerpt is not empty
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "    if (x > 10) {\n" +
        "      throw new Error('Test error');\n" +
        "    }\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 3, excerptFormatter)).thenReturn("x = x + 1;");

    JSError error = new JSError("testError", "Test.js", 3, 11);
    String expected = "Test.js:3:11 ERROR  - testError\n" +
        "x = x + 1;\n";
    String actual = lmf.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSourceExcerpt() throws IOException {
    // Test case for when excerpt is not empty
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "    if (x > 10) {\n" +
        "      console.log('Warning: x is greater than 10');\n" +
        "    }\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 4, excerptFormatter)).thenReturn("console.log('Warning: x is greater than 10');");

    JSError warning = new JSError("testWarning", "Test.js", 4, 0);
    String expected = "Test.js:4:0 WARNING  - Warning: x is greater than 10\n" +
        "console.log('Warning: x is greater than 10');\n";
    String actual = lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorNoSourceAndWarning() {
    // Test case for when error has no source and warning has no source
    JSError error = new JSError("testError", "", 0, 0);
    JSError warning = new JSError("testWarning", "", 0, 0);

    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 0, excerptFormatter)).thenReturn("");

    String expected = "ERROR  - testError\n" +
        "WARNING  - testWarning\n";
    String actual = lmf.formatError(error) + lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSourceAndWarningNoSource() {
    // Test case for when error has source and warning has no source
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "    if (x > 10) {\n" +
        "      throw new Error('Error message');\n" +
        "    }\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 3, excerptFormatter)).thenReturn("x = x + 1;");

    JSError error = new JSError("testError", "Test.js", 3, 11);
    JSError warning = new JSError("testWarning", "", 0, 0);

    String expected = "Test.js:3:11 ERROR  - error message\n" +
        "WARNING  - testWarning\n";
    String actual = lmf.formatError(error) + lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorAndWarningWithSource() throws IOException {
    // Test case for when error and warning have source
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "    if (x > 10) {\n" +
        "      console.log('Warning: x is greater than 10');\n" +
        "      throw new Error('Error message');\n" +
        "    }\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 3, excerptFormatter)).thenReturn("x = x + 1;");
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 4, excerptFormatter)).thenReturn("console.log('Warning: x is greater than 10');");

    JSError error = new JSError("testError", "Test.js", 3, 11);
    JSError warning = new JSError("testWarning", "Test.js", 4, 0);

    String expected = "Test.js:3:11 ERROR  - error message\n" +
        "Test.js:4:0 WARNING  - Warning: x is greater than 10\n";
    String actual = lmf.formatError(error) + lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorAndWarningWithEmptySourceExcerpt() throws IOException {
    // Test case for when error and warning have empty source excerpt
    String sourceCode = "class Test {\n" +
        "  testFunction() {\n" +
        "    var x = 0;\n" +
        "    x = x + 1;\n" +
        "    if (x > 10) {\n" +
        "      console.log('Warning: x is greater than 10');\n" +
        "      throw new Error('Error message');\n" +
        "    }\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 3, excerptFormatter)).thenReturn("x = x + 1;");
    when(sourceExcerptProviderMock.getSourceExcerpt(AnyString.class, AnyString.class,
        (int) 4, excerptFormatter)).thenReturn("");

    JSError error = new JSError("testError", "Test.js", 3, 11);
    JSError warning = new JSError("testWarning", "Test.js", 4, 0);

    String expected = "Test.js:3:11 ERROR  - error message\n" +
        "Test.js:4: WARNING  - Warning:\n";
    String actual = lmf.formatError(error) + lmf.formatWarning(warning);
    assertEquals(expected, actual);
  }
}