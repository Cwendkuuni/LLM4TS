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
import com.google.javascript.jscomp.NodeTraversal.CallbackFlags;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(null, null, checker, null, null, null, null);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = mock(Node.class);

    when(root.getParent()).thenReturn(null);
    when(root.getType()).thenReturn(Token.ROOT);

    Node globalThis = new Node(Token.THIS);

    // Traverse the root node
    NodeTraversal.traverse(root, traversal, null);

    // Check that the globalThis node was traversed
    assertThat(checker.shouldTraverse(traversal, globalThis, root)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructor = new Node(Token.FUNCTION);
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setConstructor(true);

    Node constructorParent = new Node(Token.VAR);
    constructorParent.setJSDocInfo(constructorJsDoc);

    Node constructorThis = new Node(Token.THIS);

    // Traverse the constructor function's parent node
    NodeTraversal.traverse(constructorParent, traversal, null);

    // Check that the constructorThis node is not traversed
    assertThat(checker.shouldTraverse(traversal, constructorThis, constructorParent)).isFalse();
  }

  @Test
  public void testShouldNotTraversePrototypeProperty() {
    Node prototype = new Node(Token.GETPROP);
    prototype.setLastChild(new Node(Token.STRING, "prototype"));

    Node thisNode = new Node(Token.THIS);

    Node object = new Node(Token.NAME, "object");
    Node objectThis = new Node(Token.THIS);

    // Traverse the object node
    NodeTraversal.traverse(object, traversal, null);

    // Check that the thisNode is not traversed when accessed as a prototype property
    NodeTraversal.traverse(prototype, traversal, object);
    assertThat(checker.shouldTraverse(traversal, thisNode, prototype)).isFalse();

    // Check that the objectThis node is traversed when accessed normally
    NodeTraversal.traverse(objectThis, traversal, prototype);
    assertThat(checker.shouldTraverse(traversal, thisNode, objectThis)).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assign = new Node(Token.ASSIGN);
    Node left = new Node(Token.THIS);
    Node right = new Node(Token.INT_LITERAL, 42);

    Node assignmentParent = new Node(Token.VAR);

    // Traverse the assignment's parent node
    NodeTraversal.traverse(assignmentParent, traversal, null);

    // Check that the this node is reported when it's on the left side of an assignment
    NodeTraversal.traverse(assign, traversal, assignmentParent);
    assertReported(GLOBAL_THIS, left);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assign = new Node(Token.ASSIGN);
    Node left = new Node(Token.NAME, "a");
    Node thisNode = new Node(Token.THIS);

    Node assignmentParent = new Node(Token.VAR);

    // Traverse the assignment's parent node
    NodeTraversal.traverse(assignmentParent, traversal, null);

    // Check that the this node is not reported when it's on the right side of an assignment
    NodeTraversal.traverse(left, traversal, assignmentParent);
    NodeTraversal.traverse(thisNode, traversal, left);
    assertNoReports(GLOBAL_THIS);
  }

  @Test
  public void testShouldNotReportThisInFunctionAnnotatedWithThis() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);
    functionNode.setJSDocInfo(jsDoc);

    Node functionParent = new Node(Token.VAR);
    functionParent.setJSDocInfo(jsDoc);

    Node functionThis = new Node(Token.THIS);

    // Traverse the function node's parent node
    NodeTraversal.traverse(functionParent, traversal, null);

    // Check that the this node is not reported when it's annotated with @this
    NodeTraversal.traverse(functionThis, traversal, functionParent);
    assertNoReports(GLOBAL_THIS);
  }

  @Test
  public void testShouldReportThisInFunctionWithoutThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionThis = new Node(Token.THIS);

    Node functionParent = new Node(Token.VAR);

    // Traverse the function node's parent node
    NodeTraversal.traverse(functionParent, traversal, null);

    // Check that the this node is reported when it's not annotated with @this
    NodeTraversal.traverse(functionThis, traversal, functionParent);
    assertReported(GLOBAL_THIS, functionThis);
  }

  @Test
  public void testShouldNotReportThisInFunctionWhereLogicalPlaceExists() {
    Node functionNode = new Node(Token.FUNCTION);
    Node thisNode = new Node(Token.THIS);

    Node functionParent = new Node(Token.VAR);
    Node prototypeProperty = new Node(Token.GETPROP);
    prototypeProperty.setLastChild(new Node(Token.STRING, "prototype"));

    Node prototypeFunction = new Node(Token.FUNCTION);
    prototypeFunction.setJSDocInfo(new JSDocInfo());
    prototypeFunction.setJSDocInfo(new JSDocInfo()).setConstructor(true);

    functionParent.addChildToEnd(prototypeProperty);
    functionParent.addChildToEnd(prototypeFunction);

    Node functionThisProperty = new Node(Token.GETPROP);
    functionThisProperty.setLastChild(new Node(Token.STRING, "prototype"));
    functionThisProperty.setLastChild(functionThisProperty.getLastChild().addSibling(new Node(Token.DOT)));
    functionThisProperty.setLastChild(functionThisProperty.getLastChild().addSibling(new Node(Token.STRING, "this")));

    Node functionThisAssignment = new Node(Token.ASSIGN);
    functionThisAssignment.setLhs(functionThisProperty);
    functionThisAssignment.setRhs(thisNode);

    Node functionParentThis = new Node(Token.THIS);

    // Traverse the function node's parent node
    NodeTraversal.traverse(functionParent, traversal, null);

    // Check that the this node is not reported when it's in a logical place (prototype property)
    NodeTraversal.traverse(functionParentThis, traversal, functionParent);
    NodeTraversal.traverse(functionThisAssignment, traversal, functionParentThis);
    assertNoReports(GLOBAL_THIS);
  }

  private void assertReported(DiagnosticType diagnostic, Node node) {
    NodeTraversal.traverse(node, traversal, null);
    assertThat(compiler.getDiagnostics()).hasSize(1);
    assertThat(compiler.getDiagnostics().get(0).getDiagnosticType()).isEqualTo(diagnostic);
  }

  private void assertNoReports(DiagnosticType diagnostic) {
    NodeTraversal.traverse(node, traversal, null);
    assertThat(compiler.getDiagnostics()).isEmpty();
  }
}