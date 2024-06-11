import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Typical use case
    JSError error = new JSError("Syntax Error", "test.js", 10, 5);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String expected = "test.js:10: ERROR - Syntax Error\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);

    // Edge case: error on the first line
    error = new JSError("Undefined variable", "test.js", 1, 0);
    expected = "test.js:1: ERROR - Undefined variable\n";
    actual = formatter.formatError(error);
    assertEquals(expected, actual);

    // Error scenario: null error source name
    error = new JSError("Invalid syntax", null, 5, 0);
    expected = "ERROR - Invalid syntax\n";
    actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    // Typical use case
    JSError warning = new JSError("Unused variable", "test.js", 5, 12);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String expected = "test.js:5: WARNING - Unused variable\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);

    // Edge case: warning on the last line
    warning = new JSError("Possible typo", "test.js", Integer.MAX_VALUE, 0);
    expected = "test.js:2147483647: WARNING - Possible typo\n";
    actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);

    // Error scenario: null warning description
    warning = new JSError(null, "test.js", 1, 0);
    expected = "test.js:1: WARNING - \n";
    actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
    // Additional assertions can be added for this method
  }

  @Test
  public void testFormat() {
    // Typical use case
    JSError error = new JSError("Invalid argument", "test.js", 15, 5);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String expected = "test.js:15: ERROR - Invalid argument\n";
    String actual = formatter.format(error, false);
    assertEquals(expected, actual);

    // Edge case: no source name and no line number
    error = new JSError("Null pointer exception", null, -1, 0);
    expected = "ERROR - Null pointer exception\n";
    actual = formatter.format(error, false);
    assertEquals(expected, actual);

    // Error scenario: null error description
    error = new JSError(null, "test.js", 10, 0);
    expected = "test.js:10: ERROR - \n";
    actual = formatter.format(error, false);
    assertEquals(expected, actual);
  }

  @Test
  public void testLineNumberingFormatter_formatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    // Typical use case
    String line = "if (foo) {";
    int lineNumber = 5;
    String expected = line;
    String actual = formatter.formatLine(line, lineNumber);
    assertEquals(expected, actual);

    // Edge case: empty line
    line = "";
    expected = "";
    actual = formatter.formatLine(line, lineNumber);
    assertEquals(expected, actual);

    // Error scenario: null line
    line = null;
    expected = null;
    actual = formatter.formatLine(line, lineNumber);
    assertEquals(expected, actual);
  }

  @Test
  public void testLineNumberingFormatter_formatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    // Typical use case
    Region region = new Region("if (foo) {\n  alert('bar');\n}");
    String expected = "   1| if (foo) {\n   2|   alert('bar');\n}";
    String actual = formatter.formatRegion(region);
    assertEquals(expected, actual);

    // Edge case: empty region
    region = new Region("");
    expected = "";
    actual = formatter.formatRegion(region);
    assertEquals(expected, actual);

    // Error scenario: null region
    region = null;
    expected = null;
    actual = formatter.formatRegion(region);
    assertEquals(expected, actual);
  }

}