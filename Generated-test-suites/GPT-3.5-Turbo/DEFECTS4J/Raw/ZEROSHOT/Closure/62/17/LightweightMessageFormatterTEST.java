import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerpt;
import java.util.ArrayList;
import java.util.List;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    formatter = new LightweightMessageFormatter(source);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("Error", "test", 1, "Description");
    String expected = "test:1: ERROR - Description\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Warning", "test", 1, "Description");
    String expected = "test:1: WARNING - Description\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Error", null, 0, "Description");
    String expected = "ERROR - Description\n";
    assertEquals(expected, LightweightMessageFormatter.withoutSource().formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Warning", null, 0, "Description");
    String expected = "WARNING - Description\n";
    assertEquals(expected, LightweightMessageFormatter.withoutSource().formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithSourceExcerpt() {
    JSError error = new JSError("Error", "test", 1, "Description");
    String sourceExcerpt = "this is a test\n";
    SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
    formatter = new LightweightMessageFormatter(source);
    String expected = "test:1: ERROR - Description\n" + sourceExcerpt;
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSourceExcerpt() {
    JSError warning = new JSError("Warning", "test", 1, "Description");
    String sourceExcerpt = "this is a test\n";
    SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
    formatter = new LightweightMessageFormatter(source);
    String expected = "test:1: WARNING - Description\n" + sourceExcerpt;
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithEmptySourceExcerpt() {
    JSError error = new JSError("Error", "test", 1, "Description");
    SourceExcerptProvider source = new MockSourceExcerptProvider("");
    formatter = new LightweightMessageFormatter(source);
    String expected = "test:1: ERROR - Description\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithEmptySourceExcerpt() {
    JSError warning = new JSError("Warning", "test", 1, "Description");
    SourceExcerptProvider source = new MockSourceExcerptProvider("");
    formatter = new LightweightMessageFormatter(source);
    String expected = "test:1: WARNING - Description\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithCharno() {
    JSError error = new JSError("Error", "test", 1, "Description");
    error.setCharno(5);
    String sourceExcerpt = "this is a test\n";
    SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
    formatter = new LightweightMessageFormatter(source);
    String expected = "test:1: ERROR - Description\n" + sourceExcerpt + "     ^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithCharno() {
    JSError warning = new JSError("Warning", "test", 1, "Description");
    warning.setCharno(5);
    String sourceExcerpt = "this is a test\n";
    SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
    formatter = new LightweightMessageFormatter(source);
    String expected = "test:1: WARNING - Description\n" + sourceExcerpt + "     ^\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  private class MockSourceExcerptProvider implements SourceExcerptProvider {

    private String sourceExcerpt;

    public MockSourceExcerptProvider(String sourceExcerpt) {
      this.sourceExcerpt = sourceExcerpt;
    }

    public String getSourceExcerpt(SourceExcerpt sourceExcerpt) {
      return this.sourceExcerpt;
    }
  }
}