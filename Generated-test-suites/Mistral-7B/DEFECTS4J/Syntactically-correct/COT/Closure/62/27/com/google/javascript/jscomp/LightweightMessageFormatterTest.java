import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import com.google.common.base.Strings;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.TestingUtils;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private static final String TEST_SOURCE = "test.js";
  private static final int TEST_LINE_NUMBER = 10;

  private SourceExcerptProvider source;
  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    source = TestingUtils.createSourceExcerptProvider(TEST_SOURCE);
    formatter = new LightweightMessageFormatter(source, LINE);
  }

  @Test
  public void testWithoutSource() {
    LightweightMessageFormatter withoutSourceFormatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, "Test Error", 1, 1, 1);
    assertEquals("ERROR - Test Error\n", withoutSourceFormatter.formatError(error));
  }

  @Test
  public void testSimpleErrorFormat() {
    JSError error = new JSError(source, "TestErrorSource.js", TEST_LINE_NUMBER, "Test Error");
    assertEquals("TESTERRORSOURCE.js:10: ERROR - ERROR\n", formatter.formatError(error));
  }

  @Test
  public void testSourceExcerptFormat() {
    Region region = new Region("test.js", 1, 1, 15, 25);
    ExcerptFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String sourceExcerpt = formatter.formatRegion(region);
    String expected = " 1| int a = 1;\n 2| int b = 2;\n 3| int c = 3;\n 4| int d = 4;\n 5| int e = 5;\n 6| int f = 6;\n 7| int g = 7;\n 8| int h = 8;\n 9| int i = 9;\n 10| int j = 10;\n";
    assertEquals(expected, sourceExcerpt);
  }

  @Test
  public void testWarningFormat() {
    JSError warning = new JSError(source, "TestErrorSource.js", TEST_LINE_NUMBER, "Test Warning", null);
    assertEquals("WARNING - WARNING\n", formatter.formatWarning(warning));
  }

  @Test(expected = NullPointerException.class)
  public void testNullSourceExcerpt() {
    JSError error = new JSError(source, null, 1, 1, 1, "Test Error");
    formatter.formatError(error);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLineNumber() {
    JSError error = new JSError(source, "TestErrorSource.js", -1, "Test Error");
    formatter.formatError(error);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testExcerptBeyondSourceEnd() {
    JSError error = new JSError(source, "TestErrorSource.js", 1, "Test Error");
    source.setSource(TEST_SOURCE, null);
    formatter.formatError(error);
  }
}