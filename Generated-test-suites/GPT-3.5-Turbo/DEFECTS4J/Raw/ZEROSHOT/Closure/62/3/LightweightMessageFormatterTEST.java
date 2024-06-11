import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("Test Error", "test.js", 1);
    String expected = "test.js:1: ERROR - Test Error\n";
    assertEquals(expected, formatter.formatError(error));
  }
  
  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError("Test Warning", "test.js", 1);
    String expected = "test.js:1: WARNING - Test Warning\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }
  
  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }
  
  @Test
  public void testFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "alert('Hello, world!');";
    int lineNumber = 1;
    String expected = line;
    assertEquals(expected, formatter.formatLine(line, lineNumber));
  }
  
  @Test
  public void testFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String code = "if (foo) {\n  alert('bar');\n}";
    int beginningLineNumber = 9;
    int endingLineNumber = 11;
    LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region(code, beginningLineNumber, endingLineNumber);
    String expected = "   9| if (foo) {\n  10|   alert('bar');\n  11| }";
    assertEquals(expected, formatter.formatRegion(region));
  }
  
}