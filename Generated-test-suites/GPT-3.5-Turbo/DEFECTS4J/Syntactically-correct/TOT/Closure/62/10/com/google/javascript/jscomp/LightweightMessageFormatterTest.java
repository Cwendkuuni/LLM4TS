import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  // Test case for formatError()
  @Test
  public void testFormatError() {
    JSError error = new JSError("Error description", "test.js", 10, 3);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String formattedError = formatter.formatError(error);
    String expectedError = "test.js:10: ERROR - Error description\n";
    assertEquals(expectedError, formattedError);
  }

  // Test case for formatWarning()
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Warning description", "test.js", 5, 7);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String formattedWarning = formatter.formatWarning(warning);
    String expectedWarning = "test.js:5: WARNING - Warning description\n";
    assertEquals(expectedWarning, formattedWarning);
  }

  // Test case for format() with source excerpt
  @Test
  public void testFormatWithSourceExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    source.setSourceCode("var firstLine = 'This is the first line';\n" +
                         "var secondLine = 'This is the second line';\n" +
                         "var thirdLine = 'This is the third line';");
    JSError error = new JSError("Error description", "test.js", 2, 5);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String formattedError = formatter.format(error, false);
    String expectedError = "test.js:2: ERROR - Error description\n" +
                           "var secondLine = 'This is the second line';\n" +
                           "     ^\n";
    assertEquals(expectedError, formattedError);
  }

  // Test case for format() without source
  @Test
  public void testFormatWithoutSource() {
    JSError error = new JSError("Error description", null, -1, -1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter.withoutSource();
    String formattedError = formatter.format(error, false);
    String expectedError = "ERROR - Error description\n";
    assertEquals(expectedError, formattedError);
  }

  // Test case for LineNumberingFormatter.formatLine()
  @Test
  public void testLineNumberingFormatterFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "var test = 'This is a line';";
    String formattedLine = formatter.formatLine(line, 5);
    assertEquals(line, formattedLine);
  }

  // Test case for LineNumberingFormatter.formatRegion() with a valid region
  @Test
  public void testLineNumberingFormatterFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    SourceExcerptProvider source = new SourceExcerptProvider();
    source.setSourceCode("var line1 = 'This is line 1';\n" +
                         "function test() {\n" +
                         "  var line2 = 'This is line 2';\n" +
                         "}\n" +
                         "var line3 = 'This is line 3';");
    Regional regional = new Regional(source, 2, 4, "test.js");
    String formattedRegion = formatter.formatRegion(region);
    String expectedRegion = "  2| function test() {\n" +
                            "  3|   var line2 = 'This is line 2';\n" +
                            "  4| }";
    assertEquals(expectedRegion, formattedRegion);
  }

  // Test case for LineNumberingFormatter.formatRegion() with a null region
  @Test
  public void testLineNumberingFormatterFormatRegionNull() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String formattedRegion = formatter.formatRegion(null);
    assertNull(formattedRegion);
  }

  // Test case for LineNumberingFormatter.formatRegion() with a region containing an empty code
  @Test
  public void testLineNumberingFormatterFormatRegionEmpty() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    SourceExcerptProvider source = new SourceExcerptProvider();
    source.setSourceCode("");
    Regional regional = new Regional(source, 1, 1, "test.js");
    String formattedRegion = formatter.formatRegion(region);
    assertNull(formattedRegion);
  }
}