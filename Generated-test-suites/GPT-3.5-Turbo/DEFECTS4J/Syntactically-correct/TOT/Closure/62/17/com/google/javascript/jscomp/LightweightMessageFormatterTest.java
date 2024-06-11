import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  private LightweightMessageFormatter formatter;

  @Before
  public void setup() {
    formatter = new LightweightMessageFormatter(null);
  }

  @Test
  public void formatError_shouldFormatErrorCorrectly() {
    JSError error = new JSError("Test Error");
    error.setSourceName("test.js");
    error.setLineNumber(10);
    error.setDescription("This is a test error");

    String formattedError = formatter.formatError(error);

    // Assert that the formatted error contains the correct information
    assertTrue(formattedError.contains("test.js"));
    assertTrue(formattedError.contains("10"));
    assertTrue(formattedError.contains("ERROR"));
    assertTrue(formattedError.contains("This is a test error"));
  }

  @Test
  public void formatWarning_shouldFormatWarningCorrectly() {
    JSError warning = new JSError("Test Warning");
    warning.setSourceName("test.js");
    warning.setLineNumber(5);
    warning.setDescription("This is a test warning");

    String formattedWarning = formatter.formatWarning(warning);

    // Assert that the formatted warning contains the correct information
    assertTrue(formattedWarning.contains("test.js"));
    assertTrue(formattedWarning.contains("5"));
    assertTrue(formattedWarning.contains("WARNING"));
    assertTrue(formattedWarning.contains("This is a test warning"));
  }

  @Test
  public void format_shouldHandleNullSourceExcerpt() {
    JSError error = new JSError("Test Error");
    error.setSourceName("test.js");
    error.setLineNumber(3);
    error.setDescription("This is a test error");

    String formattedError = formatter.formatError(error);

    // Assert that the formatted error does not contain source excerpt
    assertFalse(formattedError.contains("source excerpt"));
  }

  @Test
  public void format_shouldHandleNonNullSourceExcerpt() {
    JSError error = new JSError("Test Error");
    error.setSourceName("test.js");
    error.setLineNumber(3);
    error.setDescription("This is a test error");

    String formattedError = formatter.formatError(error);

    // Assert that the formatted error contains source excerpt
    assertTrue(formattedError.contains("source excerpt"));
  }

  @Test
  public void format_shouldHandleCharnoInRange() {
    JSError error = new JSError("Test Error");
    error.setSourceName("test.js");
    error.setLineNumber(5);
    error.setCharno(10);
    error.setDescription("This is a test error");

    String formattedError = formatter.formatError(error);

    // Assert that the formatted error contains arrow marker
    assertTrue(formattedError.contains("^"));
  }

  @Test
  public void format_shouldHandleCharnoOutOfRange() {
    JSError error = new JSError("Test Error");
    error.setSourceName("test.js");
    error.setLineNumber(5);
    error.setCharno(20);
    error.setDescription("This is a test error");

    String formattedError = formatter.formatError(error);

    // Assert that the formatted error does not contain arrow marker
    assertFalse(formattedError.contains("^"));
  }

  @Test
  public void format_shouldHandleEmptySourceExcerpt() {
    JSError error = new JSError("Test Error");
    error.setSourceName("test.js");
    error.setLineNumber(5);
    error.setCharno(10);
    error.setDescription("This is a test error");

    String formattedError = formatter.formatError(error);

    // Assert that the formatted error does not contain source excerpt
    assertFalse(formattedError.contains("source excerpt"));
  }
}