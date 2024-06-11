import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        try {
          return Files.readString(Path.of("src/test/resources/" + sourceName));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TypeError", "foo is not a function", "spec.js", 5);
    String expected = "5: TypeError - foo is not a function\n"
        + "spec.js:5 foo is not a function\n";
    String result = formatter.formatError(error);
    assertEquals(expected, result);

    error = new JSError("TypeError", "foo is not a function", "spec.js", 5, 10);
    expected = "5: TypeError - foo is not a function\n"
        + "spec.js:5         ^--------^---------\n"
        + "spec.js:5 foo is not a function\n";
    result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError("DeprecationWarning", "foo is deprecated",
        "spec.js", 5, -1);
    String expected = "5: DEPRECATIONWARNING - foo is deprecated\n";
    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);

    warning = new JSError("DeprecationWarning", "foo is deprecated",
        "spec.js", 5, 10);
    expected = "5: DEPRECATIONWARNING - foo is deprecated\n"
        + "spec.js:5         ^--------^---------\n";
    result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSourceInfoWhenExcerptIsLine() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n"
            + "  bar();\n"
            + "}";
      }
    };

    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, FORMATTER);
    JSError error = new JSError("TypeError", "foo is not a function", "spec.js", 1);
    String expected = "1: TypeError - foo is not a function\n"
        + "spec.js:1 function foo() {\n"
        + "  bar();\n"
        + "}\n";
    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSourceInfoWhenExcerptIsRegion() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n"
            + "  bar();\n"
            + "}";
      }
    };

    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, REGION);
    JSError error = new JSError("TypeError", "foo is not a function", "spec.js", 1);
    String expected = "1: TypeError - foo is not a function\n"
        + "function foo() {\n"
        + "  bar();\n"
        + "}\n";
    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSourceInfoWhenExcerptIsNull() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("TypeError", "foo is not a function", "spec.js", 1);
    String expected = "1: TypeError - foo is not a function\n";
    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSourceInfoWhenExcerptIsLine() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n"
            + "  bar();\n"
            + "}";
      }
    };

    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, FORMATTER);
    JSError warning = new JSError("DeprecationWarning", "foo is deprecated",
        "spec.js", 1, -1);
    String expected = "1: DEPRECATIONWARNING - foo is deprecated\n"
        + "function foo() {\n"
        + "  bar();\n"
        + "}\n";
    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSourceInfoWhenExcerptIsRegion() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n"
            + "  bar();\n"
            + "}";
      }
    };

    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, REGION);
    JSError warning = new JSError("DeprecationWarning", "foo is deprecated",
        "spec.js", 1);
    String expected = "1: DEPRECATIONWARNING - foo is deprecated\n"
        + "function foo() {\n"
        + "  bar();\n"
        + "}\n";
    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }
}