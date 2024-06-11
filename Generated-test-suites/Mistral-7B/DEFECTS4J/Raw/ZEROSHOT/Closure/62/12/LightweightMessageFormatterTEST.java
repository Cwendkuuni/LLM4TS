package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.BaseCompilerTestCase;
import com.google.javascript.jscomp.testing.CodeSnippet;
import com.google.javascript.jscomp.testing.TypeChecker;

public class LightweightMessageFormatterTest extends BaseCompilerTestCase {

  @Test
  public void testFormatError() {
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider();
    SourceExcerpt excerpt = SourceExcerpt.LINE;
    LightweightMessageFormatter lightweightMessageFormatter =
        new LightweightMessageFormatter(sourceExcerptProvider, excerpt);

    String input = "function foo() { return 1; }";
    JSError error = checkCompilationError(input);

    assertEquals(
        "ERROR - function:1:1\n" +
            "        1| function foo() { return 1; }\n" +
            "        ^\n",
        lightweightMessageFormatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider();
    LightweightMessageFormatter lightweightMessageFormatter =
        new LightweightMessageFormatter(sourceExcerptProvider);

    String input = "function foo() { return 1; }";
    JSError warning = new JSError(
        null,
        null,
        1,
        1,
        0,
        null,
        "Unitialized variable",
        CheckLevel.WARNING,
        null,
        null,
        null);

    assertEquals(
        "WARNING - function:1:1\n" +
            "        1| function foo() { return 1; }\n" +
            "        ^^ Unitialized variable\n",
        lightweightMessageFormatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider();
    SourceExcerpt excerpt = SourceExcerpt.LINE;
    LightweightMessageFormatter lightweightMessageFormatter =
        new LightweightMessageFormatter(sourceExcerptProvider, excerpt);

    String input = "function foo() { return x; }";
    JSError error = checkCompilationError(input, "x.js", 1, 1);

    assertEquals(
        "ERROR - x.js:1:1\n" +
            "        1| function foo() { return x; }\n" +
            "        ^\n" +
            "x.js:1:2: 'x' is not defined.\n",
        lightweightMessageFormatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider();
    LightweightMessageFormatter lightweightMessageFormatter =
        new LightweightMessageFormatter(sourceExcerptProvider);

    String input = "function foo() { return x; }";
    JSError warning = checkCompilationWarning(input, "x.js", 1, 1);

    assertEquals(
        "WARNING - x.js:1:1\n" +
            "        1| function foo() { return x; }\n" +
            "        ^^ 'x' is not defined.\n" +
            "x.js:1:2",
        lightweightMessageFormatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter lightweightMessageFormatter =
        LightweightMessageFormatter.withoutSource();

    String input = "function foo() { return 1; }";
    JSError error = checkCompilationError(input);

    assertEquals(
        "ERROR - function:1:1\n" +
            "        1| function foo() { return 1; }\n",
        lightweightMessageFormatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter lightweightMessageFormatter =
        LightweightMessageFormatter.withoutSource();

    String input = "function foo() { return 1; }";
    JSError warning = new JSError(
        null,
        null,
        1,
        1,
        0,
        null,
        "Unitialized variable",
        CheckLevel.WARNING,
        null,
        null,
        null);

    assertEquals(
        "WARNING - function:1:1\n" +
            "        1| function foo() { return 1; }\n" +
            "        ^^ Unitialized variable",
        lightweightMessageFormatter.formatWarning(warning));
  }

  private JSError checkCompilationError(String input, String fileName, int lineNumber, int charNumber) {
    TypeChecker typeChecker = new TypeChecker(getCompiler());
    typeChecker.checkTypeErrors(input, fileName, lineNumber, charNumber);
    return typeChecker.getErrors().get(0);
  }

  private JSError checkCompilationError(String input) {
    return checkCompilationError(input, null, 0, 0);
  }

  private JSError checkCompilationWarning(String input, String fileName, int lineNumber, int charNumber) {
    TypeChecker typeChecker = new TypeChecker(getCompiler());
    typeChecker.checkTypeErrors(input, fileName, lineNumber, charNumber);
    return typeChecker.getWarnings().get(0);
  }
}

class CodeSnippet {
  // empty class, used to make the test compiler the input code
}