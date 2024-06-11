package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    JSError error = new JSError("TestError", "TestSource.js", 1, 1,
        "Test Message", 5);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(error);
    assertEquals("Test Source.js:1: Test Message\n", result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError("TestError", "TestSource.js", 1, 1,
        "Test Message", 5, "testCode");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource(String name) {
        if ("TestSource.js".equals(name)) {
          return new SourceExcerpt() {
            @Override
            public String getCode() {
              return "function testCode() {\n  // test code\n}";
            }

            @Override
            public ExcerptFormatter getFormatter() {
              return new LineNumberingFormatter();
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatError(error);
    assertEquals("Test Source.js:1: ERROR - Test Message\nfunction testCode() {\n  // test code\n}", result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("TestWarning", "TestSource.js", 1, 1,
        "Test Message", 5, null, CheckLevel.WARNING);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatWarning(warning);
    assertEquals("Test Source.js:1: WARNING - Test Message\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError("TestWarning", "TestSource.js", 1, 1,
        "Test Message", 5, "testCode", CheckLevel.WARNING);
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource(String name) {
        if ("TestSource.js".equals(name)) {
          return new SourceExcerpt() {
            @Override
            public String getCode() {
              return "function testCode() {\n  // test code\n}";
            }

            @Override
            public ExcerptFormatter getFormatter() {
              return new LineNumberingFormatter();
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatWarning(warning);
    assertEquals("Test Source.js:1: WARNING - Test Message\nfunction testCode() {\n  // test code\n}", result);
  }

  @Test
  public void testFormatRegion() {
    String code = "function testCode() {\n  // test code\n}";
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    Region region = new Region(1, 1, code.length(), code);
    String result = formatter.formatRegion(region);
    assertEquals("  TestSource.js:1| function testCode() {\n    // test code\n}", result);
  }

  @Test
  public void testFormatRegionWithSource() {
    String code = "function testCode() {\n  // test code\n}";
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSource(String name) {
        if ("TestSource.js".equals(name)) {
          return new SourceExcerpt() {
            @Override
            public String getCode() {
              return code;
            }

            @Override
            public ExcerptFormatter getFormatter() {
              return new LineNumberingFormatter();
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    Region region = new Region(1, 1, code.length(), code);
    String result = formatter.formatRegion(region);
    assertEquals("  TestSource.js:1| function testCode() {\n    // test code\n}", result);
  }

  static class LineNumberingFormatter implements ExcerptFormatter {
    @Override
    public String formatLine(String line, int lineNumber) {
      return line;
    }

    @Override
    public String formatRegion(Region region) {
      String code = region.getSourceExcerpt();
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