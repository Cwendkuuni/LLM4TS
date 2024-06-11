import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestingUtils;
import com.google.javascript.jscomp.testing.types.Region;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LightweightMessageFormatterTest {

  @InjectMocks
  private LightweightMessageFormatter formatter;

  @Test
  public void testEmptyConstructor() {
    assertEquals("", formatter.formatError(TestingUtils.createError()));
  }

  @Test
  public void testSourceConstructor() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);

    String expected = "test.js:1: ERROR - errorMessage\ntest code\n^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithNullSource() {
    String expected = "ERROR - errorMessage\n";
    assertEquals(expected, formatter.formatError(TestingUtils.createError()));
  }

  @Test
  public void testFormatWarningWithNullSource() {
    String expected = "WARNING - warningMessage\n";
    assertEquals(expected, formatter.formatWarning(TestingUtils.createWarning()));
  }

  @Test
  public void testFormatErrorWithValidSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);

    String expected = "test.js:1: ERROR - errorMessage\ntest code\n^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithValidSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(1);

    String expected = "test.js:1: WARNING - warningMessage\ntest code\n^\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullError() {
    formatter.formatError(null);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatWarningWithNullWarning() {
    formatter.formatWarning(null);
  }

  @Test
  public void testFormatErrorWithLongLine() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code".repeat(100));

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);

    String expected = "test.js:1: ERROR - errorMessage\ntest code\ntest code\n..."
        + "test code\n^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithLongLine() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code".repeat(100));

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(1);

    String expected = "test.js:1: WARNING - warningMessage\ntest code\ntest code\n..."
        + "test code\n^\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);

    String expected = "test.js:1: ERROR - errorMessage\n\n^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithEmptySource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(1);

    String expected = "test.js:1: WARNING - warningMessage\n\n^\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithRegion() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);
    error.setCharno(10);
    Region region = TestingUtils.createRegion(1, 1, 1, 1);

    String expected = "test.js:1: ERROR - errorMessage\ntest code\n  ^^\n";
    assertEquals(expected, formatter.formatError(error, region));
  }

  @Test
  public void testFormatWarningWithRegion() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("test code");

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(1);
    warning.setCharno(10);
    Region region = TestingUtils.createRegion(1, 1, 1, 1);

    String expected = "test.js:1: WARNING - warningMessage\ntest code\n  ^^\n";
    assertEquals(expected, formatter.formatWarning(warning, region));
  }

  @Test
  public void testFormatErrorWithNoSourceName() {
    formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName(null);
    error.setLineNumber(1);

    String expected = "1: ERROR - errorMessage\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithNoSourceName() {
    formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName(null);
    warning.setLineNumber(1);

    String expected = "1: WARNING - warningMessage\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithNoLineNumber() {
    formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(0);

    String expected = "test.js: ERROR - errorMessage\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithNoLineNumber() {
    formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(0);

    String expected = "test.js: WARNING - warningMessage\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithNegativeLineNumber() {
    formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(-1);

    String expected = "test.js: ERROR - errorMessage\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithNegativeLineNumber() {
    formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(-1);

    String expected = "test.js: WARNING - warningMessage\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithNullExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn(null);

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);

    String expected = "test.js:1: ERROR - errorMessage\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithNullExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn(null);

    formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(1);

    String expected = "test.js:1: WARNING - warningMessage\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithEmptyExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("");

    formatter = new LightlightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = TestingUtils.createError();
    error.setSourceName("test.js");
    error.setLineNumber(1);

    String expected = "test.js:1: ERROR - errorMessage\n\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithEmptyExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(eq("test.js"), any(int.class), any(int.class),
        eq(LightweightMessageFormatter.LINE)))
        .thenReturn("");

    formatter = new LightlightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = TestingUtils.createWarning();
    warning.setSourceName("test.js");
    warning.setLineNumber(1);

    String expected = "test.js:1: WARNING - warningMessage\n\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }
}