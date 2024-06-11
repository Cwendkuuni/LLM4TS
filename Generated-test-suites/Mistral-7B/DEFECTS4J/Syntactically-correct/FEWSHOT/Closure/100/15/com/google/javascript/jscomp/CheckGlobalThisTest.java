package com.google.javascript.jscomp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(null, checker);
    rootNode = new Node(Token.PROGRAM, null);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(functionNode);
    parent.setParent(rootNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorNode = new Node(Token.FUNCTION, null, null);
    constructorNode.setJSDocInfo(new JSDocInfo(true, null, null, null, null, null, null, null, null, null));
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(constructorNode);
    parent.setParent(rootNode);

    assertFalse(checker.shouldTraverse(traversal, constructorNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION, null, null);
    functionNode.setJSDocInfo(new JSDocInfo(null, null, null, null, null, "@this", null, null, null, null));
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(functionNode);
    parent.setParent(rootNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutAnnotation() {
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(functionNode);
    parent.setParent(rootNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.THIS, null, null);
    Node right = new Node(Token.NUM, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.setParent(rootNode);

    assertTrue(checker.shouldTraverse(traversal, left, parent));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, null, null);
    Node right = new Node(Token.THIS, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.setParent(rootNode);

    assertFalse(checker.shouldTraverse(traversal, right, parent));
  }

  @Test
  public void testShouldNotTraversePrototypeProperty() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, null, "prototype");
    Node right = new Node(Token.THIS, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node objectNode = new Node(Token.GETPROP, null, null);
    objectNode.addChild(new Node(Token.THIS, null, null));
    objectNode.addChild(left);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.addChild(objectNode);
    parent.setParent(rootNode);

    assertFalse(checker.shouldTraverse(traversal, right, parent));
  }

  @Test
  public void testShouldNotTraverseSubPropertyOfPrototype() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, null, "prototype");
    Node prototype = new Node(Token.GETPROP, null, null);
    prototype.addChild(new Node(Token.THIS, null, null));
    Node subProperty = new Node(Token.NAME, null, "subProperty");
    left.addChild(prototype);
    left.addChild(subProperty);
    Node right = new Node(Token.THIS, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.setParent(rootNode);

    assertFalse(checker.shouldTraverse(traversal, right, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.THIS, null, null);
    Node right = new Node(Token.NUM, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.setParent(rootNode);

    when(compiler.report(traversal.makeError(left, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenReturn(null);

    checker.visit(traversal, assignNode, parent);

    assertTrue(left.getFirstChild().isErrorNode());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, null, "left");
    Node right = new Node(Token.THIS, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.setParent(rootNode);

    checker.visit(traversal, assignNode, parent);

    assertNull(right.getFirstChild());
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentIfAssignLhsChildIsSet() {
    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.THIS, null, null);
    Node right = new Node(Token.NUM, null, null);
    assignNode.addChild(left);
    assignNode.addChild(right);
    Node leftSideOfAssignment = new Node(Token.ASSIGN, null, null);
    Node leftSideLeft = new Node(Token.THIS, null, null);
    Node leftSideRight = new Node(Token.NUM, null, null);
    leftSideOfAssignment.addChild(leftSideLeft);
    leftSideOfAssignment.addChild(leftSideRight);
    Node parent = new Node(Token.PROGRAM, null);
    parent.addChild(assignNode);
    parent.addChild(leftSideOfAssignment);
    parent.setParent(rootNode);

    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(traversal, assignNode, parent);
    checker.visit(traversal, leftSideOfAssignment, parent);

    assertNull(left.getFirstChild());
    assertNull(leftSideLeft.getFirstChild());
  }
}