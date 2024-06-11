package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.formatRegion;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Error message", "ErrorSource.js", 1, 1);
    assertEquals(
        "ErrorSource.js:1: - ERROR - Error message\n",
        formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithSource() {
    String code = "function foo() {\n  bar();\n}";
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public CharSequence getSourceExcerpt(String name, int line, int offset, int length) {
        return Arrays.asList(name, Integer.toString(line), code.substring(offset, offset + length)).asJava();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "ErrorSource.js", 1, 4);
    assertEquals(
        "ErrorSource.js:1: function foo() {\n  bar();\n}\n^\n",
        formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = new JSError("Warning message", "WarningSource.js", 1, 1);
    assertEquals(
        "WarningSource.js:1: - WARNING - Warning message\n",
        formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    String code = "function foo() {\n  bar();\n}";
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public CharSequence getSourceExcerpt(String name, int line, int offset, int length) {
        return Arrays.asList(name, Integer.toString(line), code.substring(offset, offset + length)).asJava();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError warning = new JSError("Warning message", "WarningSource.js", 1, 4);
    assertEquals(
        "WarningSource.js:1: function foo() {\n  bar();\n}\n  ^^\n",
        formatter.formatWarning(warning));
  }

  @Test
  public void testFormatRegion() {
    String code =
        "function foo() {\n"
            + "  if (condition) {\n"
            + "    alert('message');\n"
            + "  } else {\n"
            + "    alert('otherMessage');\n"
            + "  }\n"
            + "}";
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public CharSequence getSourceExcerpt(String name, int line, int offset, int length) {
        return Arrays.asList(name, Integer.toString(line), code.substring(offset, offset + length)).asJava();
      }
    };
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LineNumberingFormatter();
    Region region = new Region(code.indexOf("message"), code.indexOf("message") + "message".length(), code);
    String result = formatter.formatRegion(region);
    assertEquals(
        "5|   if (condition) {\n"
            + " 6|     alert('message');\n"
            + " 7|   } else {\n"
            + " 8|     alert('otherMessage');\n"
            + " 9|   }\n",
        result);
  }

  @Test
  public void testFormatErrorWithLongSourceString() {
    String code =
        "function foo() {\n"
            + "  if (condition) {\n"
            + "    alert('message');\n"
            + "  } else {\n"
            + "    alert('otherMessage');\n"
            + "  }\n"
            + "  bar();\n"
            + "}";
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public CharSequence getSourceExcerpt(String name, int line, int offset, int length) {
        return Arrays.asList(name, Integer.toString(line), code.substring(offset, offset + length)).asJava();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    JSError error = new JSError("Error message", "ErrorSource.js", 1, code.length() - 1);
    String expected =
        "ErrorSource.js:\n"
            + "1:\n"
            + "function foo() {\n"
            + "  if (condition) {\n"
            + "    alert('message');\n"
            + "  } else {\n"
            + "    alert('otherMessage');\n"
            + "  }\n"
            + "  bar();\n"
            + "}\n"
            + "^\n";
    assertEquals(expected, formatter.formatError(error));
  }
}