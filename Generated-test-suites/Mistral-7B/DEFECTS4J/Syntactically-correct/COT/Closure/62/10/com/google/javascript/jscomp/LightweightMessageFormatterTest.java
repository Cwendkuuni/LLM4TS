import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestRunner;
import com.google.javascript.jscomp.testing.TestSourceFile;

public class LightweightMessageFormatterTest {

 private static final SourceExcerptProvider SOURCE_PROVIDER = new SourceExcerptProvider() {
   @Override
   public SourceExcerpt getSourceExcerpt(String source, int lineNumber) {
      return LINE;
   }
 };

 private static final LightweightMessageFormatter FORMATTER_WITHOUT_SOURCE = new LightweightMessageFormatter();

 private static final LightweightMessageFormatter FORMATTER_WITH_SOURCE = new LightweightMessageFormatter(SOURCE_PROVIDER, LINE);

 @Test
 public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = FORMATTER_WITHOUT_SOURCE;
    JSError error = new JSError("Test error", "TestFile.js", 1, 1, 1, "testMessage");
    assertEquals("ERROR - testMessage\n", formatter.formatError(error));
  }

 @Test
 public void testConstructorWithSource() {
    LightweightMessageFormatter formatter = FORMATTER_WITH_SOURCE;
    JSError error = new JSError("Test error", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "function testFunction() {\n" +
                   "  testMessage;\n" +
                   "}";
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return SourceExcerptProvider.SourceExcerpt.ofCode(source);
      }
    };
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
        return sourceProvider.getSourceExcerpt(sourceName, lineNumber);
      }
    };
    Preconditions.checkNotNull(sourceProvider);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    assertEquals("TestFile.js:1: ERROR - testMessage\nfunction testFunction() {\n  testMessage;\n}", formatter.formatError(error));
  }

 @Test
 public void testFormatError() {
    LightweightMessageFormatter formatter = FORMATTER_WITH_SOURCE;
    JSError error = new JSError("Test error", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "function testFunction() {\n" +
                   "  testMessage;\n" +
                   "}";
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return SourceExcerptProvider.SourceExcerpt.ofCode(source);
      }
    };
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
        return sourceProvider.getSourceExcerpt(sourceName, lineNumber);
      }
    };
    Preconditions.checkNotNull(sourceProvider);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String expected = "TestFile.js:1: ERROR - testMessage\nfunction testFunction() {\n  testMessage;\n}";
    assertEquals(expected, formatter.formatError(error));
  }

 @Test
 public void testFormatWarning() {
    LightweightMessageFormatter formatter = FORMATTER_WITH_SOURCE;
    JSError warning = new JSError("Test warning", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "function testFunction() {\n" +
                   "  testMessage;\n" +
                   "}";
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return SourceExcerptProvider.SourceExcerpt.ofCode(source);
      }
    };
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
        return sourceProvider.getSourceExcerpt(sourceName, lineNumber);
      }
    };
    Preconditions.checkNotNull(sourceProvider);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String expected = "TestFile.js:1: WARNING - testMessage\nfunction testFunction() {\n  testMessage;\n}";
    assertEquals(expected, formatter.formatWarning(warning));
  }

 @Test
 public void testFormatErrorWithSourceExcerpt() {
    LightweightMessageFormatter formatter = FORMATTER_WITH_SOURCE;
    JSError error = new JSError("Test error", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "function testFunction() {\n" +
                   "  testMessage;\n" +
                   "  var testVariable = 1;\n" +
                   "}";
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return SourceExcerptProvider.SourceExcerpt.ofCode(source);
      }
    };
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
        return sourceProvider.getSourceExcerpt(sourceName, lineNumber);
      }
    };
    Preconditions.checkNotNull(sourceProvider);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String expected = "TestFile.js:1: ERROR - testMessage\nfunction testFunction() {\n  testMessage;\n  var testVariable = 1;\n}";
    assertEquals(expected, formatter.formatError(error));
  }

 @Test
 public void testFormatWarningWithSourceExcerpt() {
    LightweightMessageFormatter formatter = FORMATTER_WITHOUT_SOURCE;
    JSError warning = new JSError("Test warning", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "function testFunction() {\n" +
                   "  testMessage;\n" +
                   "  var testVariable = 1;\n" +
                   "}";
    String excerpt = "function testFunction() {\n  testMessage;\n}";
    Region region = new Region(1, 1, 1, 1, excerpt);
    String expected = "TestFile.js:1: WARNING - testMessage\n";
    assertEquals(expected, formatter.formatWarning(warning, region));
  }

 @Test
 public void testFormatErrorWithNegativeSourceExcerptIndex() {
    LightweightMessageFormatter formatter = FORMATTER_WITH_SOURCE;
    JSError error = new JSError("Test error", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "function testFunction() {\n" +
                   "  testMessage;\n" +
                   "  var testVariable = 1;\n" +
                   "}";
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return SourceExcerptProvider.SourceExcerpt.ofCode(source);
      }
    };
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
        return sourceProvider.getSourceExcerpt(sourceName, lineNumber);
      }
    };
    Preconditions.checkNotNull(sourceProvider);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String expected = "TestFile.js:1: ERROR - testMessage\nfunction testFunction() {\n  testMessage;\n  var testVariable = 1;\n}";
    Region region = new Region(1, 1, 1, -1, null);
    assertEquals(expected, formatter.formatError(error, region));
  }

 @Test
 public void testFormatErrorWithEmptySourceExcerpt() {
    LightweightMessageFormatter formatter = FORMATTER_WITH_SOURCE;
    JSError error = new JSError("Test error", "TestFile.js", 1, 1, 1, "testMessage");
    String source = "";
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return SourceExcerptProvider.SourceExcerpt.ofCode(source);
      }
    };
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
        return sourceProvider.getSourceExcerpt(sourceName, lineNumber);
      }
    };
    Preconditions.checkNotNull(sourceProvider);
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String expected = "TestFile.js:1: ERROR - testMessage\n";
    Region region = new Region(1, 1, 1, 1, null);
    assertEquals(expected, formatter.formatError(error, region));
  }
}