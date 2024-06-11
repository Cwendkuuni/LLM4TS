import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.JSError;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    JSError error = new JSError("error", "test.js", 10, 5, "Error message");

    String formattedError = formatter.formatError(error);

    assertNotNull(formattedError);
    assertTrue(formattedError.contains("ERROR - Error message"));
    assertTrue(formattedError.contains("test.js:10"));
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    JSError warning = new JSError("warning", "test.js", 5, 3, "Warning message");

    String formattedWarning = formatter.formatWarning(warning);

    assertNotNull(formattedWarning);
    assertTrue(formattedWarning.contains("WARNING - Warning message"));
    assertTrue(formattedWarning.contains("test.js:5"));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("error", "test.js", 15, 8, "Error message without source");

    String formattedError = formatter.formatError(error);

    assertNotNull(formattedError);
    assertTrue(formattedError.contains("ERROR - Error message without source"));
    assertFalse(formattedError.contains("test.js:15"));
  }

  // Additional test cases for edge cases and exception handling can be added here

}