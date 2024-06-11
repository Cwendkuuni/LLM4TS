import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }
  
  @Test
  public void testFormatError() {
    // Test case 1
    JSError error1 = new JSError("test error", "test.js", 1, "error description");
    String expected1 = "test.js:1: ERROR - error description\n";
    assertEquals(expected1, formatter.formatError(error1));
    
    // Test case 2
    JSError error2 = new JSError("test error", "test.js", 2, "another error description");
    String expected2 = "test.js:2: ERROR - another error description\n";
    assertEquals(expected2, formatter.formatError(error2));
  }
  
  @Test
  public void testFormatWarning() {
    // Test case 1
    JSError warning1 = new JSError("test warning", "test.js", 1, "warning description");
    String expected1 = "test.js:1: WARNING - warning description\n";
    assertEquals(expected1, formatter.formatWarning(warning1));
    
    // Test case 2
    JSError warning2 = new JSError("test warning", "test.js", 2, "another warning description");
    String expected2 = "test.js:2: WARNING - another warning description\n";
    assertEquals(expected2, formatter.formatWarning(warning2));
  }
  
  @Test
  public void testWithoutSource() {
    // Test case 1
    LightweightMessageFormatter formatter1 = LightweightMessageFormatter.withoutSource();
    assertNull(formatter1.getSource());
    
    // Test case 2
    LightweightMessageFormatter formatter2 = LightweightMessageFormatter.withoutSource();
    assertNull(formatter2.getSource());
  }
  
  @Test
  public void testFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    
    // Test case 1
    String line1 = "if (foo) {";
    int lineNumber1 = 9;
    String expected1 = "if (foo) {";
    assertEquals(expected1, formatter.formatLine(line1, lineNumber1));
    
    // Test case 2
    String line2 = "alert('bar');";
    int lineNumber2 = 10;
    String expected2 = "alert('bar');";
    assertEquals(expected2, formatter.formatLine(line2, lineNumber2));
  }
  
  @Test
  public void testFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    
    // Test case 1
    String region1 = "if (foo) {\n  alert('bar');\n}";
    String expected1 = "    9| if (foo) {\n   10|   alert('bar');\n   11| }";
    assertEquals(expected1, formatter.formatRegion(region1));
    
    // Test case 2
    String region2 = "function test() {\n  console.log('test');\n  console.log('test');\n}";
    String expected2 = "    9| function test() {\n   10|   console.log('test');\n   11|   console.log('test');\n   12| }";
    assertEquals(expected2, formatter.formatRegion(region2));
  }
}