import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError_withoutSourceName() {
    JSError error = new JSError("Error message", null, 0);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    
    String expected = "ERROR - Error message\n";
    String actual = formatter.formatError(error);
    
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatError_withSourceNameAndLineNumber() {
    JSError error = new JSError("Error message", "source.js", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());

    String expected = "source.js:10: ERROR - Error message\n";
    String actual = formatter.formatError(error);

    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning_withoutSourceNameAndExcerpt() {
    JSError warning = new JSError("Warning message", null, 0);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    String expected = "WARNING - Warning message\n";
    String actual = formatter.formatWarning(warning);

    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning_withSourceNameAndExcerpt() {
    JSError warning = new JSError("Warning message", "source.js", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), new SourceExcerpt());

    String expected = "source.js:10: WARNING - Warning message\n";
    String actual = formatter.formatWarning(warning);

    assertEquals(expected, actual);
  }

  @Test
  public void testFormatError_withSourceNameAndExcerptAndCharno() {
    JSError error = new JSError("Error message", "source.js", 10);
    error.setCharno(5);
    SourceExcerptProvider source = new SourceExcerptProvider();
    source.addSource("source.js", "var foo = 5;");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, new SourceExcerpt());

    String expected = "source.js:10: ERROR - Error message\nvar foo = 5;\n     ^\n";
    String actual = formatter.formatError(error);

    assertEquals(expected, actual);
  }

  @Test
  public void testFormatError_withMissingExcerpt() {
    JSError error = new JSError("Error message", "source.js", 10);
    error.setCharno(50);
    SourceExcerptProvider source = new SourceExcerptProvider();
    source.addSource("source.js", "var foo = 5;");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, new SourceExcerpt());

    String expected = "source.js:10: ERROR - Error message\nvar foo = 5;\n";
    String actual = formatter.formatError(error);

    assertEquals(expected, actual);
  }
}