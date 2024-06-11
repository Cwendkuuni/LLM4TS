import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Test error", "source.js", 10, CheckLevel.ERROR);
    String expected = "source.js:10: ERROR - Test error\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = new JSError("Test warning", "source.js", 5, CheckLevel.WARNING);
    String expected = "source.js:5: WARNING - Test warning\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatNullSourceExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Test error", null, 0, CheckLevel.ERROR);
    String expected = "ERROR - Test error\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testNoSourceExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    JSError error = new JSError("Test error", "source.js", 1, CheckLevel.ERROR);
    String expected = "source.js:1: ERROR - Test error\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWithSourceExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    JSError error = new JSError("Test error", "source.js", 2, CheckLevel.ERROR);
    error.setCharno(5);
    String expected = "source.js:2: ERROR - Test error\n     ^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testLineNumberingFormatterFormat() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "if (foo) {";
    int lineNumber = 1;
    String expected = "if (foo) {";
    assertEquals(expected, formatter.formatLine(line, lineNumber));
  }

  @Test
  public void testLineNumberingFormatterFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("if (foo) {\n  alert('bar');\n}", 1, 2);
    String expected = "  1| if (foo) {\n  2|   alert('bar');";
    assertEquals(expected, formatter.formatRegion(region));
  }
}