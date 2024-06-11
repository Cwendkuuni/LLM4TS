import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  @Test
  public void testWithoutSourceConstructor() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }
  
  @Test
  public void testConstructorWithSourceExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    SourceExcerpt excerpt = new SourceExcerpt();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
    assertNotNull(formatter);
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError();
    error.sourceName = "test";
    error.lineNumber = 10;
    error.description = "Test error";
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    
    String expected = "test:10: ERROR - Test error\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError();
    warning.sourceName = "test";
    warning.lineNumber = 10;
    warning.description = "Test warning";
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatWarning(warning);
    
    String expected = "test:10: WARNING - Test warning\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testLineFormatterFormatLine() {
    String line = "if (foo) {";
    int lineNumber = 10;
    
    LightweightMessageFormatter.LineNumberingFormatter lineFormatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String result = lineFormatter.formatLine(line, lineNumber);
    
    assertEquals(line, result);
  }
  
  @Test
  public void testLineFormatterFormatRegion() {
    Region region = new Region();
    region.setSourceExcerpt("if (foo) {\n  alert('bar');\n}");
    region.setBeginningLineNumber(10);
    region.setEndingLineNumber(12);
    
    LightweightMessageFormatter.LineNumberingFormatter lineFormatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String result = lineFormatter.formatRegion(region);
    
    String expected = "  10| if (foo) {\n  11|   alert('bar');\n  12| }";
    assertEquals(expected, result);
  }
}