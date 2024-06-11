package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.NodeUtil;

import java.util.List;

/**
 * Test cases for CheckSideEffects class.
 */
public class CheckSideEffectsTest {

  private static AbstractCompiler compiler;

  @BeforeClass
  public static void setup() {
    compiler = new TestCompiler();
  }

  @Test
  public void testCheckSideEffects() {
    String code = "function foo() {} var x = 1; x = foo();";
    CompilerInput input = new CompilerInput(code, "Test.js");
    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input, null);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(18, problemNode.getLineNumber());
  }

  @Test
  public void testCheckSideEffectsNoProblem() {
    String code = "function foo() {} var x = 1; x = foo(); if (x > 0) {}",
        expectedCode = "function foo() {} var x = 1; x += 1; if (x > 0) {}";
    CompilerInput input = new CompilerInput(code, "Test.js");
    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input, null);
    assertEquals(expectedCode, compiler.getCompiledCode());
    assertNotNull(checker.problemNodes);
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsProtectFreeCode() {
    String code = "function foo() {} var x = 1; x = foo();";
    CompilerInput input = new CompilerInput(code, "Test.js");
    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
    checker.process(input, null);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    // Check that the protected code is present in the compiled code.
    String compiledCode = compiler.getCompiledCode();
    Node protectedCode = NodeUtil.findNodeByDescription(compiledCode,
        "return JSCOMPILER_PRESERVE.call(JSCOMPILER_PRESERVE, JSCOMPILER_PRESERVE.call(JSCOMPILER_PRESERVE, thisArg, arg0));");
    assertEquals(18, problemNode.getLineNumber());
    assertEquals(12, protectedCode.getLineNumber());
  }

  @Test
  public void testCheckSideEffectsRemoveProtection() {
    String code = "function foo() {} var x = 1; x = foo();";
    CompilerInput input = new CompilerInput(code, "Test.js");
    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
    checker.process(input, null);
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);
    stripper.process(input, null);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    // Check that the protected code is removed from the compiled code.
    String compiledCode = compiler.getCompiledCode();
    Node protectedCode = NodeUtil.findNodeByDescription(compiledCode,
        "return JSCOMPILER_PRESERVE.call(JSCOMPILER_PRESERVE, JSCOMPILER_PRESERVE.call(JSCOMPILER_PRESERVE, thisArg, arg0));");
    assertNull(protectedCode);
  }
}