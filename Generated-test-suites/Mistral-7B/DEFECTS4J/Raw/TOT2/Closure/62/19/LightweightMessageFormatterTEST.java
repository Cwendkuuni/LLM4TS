import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.ExcerptFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter.LineNumberingFormatter;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    JSError error = new JSError("TestError", "TestDescription", 1, "TestFile.java", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(error);
    String expected = "ERROR: TestFile.java:10: TestError - TestDescription\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(null);
    assertNull(result);
  }

  @Test
  public void testFormatErrorWithEmptyDescription() {
    JSError error = new JSError("TestError", "", 1, "TestFile.java", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(error);
    String expected = "ERROR: TestFile.java:10: TestError\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNegativeLineNumber() {
    JSError error = new JSError("TestError", "TestDescription", -1, "TestFile.java", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(error);
    assertNull(result);
  }

  @Test
  public void testFormatWarningWithNullError() {
    JSError error = null;
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatWarning(error);
    assertNull(result);
  }

  @Test
  public void testFormatWarningWithEmptyDescription() {
    JSError error = new JSError("TestError", "", 1, "TestFile.java", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatWarning(error);
    String expected = "WARNING: TestFile.java:10: TestError\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNullSourceProvider() {
    JSError error = new JSError("TestError", "TestDescription", 1, "TestFile.java", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, null);
    String result = formatter.formatError(error);
    String expected = "ERROR: TestError - TestDescription\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithNullFormatter() {
    JSError error = new JSError("TestError", "TestDescription", 1, "TestFile.java", 10);
    LightweightMessageFormatter formatter = null;
    String result = formatter.formatError(error);
    assertNull(result);
  }
}