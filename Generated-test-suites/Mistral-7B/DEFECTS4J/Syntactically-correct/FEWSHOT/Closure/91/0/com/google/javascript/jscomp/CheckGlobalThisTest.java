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
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalFunctions() {
    Node functionNode = Node.newFunction(new Node[0], null, null);
    Node parentNode = Node.newName("window");
    Node globalContextNode = Node.newName("global");

    when(compiler.getCurrentNode()).thenReturn(globalContextNode);

    NodeTraversal traversal = new NodeTraversal(functionNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunctions() {
    Node constructorNode = Node.newFunction(new Node[0], null, null);
    constructorNode.setIsConstructor(true);

    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(constructorNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(constructorNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraversetotypeFunctions() {
    Node prototypeNode = Node.newFunction(new Node[0], null, Node.newName("prototype"));

    Node parentNode = Node.newName("Object");

    NodeTraversal traversal = new NodeTraversal(prototypeNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(prototypeNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(new Node[0]);

    Node annotatedFunctionNode = Node.newFunction(new Node[0], jsDoc, null);

    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(annotatedFunctionNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(annotatedFunctionNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node unannotatedFunctionNode = Node.newFunction(new Node[0], null, null);

    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(unannotatedFunctionNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(unannotatedFunctionNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node localVariableNode = Node.newLocal("localVariable", Node.newInt(0));
    Node parentNode = Node.newBlock(new Node[0]);

    NodeTraversal traversal = new NodeTraversal(localVariableNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(localVariableNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newName("someProperty"));
    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(assignNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssign(Node.newName("prototype"), Node.newThis());
    Node parentNode = Node.newName("Object");

    NodeTraversal traversal = new NodeTraversal(assignNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseRightSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("someVariable"), Node.newThis());
    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(assignNode, null);

    boolean result = checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode));

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("assignLhsChild"), Node.newThis());
    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(assignNode, null);

    boolean assignLhsChildSet = false;

    checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode));
    checker.visit(traversal, assignNode, parentNode);

    assertThat(assignLhsChildSet).isTrue();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("someVariable"), Node.newThis());
    Node parentNode = Node.newName("window");

    NodeTraversal traversal = new NodeTraversal(assignNode, null);

    checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode));
    checker.visit(traversal, assignNode, parentNode);

    assertNull(checker.assignLhsChild);
  }
}