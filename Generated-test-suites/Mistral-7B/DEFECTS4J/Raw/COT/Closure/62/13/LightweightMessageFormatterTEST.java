import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.CheckLevel;
import com.google.common.base.Preconditions;

import java.util.Arrays;

public class LightweightMessageFormatterTest {

  @org.junit.Test
  public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Test Error", "Test File", 1, 1, "Test Message");
    String message = formatter.formatError(error);
    String expected = "ERROR - Test Error - Test Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testConstructorWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt(String.format("File %s Line %d: %s", sourceName, lineNumber, "Test Code"));
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Test Error", "Test File", 1, 1, "Test Message");
    String message = formatter.formatError(error);
    String expected = "Test File:1: Test Code\nERROR - Test Error - Test Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatError() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt(String.format("File %s Line %d: %s", sourceName, lineNumber, "Test Code"));
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

    JSError error1 = new JSError("Test Error 1", "Test File 1", 1, 1, "Test Message 1");
    String message1 = formatter.formatError(error1);
    String expected1 = "Test File 1:1: Test Code\nERROR - Test Error 1 - Test Message 1\n";
    assertEquals(expected1, message1);

    JSError error2 = new JSError("Test Error 2", "Test File 2", 2, 2, "Test Message 2");
    String message2 = formatter.formatError(error2);
    String expected2 = "Test File 2:2: Test Code\nERROR - Test Error 2 - Test Message 2\n";
    assertEquals(expected2, message2);
  }

  @org.junit.Test
  public void testFormatWarning() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt(String.format("File %s Line %d: %s", sourceName, lineNumber, "Test Code"));
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

    JSError warning1 = new JSError("Test Warning 1", "Test File 1", 1, 1, "Test Message 1");
    String message1 = formatter.formatWarning(warning1);
    String expected1 = "WARNING - Test Error 1 - Test Message 1\n";
    assertEquals(expected1, message1);

    JSError warning2 = new JSError("Test Warning 2", "Test File 2", 2, 2, "Test Message 2");
    String message2 = formatter.formatWarning(warning2);
    String expected2 = "WARNING - Test Error 2 - Test Message 2\n";
    assertEquals(expected2, message2);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testFormatErrorNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    formatter.formatError(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testFormatWarningNullError() {
    LightweightMessageFormatter formatter = new LightwarMessageFormatter(null, LightweightMessageFormatter.LINE);
    formatter.formatWarning(null);
  }

  @org.junit.Test
  public void testFormatErrorEmptySource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return null;
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Test Error", "Test File", 1, 1, "Test Message");
    String message = formatter.formatError(error);
    String expected = "ERROR - Test Error - Test Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatErrorEmptySourceExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt("");
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Test Error", "Test File", 1, 1, "Test Message");
    String message = formatter.formatError(error);
    String expected = "ERROR - Test Error - Test Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatErrorLineNumberOutOfRange() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        String code = String.format("File %s Line %d: %s", sourceName, lineNumber, "Test Code");
        return new SourceExcerpt(code);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Test Error", "Test File", 1, Integer.MAX_VALUE, "Test Message");
    String message = formatter.formatError(error);
    String expected = "ERROR - Test Error - Test Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatErrorCharnoOutOfRange() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        String code = String.format("File %s Line %d: %s", sourceName, lineNumber, "Test Code");
        return new SourceExcerpt(code);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Test Error", "Test File", 1, 1, "Test Message", 1000);
    String message = formatter.formatError(error);
    String expected = "ERROR - Test Error - Test Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatErrorEmptyDescription() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt(String.format("File %s Line %d: %s", sourceName, lineNumber, "Test Code"));
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Test Error", "Test File", 1, 1, "");
    String message = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, message);
  }
}