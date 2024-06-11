package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
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
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseGlobalContexts() {
    Node globalContext = Node.newScopeNode();
    Node functionDeclaration = Node.newFunctionNode("foo", null, null, null, null, null, null);

    when(traversal.getCurrentScope()).thenReturn(globalContext);
    when(traversal.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionDeclaration, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node functionDeclaration = Node.newFunctionNode("foo", null, null, null, null, null, null);
    Node prototypeProperty = Node.newGetPropNode("prototype", Node.newNameNode("Object"));

    when(traversal.getCurrentScope()).thenReturn(functionDeclaration);
    when(traversal.getParent()).thenReturn(prototypeProperty);

    boolean result = checker.shouldTraverse(traversal, Node.newNameNode("this"), null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode(Node.newNameNode("a"), Node.newNameNode("this"));
    Node parent = Node.newAssignNode(Node.newNameNode("x"), assignNode);

    when(traversal.getCurrentScope()).thenReturn(Node.newScopeNode());
    when(traversal.getParent()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, Node.newNameNode("this"), null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssignNode(Node.newNameNode("a.prototype.x"), Node.newNameNode("this"));

    when(traversal.getCurrentScope()).thenReturn(Node.newScopeNode());
    when(traversal.getParent()).thenReturn(assignNode);

    boolean result = checker.shouldTraverse(traversal, Node.newNameNode("this"), null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = Node.newThisKeywordNode();
    Node parent = Node.newAssignNode(Node.newNameNode("a"), Node.newNameNode("b"));

    when(traversal.getCurrentScope()).thenReturn(Node.newScopeNode());
    when(traversal.getParent()).thenReturn(parent);

    checker.visit(traversal, null, null);
    checker.visit(traversal, globalThisNode, null);

    Node error = compiler.getLastReportedError();

    assertThat(error).isNotNull();
    assertThat(error.getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeMethod() {
    Node prototypeMethod = Node.newFunctionNode("foo", null, null, null, null, null, null);
    Node functionNode = Node.newFunctionNode("bar", null, null, null, null, null, prototypeMethod);
    Node thisNode = Node.newThisKeywordNode();

    when(traversal.getCurrentScope()).thenReturn(prototypeMethod);
    when(traversal.getParent()).thenReturn(functionNode);

    checker.visit(traversal, null, null);
    checker.visit(traversal, thisNode, null);

    Node error = compiler.getLastReportedError();

    assertThat(error).isNull();
  }

  @Test
  public void testShouldNotReportThisInsideConstructor() {
    Node constructorNode = Node.newFunctionNode("Foo", null, null, null, null, null, null);
    Node thisNode = Node.newThisKeywordNode();

    when(traversal.getCurrentScope()).thenReturn(constructorNode);
    when(traversal.getParent()).thenReturn(null);

    checker.visit(traversal, null, null);
    checker.visit(traversal, thisNode, null);

    Node error = compiler.getLastReportedError();

    assertThat(error).isNull();
  }

  @Test
  public void testShouldNotReportThisInsideFunctionAnnotatedWithThis() {
    Node thisAnnotatedFunctionNode = Node.newFunctionNode("foo", null, null, null, null, null, null);
    Node thisAnnotatedFunctionDocInfo = Node.newJSDocInfoNode(Node.newTagNode("@this"));
    Node thisNode = Node.newThisKeywordNode();

    when(traversal.getCurrentScope()).thenReturn(thisAnnotatedFunctionNode);
    when(traversal.getParent()).thenReturn(thisAnnotatedFunctionDocInfo);

    checker.visit(traversal, null, null);
    checker.visit(traversal, thisNode, null);

    Node error = compiler.getLastReportedError();

    assertThat(error).isNull();
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    Node functionNode = Node.newFunctionNode("foo", null, null, null, null, null, null);
    Node thisNode = Node.newThisKeywordNode();

    when(traversal.getCurrentScope()).thenReturn(functionNode);
    when(traversal.getParent()).thenReturn(null);

    checker.visit(traversal, null, null);
    checker.visit(traversal, thisNode, null);

    Node error = compiler.getLastReportedError();

    assertThat(error).isNull();
  }
}