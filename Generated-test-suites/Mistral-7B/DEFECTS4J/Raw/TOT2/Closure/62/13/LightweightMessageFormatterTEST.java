import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testDefaultConstructor() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    // add assertions here
  }

  @Test
  public void testConstructorWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    // add assertions here
  }

  @Test
  public void testConstructorWithSourceAndExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
    // add assertions here
  }

  @Test
  public void testWithoutSourceConstructor() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    // add assertions here
  }

  @Test
  public void testFormatError() {
    JSError error = createError("TestError", 1, 1, 1, 1, "test error message");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    String result = formatter.formatError(error);
    assertEquals("ERROR - test error message\n1:1 ", result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = createError("TestWarning", 1, 1, 1, 1, "test warning message");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    String result = formatter.formatWarning(warning);
    assertEquals("WARNING - test warning message\n1:1 ", result);
  }

  @Test
  public void testFormat() {
    JSError error = createError("TestError", 1, 1, 1, 1, "test error message");
    boolean warning = false;
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LINE);
    String result = formatter.format(error, warning);
    assertEquals("ERROR - test error message\n1:1 ", result);
  }

  private JSError createError(String name, int lineNumber, int charNo, int start,
      int end, String description) {
    JSError error = new JSError();
    error.sourceName = new SourceName(name);
    error.lineNumber = lineNumber;
    error.charno = charNo;
    error.description = description;
    return error;
  }
}