import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private SourceExcerptProvider source;
  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    source = new SourceExcerptProvider(); // example source excerpt provider
    formatter = new LightweightMessageFormatter(source);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("Test Error", "test.js", 10, 5);
    String formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Test Error\n", formattedError); // checking basic error formatting
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Warning", "test.js", 15, 3);
    String formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - Test Warning\n", formattedWarning); // checking basic warning formatting
  }

  @Test
  public void testFormatWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error without source", null, 0, 0);
    String formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Error without source\n", formattedError); // testing formatting without source
  }

  // Add more test cases for edge cases and exception handling scenarios here

}