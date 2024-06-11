package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.Strings;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Region;
import com.google.javascript.jscomp.util.SourceExcerptProvider.SourceCodeLocation;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Error Message", 1, 1, 1, 1, null);
    assertEquals("ERROR - Error Message\n", messageFormatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithSource() {
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(SourceCodeLocation location) {
        return new SourceExcerpt("sourceCode") {
          @Override
          public String getSource() {
            return "sourceCode";
          }
        };
      }
    }, LightweightMessageFormatter.LINE);

    JSError error = new JSError("Error Message", new SourceCodeLocation("test.js", 1, 1), null);
    assertEquals("test.js:1: ERROR - Error Message\nsourceCode\n", messageFormatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Warning Message", 1, 1, 1, 1, null);
    assertEquals("WARNING - Warning Message\n", messageFormatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(SourceCodeLocation location) {
        return new SourceExcerpt("sourceCode") {
          @Override
          public String getSource() {
            return "sourceCode";
          }
        };
      }
    }, LightweightMessageFormatter.LINE);

    JSError warning = new JSError("Warning Message", new SourceCodeLocation("test.js", 1, 1), null);
    assertEquals("test.js:1: WARNING - Warning Message\nsourceCode\n", messageFormatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithRegion() {
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(SourceCodeLocation location) {
        return new SourceExcerpt("sourceCode") {
          @Override
          public String getSource() {
            return "sourceCode";
          }
        };
      }
    }, LightweightMessageFormatter.LINE);

    JSError error = new JSError("Error Message", new SourceCodeLocation("test.js", 1, 1), null);
    Region region = new Region(new SourceCodeLocation("test.js", 1, 1), new SourceCodeLocation("test.js", 2, 2));

    assertEquals("test.js:1: ERROR - Error Message\n 1| if (foo) {\n 2|   alert('bar');\n", messageFormatter.formatError(error, region));
  }

  @Test
  public void testFormatWarningWithRegion() {
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(SourceCodeLocation location) {
        return new SourceExcerpt("sourceCode") {
          @Override
          public String getSource() {
            return "sourceCode";
          }
        };
      }
    }, LightweightMessageFormatter.LINE);

    JSError warning = new JSError("Warning Message", new SourceCodeLocation("test.js", 1, 1), null);
    Region region = new Region(new SourceCodeLocation("test.js", 1, 1), new SourceCodeLocation("test.js", 2, 2));

    assertEquals("test.js:1: WARNING - Warning Message\n 1| if (foo) {\n 2|   alert('bar');\n", messageFormatter.formatWarning(warning, region));
  }

  static class LineNumberingFormatter implements ExcerptFormatter {
    static final String Strings = new String("") {
      @Override
      public String repeat(int count) {
        return new String(new char[count]).replace("\0", " ");
      }
    };

    @Override
    public String formatLine(String line, int lineNumber) {
      return line;
    }

    @Override
    public String formatRegion(Region region) {
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
}