package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.FormatRegion.LINE_NUMBER;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Region;
import com.google.javascript.jscomp.util.Strings;

public class LightweightMessageFormatterTest {
  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError(CheckLevel.ERROR, "Test Error", "test.js", 1, 1, 0);
    assertEquals("test.js:1: ERROR - Test Error\n", formatter.formatError(error));
  }

  @Test
  public void testFormatWarningNoSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = new JSError(CheckLevel.WARNING, "Test Warning", null, -1, -1, 0);
    assertEquals(" Test Warning\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    String testSource = "function test() {\n" +
                        "  var x = 1;\n" +
                        "  var y = 2;\n" +
                        "  return x + y;\n" +
                        "}";

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, SourceExcerpt excerptType) {
            if (sourceName.equals("test.js")) {
              return new SourceExcerpt() {
                @Override
                public String getSourceCode() {
                  return testSource;
                }

                @Override
                public int getLength() {
                  return testSource.length();
                }
              };
            }
            return null;
          }
        });

    JSError warning = new JSError(CheckLevel.WARNING, "Test Warning", "test.js", 2, 3, 0);
    assertEquals("test.js:2: WARNING - Test Warning\ntest.js:\n" +
                 " 2 |   return x + y;\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithSource() {
    String testSource = "function test() {\n" +
                        "  var x = 1;\n" +
                        "  var y = 2;\n" +
                        "  return x + y;\n" +
                        "}";

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, SourceExcerpt excerptType) {
            if (sourceName.equals("test.js")) {
              return new SourceExcerpt() {
                @Override
                public String getSourceCode() {
                  return testSource;
                }

                @Override
                public int getLength() {
                  return testSource.length();
                }
              };
            }
            return null;
          }
        });

    JSError error = new JSError(CheckLevel.ERROR, "Test Error", "test.js", 2, 3, 1);
    assertEquals("test.js:2: ERROR - Test Error\ntest.js:\n" +
                 " 2 |   return x + y;\n" +
                 "^\n", formatter.formatError(error));
  }

  @Test
  public void testFormatRegion() {
    String testSource = "function test() {\n" +
                        "  var x = 1;\n" +
                        "  var y = 2;\n" +
                        "  return x + y;\n" +
                        "}\n" +
                        "function test2() {\n" +
                        "  var z = 3;\n" +
                        "  var w = 4;\n" +
                        "  return z + w;\n" +
                        "}";

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    Region region = new Region("test.js", 2, 1, 2, 6);
    assertEquals(" 2 |   return x + y;\n", formatter.LineNumberingFormatter.formatRegion(region));
  }
}

class LightweightMessageFormatterTest$LineNumberingFormatter {
  static class FormatRegion {
    final int LINE_NUMBER;

    FormatRegion(int lineNumber) {
      this.LINE_NUMBER = lineNumber;
    }
  }

  static class LineNumberingFormatter implements ExcerptFormatter {
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

      // max length of the number display
      int numberLength = Integer.toString(region.getEndingLineNumber()).length();

      // formatting
      StringBuilder builder = new StringBuilder(code.length() * 2);
      int start = 0;
      int end = code.indexOf('\n', start);
      int lineNumber = region.getBeginningLineNumber();
      while (start >= 0) {
        // line extraction
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

        // nice spaces for the line number
        int spaces = numberLength - Integer.toString(lineNumber).length();
        builder.append(Strings.repeat(" ", spaces));
        builder.append(LINE_NUMBER);
        builder.append("| ");

        // end & update
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