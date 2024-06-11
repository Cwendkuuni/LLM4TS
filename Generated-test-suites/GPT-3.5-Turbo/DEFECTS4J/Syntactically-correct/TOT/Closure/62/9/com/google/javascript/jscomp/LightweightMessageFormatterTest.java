import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  
  //Test case for formatError method
  @Test
  public void testFormatError() {
    JSError error = new JSError("Invalid variable declaration");
    error.setSourceName("TestFile.js");
    error.setLineNumber(10);
    error.setDescription("Variable declaration missing semicolon");
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    
    String expected = "TestFile.js:10: ERROR - Variable declaration missing semicolon\n";
    assertEquals(expected, result);
  }
  
  //Test case for formatWarning method
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Possible null pointer dereference");
    warning.setSourceName("TestFile.js");
    warning.setLineNumber(5);
    warning.setDescription("Null pointer possibly dereferenced");
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatWarning(warning);
    
    String expected = "TestFile.js:5: WARNING - Null pointer possibly dereferenced\n";
    assertEquals(expected, result);
  }
  
  //Test case for format method
  @Test
  public void testFormat() {
    JSError error = new JSError("Invalid function call");
    error.setSourceName("TestFile.js");
    error.setLineNumber(15);
    error.setDescription("Function call with wrong number of arguments");
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.format(error, false);
    
    String expected = "TestFile.js:15: ERROR - Function call with wrong number of arguments\n";
    assertEquals(expected, result);
  }
  
  //Test case for getSource method
  @Test
  public void testGetSource() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    SourceExcerptProvider result = formatter.getSource();
    
    assertEquals(source, result);
  }
  
  //Test case for withoutSource method
  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    SourceExcerptProvider result = formatter.getSource();
    
    assertNull(result);
  }
  
  //Test case for formatLine method of LineNumberingFormatter class
  @Test
  public void testFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    
    String line = "if (foo) {";
    String result = formatter.formatLine(line, 9);
    
    assertEquals(line, result);
  }
  
  //Test case for formatRegion method of LineNumberingFormatter class
  @Test
  public void testFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    
    Region region = new Region();
    region.setSourceExcerpt("if (foo) {\n  alert('bar');\n}");
    region.setBeginningLineNumber(9);
    region.setEndingLineNumber(11);
    
    String result = formatter.formatRegion(region);
    
    String expected = "  9| if (foo) {\n 10|   alert('bar');\n 11| }";
    assertEquals(expected, result);
  }
  
}