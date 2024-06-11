import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  private JSError error;
  private ByteArrayOutputStream outContent;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(null);
    error = new JSError("Test error", "test.js", 1, 1);
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @Test
  public void testFormatError() {
    String expected = "test.js:1: ERROR - Test error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    String expected = "test.js:1: WARNING - Test error\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    formatter = LightweightMessageFormatter.withoutSource();
    String expected = "ERROR - Test error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    formatter = LightweightMessageFormatter.withoutSource();
    String expected = "WARNING - Test error\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), LINE);
    String expected = "test.js:1: ERROR - Test error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), LINE);
    String expected = "test.js:1: WARNING - Test error\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithoutSourceExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), null);
    String expected = "test.js:1: ERROR - Test error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithoutSourceExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), null);
    String expected = "test.js:1: WARNING - Test error\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithSourceExcerpt() {
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    SourceExcerpt excerpt = new SourceExcerpt() {
      public String get(SourceExcerptProvider source, String sourceName, int lineNumber, ExcerptFormatter formatter) {
        return sourceExcerpt;
      }
    };
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), excerpt);
    String expected = "test.js:1: ERROR - Test error\nif (foo) {\n  alert('bar');\n}\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithSourceExcerpt() {
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    SourceExcerpt excerpt = new SourceExcerpt() {
      public String get(SourceExcerptProvider source, String sourceName, int lineNumber, ExcerptFormatter formatter) {
        return sourceExcerpt;
      }
    };
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), excerpt);
    String expected = "test.js:1: WARNING - Test error\nif (foo) {\n  alert('bar');\n}\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithLineExcerpt() {
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    SourceExcerpt excerpt = new SourceExcerpt() {
      public String get(SourceExcerptProvider source, String sourceName, int lineNumber, ExcerptFormatter formatter) {
        return sourceExcerpt;
      }
    };
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), excerpt);
    String expected = "test.js:1: ERROR - Test error\nif (foo) {\n  alert('bar');\n}\n     ^\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithLineExcerpt() {
    String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
    SourceExcerpt excerpt = new SourceExcerpt() {
      public String get(SourceExcerptProvider source, String sourceName, int lineNumber, ExcerptFormatter formatter) {
        return sourceExcerpt;
      }
    };
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), excerpt);
    String expected = "test.js:1: WARNING - Test error\nif (foo) {\n  alert('bar');\n}\n     ^\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }
}