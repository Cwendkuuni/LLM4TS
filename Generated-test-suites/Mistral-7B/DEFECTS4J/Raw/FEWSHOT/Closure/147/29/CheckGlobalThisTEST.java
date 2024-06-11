package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.traverse.NodeTraversal;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_globalFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.SCRIPT, null);
    Node functionNode = Node.newNode(Token.FUNCTION, null);

    when(functionNode.getParent()).thenReturn(parent);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertThat(shouldTraverse).isTrue();
  }

  @Test
  public void testShouldTraverse_constructorFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.NAME, "Constructor");
    Node constructorNode = Node.newNode(Token.FUNCTION, null);
    constructorNode.setJSDocInfo(new JSDocInfo(true));

    when(constructorNode.getParent()).thenReturn(parent);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, constructorNode, parent);

    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.SCRIPT, null);
    Node functionNode = Node.newNode(Token.FUNCTION, null);
    functionNode.setJSDocInfo(new JSDocInfo(null, null, new String[]{"@this"}));

    when(functionNode.getParent()).thenReturn(parent);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithNoParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newNode(Token.FUNCTION, null);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, null);

    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithPrototypePropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.ASSIGN, null);
    Node getPropNode = Node.newNode(Token.GETPROP, null, "prototype");
    Node functionNode = Node.newNode(Token.FUNCTION, null);

    parent.addChildToEnd(getPropNode);
    getPropNode.addChildToEnd(functionNode);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithPrototypePropertyAccessDeep() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.ASSIGN, null);
    Node getProp1Node = Node.newNode(Token.GETPROP, null, "prototype");
    Node getProp2Node = Node.newNode(Token.GETPROP, null, "prototype");
    Node functionNode = Node.newNode(Token.FUNCTION, null);

    parent.addChildToEnd(getProp1Node);
    getProp1Node.addChildToEnd(getProp2Node);
    getProp2Node.addChildToEnd(functionNode);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithAssignLeftSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.ASSIGN, null);
    Node lhs = Node.newNode(Token.GETPROP, null, "this");
    Node rhs = Node.newNode(Token.FUNCTION, null);

    parent.addChildToEnd(lhs);
    lhs.addChildToEnd(rhs);

    boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, rhs, parent);

    assertThat(shouldTraverse).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithAssignLeftSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.ASSIGN, null);
    Node lhs = Node.newNode(Token.GETPROP, null, "this");
    Node rhs = Node.newNode(Token.FUNCTION, null);

    parent.addChildToEnd(lhs);
    lhs.addChildToEnd(rhs);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(rhs, parent);

    assertThat(shouldReportThis).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithPropertyAccessLeftSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.ASSIGN, null);
    Node getPropNode = Node.newNode(Token.GETPROP, null, "prototype");
    Node lhs = Node.newNode(Token.GETPROP, null, "this");
    Node rhs = Node.newNode(Token.FUNCTION, null);

    parent.addChildToEnd(getPropNode);
    getPropNode.addChildToEnd(lhs);
    lhs.addChildToEnd(rhs);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(rhs, parent);

    assertThat(shouldReportThis).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithPropertyAccessLeftSideDeep() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.ASSIGN, null);
    Node getProp1Node = Node.newNode(Token.GETPROP, null, "prototype");
    Node getProp2Node = Node.newNode(Token.GETPROP, null, "prototype");
    Node lhs = Node.newNode(Token.GETPROP, null, "this");
    Node rhs = Node.newNode(Token.FUNCTION, null);

    parent.addChildToEnd(getProp1Node);
    getProp1Node.addChildToEnd(getProp2Node);
    getProp2Node.addChildToEnd(lhs);
    lhs.addChildToEnd(rhs);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(rhs, parent);

    assertThat(shouldReportThis).isTrue();
  }
}