import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {
  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Expected identifier", "Test.java", 1, 1);
    String expected = "ERROR - Expected identifier\nTest.java:1:\n 1| Expected identifier\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }
}