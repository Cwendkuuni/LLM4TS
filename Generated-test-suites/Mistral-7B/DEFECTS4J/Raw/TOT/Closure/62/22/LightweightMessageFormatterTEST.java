import static com.google.javascript.jscomp.CheckLevel.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TopLevelTestingCompiler;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testErrorMessageWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {" +
            "  bar(); // line with error" +
            "}";
      }
    };

    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  bar(); // line with error" +
        "}";
    JSError error = compiler.compile(jsCode, null, source, new LightweightMessageFormatter(source));
    String expectedErrorMessage = "LINE 3: ERROR - ERROR: Uncaught exception: TypeError: bar is not a function\n" +
        "function foo() {\n" +
        "  bar(); // line with error\n" +
        "}\n";
    String actualErrorMessage = error.getMessage();
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testErrorMessageWithoutSource() {
    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  bar(); // line with error" +
        "}";
    JSError error = compiler.compile(jsCode, null, null, new LightweightMessageFormatter());
    String expectedErrorMessage = "LINE 3: ERROR - ERROR: Uncaught exception: TypeError: bar is not a function";
    String actualErrorMessage = error.getMessage();
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testWarningMessageWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {" +
            "  bar(); // line with warning" +
            "}";
      }
    };

    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  var x = y; // undefined variable" +
        "  bar(); // line with warning" +
        "}";
    JSError warning = compiler.compile(jsCode, null, source, new LightweightMessageFormatter(source));
    String expectedWarningMessage = "LINE 3: WARNING - WARNING: Undeclared variable: y\n" +
        "function foo() {" +
        "  var x = y; // undefined variable\n" +
        "  bar(); // line with warning\n" +
        "}";
    String actualWarningMessage = warning.getMessage();
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testWarningMessageWithoutSource() {
    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  var x = y; // undefined variable" +
        "  bar(); // line with warning" +
        "}";
    JSError warning = compiler.compile(jsCode, null, null, new LightweightMessageFormatter());
    String expectedWarningMessage = "LINE 3: WARNING - WARNING: Undeclared variable: y";
    String actualWarningMessage = warning.getMessage();
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testErrorMessageWithMultiLineSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {" +
            "  var x = y;" +
            "  function bar() {" +
            "    z = 5; // line with error" +
            "  }" +
            "}";
      }
    };

    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  var x = y;" +
        "  function bar() {" +
        "    z = 5; // line with error" +
        "  }" +
        "}";
    JSError error = compiler.compile(jsCode, null, source, new LightweightMessageFormatter(source));
    String expectedErrorMessage = "LINE 4: COLUMN 11: ERROR - ERROR: Uncaught exception: ReferenceError: z is not defined\n" +
        "function foo() {\n" +
        "  var x = y;\n" +
        "  function bar() {\n" +
        "    z = 5; // line with error\n" +
        "  }\n" +
        "}";
    String actualErrorMessage = error.getMessage();
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testWarningMessageWithMultiLineSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {" +
            "  var x = y;" +
            "  function bar() {" +
            "    z = 5; // line with warning" +
            "  }" +
            "}";
      }
    };

    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  var x = y;" +
        "  function bar() {" +
        "    z = 5; // line with warning" +
        "  }" +
        "}";
    JSError warning = compiler.compile(jsCode, null, source, new LightweightMessageFormatter(source));
    String expectedWarningMessage = "LINE 4: COLUMN 1: WARNING - WARNING: Undeclared variable: z\n" +
        "function foo() {\n" +
        "  var x = y;\n" +
        "  function bar() {\n" +
        "    z = 5; // line with warning\n" +
        "  }\n" +
        "}";
    String actualWarningMessage = warning.getMessage();
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testErrorMessageWithEmptySource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "";
      }
    };

    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  bar(); // line with error" +
        "}";
    JSError error = compiler.compile(jsCode, null, source, new LightweightMessageFormatter(source));
    String expectedErrorMessage = "LINE 1: ERROR - ERROR: Uncaught exception: TypeError: bar is not a function";
    String actualErrorMessage = error.getMessage();
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testWarningMessageWithEmptySource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "";
      }
    };

    TopLevelTestingCompiler compiler = new TopLevelTestingCompiler();
    String jsCode = "function foo() {" +
        "  var x = y;" +
        "  bar(); // line with warning" +
        "}";
    JSError warning = compiler.compile(jsCode, null, source, new LightweightMessageFormatter(source));
    String expectedWarningMessage = "LINE 1: WARNING - WARNING: Undeclared variable: y";
    String actualWarningMessage = warning.getMessage();
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }
}