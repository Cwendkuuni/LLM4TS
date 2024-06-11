import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  @Test
  public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("error", CheckLevel.ERROR, "description", "sourceName", 1, 0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - description\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("warning", CheckLevel.WARNING, "description", "sourceName", 1, 0);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("WARNING - description\n", formattedWarning);
  }

  // Additional test cases for edge cases and exceptions can be added here

}