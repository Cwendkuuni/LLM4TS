import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("test-source", 5, CheckLevel.ERROR, "Test error message");
    String formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("test-source:5: ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("test-source", 10, CheckLevel.WARNING, "Test warning message");
    String formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("test-source:10: WARNING - Test warning message\n", formattedWarning);
  }

  @Test
  public void testFormatErrorWithNullSource() {
    JSError error = new JSError(null, 0, CheckLevel.ERROR, "Test error message");
    String formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarningWithNullSource() {
    JSError warning = new JSError(null, 0, CheckLevel.WARNING, "Test warning message");
    String formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - Test warning message\n", formattedWarning);
  }

  @Test
  public void testFormatErrorWithNoLineNumber() {
    JSError error = new JSError("test-source", 0, CheckLevel.ERROR, "Test error message");
    String formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("test-source: ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarningWithNoLineNumber() {
    JSError warning = new JSError("test-source", 0, CheckLevel.WARNING, "Test warning message");
    String formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("test-source: WARNING - Test warning message\n", formattedWarning);
  }

  @Test
  public void testFormatErrorWithSourceExcerpt() {
    JSError error = new JSError("test-source", 5, CheckLevel.ERROR, "Test error message");
    // Additional test cases can be added to test specific conditions
  }

  @Test
  public void testFormatWarningWithSourceExcerpt() {
    JSError warning = new JSError("test-source", 10, CheckLevel.WARNING, "Test warning message");
    // Additional test cases can be added to test specific conditions
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, 0, CheckLevel.ERROR, "Test error message");
    String formattedError = formatterWithoutSource.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Test error message\n", formattedError);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(null, 0, CheckLevel.WARNING, "Test warning message");
    String formattedWarning = formatterWithoutSource.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - Test warning message\n", formattedWarning);
  }

  @Test
  public void testFormatErrorWithInvalidCharNo() {
    JSError error = new JSError("test-source", 5, CheckLevel.ERROR, "Test error message");
    // Additional test cases can be added to test specific conditions
  }

}