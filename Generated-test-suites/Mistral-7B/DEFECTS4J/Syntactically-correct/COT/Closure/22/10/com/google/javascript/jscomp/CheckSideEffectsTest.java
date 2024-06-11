package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerInput input;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new TestCompiler(options);
    input = new CompilerInput();
  }

  @Test
  public void testCheckSideEffectsFunctionality() {
    String sourceCode = "function testFunction() {"
        + "  var x = 1;"
        + "  var y = 2;"
        + "  var z = x + y;"
        + "  return z;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsVoidStatement() {
    String sourceCode = "function testFunction() {"
        + "  ;;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsCommaStatement() {
    String sourceCode = "function testFunction() {"
        + "  var x = 1,"
        + "      y = 2;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsNoResultUsed() {
    String sourceCode = "function testFunction() {"
        + "  var x = 1 + 2;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertFalse(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsSimpleOp() {
    String sourceCode = "function testFunction() {"
        + "  var x = 1 + 1;"
        + "  return x;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsString() {
    String sourceCode = "function testFunction() {"
        + "  var s = \"this string is \"";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsEval() {
    String sourceCode = "function testFunction() {"
        + "  eval('var x = 1;');"
        + "  return x;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsForLoop() {
    String sourceCode = "function testFunction() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    var x = i * 2;"
        + "  }"
        + "  return x;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsProtectedCode() {
    String sourceCode = "function testFunction() {"
        + "  var x = JSCOMPILER_PROTECTED;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    new CheckSideEffects(compiler, CheckLevel.ERROR, true).process(null, null);
  }

  @Test
  public void testCheckSideEffectsRemoveProtection() {
    String sourceCode = "function testFunction() {"
        + "  JSCOMPILER_PRESERVE = function() {}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    new StripProtection(compiler).process(null, null);
    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsJSDocInfo() {
    String sourceCode = "/** @preserve */\n"
        + "function testFunction() {"
        + "  /** @type {number} */ var x = 1;"
        + "}";

    input.setSourceCode(0, "testFile.js", sourceCode);
    compiler.compile(input);

    List<Node> problemNodes = new CheckSideEffects(compiler, CheckLevel.ERROR, false).getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }
}