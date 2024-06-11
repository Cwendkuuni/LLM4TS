package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, false);

    Node root = createNode("var x = 1; x = 2;");
    NodeTraversal.traverse(null, root, checkSideEffects);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    checkSideEffects.problemNodes.clear();

    assertTrue(problemNodes.isEmpty());

    root = createNode("var x = 1 + 1;");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(Token.ASSIGN, problemNode.getType());

    root = createNode("function foo() {} var x = foo();");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());

    root = createNode("var x = 1 + 1; 2;");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.NUMBER, problemNode.getType());

    root = createNode("function foo() {} foo();");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.FUNCTION, problemNode.getType());

    root = createNode("function foo() {} foo();;");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.SEMICOLON, problemNode.getType());

    root = createNode("function foo() {}");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertTrue(problemNodes.isEmpty());

    root = createNode("function foo() { return 1 + 1; }");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertTrue(problemNodes.isEmpty());

    root = createNode("function foo() { return 1 + 1; } 2;");
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.NUMBER, problemNode.getType());
  }

  @Test
  public void testProtectSideEffects() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, protectSideEffectFreeCode);

    Node root = createNode("var x = 1; x = 2;");
    NodeTraversal.traverse(null, root, checkSideEffects);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    checkSideEffects.problemNodes.clear();

    assertTrue(problemNodes.isEmpty());

    checkSideEffects.protectSideEffects();

    root = createNode("var x = 1; x = 2;");
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(Token.ASSIGN, problemNode.getType());

    // Protected node
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    root = createNode("var x = 1 + 1;");
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.ASSIGN, problemNode.getType());

    // Protected node with JSDoc info
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    root = createNode("/** @type {number} */ var x = 1 + 1;");
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.ASSIGN, problemNode.getType());

    // Protected node that is an expression result
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    root = createNode("var x = (1 + 1);");
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertFalse(problemNodes.isEmpty());
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(Token.EXPR_RESULT, problemNode.getType());

    // Protected node that is a statement
    problemNodes = Lists.newArrayList();
    checkSideEffects.problemNodes = problemNodes;
    root = createNode("if (true) {}");
    NodeTraversal.traverse(null, root, checkSideEffects);

    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testRemoveProtection() {
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, false);

    Node root = createNode("(function() { return JSCOMPILER_PRESERVE(1 + 1); })();");
    NodeTraversal.traverse(null, root, checkSideEffects.StripProtection);

    assertTrue(root.getChildCount() == 0);
  }

  private Node createNode(String javaScriptCode) {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput();
    input.setCode(javaScriptCode, "test", null);
    input.setOptions(options);
    AbstractCompiler compiler = new CompilationUnitCompiler(options, null);
    Node root = compiler.parseTree(input, null);
    return root;
  }

  private static boolean equals(Node node1, Node node2) {
    if (node1 == null && node2 == null) {
      return true;
    }
    if (node1 == null || node2 == null) {
      return false;
    }
    if (node1.getType() != node2.getType()) {
      return false;
    }
    if (node1.getChildCount() != node2.getChildCount()) {
      return false;
    }
    for (int i = 0; i < node1.getChildCount(); i++) {
      if (!equals(node1.getChild(i), node2.getChild(i))) {
        return false;
      }
    }
    return true;
  }

  private static void assertEquals(Node expected, Node actual) {
    assertEquals(expected, actual, "Expected and actual nodes do not match.");
  }

  private static void assertEquals(Node expected, Node actual, String errorMessage) {
    if (!equals(expected, actual)) {
      throw new AssertionError(errorMessage + " Expected: " + printNode(expected) +
          ", Actual: " + printNode(actual));
    }
  }

  private static void assertNull(Node node) {
    if (node != null) {
      throw new AssertionError("Expected null, but got: " + printNode(node));
    }
  }

  private static String printNode(Node node) {
    return NodePrinter.printNode(node);
  }
}