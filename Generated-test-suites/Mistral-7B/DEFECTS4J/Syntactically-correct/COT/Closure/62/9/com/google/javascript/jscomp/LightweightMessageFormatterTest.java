import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSError.Level;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.jscomp.testing.TestSourceFile.Region;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatter_withoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(Level.ERROR, "Test Error", "testFile.js", 1, 1);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);

    error.setSourceName("testFile.js");
    error.setLineNumber(1);
    error.setCharno(1);
    result = formatter.formatError(error);
    expected += "1:1";
    assertEquals(expected, result);

    error.setSourceName(null);
    result = formatter.formatError(error);
    expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }

  @Test
  public void testLightweightMessageFormatter_withSource() {
    SourceExcerptProvider source = new TestSourceFile("testFile.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError(ERROR, "Test Error", "testFile.js", 1, 1);
    String result = formatter.formatError(error);
    String expected = "testFile.js:1:1 ERROR - Test Error\n";
    assertEquals(expected, result);

    Region region = new TestSourceFile("testFile.js").getRegion(1, 0, 3);
    error.setCharno(region.getEndingCharacterNumber());
    result = formatter.formatError(error);
    expected += " 1| ";
    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testLightweightMessageFormatter_nullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLightweightMessageFormatter_nullExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TestSourceFile("testFile.js"), null);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TestSourceFile("testFile.js"), LINE);
    JSError warning = new JSError(WARNING, "Test Warning", "testFile.js", 1, 1);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test Warning\n";
    assertEquals(expected, result);
  }
}