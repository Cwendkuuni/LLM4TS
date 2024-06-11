package com.example;

import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
  }

  @Test
  public void testShouldTraverseNonFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.NULL);
    assertFalse(checkGlobalThis.shouldTraverse(null, n, null));
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.PROGRAM);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo(new JSDocTag("@constructor")));
    Node parent = new Node(Token.PROGRAM);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithoutParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node functionNode = new Node(Token.FUNCTION);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignmentLeftSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.NUMBER, "10");
    assignNode.addChildToEnd(lhs);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(Token.PROGRAM);
    parent.addChildToEnd(assignNode);
    Node functionNode = new Node(Token.FUNCTION);
    parent.addChildToEnd(functionNode);
    CheckGlobalThis instance = new CheckGlobalThis(null, null);
    instance.shouldTraverse(null, functionNode, parent);
    assertTrue(instance.assignLhsChild == lhs);
  }

  @Test
  public void testShouldTraverseFunctionWithAssignmentRightSide() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, null, new Node(Token.THIS), new Node(Token.STRING, "x"));
    Node rhs = new Node(Token.FUNCTION);
    assignNode.addChildToEnd(lhs);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(Token.PROGRAM);
    parent.addChildToEnd(assignNode);
    Node functionNode = new Node(Token.FUNCTION);
    parent.addChildToEnd(functionNode);
    CheckGlobalThis instance = new CheckGlobalThis(null, null);
    instance.shouldTraverse(null, functionNode, parent);
    assertFalse(instance.assignLhsChild != null);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.STRING, "something");
    assignNode.addChildToEnd(lhs);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(Token.PROGRAM);
    parent.addChildToEnd(assignNode);
    CheckGlobalThis instance = new CheckGlobalThis(null, null);
    instance.shouldReportThis(assignNode.getFirstChild(), assignNode);
    assertTrue(instance.shouldReportThis(assignNode.getFirstChild(), assignNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.STRING, "something");
    assignNode.addChildToEnd(lhs);
    assignNode.addChildToEnd(rhs);
    Node parent = new Node(Token.PROGRAM);
    parent.addChildToEnd(assignNode);
    CheckGlobalThis instance = new CheckGlobalThis(null, null);
    instance.shouldReportThis(rhs, assignNode);
    assertFalse(instance.shouldReportThis(rhs, assignNode));
  }

  @Test
  public void testShouldNotReportThisOnPrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node parent = new Node(Token.PROGRAM);
    Node prototype = new Node(Token.GETPROP, null, new Node(Token.THIS), new Node(Token.STRING, "prototype"));
    Node functionNode = new Node(Token.FUNCTION);
    parent.addChildToEnd(prototype);
    parent.addChildToEnd(functionNode);
    CheckGlobalThis instance = new CheckGlobalThis(null, null);
    instance.shouldReportThis(prototype, functionNode);
    assertFalse(instance.shouldReportThis(prototype, functionNode));
  }

  @Test
  public void testShouldNotReportThisOnPrototypeSubProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node parent = new Node(Token.PROGRAM);
    Node prototype = new Node(Token.GETPROP, null, new Node(Token.THIS), new Node(Token.STRING, "prototype"));
    Node getProp = new Node(Token.GETPROP, null, prototype, new Node(Token.STRING, "x"));
    Node functionNode = new Node(Token.FUNCTION);
    parent.addChildToEnd(prototype);
    parent.addChildToEnd(getProp);
    parent.addChildToEnd(functionNode);
    CheckGlobalThis instance = new CheckGlobalThis(null, null);
    instance.shouldReportThis(getProp, functionNode);
    assertFalse(instance.shouldReportThis(getProp, functionNode));
  }

  @Test(expected = NullPointerException.class)
  public void testNullArgumentsInShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.shouldTraverse(null, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullArgumentsInVisit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.visit(null, null, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testNullArgumentsInConstructor() {
    new CheckGlobalThis(null, null);
  }
}