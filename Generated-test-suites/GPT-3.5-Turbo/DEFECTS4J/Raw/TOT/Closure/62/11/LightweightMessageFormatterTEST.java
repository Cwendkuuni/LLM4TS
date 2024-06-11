import static org.junit.Assert.*;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  // Test case for LightweightMessageFormatter()
  @Test
  public void testLightweightMessageFormatterDefaultConstructor() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertNotNull(formatter);
  }

  // Test case for LightweightMessageFormatter(SourceExcerptProvider source)
  @Test
  public void testLightweightMessageFormatterConstructorWithSourceExcerptProvider() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    assertNotNull(formatter);
  }

  // Test case for LightweightMessageFormatter(SourceExcerptProvider source, SourceExcerpt excerpt)
  @Test
  public void testLightweightMessageFormatterConstructorWithSourceExcerptProviderAndExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    SourceExcerpt excerpt = new SourceExcerpt(); 
    LightweightMessageFormatter formatter = 
        new LightweightMessageFormatter(source, excerpt);
    assertNotNull(formatter);
  }

  // Test case for static LightweightMessageFormatter withoutSource()
  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }

  // Test case for formatError(JSError error)
  @Test
  public void testFormatError() {
    JSError error = new JSError("Error description");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String expectedOutput = "ERROR - Error description\n";
    assertEquals(expectedOutput, formatter.formatError(error));
  }

  // Test case for formatWarning(JSError warning)
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Warning description");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String expectedOutput = "WARNING - Warning description\n";
    assertEquals(expectedOutput, formatter.formatWarning(warning));
  }

  // Test case for format(JSError error, boolean warning)
  @Test
  public void testFormat() {
    JSError error = new JSError("Error description");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String expectedOutput = "ERROR - Error description\n";
    assertEquals(expectedOutput, formatter.format(error, false));
  }

  // Test case for LineNumberingFormatter.formatLine(String line, int lineNumber)
  @Test
  public void testLineNumberingFormatterFormatLine() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String line = "if (foo) {";
    int lineNumber = 9;
    String expectedOutput = "if (foo) {";
    assertEquals(expectedOutput, formatter.formatLine(line, lineNumber));
  }

  // Test case for LineNumberingFormatter.formatRegion(Region region)
  @Test
  public void testLineNumberingFormatterFormatRegion() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region();
    region.setSourceExcerpt("if (foo) {\n  alert('bar');\n}");
    region.setBeginningLineNumber(9);
    region.setEndingLineNumber(11);
    String expectedOutput = "  9| if (foo) {\n  10|   alert('bar');\n  11| }";
    assertEquals(expectedOutput, formatter.formatRegion(region));
  }

}