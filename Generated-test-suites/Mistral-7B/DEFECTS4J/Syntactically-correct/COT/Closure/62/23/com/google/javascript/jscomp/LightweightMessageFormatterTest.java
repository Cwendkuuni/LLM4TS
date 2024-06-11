import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.CodeSnippetTestHelper;
import com.google.javascript.jscomp.testing.TypeCheckerTestHelper;

public class LightweightMessageFormatterTest {

 private static final ExcerptFormatter LINE_FORMATTER = new LineNumberingFormatter();

 @Test
 public void testConstructorWithoutSource() {
   LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
   JSError error = new JSError("test", 1, "Error message", null, null);
   String result = formatter.formatError(error);
   String expected = "Error - Error message\n";
   assertEquals(expected, result);
 }

 @Test
 public void testConstructorWithSource() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError error = new JSError("test", 1, "Error message", "source.js", 10);
   String result = formatter.formatError(error);
   String expected = "test:10: Error - Error message\nsource\n ^";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatError() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError error = new JSError("test", 1, "Error message", "source.js", 10);
   String result = formatter.formatError(error);
   String expected = "test:10: Error - Error message\nsource\n ^\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatWarning() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError warning = new JSError("test", 1, "Warning message", "source.js", 10);
   String result = formatter.formatWarning(warning);
   String expected = "warning:10: - Warning message\nsource\n ^\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatErrorWithNullSourceName() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return null;
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError error = new JSError(null, 1, "Error message", null, null);
   String result = formatter.formatError(error);
   String expected = "Error - Error message\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatErrorWithNullSource() {
   LightweightMessageFormatter formatter = new LightweightMessageFormatter();
   JSError error = new JSError("test", 1, "Error message", null, null);
   String result = formatter.formatError(error);
   String expected = "Error - Error message\n";
   assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testFormatErrorWithNullError() {
   LightweightMessageFormatter formatter = new LightweightMessageFormatter();
   formatter.formatError(null);
 }

 @Test
 public void testFormatErrorWithLongDescription() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.";
   JSError error = new JSError("test", 1, description, "source.js", 10);
   String result = formatter.formatError(error);
   String expected = "test:10: Error - Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.\nsource\n ^\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatWarningWithLongDescription() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.";
   JSError warning = new JSError("test", 1, description, "source.js", 10);
   String result = formatter.formatWarning(warning);
   String expected = "warning:10: - Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.\nsource\n ^\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatErrorWithEmptyDescription() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError error = new JSError("test", 1, "", "source.js", 10);
   String result = formatter.formatError(error);
   String expected = "test:10: Error - \nsource\n ^\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatWarningWithEmptyDescription() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError warning = new JSError("test", 1, "", "source.js", 10);
   String result = formatter.formatWarning(warning);
   String expected = "warning:10: - \nsource\n ^\n";
   assertEquals(expected, result);
 }

 @Test
 public void testFormatErrorWithNegativeLineNumber() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError error = new JSError("test", -1, "Error message", "source.js", 10);
   try {
     formatter.formatError(error);
     fail("Expected NullPointerException");
   } catch (NullPointerException e) {
     // Expected
   }
 }

 @Test
 public void testFormatWarningWithNegativeLineNumber() {
   SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
     @Override
     public SourceExcerpt getExcerpt(String source, int lineNumber, int offset) {
       return new SourceExcerpt("source", source, lineNumber, offset, LINE_FORMATTER);
     }
   };
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
   JSError warning = new JSError("test", -1, "Warning message", "source.js", 10);
   try {
     formatter.formatWarning(warning);
     fail("Expected NullPointerException");
   } catch (NullPointerException e) {
     // Expected
   }
 }
}

class LineNumberingFormatter implements ExcerptFormatter {
  @Override
  public String formatLine(String line, int lineNumber) {
    return line;
  }

  @Override
  public String formatRegion(Region region) {
    if (region == null) {
      return null;
    }
    String code = region.getSourceExcerpt();
    if (code.length() == 0) {
      return null;
    }

    int numberLength = Integer.toString(region.getEndingLineNumber()).length();

    StringBuilder builder = new StringBuilder(code.length() * 2);
    int start = 0;
    int end = code.indexOf('\n', start);
    int lineNumber = region.getBeginningLineNumber();
    while (start >= 0) {
      String line;
      if (end < 0) {
        line = code.substring(start);
        if (line.length() == 0) {
          return builder.substring(0, builder.length() - 1);
        }
      } else {
        line = code.substring(start, end);
      }
      builder.append("  ");

      int spaces = numberLength - Integer.toString(lineNumber).length();
      builder.append(Strings.repeat(" ", spaces));
      builder.append(lineNumber);
      builder.append("| ");

      if (end < 0) {
        builder.append(line);
        start = -1;
      } else {
        builder.append(line);
        builder.append('\n');
        start = end + 1;
        end = code.indexOf('\n', start);
        lineNumber++;
      }
    }
    return builder.toString();
  }
}