import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestRunner;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.List;

public class LightweightMessageFormatterTest {

  @org.junit.Test
  public void testLightweightMessageFormatter_withoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(CheckLevel.ERROR, "TestError", 1, 1, null, 1, 1, null);
    String message = formatter.formatError(error);
    String expected = "ERROR - TestError\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testLightweightMessageFormatter_withSource() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt(Node.SOURCE_FILES[0], lineNumber, "TestSource") {
          @Override
          public String getSourceCode() {
            return "function testFunction() { throw new Error(\"TestError\"); }";
          }
        };
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    JSError error = new JSError(ERROR, "TestError", 1, 1, "TestSource", 1, 1, null);
    String message = formatter.formatError(error);
    String expected = "TestSource:1: TestError\nfunction testFunction() { throw new Error(\"TestError\"); }^\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = new JSError(WARNING, "TestWarning", 1, 1, null, 1, 1, null);
    String message = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarning\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testLineNumberingFormatter_formatRegion_nullRegion() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String message = formatter.formatRegion(null);
    assertNull(message);
  }

  @org.junit.Test
  public void testLineNumberingFormatter_formatRegion_emptySource() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(Node.SOURCE_FILES[0], 0, 0, 1, 1, 1, 1, null);
    String message = formatter.formatRegion(region);
    assertNull(message);
  }

  @org.junit.Test
  public void testLineNumberingFormatter_formatRegion_singleLineSource() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(Node.SOURCE_FILES[0], 1, 1, 1, 2, 1, 1, null);
    String message = formatter.formatRegion(region);
    String expected = " 1| function testFunction() { throw new Error(\"TestError\"); }\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testLineNumberingFormatter_formatRegion_multipleLinesSource() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(Node.SOURCE_FILES[0], 1, 1, 3, 3, 1, 1, null);
    String message = formatter.formatRegion(region);
    String expected = " 1| function testFunction() {\n" +
                      " 2|   var a = 1;\n" +
                      " 3|   var b = 2;\n" +
                      "  ";
    assertEquals(expected, message.substring(0, expected.length() - 1));
  }

  @org.junit.Test
  public void testLineNumberingFormatter_formatRegion_outOfRangeEnd() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(Node.SOURCE_FILES[0], 1, 1, 3, 2, 1, 1, null);
    String message = formatter.formatRegion(region);
    String expected = " 1| function testFunction() {\n" +
                      " 2|   var a = 1;\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testLineNumberingFormatter_formatRegion_sourceExcerptLongerThanCode() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(Node.SOURCE_FILES[0], 1, 1, 3, 3, 1, 1, null);
    String message = formatter.formatRegion(region);
    String expected = " 1| function testFunction() {\n" +
                      " 2|   var a = 1;\n" +
                      " 3|   var b = 2;\n" +
                      "  ";
    assertEquals(expected, message.substring(0, expected.length()));
  }

}