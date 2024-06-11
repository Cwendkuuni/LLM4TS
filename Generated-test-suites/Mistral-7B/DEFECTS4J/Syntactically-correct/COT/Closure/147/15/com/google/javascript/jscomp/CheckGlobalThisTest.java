package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    functionNode.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);
    Node result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("this");
    functionNode.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);
    Node result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithInterface() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("interface");
    functionNode.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);
    Node result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("override");
    functionNode.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);
    Node result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseFunctionNotAnnotatedWithConstructorOrThisOrInterfaceOrOverride() {
    Node functionNode = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);
    Node result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionInsideBlockOrScriptOrNameOrAssign() {
    Node functionNode = new Node(Token.FUNCTION);

    Node parent = new Node(Token.BLOCK);
    Node result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);

    parent = new Node(Token.SCRIPT);
    result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);

    parent = new Node(Token.NAME);
    result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);

    parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.FUNCTION);
    rhs.setJSDocInfo(new JSDocInfo("foo"));
    parent.setFirstChild(lhs);
    parent.setLastChild(rhs);
    result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.setFirstChild(assignNode);
    parent.setLastChild(rhs);

    checker.visit(mock(NodeTraversal.class), null, parent);
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypePropertyOrSubproperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.setLastChild(new Node("prototype"));
    Node rhs = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.setFirstChild(assignNode);
    parent.setLastChild(rhs);

    checker.visit(mock(NodeTraversal.class), null, parent);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.setFirstChild(assignNode);
    parent.setLastChild(lhs);

    checker.visit(mock(NodeTraversal.class), null, parent);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node getPropNode = new Node(Token.GETPROP);
    Node parent = new Node(Token.THIS);
    getPropNode.setLastChild(new Node("property"));

    Node traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parent);

    checker.visit(traversal, null, getPropNode);
    verify(compiler, times(1)).report(eq(traversal.makeError(getPropNode, checker.level, CheckGlobalThis.GLOBAL_THIS)));
  }
}