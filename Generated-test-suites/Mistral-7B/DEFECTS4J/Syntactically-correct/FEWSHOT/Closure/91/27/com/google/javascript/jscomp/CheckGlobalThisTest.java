package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node root = new Node(Token.SCRIPT);

    Node function1 = createFunctionNode("function foo() { this.x = 1; }");
    Node function2 = createFunctionNode("function bar() { return this; }");
    Node function3 = createFunctionNode("var x = function() { this.y = 1; }");
    Node function4 = createFunctionNode("function baz() { this.prototype.x = 1; }");

    Node object1 = createObjectNode("{ x: function() { this.y = 1; } }");
    Node object2 = createObjectNode("{ x: { y: function() {} } }");

    Node assign1 = createAssignNode("a = this");;
    Node assign2 = createAssignNode("a.x = this");;
    Node assign3 = createAssignNode("(a = this).x = 1");;
    Node assign4 = createAssignNode("a.prototype.x = this");;

    Node get1 = createGetNode("a.prototype");
    Node get2 = createGetNode("a.prototype.x");

    Node block = new Node(Token.BLOCK);
    Node blockStmt1 = new Node(Token.VAR, "a", null, null, null, new Node(Token.ASSIGN, null, assign1, null));
    Node blockStmt2 = new Node(Token.ASSIGN, "a", assign2, null);
    Node blockStmt3 = new Node(Token.ASSIGN, "a.x", assign3, null);
    Node blockStmt4 = new Node(Token.ASSIGN, "a.prototype.x", assign4, null);
    block.setFirstChild(blockStmt1);
    block.setLastChild(blockStmt4);

    root.setFirstChild(function1);
    root.setLastChild(block);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(traversal.getCurrentNode()).thenReturn(function1);
    when(traversal.getParentNode()).thenReturn(root);
    when(traversal.makeError(Node.ANY_NODE, CheckLevel.WARNING, DiagnosticType.warning(null, null))).thenReturn(new Node(Token.ERROR));

    checker.shouldTraverse(traversal, function1, root);
    assertThat(checker.shouldTraverse(traversal, function2, root)).isFalse();
    assertThat(checker.shouldTraverse(traversal, function3, root)).isFalse();
    assertThat(checker.shouldTraverse(traversal, function4, root)).isTrue();

    when(traversal.getCurrentNode()).thenReturn(assign1);
    checker.shouldTraverse(traversal, assign1, root);

    when(traversal.getCurrentNode()).thenReturn(assign2);
    checker.shouldTraverse(traversal, assign2, root);

    when(traversal.getCurrentNode()).thenReturn(assign3);
    checker.shouldTraverse(traversal, assign3, root);

    when(traversal.getCurrentNode()).thenReturn(assign4);
    checker.shouldTraverse(traversal, assign4, root);

    when(traversal.getCurrentNode()).thenReturn(get1);
    checker.shouldTraverse(traversal, get1, root);

    when(traversal.getCurrentNode()).thenReturn(get2);
    checker.shouldTraverse(traversal, get2, root);
  }

  @Test
  public void testVisit() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node root = new Node(Token.SCRIPT);

    Node function1 = createFunctionNode("function foo() { this.x = 1; }");
    Node function2 = createFunctionNode("function bar() { return this; }");
    Node function3 = createFunctionNode("var x = function() { this.y = 1; }");
    Node function4 = createFunctionNode("function baz() { this.prototype.x = 1; }");

    Node object1 = createObjectNode("{ x: function() { this.y = 1; } }");
    Node object2 = createObjectNode("{ x: { y: function() {} } }");

    Node assign1 = createAssignNode("a = this");;
    Node assign2 = createAssignNode("a.x = this");;
    Node assign3 = createAssignNode("(a = this).x = 1");;
    Node assign4 = createAssignNode("a.prototype.x = this");;

    Node get1 = createGetNode("a.prototype");
    Node get2 = createGetNode("a.prototype.x");

    Node block = new Node(Token.BLOCK);
    Node blockStmt1 = new Node(Token.VAR, "a", null, null, null, new Node(Token.ASSIGN, null, assign1, null));
    Node blockStmt2 = new Node(Token.ASSIGN, "a.x", assign2, null);
    Node blockStmt3 = new Node(Token.ASSIGN, "a.x", assign3, null);
    Node blockStmt4 = new Node(Token.ASSIGN, "a.prototype.x", assign4, null);
    block.setFirstChild(blockStmt1);
    block.setLastChild(blockStmt4);

    root.setFirstChild(function1);
    root.setLastChild(block);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(traversal.getParentNode()).thenReturn(root);

    checker.visit(traversal, function1, null);
    checker.visit(traversal, function2, null);
    checker.visit(traversal, function3, null);
    checker.visit(traversal, function4, null);

    when(traversal.getCurrentNode()).thenReturn(assign1);
    checker.visit(traversal, assign1, null);

    when(traversal.getCurrentNode()).thenReturn(assign2);
    checker.visit(traversal, assign2, null);

    when(traversal.getCurrentNode()).thenReturn(assign3);
    checker.visit(traversal, assign3, null);

    when(traversal.getCurrentNode()).thenReturn(assign4);
    checker.visit(traversal, assign4, null);

    when(traversal.getCurrentNode()).thenReturn(get1);
    checker.visit(traversal, get1, null);

    when(traversal.getCurrentNode()).thenReturn(get2);
    checker.visit(traversal, get2, null);
  }

  private Node createAssignNode(String nodeString) {
    return NodeUtil.createNodeFromString(nodeString, null);
  }

  private Node createFunctionNode(String nodeString) {
    return NodeUtil.createNodeFromString(nodeString, Token.FUNCTION);
  }

  private Node createGetNode(String nodeString) {
    return NodeUtil.createNodeFromString(nodeString, Token.GETPROP);
  }

  private Node createObjectNode(String nodeString) {
    return NodeUtil.createNodeFromString(nodeString, Token.OBJECT);
  }
}