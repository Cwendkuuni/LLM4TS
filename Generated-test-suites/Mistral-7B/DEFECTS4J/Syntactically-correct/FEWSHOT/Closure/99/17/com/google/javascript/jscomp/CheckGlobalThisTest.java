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
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private Node node;
  private Node parentNode;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
    node = mock(Node.class);
    parentNode = mock(Node.class);
    when(node.getParent()).thenReturn(parentNode);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    when(parentNode.getType()).thenReturn(Token.SCRIPT);
    assertTrue(checker.shouldTraverse( NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    when(parentNode.getType()).thenReturn(Token.BLOCK);
    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.isConstructor()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.hasThisType()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.isOverride()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutParent() {
    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = mock(Node.class);
    Node assignLeftChild = mock(Node.class);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(assignLeftChild);
    when(assignLeftChild).thenReturn(node);

    assertTrue(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = mock(Node.class);
    Node assignLeftChild = mock(Node.class);
    Node assignRightChild = mock(Node.class);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(assignLeftChild);
    when(assignLeftChild.getType()).thenReturn(Token.GETPROP);
    when(assignLeftChild.getLastChild()).thenReturn(assignRightChild);
    when(assignRightChild.getString()).thenReturn("prototype");

    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToSubProperty() {
    Node assignNode = mock(Node.class);
    Node assignLeftChild = mock(Node.class);
    Node assignRightChild = mock(Node.class);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(assignLeftChild);
    when(assignLeftChild.getQualifiedName()).thenReturn("a.b");
    when(assignRightChild.getString()).thenReturn("c");

    assertFalse(checker.shouldTraverse(NodeTraversal.TRAVERSAL, node, parentNode));
  }

  @Test
  public void testShouldReportGlobalThis() {
    when(parentNode.getType()).thenReturn(Token.SCRIPT);

    checker.visit(NodeTraversal.TRAVERSAL, node, parentNode);
    assertThat(compiler.reportCallCount()).isEqualTo(1);
  }

  @Test
  public void testShouldNotReportGlobalThisOnLeftSideOfAssignment() {
    Node assignNode = mock(Node.class);
    Node assignLeftChild = mock(Node.class);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(assignLeftChild);
    when(assignLeftChild).thenReturn(node);

    checker.visit(NodeTraversal.TRAVERSAL, node, parentNode);
    assertThat(compiler.reportCallCount()).isZero();
  }

  @Test
  public void testShouldNotReportGlobalThisOnRightSideOfAssignment() {
    Node assignNode = mock(Node.class);
    Node assignLeftChild = mock(Node.class);
    Node assignRightChild = mock(Node.class);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(assignLeftChild);
    when(assignLeftChild).thenReturn(parentNode);
    when(assignRightChild).thenReturn(node);

    checker.visit(NodeTraversal.TRAVERSAL, node, parentNode);
    assertThat(compiler.reportCallCount()).isZero();
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    Node functionNode = mock(Node.class);
    Node functionParentNode = mock(Node.class);

    when(functionNode.getParent()).thenReturn(functionParentNode);

    when(functionParentNode.getType()).thenReturn(Token.NAME);
    when(functionParentNode.getFirstChild()).thenReturn(functionNode);

    when(functionNode.getJSDocInfo()).thenReturn(null);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);
    assertNull(result);

    when(functionParentNode.getType()).thenReturn(Token.VAR);
    result = checker.getFunctionJsDocInfo(functionNode);
    assertNull(result);

    when(functionParentNode.getType()).thenReturn(Token.SCRIPT);
    result = checker.getFunctionJsDocInfo(functionNode);
    assertThat(result).isEqualTo(jsDocInfo);
  }
}