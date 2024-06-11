import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  private JSError error;
  
  @Before
  public void setUp() {
    formatter = LightweightMessageFormatter.withoutSource();
    error = new JSError("Test error message", "test.js", 10, 5);
  }
  
  @Test
  public void testFormatError() {
    String expected = "test.js:10: ERROR - Test error message\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatWarning() {
    error = new JSError("Test warning message", "test.js", 15, 8);
    String expected = "test.js:15: WARNING - Test warning message\n";
    String actual = formatter.formatWarning(error);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }
  
  @Test
  public void testFormatErrorWithExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProviderStub();
    formatter = new LightweightMessageFormatter(source);
    error = new JSError("Test error message", "test.js", 10, 5);
    String expected = "test.js:10: ERROR - Test error message\n" +
                      "  line 8| \n" +
                      "  line 9| \n" +
                      "  line 10| \n" +
                      "^^^^^^^^\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }
  
  private static class SourceExcerptProviderStub implements SourceExcerptProvider {
    public String get(SourceFile source, int lineNumber) {
      return "";
    }
  }
  
}