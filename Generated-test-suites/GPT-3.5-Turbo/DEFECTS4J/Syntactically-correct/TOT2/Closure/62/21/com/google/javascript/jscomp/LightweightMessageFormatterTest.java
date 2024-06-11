import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String source, int line, ExcerptFormatter formatter) {
        return "Test Source Excerpt";
      }
    });
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("Test Source", 1, "Test Error Description", 0);
    String formattedError = formatter.formatError(error);
    assertEquals("Error - Test Error Description\nTest Source Excerpt\n", formattedError);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Source", 1, "Test Warning Description", 0);
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("Warning - Test Warning Description\nTest Source Excerpt\n", formattedWarning);
  }
  
  @Test
  public void testFormatErrorWithNullSource() {
    JSError error = new JSError(null, 0, "Test Error Description", 0);
    String formattedError = formatter.formatError(error);
    assertEquals("Error - Test Error Description\n", formattedError);
  }
  
  @Test
  public void testFormatWithSourceExcerptEndingWithArrow() {
    JSError error = new JSError("Test Source", 1, "Test Error Description", 5);
    String formattedError = formatter.formatError(error);
    assertEquals("Test Source:1: Error - Test Error Description\nTest Source Excerpt\n     ^\n", formattedError);
  }
  
  @Test
  public void testFormatWithoutSourceExcerpt() {
    formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Test Source", 1, "Test Error Description", 0);
    String formattedError = formatter.formatError(error);
    assertEquals("Error - Test Error Description\n", formattedError);
  }
}