import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;

public class LightweightMessageFormatterTest {

  // Test case for formatError method
  @Test 
  public void testFormatError() {
    // Basic test case
    JSError error = new JSError("test source name", 10, "test description");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    assertEquals("test source name:10: ERROR - test description\n", result);
    
    // Edge case: error with null source name and line number
    error = new JSError(null, 0, "test description");
    formatter = new LightweightMessageFormatter();
    result = formatter.formatError(error);
    assertEquals("ERROR - test description\n", result);
    
    // Exception handling: if the error is null
    error = null;
    formatter = new LightweightMessageFormatter();
    result = formatter.formatError(error);
    assertEquals("", result);
  }

  // Test case for formatWarning method
  @Test
  public void testFormatWarning() {
    // Basic test case
    JSError warning = new JSError("test source name", 10, "test description");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatWarning(warning);
    assertEquals("test source name:10: WARNING - test description\n", result);
    
    // Edge case: warning with null source name and line number
    warning = new JSError(null, 0, "test description");
    formatter = new LightweightMessageFormatter();
    result = formatter.formatWarning(warning);
    assertEquals("WARNING - test description\n", result);
    
    // Exception handling: if the warning is null
    warning = null;
    formatter = new LightweightMessageFormatter();
    result = formatter.formatWarning(warning);
    assertEquals("", result);
  }
}