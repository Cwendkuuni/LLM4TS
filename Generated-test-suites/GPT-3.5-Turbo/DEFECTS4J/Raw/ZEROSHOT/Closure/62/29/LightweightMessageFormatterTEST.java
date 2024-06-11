import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("Error description", "test.js", 1);
    String result = formatter.formatError(error);
    
    String expected = "test.js:1: ERROR - Error description\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError("Warning description", "test.js", 1);
    String result = formatter.formatWarning(warning);
    
    String expected = "test.js:1: WARNING - Warning description\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error description", "test.js", 1);
    String result = formatter.formatError(error);
    
    String expected = "ERROR - Error description\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Warning description", "test.js", 1);
    String result = formatter.formatWarning(warning);
    
    String expected = "WARNING - Warning description\n";
    assertEquals(expected, result);
  }
}