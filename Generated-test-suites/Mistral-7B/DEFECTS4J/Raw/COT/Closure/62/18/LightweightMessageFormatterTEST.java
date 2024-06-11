import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TopLevelSourceExcerptProvider;
import com.google.common.base.Preconditions;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, 1, "test", "Message without source", null);
    String result = formatter.formatError(error);
    String expected = "ERROR - Message without source\n";
    assertEquals(expected, result);
  }

  @Test
  public void testConstructorWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TopLevelSourceExcerptProvider(), LIGHTWEIGHT_MESSAGE_FORMATTER_LINE);
    JSError error = new JSError(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource() {
        return new SourceExcerpt("Test.java", 1, null);
      }
    }, 1, "Test.java", "test", new int[] {1, 5});
    String result = formatter.formatError(error);
    String expected = "Test.java:1: ERROR - test\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TopLevelSourceExcerptProvider(), LIGHTWEIGHT_MESSAGE_FORMATTER_LINE);
    JSError error = new JSError(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource() {
        return new SourceExcerpt("Test.java", 1, null);
      }
    }, 1, "Test.java", "test", new int[] {1, 5, 7, 8});
    String result = formatter.formatError(error);
    String expected = "Test.java:1: ERROR - test\nTest.java:5: ^^\nTest.java:7: ^^\nTest.java:8: ^^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TopLevelSourceExcerptProvider(), LIGHTWEIGHT_MESSAGE_FORMATTER_LINE);
    JSError warning = new JSError(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource() {
        return new SourceExcerpt("Test.java", 1, null);
      }
    }, 1, "Test.java", "test", null);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - test\nTest.java:1: \n";
    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testNullSourceProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        null, LIGHTWEIGHT_MESSAGE_FORMATTER_LINE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TopLevelSourceExcerptProvider(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new TopLevelSourceExcerptProvider(), new Object());
  }

  private static final SourceExcerptProvider TOP_LEVEL_SOURCE_EXCEPT_PROVIDER =
      new TopLevelSourceExcerptProvider();
  private static final int LIGHTWEIGHT_MESSAGE_FORMATTER_LINE = LINE;
}