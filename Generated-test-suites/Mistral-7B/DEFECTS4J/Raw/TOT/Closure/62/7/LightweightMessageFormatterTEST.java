import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.withoutSource;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestHelper;
import com.google.javascript.jscomp.testing.TestSourceExcerptProvider;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError(
        "TestType", 1, 1, "TestMessage", 1, 4, "TestFile.java");

    assertEquals(
        "TestFile:1:4 - ERROR - TestMessage\n" +
            "^",
        formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TestType", 1, 1, "TestMessage");

    assertEquals("ERROR - TestMessage\n", formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError warning = new JSError(
        "TestType", 1, 1, "TestMessage", null, CheckLevel.WARNING);

    assertEquals(
        "TestFile:1:1 - WARNING - TestMessage\n",
        formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("TestType", 1, 1, "TestMessage", null, CheckLevel.WARNING);

    assertEquals("WARNING - TestMessage\n", formatter.formatWarning(warning));
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError error = new JSError("TestType", 1, 1, "TestMessage");

    formatter.formatError(error);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatWarningWithNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    JSError warning = new JSError("TestType", 1, 1, "TestMessage", null, CheckLevel.WARNING);

    formatter.formatWarning(warning);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorWithInvalidExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, "INVALID_EXCEPT");
    JSError error = new JSError("TestType", 1, 1, "TestMessage");

    formatter.formatError(error);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWarningWithInvalidExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, "INVALID_EXCEPT");
    JSError warning = new JSError("TestType", 1, 1, "TestMessage", null, CheckLevel.WARNING);

    formatter.formatWarning(warning);
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError("TestType", 1, 1, "TestMessage");

    assertEquals("TestType:1:1 - ERROR - TestMessage\n", formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithEmptySource() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError warning = new JSError("TestType", 1, 1, "TestMessage", null, CheckLevel.WARNING);

    assertEquals("TestType:1:1 - WARNING - TestMessage\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithRegion() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, REGION);
    JSError error = new JSError(
        "TestType", 1, 1, "TestMessage", 1, 4, "TestFile.java");

    CodeSnippetTestHelper testHelper = new CodeSnippetTestHelper();
    String code = testHelper.getCode("TestFile.java", "int a = 1; if (a > 0) { alert('Test'); }");
    SourceExcerptProvider sourceProvider = new TestSourceExcerptProvider(code);
    formatter = new LightweightMessageFormatter(sourceProvider, REGION);

    Region region = testHelper.getRegion("TestFile.java", 1, 1, 2);

    assertEquals(
        "TestFile:1:1 - ERROR - TestMessage\n" +
            "int a = 1;\n" +
            "  | ^--------\n",
        formatter.formatError(error, region));
  }

  @Test
  public void testFormatWarningWithRegion() {
    SourceExcerptProvider source = new TestSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, REGION);
    JSError warning = new JSError(
        "TestType", 1, 1, "TestMessage", null, CheckLevel.WARNING);

    CodeSnippetTestHelper testHelper = new CodeSnippetTestHelper();
    String code = testHelper.getCode("TestFile.java", "int a = 1; if (a > 0) { alert('Test'); }");
    SourceExcerptProvider sourceProvider = new TestSourceExcerptProvider(code);
    formatter = new LightweightMessageFormatter(sourceProvider, REGION);

    Region region = testHelper.getRegion("TestFile.java", 1, 1, 2);

    assertEquals(
        "TestFile:1:1 - WARNING - TestMessage\n" +
            "int a = 1;\n" +
            "  | ^--------\n",
        formatter.formatWarning(warning, region));
  }
}