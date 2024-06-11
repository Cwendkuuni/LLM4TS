import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("Test Error", "File.js", 10);
    String expectedError = "File.js:10: ERROR - Test Error\n";
    assertEquals(expectedError, formatter.formatError(error));
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Warning", "File.js", 10);
    String expectedWarning = "File.js:10: WARNING - Test Warning\n";
    assertEquals(expectedWarning, formatter.formatWarning(warning));
  }
  
  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Test Error");
    String expectedError = "ERROR - Test Error\n";
    assertEquals(expectedError, formatter.formatError(error));
  }
  
  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Test Warning");
    String expectedWarning = "WARNING - Test Warning\n";
    assertEquals(expectedWarning, formatter.formatWarning(warning));
  }
  
  @Test
  public void testFormatErrorWithSourceExcerpt() {
    SourceExcerptProvider source = createMockSourceExcerptProvider();
    JSError error = new JSError("Test Error", "File.js", 10);
    String expectedError = "File.js:10: ERROR - Test Error\n" +
                           "    line 1\n" +
                           "    line 2\n" +
                           "    line 3\n" +
                           "    ^\n";
    assertEquals(expectedError, formatter.formatError(error));
  }
  
  @Test
  public void testFormatWarningWithSourceExcerpt() {
    SourceExcerptProvider source = createMockSourceExcerptProvider();
    JSError warning = new JSError("Test Warning", "File.js", 10);
    String expectedWarning = "File.js:10: WARNING - Test Warning\n" +
                             "    line 1\n" +
                             "    line 2\n" +
                             "    line 3\n" +
                             "    ^\n";
    assertEquals(expectedWarning, formatter.formatWarning(warning));
  }
  
  private SourceExcerptProvider createMockSourceExcerptProvider() {
    return new SourceExcerptProvider() {
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (sourceName.equals("File.js") && lineNumber == 10) {
          return "line 1\nline 2\nline 3";
        }
        return null;
      }
    };
  }
}