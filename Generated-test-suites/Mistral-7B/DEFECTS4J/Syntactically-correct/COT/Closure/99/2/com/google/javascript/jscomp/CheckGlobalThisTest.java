package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.ParsingTest;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test for {@link CheckGlobalThis} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  private static final String TEST_SOURCE =
      "function evil() { this.useful = undefined; }";

  private AbstractCompiler compiler;
  private Checker checker;
  private CheckGlobalThis checkerUnderTest;
  private NodeFactory nodeFactory;
  private Parser parser;

  @Mock
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = mock(Checker.class);
    checkerUnderTest = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    nodeFactory = mock(NodeFactory.class);
    parser = mock(Parser.class);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.getOptions().getCheckGlobalThis()).thenReturn(true);
    when(compiler.getChecker()).thenReturn(checker);
    when(parser.parse(TEST_SOURCE, null, NodeFactory.getTopLevelScope(nodeFactory))).thenReturn(NodeUtil.parseNode(TEST_SOURCE));
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = NodeUtil.parseNode(
        "function evil() { this.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldTraverse(traversal, functionNode, parentNode)).thenReturn(true);
    assertThat(checkerUnderTest.shouldTraverse(nodeTraversal, functionNode, parentNode)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructorNode = NodeUtil.parseNode(
        "function EvilConstructor() {}");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(constructorNode.getJSDocInfo()).thenReturn(JSDocInfo.createFunctionConstructor());

    when(checkerUnderTest.shouldTraverse(traversal, constructorNode, parentNode)).thenReturn(false);
    assertThat(checkerUnderTest.shouldTraverse(nodeTraversal, constructorNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = NodeUtil.parseNode(
        "@this\nfunction evil() { this.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldTraverse(traversal, functionNode, parentNode)).thenReturn(false);
    assertThat(checkerUnderTest.shouldTraverse(nodeTraversal, functionNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverriddenAnnotation() {
    Node functionNode = NodeUtil.parseNode(
        "@override\nfunction evil() { this.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldTraverse(traversal, functionNode, parentNode)).thenReturn(false);
    assertThat(checkerUnderTest.shouldTraverse(nodeTraversal, functionNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node assignNode = Node.newAssign(null, Node.newThisNode(), null);
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldTraverse(traversal, assignNode, parentNode)).thenReturn(true);
    assertThat(checkerUnderTest.shouldTraverse(nodeTraversal, assignNode, parentNode)).isTrue();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssign(
        NodeUtil.parseIdentifier("prototype.useful"), Node.newThisNode(), null);
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldTraverse(traversal, assignNode, parentNode)).thenReturn(false);
    assertThat(checkerUnderTest.shouldTraverse(nodeTraversal, assignNode, parentNode)).isFalse();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newThisNode(), null, null);
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldReportThis(assignNode, parentNode)).thenReturn(true);
    when(nodeTraversal.makeError(assignNode, CheckLevel.WARNING, DiagnosticType.warning(null, null, ""))).thenReturn(null);

    checkerUnderTest.visit(nodeTraversal, assignNode, parentNode);
    assertThat(compiler.report).wasCalledOnce();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = NodeUtil.parseNode(
        "function evil() { var a = this; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldReportThis(assignNode, parentNode)).thenReturn(false);

    checkerUnderTest.visit(nodeTraversal, assignNode, parentNode);
    assertThat(compiler.report).wasNotCalled();
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node getNode = NodeUtil.parseNode(
        "function evil() { this.prototype.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldReportThis(getNode, parentNode)).thenReturn(true);
    when(nodeTraversal.makeError(getNode, CheckLevel.WARNING, DiagnosticType.warning(null, null, ""))).thenReturn(null);

    checkerUnderTest.visit(nodeTraversal, getNode, parentNode);
    assertThat(compiler.report).wasCalledOnce();
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithThisAnnotation() {
    Node getNode = NodeUtil.parseNode(
        "@this\nfunction evil() { this.prototype.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldReportThis(getNode, parentNode)).thenReturn(false);

    checkerUnderTest.visit(nodeTraversal, getNode, parentNode);
    assertThat(compiler.report).wasNotCalled();
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithOverriddenAnnotation() {
    Node getNode = NodeUtil.parseNode(
        "@override\nfunction evil() { this.prototype.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldReportThis(getNode, parentNode)).thenReturn(false);

    checkerUnderTest.visit(nodeTraversal, getNode, parentNode);
    assertThat(compiler.report).wasNotCalled();
  }

  @Test
  public void testShouldReportThisOnFunctionWithNoThisAnnotation() {
    Node functionNode = NodeUtil.parseNode(
        "function evil() { this.useful = undefined; }");
    Node parentNode = Node.newScopeNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(checkerUnderTest.shouldReportThis(functionNode, parentNode)).thenReturn(true);
    when(nodeTraversal.makeError(functionNode, CheckLevel.WARNING, DiagnosticType.warning(null, null, ""))).thenReturn(null);

    checkerUnderTest.visit(nodeTraversal, functionNode, parentNode);
    assertThat(compiler.report).wasCalledOnce();
  }
}