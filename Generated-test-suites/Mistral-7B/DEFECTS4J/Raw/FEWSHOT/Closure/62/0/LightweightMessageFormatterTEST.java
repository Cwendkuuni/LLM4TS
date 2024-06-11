package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.Strings;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Region;
import com.google.javascript.jscomp.util.SourceFile;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(JSError.Type.ERROR, "Invalid Syntax", "test.js", 1, 1);
    String expected = "ERROR - Invalid Syntax\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String filePath, int lineNumber, int startOffset, int endOffset) {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function foo() {\n  // error here\n}";
        return new SourceExcerpt(sourceFile, lineNumber, code);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(JSError.Type.ERROR, "Invalid Syntax", "test.js", 1, 1);
    String expected = "test.js:1: ERROR - Invalid Syntax\n// function foo() {\n//   // error here\n//}\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(JSError.Type.WARNING, "Deprecated Feature", "test.js", 1, 1);
    String expected = "WARNING - Deprecated Feature\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String filePath, int lineNumber, int startOffset, int endOffset) {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function foo() {\n  // warning here\n}";
        return new SourceExcerpt(sourceFile, lineNumber, code);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(JSError.Type.WARNING, "Deprecated Feature", "test.js", 1, 1);
    String expected = "test.js:1: WARNING - Deprecated Feature\n// function foo() {\n//   // warning here\n//}\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorMultipleLines() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String filePath, int lineNumber, int startOffset, int endOffset) {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function foo() {\n  var a = 1;\n  if (a > 0) {\n    // error here\n  }\n}";
        return new SourceExcerpt(sourceFile, lineNumber, code);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(JSError.Type.ERROR, "Invalid Syntax", "test.js", 2, 1);
    String expected = "test.js:2: ERROR - Invalid Syntax\n// function foo() {\n//   var a = 1;\n//   if (a > 0) {\n//     // error here\n//   }\n//}\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningMultipleLines() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String filePath, int lineNumber, int startOffset, int endOffset) {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function foo() {\n  var a = 1;\n  if (a > 0) {\n    // warning here\n  }\n}";
        return new SourceExcerpt(sourceFile, lineNumber, code);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(JSError.Type.WARNING, "Deprecated Feature", "test.js", 2, 1);
    String expected = "test.js:2: WARNING - Deprecated Feature\n// function foo() {\n//   var a = 1;\n//   if (a > 0) {\n//     // warning here\n//   }\n//}\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithRegion() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String filePath, int lineNumber, int startOffset, int endOffset) {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function foo() {\n  var a = 1;\n  if (a > 0) {\n    // error here\n    bar();\n  }\n}";
        Region region = new Region("test.js", 2, 3, 2, 25);
        return new SourceExcerpt(sourceFile, region);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(JSError.Type.ERROR, "Invalid Syntax", "test.js", 2, 8);
    String expected = "test.js:2:5: ERROR - Invalid Syntax\n// function foo() {\n//   var a = 1;\n//   if (a > 0) {\n//     // error here\n//     bar();\n//   }\n//}\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithRegion() {
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String filePath, int lineNumber, int startOffset, int endOffset) {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function foo() {\n  var a = 1;\n  if (a > 0) {\n    // warning here\n    bar();\n  }\n}";
        Region region = new Region("test.js", 2, 3, 2, 25);
        return new SourceExcerpt(sourceFile, region);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(JSError.Type.WARNING, "Deprecated Feature", "test.js", 2, 8);
    String expected = "test.js:2:5: WARNING - Deprecated Feature\n// function foo() {\n//   var a = 1;\n//   if (a > 0) {\n//     // warning here\n//     bar();\n//   }\n//}\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }
}

class LineNumberingFormatter implements ExcerptFormatter {
  static final String Strings = "com.google.common.base.Strings";

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
      builder.append(lineNumber);
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