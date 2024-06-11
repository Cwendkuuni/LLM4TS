package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeType;

import java.util.List;

/**
 * Test cases for {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    String sourceCode = "function foo() { return; } " +
        "var x = foo();";

    Node root = NodeUtil.parseJavaScriptText(sourceCode, null);
    checker.check(root, null);
    NodeTraversal.traverse(checkSideEffects, root, null);
    checker.process();
    List<String> messages = checker.getDiagnostics();
    assertTrue(messages.isEmpty());

    sourceCode = "function foo() { return; } " +
        "var x = foo();;";

    root = NodeUtil.parseJavaScriptText(sourceCode, null);
    checker.check(root, null);
    NodeTraversal.traverse(checkSideEffects, root, null);
    checker.process();
    messages = checker.getDiagnostics();
    assertFalse(messages.isEmpty());
    assertEquals(1, messages.size());
    DiagnosticType diagnosticType = messages.get(0).getType();
    assertEquals(CheckSideEffects.USELESS_CODE_ERROR, diagnosticType);
  }

  @Test
  public void testProtectSideEffects() {
    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "var x = 1 + 1;";

    Node root = NodeUtil.parseJavaScriptText(sourceCode, null);
    checker.check(root, null);
    NodeTraversal.traverse(checkSideEffects, root, null);
    checker.process();
    List<String> messages = checker.getDiagnostics();
    assertTrue(messages.isEmpty());

    sourceCode = "function protect() { var x = 1 + 1; } " +
        "protect();";

    root = NodeUtil.parseJavaScriptText(sourceCode, null);
    checker.check(root, null);
    NodeTraversal.traverse(checkSideEffects, root, null);
    checker.process();
    messages = checker.getDiagnostics();
    assertTrue(messages.isEmpty());

    sourceCode = "function protect() { var x = 1 + 1; } " +
        "protect();";

    root = NodeUtil.parseJavaScriptText(sourceCode, null);
    checker.check(root, null);
    NodeTraversal.traverse(checkSideEffects, root, null);
    checker.process();
    messages = checker.getDiagnostics();
    assertFalse(messages.isEmpty());
    assertEquals(1, messages.size());
    DiagnosticType diagnosticType = messages.get(0).getType();
    assertEquals(CheckSideEffects.USELESS_CODE_ERROR, diagnosticType);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal.traverse(stripProtection, root, null);
    checker.process();
    messages = checker.getDiagnostics();
    assertTrue(messages.isEmpty());
  }
}