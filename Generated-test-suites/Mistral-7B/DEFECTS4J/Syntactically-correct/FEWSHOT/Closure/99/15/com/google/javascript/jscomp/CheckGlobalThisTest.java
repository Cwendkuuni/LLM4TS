package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.WarningLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    CheckLevel level = new CheckLevel(WarningLevel.ALL);
    traversal = NodeTraversal.create(compiler, level);
    checker = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldNotTraverseImplicitConstructor() {
    Node constructorNode = createNode(Token.FUNCTION, "MyClass", null, null);
    Node parent = createNode(Token.SCRIPT, null, null);
    Node grandParent = createNode(Token.SCRIPT, null, null);

    when(compiler.getGlobalScope()).thenReturn(grandParent);
    when(grandParent.getFirstChild()).thenReturn(parent);
    when(parent.getFirstChild()).thenReturn(constructorNode);

    Node nodeToTraverse = createNode(Token.THIS, null, constructorNode, null);

    boolean result = checker.shouldTraverse(NodeTraversal.TRAVERSAL, nodeToTraverse, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node constructorNode = createNode(Token.FUNCTION, "MyClass", null, null);
    Node jsDocInfo = createNode(Token.CONSTRUCTOR, null, null, createNode(Token.ANNOTATION, "@this", null, null, null));
    Node jsDocInfoParent = createNode(Token.NAME, null, null, jsDocInfo);

    Node parent = createNode(Token.SCRIPT, null, null);
    Node grandParent = createNode(Token.SCRIPT, null, null);

    when(compiler.getGlobalScope()).thenReturn(grandParent);
    when(grandParent.getFirstChild()).thenReturn(parent);
    when(parent.getFirstChild()).thenReturn(constructorNode);
    when(constructorNode.getFirstChild()).thenReturn(jsDocInfoParent);

    Node nodeToTraverse = createNode(Token.THIS, null, constructorNode, null);

    boolean result = checker.shouldTraverse(NodeTraversal.TRAVERSAL, nodeToTraverse, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraversePrototypeProperty() {
    Node objNode = createNode(Token.NAME, "obj", null, null);
    Node prototypeNode = createNode(Token.NAME, "prototype", null, null);
    Node getPropNode = createNode(Token.GETPROP, null, objNode, prototypeNode);

    Node parent = createNode(Token.SCRIPT, null, null);
    Node nodeToTraverse = createNode(Token.THIS, null, null, null);

    when(parent.getFirstChild()).thenReturn(nodeToTraverse);
    when(nodeToTraverse.getNext()).thenReturn(getPropNode);

    boolean result = checker.shouldTraverse(NodeTraversal.TRAVERSAL, nodeToTraverse, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraversePrototypeSubProperty() {
    Node objNode = createNode(Token.NAME, "obj", null, null);
    Node prototypeNode = createNode(Token.NAME, "prototype", null, null);
    Node getPropNode = createNode(Token.GETProp, null, objNode, createNode(Token.NAME, "subProperty", null, null));

    Node parent = createNode(Token.SCRIPT, null, null);
    Node nodeToTraverse = createNode(Token.THIS, null, null, null);

    when(parent.getFirstChild()).thenReturn(nodeToTraverse);
    when(nodeToTraverse.getNext()).thenReturn(getPropNode);

    boolean result = checker.shouldTraverse(NodeTraversal.TRAVERSAL, nodeToTraverse, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseAssignmentToThis() {
    Node parent = createNode(Token.ASSIGN, null, null, null);
    Node lhs = createNode(Token.THIS, null, null, null);
    Node rhs = createNode(Token.NUMBER, "42", null, null);

    Node nodeToTraverse = createNode(Token.THIS, null, null, null);

    when(parent.getFirstChild()).thenReturn(nodeToTraverse);
    when(parent.getLastChild()).thenReturn(rhs);

    boolean result = checker.shouldTraverse(NodeTraversal.TRAVERSAL, nodeToTraverse, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportDangerousThisUse() {
    Node parent = createNode(Token.SCRIPT, null, null);
    Node nodeToTraverse = createNode(Token.THIS, null, null, null);

    Diagnostics diagnostics = traverseAndGetDiagnostics(parent, nodeToTraverse);

    assertThat(diagnostics.getNumberOfErrors()).isEqualTo(1);
    assertThat(diagnostics.getError(0).getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testShouldNotReportSafeThisUse() {
    Node parent = createNode(Token.SCRIPT, null, null);
    Node constructorNode = createNode(Token.FUNCTION, "MyClass", null, null);
    Node jsDocInfo = createNode(Token.CONSTRUCTOR, null, null, createNode(Token.ANNOTATION, "@constructor", null, null, null));
    Node jsDocInfoParent = createNode(Token.NAME, null, null, jsDocInfo);

    Node nodeToTraverse = createNode(Token.THIS, null, constructorNode, null);

    when(parent.getFirstChild()).thenReturn(constructorNode);
    when(constructorNode.getFirstChild()).thenReturn(jsDocInfoParent);

    Diagnostics diagnostics = traverseAndGetDiagnostics(parent, nodeToTraverse);

    assertThat(diagnostics.getNumberOfErrors()).isEqualTo(0);
  }

  private Node createNode(int type, String name, Node... children) {
    Node node = new Node();
    node.setType(type);
    node.setName(name);
    if (children != null && children.length > 0) {
      node.addChildren(children);
    }
    return node;
  }

  private Diagnostics traverseAndGetDiagnostics(Node parent, Node nodeToTraverse) {
    checker.traverse(NodeTraversal.TRAVERSAL, parent, null);
    return compiler.getDiagnostics();
  }
}