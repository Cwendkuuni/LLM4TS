import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(null);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("error", "source.js", 10, "description");
    String result = formatter.formatError(error);
    String expected = "source.js:10: ERROR - description\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("warning", "source.js", 20, "description");
    String result = formatter.formatWarning(warning);
    String expected = "source.js:20: WARNING - description\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("error", "source.js", 10, "description");
    String result = formatterWithoutSource.formatError(error);
    String expected = "ERROR - description\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("warning", "source.js", 20, "description");
    String result = formatterWithoutSource.formatWarning(warning);
    String expected = "WARNING - description\n";
    assertEquals(expected, result);
  }

  @Test
  public void testLineNumberingFormatterFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "if (foo) {";
    int lineNumber = 9;
    String result = formatter.formatLine(line, lineNumber);
    String expected = "if (foo) {";
    assertEquals(expected, result);
  }

  @Test
  public void testLineNumberingFormatterFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("source.js", 5, 10, "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11\n");
    String result = formatter.formatRegion(region);
    String expected = "  5| line1\n" +
        "  6| line2\n" +
        "  7| line3\n" +
        "  8| line4\n" +
        "  9| line5\n" +
        " 10| line6\n" +
        " 11| line7\n" +
        " 12| line8\n" +
        " 13| line9\n" +
        " 14| line10\n" +
        " 15| line11\n";
    assertEquals(expected, result);
  }

}