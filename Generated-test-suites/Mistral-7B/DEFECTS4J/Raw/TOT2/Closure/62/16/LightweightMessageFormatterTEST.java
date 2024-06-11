import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Message;
import com.google.javascript.jscomp.Messages;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TypeCheckerTestBase;

public class LightweightMessageFormatterTest extends TypeCheckerTestBase {

  @Test
  public void testFormatError() {
    JSError error = new JSError(new SourceFile("test.js"), "test", 1, "Test Error", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter( SourceExcerptProvider.createForTesting() );
    String result = formatter.formatError(error);
    String expected = String.format("%s:1: Test Error%n", "test");
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError(new SourceFile("test.js"), "test", 1, "Test Warning", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter( SourceExcerptProvider.createForTesting() );
    String result = formatter.formatWarning(warning);
    String expected = String.format("%s:1: WARNING - Test Warning%n", "test");
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError(new SourceFile("test.js"), "test", 1, "Test Error", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt get(SourceFile source, String name, int lineNumber, ExcerptFormatter excerptFormatter) {
            return excerptFormatter.formatLine(source.getContent().get(lineNumber - 1), lineNumber);
          }
        },
        LineNumberingFormatter.class
    );
    String result = formatter.formatError(error);
    String expected = String.format("%s:1 test%n  % 1: Test Error%n", "test");
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError(new SourceFile("test.js"), "test", 1, "Test Warning", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt get(SourceFile source, String name, int lineNumber, ExcerptFormatter excerptFormatter) {
            return excerptFormatter.formatLine(source.getContent().get(lineNumber - 1), lineNumber);
          }
        },
        LineNumberingFormatter.class
    );
    String result = formatter.formatWarning(warning);
    String expected = String.format("%s:1 test%n  % 1: WARNING - Test Warning%n", "test");
    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatErrorWithNullSource() {
    JSError error = new JSError(new SourceFile("test.js"), "test", 1, "Test Error", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    formatter.formatError(error);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWarningWithNullSource() {
    JSError warning = new JSError(new SourceFile("test.js"), "test", 1, "Test Warning", null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    formatter.formatWarning(warning);
  }

  @Test(expected = IllegalStateException.class)
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError(new SourceFile("test.js"), "test", 1, "Test Error", null);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    formatter.formatError(error);
  }

  @Test(expected = IllegalStateException.class)
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError(new SourceFile("test.js"), "test", 1, "Test Warning", null);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    formatter.formatWarning(warning);
  }
}