package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
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
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traverser;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traverser = NodeTraversal.create(checker, null, null);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);

    assertTrue(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);
    when(fnNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);

    assertFalse(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);
    when(fnNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);

    assertFalse(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);
    when(fnNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isInterface()).thenReturn(true);

    assertFalse(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);
    when(fnNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isOverride()).thenReturn(true);

    assertFalse(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeProperty() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    Node getPropNode = mock(Node.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getString()).thenReturn("prototype");
    when(fnNode.getFirstChild()).thenReturn(getPropNode);
    when(getPropNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getLastChild().getString()).thenReturn("prototype");

    assertFalse(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubProperty() {
    Node fnNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    Node getPropNode = mock(Node.class);
    Node getPropChildNode = mock(Node.class);

    when(fnNode.getType()).thenReturn(Token.FUNCTION);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getString()).thenReturn("prototype");
    when(fnNode.getFirstChild()).thenReturn(getPropNode);
    when(getPropNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getLastChild()).thenReturn(getPropChildNode);
    when(getPropChildNode.getType()).thenReturn(Token.GETPROP);

    assertFalse(checker.shouldTraverse(traverser, fnNode, parentNode));
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);

    assertTrue(checker.shouldTraverse(traverser, rhsNode, assignNode));
  }

  @Test
  public void testShouldTraversePropertyAccess() {
    Node getPropNode = mock(Node.class);
    Node parentNode = mock(Node.class);

    when(parentNode.getType()).thenReturn(Token.SCRIPT);
    when(getPropNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getLastChild().getString()).thenReturn("this");

    assertTrue(checker.shouldTraverse(traverser, getPropNode, parentNode));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.STRING);
    when(lhsNode).thenReturn(assignLhsChild);

    assertFalse(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithNoAssignLhsChild() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.STRING);

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithAssignLhsChild() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node assignLhsChild = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.STRING);
    when(lhsNode).thenReturn(assignLhsChild);
    when(assignLhsChild.getType()).thenReturn(Token.ASSIGN);
    when(assignLhsChild.getFirstChild()).thenReturn(lhsNode);

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPrototypeProperty() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node prototypeNode = mock(Node.class);
    Node getPropNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.STRING);
    when(lhsNode).thenReturn(getPropNode);
    when(getPropNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getLastChild().getType()).thenReturn(Token.STRING);
    when(getPropNode.getLastChild().getString()).thenReturn("prototype");
    when(getPropNode.getFirstChild()).thenReturn(prototypeNode);
    when(prototypeNode.getType()).thenReturn(Token.NAME);
    when(prototypeNode.getString()).thenReturn("prototype");

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPrototypeSubProperty() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node prototypeNode = mock(Node.class);
    Node getPropNode = mock(Node.class);
    Node getPropChildNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.STRING);
    when(lhsNode).thenReturn(getPropNode);
    when(getPropNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getLastChild()).thenReturn(getPropChildNode);
    when(getPropChildNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getFirstChild()).thenReturn(prototypeNode);
    when(prototypeNode.getType()).thenReturn(Token.NAME);
    when(prototypeNode.getString()).thenReturn("prototype");
    when(getPropChildNode.getLastChild().getType()).thenReturn(Token.STRING);
    when(getPropChildNode.getLastChild().getString()).thenReturn("subProperty");

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.THIS);

    assertFalse(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnRightSideOfAssignmentWithPrototypeProperty() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node prototypeNode = mock(Node.class);
    Node getPropNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.GETPROP);
    when(rhsNode.getLastChild().getType()).thenReturn(Token.STRING);
    when(rhsNode.getLastChild().getString()).thenReturn("prototype");
    when(rhsNode.getFirstChild()).thenReturn(prototypeNode);
    when(prototypeNode.getType()).thenReturn(Token.NAME);
    when(prototypeNode.getString()).thenReturn("prototype");

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnRightSideOfAssignmentWithPrototypeSubProperty() {
    Node assignNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node prototypeNode = mock(Node.class);
    Node getPropNode = mock(Node.class);
    Node getPropChildNode = mock(Node.class);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(lhsNode.getType()).thenReturn(Token.THIS);
    when(rhsNode.getType()).thenReturn(Token.GETPROP);
    when(rhsNode.getLastChild()).thenReturn(getPropChildNode);
    when(getPropChildNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getType()).thenReturn(Token.STRING);
    when(getPropNode.getString()).thenReturn("prototype");
    when(getPropChildNode.getLastChild().getType()).thenReturn(Token.STRING);
    when(getPropChildNode.getLastChild().getString()).thenReturn("subProperty");
    when(getPropNode.getFirstChild()).thenReturn(prototypeNode);
    when(prototypeNode.getType()).thenReturn(Token.NAME);
    when(prototypeNode.getString()).thenReturn("prototype");

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  private Node assignLhsChild;

  private void assertFalse(boolean condition) {
    if (condition) {
      fail("Expected checker to not report a THIS on left side of an assignment.");
    }
  }

  private void assertTrue(boolean condition) {
    if (!condition) {
      fail("Expected checker to report a THIS on left side of an assignment.");
    }
  }
}