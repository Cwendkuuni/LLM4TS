import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Test case 1
    JSError error1 = new JSError("test error", "test.js", 10, "description");
    LightweightMessageFormatter formatter1 = new LightweightMessageFormatter();
    String result1 = formatter1.formatError(error1);
    String expected1 = "test.js:10: ERROR - description\n";
    assertEquals(expected1, result1);

    // Test case 2
    JSError error2 = new JSError("test error", null, 0, "description");
    LightweightMessageFormatter formatter2 = new LightweightMessageFormatter();
    String result2 = formatter2.formatError(error2);
    String expected2 = "ERROR - description\n";
    assertEquals(expected2, result2);
  }

  @Test
  public void testFormatWarning() {
    // Test case 1
    JSError warning1 = new JSError("test warning", "test.js", 10, "description");
    LightweightMessageFormatter formatter1 = new LightweightMessageFormatter();
    String result1 = formatter1.formatWarning(warning1);
    String expected1 = "test.js:10: WARNING - description\n";
    assertEquals(expected1, result1);

    // Test case 2
    JSError warning2 = new JSError("test warning", null, 0, "description");
    LightweightMessageFormatter formatter2 = new LightweightMessageFormatter();
    String result2 = formatter2.formatWarning(warning2);
    String expected2 = "WARNING - description\n";
    assertEquals(expected2, result2);
  }
  
  @Test
  public void testFormat() {
    // Test case 1
    JSError error1 = new JSError("test error", "test.js", 10, "description");
    LightweightMessageFormatter formatter1 = new LightweightMessageFormatter();
    String result1 = formatter1.format(error1, false);
    String expected1 = "test.js:10: ERROR - description\n";
    assertEquals(expected1, result1);

    // Test case 2
    JSError error2 = new JSError("test warning", "test.js", 10, "description");
    LightweightMessageFormatter formatter2 = new LightweightMessageFormatter();
    String result2 = formatter2.format(error2, true);
    String expected2 = "test.js:10: WARNING - description\n";
    assertEquals(expected2, result2);
  }
}