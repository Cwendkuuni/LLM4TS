package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null, null);
    Node parentNode = Node.newNameNode("functionName", null);

    when(functionNode.getParent()).thenReturn(parentNode);
    when(compiler.report(any(NodeTraversal.class), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null, null);
    Node parentNode = Node.newNameNode("functionName", null);
    JSDocInfo jsDoc = Node.newJsDocNode(Token.JSDOC, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    when(functionNode.getParent()).thenReturn(parentNode);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), functionNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null, null);
    Node parentNode = Node.newNameNode("functionName", null);
    JSDocInfo jsDoc = Node.newJsDocNode(Token.JSDOC, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    when(functionNode.getParent()).thenReturn(parentNode);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), functionNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null, null);
    Node parentNode = Node.newNameNode("functionName", null);
    Node parentParentNode = Node.newBlock(null);

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(parentParentNode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node assignNode = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node leftNode = Node.newNameNode("left", null);
    Node rightNode = Node.newThisNode();

    when(assignNode.getFirstChild()).thenReturn(leftNode);
    when(assignNode.getLastChild()).thenReturn(rightNode);

    Node parentNode = Node.newNameNode("parentNode", null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), assignNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node assignNode = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node leftNode = Node.newGetPropNode("prototype", Node.newNameNode("parentNode", null));
    Node rightNode = Node.newThisNode();

    when(assignNode.getFirstChild()).thenReturn(leftNode);
    when(assignNode.getLastChild()).thenReturn(rightNode);

    Node parentNode = Node.newNameNode("parentNode", null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), assignNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseAssignmentToProperty() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    Node assignNode = Node.newAssignNode(Token.ASSIGN, null, null, null);
    Node leftNode = Node.newNameNode("left", null);
    Node rightNode = Node.newNameNode("right", null);
    Node rightRightNode = Node.newThisNode();

    when(assignNode.getFirstChild()).thenReturn(leftNode);
    when(assignNode.getLastChild()).thenReturn(rightNode);
    when(rightNode.getLastChild()).thenReturn(rightRightNode);

    Node parentNode = Node.newNameNode("parentNode", null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), assignNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseNull() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    boolean result = checker.shouldTraverse(NodeTraversal.create(traversal), null, null);
    assertNull(result);
  }
}