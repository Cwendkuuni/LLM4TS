package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Tests for the CheckGlobalThis class.
 */
@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test shouldTraverse method

    // Test traversing a global function
    NodeTraversal traversal = new NodeTraversal();
    Node root = createTestNode("function test() {});");
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    checker.shouldTraverse(traversal, root, null);
    assertTrue(checker.shouldTraverse(traversal, root, null));

    // Test traversing a function annotated with @constructor
    root = createTestNode("/** @constructor */ function test() {});");
    checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    checker.shouldTraverse(traversal, root, null);
    assertFalse(checker.shouldTraverse(traversal, root, null));

    // Test traversing a function annotated with @this
    root = createTestNode("function test() { @this; }");
    checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    checker.shouldTraverse(traversal, root, null);
    assertFalse(checker.shouldTraverse(traversal, root, null));

    // Test traversing a prototype method
    root = createTestNode("var obj = {}; obj.test = function() {}; function test() { }");
    checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    checker.shouldTraverse(traversal, root, null);
    assertTrue(checker.shouldTraverse(traversal, root, null));
  }

  @Test
  public void testShouldReportThis() {
    // Test shouldReportThis method

    Node root = createTestNode("function test() { this; }");
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    assertTrue(checker.shouldReportThis(createTestNode("function test() { this; }"), null));

    root = createTestNode("function test() { this.property; }");
    checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    assertFalse(checker.shouldReportThis(createTestNode("function test() { this.property; }"), null));

    root = createTestNode("function test() { this.prototype.property; }");
    checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    assertFalse(checker.shouldReportThis(createTestNode("function test() { this.prototype.property; }"), null));

    root = createTestNode("function test() { this.prototype.property.subProperty; }");
    checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    assertFalse(checker.shouldReportThis(createTestNode("function test() { this.prototype.property.subProperty; }"), null));
  }

  @Test
  public void testShouldTraverseInAssignmentContext() {
    // Test shouldTraverse method in assignment context

    Node root = createTestNode("var a = this;");
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);
    assertTrue(checker.shouldTraverse(null, null, root));
  }

  @Test
  public void testVisit() {
    // Test visit method

    Node root = createTestNode("function test() { this; }");
    NodeTraversal traversal = new NodeTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(root), CheckLevel.WARNING);

    root = createTestNode("function test() { this; }");
    Node node = createTestNode("this;", root);
    checker.visit(traversal, node, root);
    assertTrue(traversal.hasError(DiagnosticType.WARNING, node));

    root = createTestNode("function test() { this.property; }");
    node = createTestNode("this.property", root);
    checker.visit(traversal, node, root);
    assertFalse(traversal.hasError(DiagnosticType.WARNING, node));

    root = createTestNode("function test() { this.prototype.property; }");
    node = createTestNode("this.prototype.property", root);
    checker.visit(traversal, node, root);
    assertFalse(traversal.hasError(DiagnosticType.WARNING, node));

    root = createTestNode("function test() { this.prototype.property.subProperty; }");
    node = createTestNode("this.prototype.property.subProperty", root);
    checker.visit(traversal, node, root);
    assertFalse(traversal.hasError(DiagnosticType.WARNING, node));

    root = createTestNode("function test() { var a = this; }");
    node = createTestNode("var a = this", root);
    checker.visit(traversal, node, root);
    assertFalse(traversal.hasError(DiagnosticType.WARNING, node));
  }

  private Node createTestNode(String nodeContent, Node parentNode) {
    Node root = null;
    if (parentNode == null) {
      root = new Node(Token.PROGRAM);
      root.setParent(null);
    } else {
      root = parentNode;
    }

    Node node = new Node(Token.STRING, nodeContent);
    node.setParent(root);
    return node;
  }

  private Node createTestNode(String nodeContent) {
    return createTestNode(nodeContent, null);
  }
}