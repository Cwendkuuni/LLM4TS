package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverse() {
    Node root = createNode("function test() { this.x = 0; }");
    Node functionNode = root.getLastChild();

    Node parent = createNode("var test = function test() { }");
    Node variableNode = parent.getLastChild();

    Node blockNode = createNode("{}");
    Node assignmentNode = createNode("var test = test;");

    Node prototypeNode = createNode("function test() { return this; }.prototype");;

    Node assignLhsChild1 = createNode("var a = this;");
    Node assignLhsChild2 = createNode("this.a = this;");

    Node getPropertyNode = createNode("var a = test.prototype.x;");

    when(compiler.getTraversal()).thenReturn(traversal);

    // Traverse top level function
    assertThat(checker.shouldTraverse(traversal, functionNode, null)).isTrue();
    // Traverse constructor function
    assertThat(checker.shouldTraverse(traversal, createNode("function Test() { }"), null)).isTrue();
    // Traverse non-constructor function with @this annotation
    assertThat(checker.shouldTraverse(traversal, createNode("@this class Test { }"), null)).isTrue();
    // Traverse function in a var statement
    assertThat(checker.shouldTraverse(traversal, variableNode, null)).isTrue();
    // Traverse function in a assignment
    assertThat(checker.shouldTraverse(traversal, assignmentNode, null)).isTrue();
    // Don't traverse functions inside prototype properties
    assertThat(checker.shouldTraverse(traversal, prototypeNode, null)).isFalse();
    // Don't traverse functions inside assignment to prototype property
    assertThat(checker.shouldTraverse(traversal, getPropertyNode, null)).isFalse();
    // Don't traverse functions inside assignments to non-prototype properties
    assertThat(checker.shouldTraverse(traversal, createNode("var a = this;"), null)).isFalse();
    // Don't traverse functions inside assignments to object properties
    assertThat(checker.shouldTraverse(traversal, assignLhsChild1, null)).isFalse();
    // Don't traverse functions inside assignments to object property with this on the left side
    assertThat(checker.shouldTraverse(traversal, assignLhsChild2, null)).isFalse();
  }

  @Test
  public void testVisit() {
    Node root = createNode("function test() { this.x = 0; }");
    Node functionNode = root.getLastChild();

    when(compiler.getTraversal()).thenReturn(new NodeTraversal() {
      @Override
      public void traverse(Node node, Callback next) {
        checker.visit(this, node, null);
        next.traverse(node, next);
      }
    });

    checker.traverse(traversal, root, null);
    assertThat(compiler.reportedErrorsCount()).isEqualTo(1);
  }

  @Test
  public void testVisitMultiple() {
    Node root = createNode("function test() { this.x = 0; } function test2() { this.x = 0; }");

    when(compiler.getTraversal()).thenReturn(new NodeTraversal() {
      int count = 0;

      @Override
      public void traverse(Node node, Callback next) {
        if (node.getType() == Token.THIS) {
          count++;
          checker.visit(this, node, null);
        }
        next.traverse(node, next);
      }

      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }
    });

    checker.traverse(traversal, root, null);
    assertThat(compiler.reportedErrorsCount()).isEqualTo(2);
  }

  private Node createNode(String code) {
    RhinoCompilerOptions options = new RhinoCompilerOptions();
    return options.parseTree(code, null);
  }
}