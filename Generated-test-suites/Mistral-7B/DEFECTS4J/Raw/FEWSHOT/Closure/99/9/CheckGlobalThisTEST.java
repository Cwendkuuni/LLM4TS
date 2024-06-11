package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = new NodeFactory();
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalFunctions() {
    Node functionNode = nodeFactory.createFunction("", null);
    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    assertThat(checker.shouldTraverse(new NodeTraversal(nodeFactory), functionNode, parentNode)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunctions() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);

    Node constructorNode = nodeFactory.createFunction("", jsDoc, null);
    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    when(compiler.report(Mockito.any())).thenReturn(null);

    assertThat(checker.shouldTraverse(traversal, constructorNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionsWithThisAnnotation() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.putThisAnnotation();

    Node functionNode = nodeFactory.createFunction("", jsDoc, null);
    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    when(compiler.report(Mockito.any())).thenReturn(null);

    assertThat(checker.shouldTraverse(traversal, functionNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionsOutsideBlockContext() {
    Node functionNode = nodeFactory.createFunction("", null);
    Node parentNode = nodeFactory.createName("window");

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    when(compiler.report(Mockito.any())).thenReturn(null);

    assertThat(checker.shouldTraverse(traversal, functionNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = nodeFactory.createAssign(Token.ASSIGN, null, null);
    Node leftNode = nodeFactory.createName("this");
    Node rightNode = nodeFactory.createNumber(1);

    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    checker.visit(traversal, assignNode, parentNode);
    checker.visit(traversal, leftNode, assignNode);

    assertThat(checker.shouldReportThis(leftNode, assignNode)).isTrue();
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeProperty() {
    Node assignNode = nodeFactory.createAssign(Token.ASSIGN, null, null);
    Node leftNode = nodeFactory.createGetProp(Token.GETPROP, null, nodeFactory.createName("prototype"));
    Node rightNode = nodeFactory.createName("this");

    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    checker.visit(traversal, assignNode, parentNode);
    checker.visit(traversal, leftNode, assignNode);

    assertThat(checker.shouldReportThis(leftNode, assignNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeSubProperty() {
    Node assignNode = nodeFactory.createAssign(Token.ASSIGN, null, null);
    Node leftNode = nodeFactory.createGetProp(Token.GETPROP, null, nodeFactory.createName("prototype"), nodeFactory.createName("foo"));
    Node rightNode = nodeFactory.createName("this");

    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    checker.visit(traversal, assignNode, parentNode);
    checker.visit(traversal, leftNode, assignNode);

    assertThat(checker.shouldReportThis(leftNode, assignNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    Node assignNode = nodeFactory.createAssign(Token.ASSIGN, null, null);
    Node leftNode = nodeFactory.createName("x");
    Node rightNode = nodeFactory.createName("this");

    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    checker.visit(traversal, assignNode, parentNode);
    checker.visit(traversal, leftNode, assignNode);
    checker.visit(traversal, rightNode, assignNode);

    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = nodeFactory.createAssign(Token.ASSIGN, null, null);
    Node leftNode = nodeFactory.createName("x");
    Node rightNode = nodeFactory.createGetProp(Token.GETPROP, null, nodeFactory.createName("prototype"), nodeFactory.createName("foo"));

    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = new NodeTraversal(nodeFactory);
    checker.visit(traversal, assignNode, parentNode);
    checker.visit(traversal, leftNode, assignNode);
    checker.visit(traversal, rightNode, assignNode);

    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testShouldReportWarningForGlobalThis() {
    Node assignNode = nodeFactory.createAssign(Token.ASSIGN, null, null);
    Node leftNode = nodeFactory.createName("x");
    Node rightNode = nodeFactory.createName("this");

    Node globalContext = nodeFactory.createScript("", null);
    Node parentNode = nodeFactory.createBlock("", globalContext);

    when(compiler.report(Mockito.any())).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(nodeFactory);

    checker.visit(traversal, assignNode, parentNode);
    checker.visit(traversal, leftNode, assignNode);
    checker.visit(traversal, rightNode, assignNode);

    DiagnosticPosition position = checker.assignLhsChild != null
        ? checker.assignLhsChild.getPosition()
        : rightNode.getPosition();

    AbstractCompiler.DiagnosticInfo diagnosticInfo = checker.compiler.getLastDiagnostic();

    assertThat(diagnosticInfo).isNotNull();
    assertThat(diagnosticInfo.getDiagnosticType()).isEqualTo(checker.GLOBAL_THIS);
    assertThat(diagnosticInfo.getSourceName()).isEqualTo("");
    assertThat(diagnosticInfo.getStartPosition().getLine()).isEqualTo(position.getLine());
    assertThat(diagnosticInfo.getStartPosition().getColumn()).isEqualTo(position.getColumn());
    assertThat(diagnosticInfo.getEndPosition().getLine()).isEqualTo(position.getLine());
    assertThat(diagnosticInfo.getEndPosition().getColumn()).isEqualTo(position.getColumn());
  }
}