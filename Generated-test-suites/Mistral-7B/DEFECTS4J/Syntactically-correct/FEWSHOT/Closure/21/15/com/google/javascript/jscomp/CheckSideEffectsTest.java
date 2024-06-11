package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String source = "function foo() {}" +
        "function bar() {}" +
        "void x = 1;" +
        "void x = 1;" +
        "var s = \"this string is continued on the next line but you forgot the +\";" +
        "x = foo();" +
        "foo();;" +
        "for (var i = 0; i < 10; i++) {" +
        "  i = i + 1;" +
        "}" +
        "var p = document.createElement('p');" +
        "p.offsetWidth;" +
        "function qux() { return 42; }";

    Parser parser = new Parser();
    CompilerInput input = new CompilerInput(Parser.parse(source), "test.js");
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    checker.process(input.getExterns(), input.getRootNode());

    // Assert expected warnings.
    List<Node> warningNodes = checker.problemNodes;
    List<Node> expectedWarningNodes = Lists.newArrayList(
        input.getRootNode().getChild(6),
        input.getRootNode().getChild(8),
        input.getRootNode().getChild(12),
        input.getRootNode().getChild(14),
        input.getRootNode().getChild(17)
    );

    for (int i = 0; i < warningNodes.size(); ++i) {
      Node warningNode = warningNodes.get(i);
      Node expectedWarningNode = expectedWarningNodes.get(i);
      assertTrue("Warning expected at line " + (expectedWarningNode.getLineNumber() + 1) + ", but none found.",
          NodeOperations.isDescendantOf(warningNode, expectedWarningNode));
    }

    // Assert no unexpected warnings.
    for (Node node = input.getRootNode(); node != null; node = node.getParent()) {
      if (node.getType() == Token.VOID) continue;
      if (node.isCall() && node.getFirstChild().isName() && node.getFirstChild().getString().equals("JSCOMPILER_PRESERVE")) continue;
      if (NodeUtil.mayHaveSideEffects(node, compiler)) continue;

      CheckSideEffects.USELESS_CODE_ERROR warningType = CheckSideEffects.USELESS_CODE_ERROR;
      assertFalse("Unexpected warning at line " + (node.getLineNumber() + 1) + ": " + warningType.getMessage(warningType, ""));
    }
  }

  @Test
  public void testProtectSideEffects() {
    String source = "function foo() {}" +
        "function bar() {}" +
        "void x = 1;" +
        "void x = 1;" +
        "var s = \"this string is continued on the next line but you forgot the +\";" +
        "x = foo();" +
        "foo();;" +
        "for (var i = 0; i < 10; i++) {" +
        "  i = i + 1;" +
        "}" +
        "var p = document.createElement('p');" +
        "p.offsetWidth;" +
        "function qux() { return 42; }";

    Parser parser = new Parser();
    CompilerInput input = new CompilerInput(Parser.parse(source), "test.js");
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    checker.process(input.getExterns(), input.getRootNode());
    checker.protectSideEffects();

    // Assert that side-effect free nodes have been protected.
    for (Node warningNode : checker.problemNodes) {
      Node expectedProtectedNode = warningNode;
      // Find the protected node by looking for a call to "JSCOMPILER_PRESERVE".
      while (expectedProtectedNode != null &&
             expectedProtectedNode.getType() != Token.CALL &&
             expectedProtectedNode.getFirstChild() != null &&
             expectedProtectedNode.getFirstChild().isName() &&
             expectedProtectedNode.getFirstChild().getString().equals("JSCOMPILER_PRESERVE")) {
        expectedProtectedNode = expectedProtectedNode.getFirstChild();
      }

      assertTrue("Expected side-effect free node [" + warningNode + "] to be protected.",
          NodeOperations.isDescendantOf(expectedProtectedNode, warningNode));
    }

    // Assert that all protected nodes have been removed.
    for (Node node = input.getRootNode(); node != null; node = node.getParent()) {
      if (node.isCall() && node.getFirstChild().isName() && node.getFirstChild().getString().equals("JSCOMPILER_PRESERVE")) {
        fail("Unexpected 'JSCOMPILER_PRESERVE' call at line " + (node.getLineNumber() + 1));
      }
    }
  }
}

class Compiler extends AbstractCompiler {
  @Override
  public void report(Node node, DiagnosticType diagnosticType, String message, Object... args) {
    // Silence warnings when testing.
  }
}