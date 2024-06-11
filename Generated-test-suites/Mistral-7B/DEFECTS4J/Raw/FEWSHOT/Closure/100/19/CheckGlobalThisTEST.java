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
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private MockedStatic<com.google.javascript.jscomp.Diagnostics> diagnostics;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    diagnostics = mockStatic(Diagnostics.class);

    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node globalFunction = Node.newFunction();
    Node parent = Node.newBlock(null);
    Node result = checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(globalFunction), ArgumentMatchers.eq(parent));
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunction = Node.newFunction();
    JSDocInfo jsDocInfo = new JSDocInfo("constructor");
    Node constructorAnnotation = Node.newAnnotation(jsDocInfo);
    Node parent = Node.newBlock(constructorAnnotation);
    Node result = checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(constructorFunction), ArgumentMatchers.eq(parent));
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionWithThisAnnotation = Node.newFunction();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType(true);
    Node thisAnnotation = Node.newAnnotation(jsDocInfo);
    Node parent = Node.newBlock(thisAnnotation);
    Node result = checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionWithThisAnnotation), ArgumentMatchers.eq(parent));
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutJsDocInfo() {
    Node functionWithoutJsDocInfo = Node.newFunction();
    Node parent = Node.newBlock(null);
    Node result = checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionWithoutJsDocInfo), ArgumentMatchers.eq(parent));
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newThis(), Node.newIdentifier("x"));
    Node parentNode = Node.newBlock(null);
    Node result = checker.shouldReportThis(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(parentNode));
    Node nodeToReport = Node.newThis();
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(nodeToReport), ArgumentMatchers.eq(assignmentNode));
    assertThat(diagnostics.getCalls().get(0).getArguments()).containsExactly(compiler, CheckLevel.WARNING, GLOBAL_THIS);
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newIdentifier("x"), Node.newThis());
    Node parentNode = Node.newBlock(null);
    Node nodeToReport = Node.newThis();
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(nodeToReport), ArgumentMatchers.eq(parentNode));
    assertNull(diagnostics.getCalls());
  }

  @Test
  public void testShouldNotReportThisKeywordOnPropertyAccess() {
    Node propertyAccessNode = Node.newGetProp(Node.newIdentifier("x"), Node.newIdentifier("prototype"));
    Node parentNode = Node.newBlock(null);
    Node nodeToReport = Node.newThis();
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(nodeToReport), ArgumentMatchers.eq(parentNode));
    assertNull(diagnostics.getCalls());
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignmentWithAssignLhsChild() {
    Node assignmentNode = Node.newAssign(Node.newThis(), Node.newIdentifier("x"));
    Node parentNode = Node.newBlock(null);
    Node assignmentNodeWithChild = Node.newAssign(Node.newIdentifier("y"), Node.newThis());
    Node assignmentNodeWithChildParent = Node.newBlock(assignmentNodeWithChild);
    Node result = checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(assignmentNode), ArgumentMatchers.eq(parentNode));
    Node nodeToReport = Node.newThis();
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(nodeToReport), ArgumentMatchers.eq(assignmentNodeWithParent));
    assertThat(diagnostics.getCalls().get(0).getArguments()).containsExactly(compiler, CheckLevel.WARNING, GLOBAL_THIS);
  }
}