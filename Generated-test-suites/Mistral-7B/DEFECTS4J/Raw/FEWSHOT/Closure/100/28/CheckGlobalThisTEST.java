package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
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
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.newTraversal(null, checker, null);
  }

  @Test
  public void testShouldReportThisOnLhsOfAssignment() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.NUMBER, 1);
    parent.addChildrenToEnd(lhs, node, rhs);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(1, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisOnRhsOfAssignment() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);
    parent.addChildrenToEnd(lhs, node, rhs);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP, "prototype");
    Node grandparent = new Node(Token.NAME, "obj");
    grandparent.addChildrenToEnd(parent);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(grandparent);

    traversal.traverse(root);

    assertEquals(1, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisOnPrototypePropertyAccess() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP, "prototype");
    Node grandparent = new Node(Token.NAME, "obj");
    Node prototype = new Node(Token.NAME, "prototype");
    prototype.addChildrenToEnd(parent);
    grandparent.addChildrenToEnd(prototype);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(grandparent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisInFunction() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    parent.addChildrenToEnd(node);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(1, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisInFunctionWithJsDoc() {
    Node node = new Node(Token.THIS);
    Node jsDocNode = new Node(Token.COMMENT, "/** @this {Object} */");
    Node parent = new Node(Token.FUNCTION, null, null);
    parent.addChildrenToEnd(jsDocNode, node);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisInConstructorFunction() {
    Node node = new Node(Token.THIS);
    Node constructorJsDoc = new Node(Token.COMMENT, "/** @constructor */");
    Node parent = new Node(Token.FUNCTION, null, null);
    parent.addChildrenToEnd(constructorJsDoc, node);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(1, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisInFunctionWithEmptyJsDoc() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    parent.addChildrenToEnd(node);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    traversal = NodeTraversal.newTraversal(null, checker, null);

    traversal.traverse(root);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertEquals(1, positions.size());
    assertThat(positions.get(0).getStartColumn()).isGreaterThan(0);
  }

  @Test
  public void testShouldReportThisInFunctionWithThisTypeJsDoc() {
    Node node = new Node(Token.THIS);
    Node thisJsDoc = new Node(Token.COMMENT, "/** @this {Function} */");
    Node parent = new Node(Token.FUNCTION, null, null);
    parent.addChildrenToEnd(thisJsDoc, node);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisInGlobalScope() {
    Node node = new Node(Token.THIS);
    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(node);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisInNestedFunctionCall() {
    Node outerFunction = createFunctionNode("function outer() {", "}");
    Node innerFunction = createFunctionNode("function inner() {", "}");
    Node outerCall = new Node(Token.CALL, outerFunction, ImmutableList.of());
    Node innerCall = new Node(Token.CALL, innerFunction, ImmutableList.of(outerCall));
    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(innerCall);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisInNestedFunctionCallWithThisInInnerFunction() {
    Node outerFunction = createFunctionNode("function outer() {", "}");
    Node innerFunction = createFunctionNode("function inner() { this; }", "}");
    Node outerCall = new Node(Token.CALL, outerFunction, ImmutableList.of());
    Node innerCall = new Node(Token.CALL, innerFunction, ImmutableList.of(outerCall));
    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(innerCall);

    traversal.traverse(root);

    assertEquals(1, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisInFunctionWithAnnotatedParameters() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    Node param = new Node(Token.NAME, "p");
    Node paramJsDoc = new Node(Token.COMMENT, "/** @this {Object} @param {number} p */");
    parent.addChildrenToEnd(paramJsDoc, param, node);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisInFunctionWithAnnotatedParametersAndThisInBody() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    Node param = new Node(Token.NAME, "p");
    Node paramJsDoc = new Node(Token.COMMENT, "/** @this {Object} @param {number} p */");
    Node body = new Node(Token.BLOCK, null);
    Node thisInBody = new Node(Token.THIS);
    body.addChildrenToEnd(thisInBody);
    parent.addChildrenToEnd(paramJsDoc, param, body);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldReportThisInFunctionWithAnnotatedParametersAndThisInBodyWithErrorLevel() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    Node param = new Node(Token.NAME, "p");
    Node paramJsDoc = new Node(Token.COMMENT, "/** @this {Object} @param {number} p */");
    Node body = new Node(Token.BLOCK, null);
    Node thisInBody = new Node(Token.THIS);
    body.addChildrenToEnd(thisInBody);
    parent.addChildrenToEnd(paramJsDoc, param, body);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    traversal = NodeTraversal.newTraversal(null, checker, null);

    traversal.traverse(root);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertEquals(1, positions.size());
    assertThat(positions.get(0).getStartColumn()).isGreaterThan(0);
  }

  @Test
  public void testShouldNotReportThisInFunctionWithAnnotatedParametersAndThisInBodyWithWarningLevel() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    Node param = new Node(Token.NAME, "p");
    Node paramJsDoc = new Node(Token.COMMENT, "/** @this {Object} @param {number} p */");
    Node body = new Node(Token.BLOCK, null);
    Node thisInBody = new Node(Token.THIS);
    body.addChildrenToEnd(thisInBody);
    parent.addChildrenToEnd(paramJsDoc, param, body);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    traversal = NodeTraversal.newTraversal(null, checker, null);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  @Test
  public void testShouldNotReportThisInFunctionWithAnnotatedParametersAndThisInBodyWithJsDocInBody() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION, null, null);
    Node param = new Node(Token.NAME, "p");
    Node paramJsDoc = new Node(Token.COMMENT, "/** @this {Object} @param {number} p */");
    Node body = new Node(Token.BLOCK, null);
    Node thisInBody = new Node(Token.THIS);
    Node thisJsDocInBody = new Node(Token.COMMENT, "/** @this {Function} */");
    body.addChildrenToEnd(thisJsDocInBody, thisInBody);
    parent.addChildrenToEnd(paramJsDoc, param, body);

    Node root = new Node(Token.PROGRAM, null);
    root.addChildrenToEnd(parent);

    traversal.traverse(root);

    assertEquals(0, compiler.getNumWarnings());
  }

  private Node createFunctionNode(String start, String end) {
    Node function = new Node(Token.FUNCTION, null, null);
    function.setSourceName("test.js");
    function.setStart(start.lastIndexOf(Token.LEFT_BRACE), start.length());
    function.setEnd(end.indexOf(Token.RIGHT_BRACE), end.length());
    return function;
  }
}