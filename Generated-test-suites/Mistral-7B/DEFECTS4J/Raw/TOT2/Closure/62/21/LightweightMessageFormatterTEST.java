import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.JSErrorProvider;
import com.google.javascript.jscomp.testing.SourceExcerptProviderTest;
import com.google.javascript.jscomp.testing.region.Region;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Given
    SourceExcerptProvider source = SourceExcerptProviderTest.newSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TestError", null, 1, 1, "Test message");

    // When
    String message = formatter.formatError(error);

    // Then
    String expected = "1: TestError: ERROR - Test message\n";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatErrorWithSource() {
    // Given
    SourceExcerptProvider source = SourceExcerptProviderTest.newSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TestError", "TestSource.java", 1, 1, "Test message");
    String source = "int a = 1;";

    // When
    String message = formatter.formatError(error);

    // Then
    String expected = "TestSource.java:1: TestError: ERROR - Test message\nint a = 1;\n";
    assertEquals(expected, message);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullSource() {
    // Given
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("TestError", null, 1, 1, "Test message");

    // When
    formatter.formatError(error);

    // Then (exception)
  }

  @Test
  public void testFormatWarning() {
    // Given
    SourceExcerptProvider source = SourceExcerptProviderTest.newSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("TestWarning", null, 1, 1, "Test message");

    // When
    String message = formatter.formatWarning(warning);

    // Then
    String expected = "1: TestWarning: WARNING - Test message\n";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatWarningWithSource() {
    // Given
    SourceExcerptProvider source = SourceExcerptProviderTest.newSourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("TestWarning", "TestSource.java", 1, 1, "Test message");
    String source = "int a = 1;";

    // When
    String message = formatter.formatWarning(warning);

    // Then
    String expected = "TestSource.java:1: TestWarning: WARNING - Test message\nint a = 1;\n";
    assertEquals(expected, message);
  }
}