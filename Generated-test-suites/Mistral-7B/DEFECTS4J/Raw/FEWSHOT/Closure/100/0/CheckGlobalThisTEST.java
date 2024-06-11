package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testGlobalThisWarning() {
    Node functionNode = Node.newFunction(Node.NO_SOURCE_NAME, Token.FUNCTION, null, null);
    when(functionNode.getJSDocInfo()).thenReturn(null);
    Node thisNode = Node.newThis(Node.NO_SOURCE_NAME, null);

    Node parentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "a"), thisNode);

    Node traverseNode = Node.newExpressionStatement(Node.ASSIGN, functionNode, Node.newName(Node.NO_SOURCE_NAME, "foo"));

    Node parentParentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "bar"), traverseNode);

    checker.traverse(traversal, parentParentNode, null);

    assertThat(compiler.errors).hasSize(1);
    assertThat(compiler.errors.get(0).getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testNoWarningForPrototypePropertyAccess() {
    Node functionNode = Node.newFunction(Node.NO_SOURCE_NAME, Token.FUNCTION, null, null);
    when(functionNode.getJSDocInfo()).thenReturn(null);
    Node prototypePropertyAccess = Node.newGetProperty(Node.NO_SOURCE_NAME, Node.newName(Node.NO_SOURCE_NAME, "prototype"), null);

    Node parentNode = Node.newExpressionStatement(Node.ASSIGN, prototypePropertyAccess, Node.newThis());

    Node traverseNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "a"), Node.newName(Node.NO_SOURCE_NAME, "foo"));

    Node parentParentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "bar"), traverseNode);

    checker.traverse(traversal, parentParentNode, null);

    assertNull(compiler.errors);
  }

  @Test
  public void testNoWarningForFunctionWithJsDoc() {
    Node functionNode = Node.newFunction(Node.NO_SOURCE_NAME, Token.FUNCTION, null, null);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    Node thisNode = Node.newThis(Node.NO_SOURCE_NAME, null);

    Node parentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "a"), thisNode);

    Node traverseNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "foo"), Node.newName(Node.NO_SOURCE_NAME, "bar"));

    Node parentParentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "bar"), traverseNode);

    checker.traverse(traversal, parentParentNode, null);

    assertNull(compiler.errors);
  }

  @Test
  public void testNoWarningForFunctionWithJsDocAndPrototypeMethod() {
    Node functionNode = Node.newFunction(Node.NO_SOURCE_NAME, Token.FUNCTION, null, null);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    Node thisNode = Node.newThis(Node.NO_SOURCE_NAME, null);

    Node parentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "a"), thisNode);

    Node traverseNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "foo"), Node.newName(Node.NO_SOURCE_NAME, "bar"));

    Node parentParentNode = Node.newExpressionStatement(Node.ASSIGN, Node.newName(Node.NO_SOURCE_NAME, "bar"), traverseNode);

    checker.traverse(traversal, parentParentNode, null);

    assertNull(compiler.errors);
  }
}