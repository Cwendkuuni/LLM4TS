import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  private LightweightMessageFormatter messageFormatter;

  @Before
  public void setUp() {
    messageFormatter = new LightweightMessageFormatter(null);
  }

  @Test
  public void testFormatError_withSource() {
    JSError error = new JSError("error", "source.js", 1, CheckLevel.ERROR);
    String expected = "source.js:1: ERROR - error\n";

    assertEquals(expected, messageFormatter.formatError(error));
  }

  @Test
  public void testFormatError_withoutSource() {
    JSError error = new JSError("error", null, -1, CheckLevel.ERROR);
    String expected = "ERROR - error\n";

    assertEquals(expected, messageFormatter.formatError(error));
  }

  @Test
  public void testFormatWarning_withSource() {
    JSError warning = new JSError("warning", "source.js", 2, CheckLevel.WARNING);
    String expected = "source.js:2: WARNING - warning\n";

    assertEquals(expected, messageFormatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarning_withoutSource() {
    JSError warning = new JSError("warning", null, -1, CheckLevel.WARNING);
    String expected = "WARNING - warning\n";

    assertEquals(expected, messageFormatter.formatWarning(warning));
  }

  @Test
  public void testFormat_withSource() {
    JSError error = new JSError("error", "source.js", 1, CheckLevel.ERROR);
    String expected = "source.js:1: ERROR - error\n";

    assertEquals(expected, messageFormatter.format(error, false));
  }

  @Test
  public void testFormat_withoutSource() {
    JSError error = new JSError("error", null, -1, CheckLevel.ERROR);
    String expected = "ERROR - error\n";

    assertEquals(expected, messageFormatter.format(error, false));
  }

  @Test
  public void testLineExcerptFormatter_formatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "if (foo) {";

    assertEquals(line, formatter.formatLine(line, 1));
  }

  @Test
  public void testLineExcerptFormatter_formatRegion_withCode() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String code = "if (foo) {\n  alert('bar');\n}";
    int startingLineNumber = 1;
    int endingLineNumber = 3;
    String expected = "  1| if (foo) {\n  2|   alert('bar');\n  3| }";

    LightweightMessageFormatter.Region region =
        new LightweightMessageFormatter.Region(code, startingLineNumber, endingLineNumber);
    String result = formatter.formatRegion(region);

    assertEquals(expected, result);
  }

  @Test
  public void testLineExcerptFormatter_formatRegion_emptyCode() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String code = "";
    int startingLineNumber = 1;
    int endingLineNumber = 1;
    String expected = null;

    LightweightMessageFormatter.Region region =
        new LightweightMessageFormatter.Region(code, startingLineNumber, endingLineNumber);
    String result = formatter.formatRegion(region);

    assertEquals(expected, result);
  }

  @Test
  public void testLineExcerptFormatter_formatRegion_nullRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    LightweightMessageFormatter.Region region = null;
    String expected = null;

    String result = formatter.formatRegion(region);

    assertEquals(expected, result);
  }

  @Test
  public void testToString() {
    String expected =
        "LightweightMessageFormatter [source=null, excerpt=LINE]";

    assertEquals(expected, messageFormatter.toString());
  }
}