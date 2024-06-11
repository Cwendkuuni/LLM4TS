import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Typical use case
    // Create a JSError object
    JSError error = new JSError("Sample error message", "filename", 10, 5);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    assertEquals("ERROR - Sample error message\n", result);

    // Edge case: error with no line number
    JSError errorNoLineNumber = new JSError("Error with no line number", "filename", 0, 0);
    String result2 = formatter.formatError(errorNoLineNumber);
    assertEquals("ERROR - Error with no line number\n", result2);

    // Exception handling: error with null source name
    JSError errorNullSourceName = new JSError("Error with null source name", null, 5, 5);
    String result3 = formatter.formatError(errorNullSourceName);
    assertEquals("ERROR - Error with null source name\n", result3);
  }

  @Test
  public void testFormatWarning() {
    // Typical use case
    // Create a JSError object
    JSError warning = new JSError("Sample warning message", "filename", 5, 3);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatWarning(warning);
    assertEquals("WARNING - Sample warning message\n", result);

    // Edge case: warning with no source
    JSError warningNoSource = new JSError("Warning with no source", null, 0, 0);
    String result2 = formatter.formatWarning(warningNoSource);
    assertEquals("WARNING - Warning with no source\n", result2);

    // Exception handling: warning with null description
    JSError warningNullDescription = new JSError(null, "filename", 5, 5);
    String result3 = formatter.formatWarning(warningNullDescription);
    assertEquals("WARNING - null\n", result3);
  }

  @Test
  public void testWithoutSource() {
    // Typical use case
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertEquals(LINE, formatter.excerpt);
  }

  // Additional test cases for other methods go here...

}