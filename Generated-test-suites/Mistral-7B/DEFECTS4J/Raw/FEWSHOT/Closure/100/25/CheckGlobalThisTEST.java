package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
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
  private CheckLevel level;
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Node nodeThis;
  private Node nodeFunction;
  private Node nodePrototype;
  private Node nodeAssignment;
  private Node nodeFunctionWithJsdoc;
  private Node nodeFunctionWithThisAnnotation;
  private Node nodeFunctionWithThisAnnotationInsidePrototype;

  @Before
  public void setUp() {
    level = CheckLevel.WARNING;
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, level);
    traversal = new NodeTraversal(checker, new CompilerOptions(), null);

    nodeThis = Node.newInstance(Token.THIS);
    nodeFunction = Node.newInstance(Token.FUNCTION);
    nodePrototype = Node.newInstance(Token.GETPROP, Node.newInstance(Token.STRING, "prototype"));
    nodeAssignment = Node.newInstance(Token.ASSIGN, Node.newInstance(Token.THIS), Node.newInstance(Token.STRING, "name"));
    nodeFunctionWithJsdoc = Node.newInstance(Token.FUNCTION);
    nodeFunctionWithJsdoc = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@constructor"));
    nodeFunctionWithThisAnnotation = Node.newInstance(Token.FUNCTION);
    nodeFunctionWithThisAnnotation =
        Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@this"));
    nodeFunctionWithThisAnnotationInsidePrototype = Node.newInstance(Token.FUNCTION);
    nodeFunctionWithThisAnnotationInsidePrototype =
        Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@this"));
    nodeFunctionWithThisAnnotationInsidePrototype =
        Node.newInstance(
            Token.GETPROP, Node.newInstance(Token.STRING, "prototype"), nodeFunctionWithThisAnnotation);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newInstance(Token.FUNCTION);
    Node parent = Node.newInstance(Token.NAME, "foo");
    when(functionNode.getParent()).thenReturn(parent);
    assertThat(checker.shouldTraverse(null, functionNode, null)).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionConstructor() {
    JSDocInfo jsDoc = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@constructor"));
    when(nodeFunctionWithJsdoc.getJSDocInfo()).thenReturn(jsDoc);
    assertThat(checker.shouldTraverse(null, nodeFunctionWithJsdoc, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDoc = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@this"));
    when(nodeFunctionWithThisAnnotation.getJSDocInfo()).thenReturn(jsDoc);
    assertThat(checker.shouldTraverse(null, nodeFunctionWithThisAnnotation, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotationInsidePrototype() {
    JSDocInfo jsDoc = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@this"));
    when(nodeFunctionWithThisAnnotationInsidePrototype.getJSDocInfo()).thenReturn(jsDoc);
    assertThat(checker.shouldTraverse(null, nodeFunctionWithThisAnnotationInsidePrototype, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node nodeFunction = Node.newInstance(Token.FUNCTION);
    Node parent = Node.newInstance(Token.ASSIGN);
    when(nodeFunction.getParent()).thenReturn(parent);
    Node lhs = Node.newInstance(Token.GETPROP, Node.newInstance(Token.STRING, "x"));
    Node rhs = Node.newInstance(Token.THIS);
    parent.addChildrenToEnd(lhs, nodeFunction, rhs);
    assertThat(checker.shouldTraverse(null, nodeFunction, null)).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignment = Node.newInstance(Token.ASSIGN, nodeThis, Node.newInstance(Token.STRING, "name"));
    assertThat(checker.shouldTraverse(null, nodeThis, assignment)).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignment = Node.newInstance(Token.ASSIGN, nodeThis, nodePrototype);
    assertThat(checker.shouldTraverse(null, nodeThis, assignment)).isFalse();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    Node assignment = Node.newInstance(Token.ASSIGN, Node.newInstance(Token.GETPROP, nodeThis, Node.newInstance(Token.STRING, "prototype")), Node.newInstance(Token.STRING, "name"));
    assertThat(checker.shouldTraverse(null, nodeThis, assignment)).isFalse();
  }

  @Test
  public void testReportGlobalThis() {
    Node parent = Node.newInstance(Token.ASSIGN);
    Node lhs = Node.newInstance(Token.THIS);
    parent.addChildrenToEnd(lhs);
    Node n = Node.newInstance(Token.THIS);
    checker.visit(traversal, n, parent);
    traversal.reportErrors();
    ImmutableList<DiagnosticPosition> diagnostics = traversal.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0).getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testDontReportThisInsidePrototypeMethod() {
    Node parent = Node.newInstance(Token.FUNCTION);
    Node prototype = Node.newInstance(Token.GETPROP, Node.newInstance(Token.STRING, "prototype"));
    Node method = Node.newInstance(Token.FUNCTION);
    Node thisAnnotation = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@this"));
    Node lhs = Node.newInstance(Token.THIS);
    Node rhs = Node.newInstance(Token.STRING, "name");
    Node functionBody = Node.newInstance(Token.BLOCK);
    parent.addChildrenToEnd(prototype);
    prototype.addChildrenToEnd(method);
    method.addChildrenToEnd(thisAnnotation, lhs, rhs, functionBody);
    checker.visit(traversal, lhs, parent);
    traversal.reportErrors();
    ImmutableList<DiagnosticPosition> diagnostics = traversal.getDiagnostics();
    assertThat(diagnostics).isEmpty();
  }

  @Test
  public void testDontReportThisInsideFunctionAnnotatedWithConstructor() {
    Node parent = Node.newInstance(Token.FUNCTION);
    Node constructorAnnotation = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@constructor"));
    Node lhs = Node.newInstance(Token.THIS);
    Node rhs = Node.newInstance(Token.STRING, "name");
    Node functionBody = Node.newInstance(Token.BLOCK);
    parent.addChildrenToEnd(constructorAnnotation, lhs, rhs, functionBody);
    checker.visit(traversal, lhs, parent);
    traversal.reportErrors();
    ImmutableList<DiagnosticPosition> diagnostics = traversal.getDiagnostics();
    assertThat(diagnostics).isEmpty();
  }

  @Test
  public void testDontReportThisInsideFunctionAnnotatedWithThis() {
    Node parent = Node.newInstance(Token.FUNCTION);
    Node thisAnnotation = Node.newInstance(Token.ANNOTATION, Node.newInstance(Token.STRING, "@this"));
    Node lhs = Node.newInstance(Token.THIS);
    Node rhs = Node.newInstance(Token.STRING, "name");
    Node functionBody = Node.newInstance(Token.BLOCK);
    parent.addChildrenToEnd(thisAnnotation, lhs, rhs, functionBody);
    checker.visit(traversal, lhs, parent);
    traversal.reportErrors();
    ImmutableList<DiagnosticPosition> diagnostics = traversal.getDiagnostics();
    assertThat(diagnostics).isEmpty();
  }

  @Test
  public void testShouldNotReportThisWhenAssignedToVariable() {
    Node assign = Node.newInstance(Token.ASSIGN, Node.newInstance(Token.VAR, Node.newInstance(Token.STRING, "a")), Node.newInstance(Token.THIS));
    Node thisNode = Node.newInstance(Token.THIS);
    checker.visit(traversal, thisNode, null);
    checker.visit(traversal, assign, null);
    traversal.reportErrors();
    ImmutableList<DiagnosticPosition> diagnostics = traversal.getDiagnostics();
    assertThat(diagnostics).isEmpty();
  }
}