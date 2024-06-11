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
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(new CheckGlobalThis(compiler, CheckLevel.WARNING));
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = new Node(Token.SCRIPT, null, null);
    Node global = new Node(Token.NAME, "this", null);
    root.setFirstChild(global);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionContext() {
    Node root = createFunctionNode("function foo() { this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorContext() {
    Node root = createFunctionNode("function Foo() { this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotationContext() {
    Node root = createFunctionNode("@this function foo() { this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotationContext() {
    Node root = createFunctionNode("function foo() { @override this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseNonFunctionContext() {
    Node root = new Node(Token.NUMBER, 1, null);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.THIS, null, null);
    Node right = new Node(Token.NUMBER, 1, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, "foo.prototype", null);
    Node right = new Node(Token.THIS, null, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubProperty() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, "foo.prototype.bar", null);
    Node right = new Node(Token.THIS, null, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isFalse();
  }

  @Test
  public void testShouldTraverseRightSideOfAssignment() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, "foo", null);
    Node right = new Node(Token.THIS, null, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    assertThat(checker.shouldTraverse(t, root, null)).isTrue();
  }

  @Test
  public void testShouldReportThisInFunctionContext() {
    Node root = createFunctionNode("function foo() { this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = root.getFirstChild();
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(1);
  }

  @Test
  public void testShouldNotReportThisInConstructorContext() {
    Node root = createFunctionNode("function Foo() { this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = root.getFirstChild();
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  @Test
  public void testShouldNotReportThisInFunctionWithThisAnnotationContext() {
    Node root = createFunctionNode("@this function foo() { this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = root.getFirstChild();
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  @Test
  public void testShouldNotReportThisInFunctionWithOverrideAnnotationContext() {
    Node root = createFunctionNode("function foo() { @override this.bar = 1; }");

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = root.getFirstChild();
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  @Test
  public void testShouldNotReportThisInNonFunctionContext() {
    Node root = new Node(Token.NUMBER, 1, null);
    checker.visit(traversal, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  @Test
  public void testShouldReportThisInLeftSideOfAssignment() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.THIS, null, null);
    Node right = new Node(Token.NUMBER, 1, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = root.getFirstChild();
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(1);
  }

  @Test
  public void testShouldNotReportThisInRightSideOfAssignmentToPrototypeProperty() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, "foo.prototype", null);
    Node right = new Node(Token.THIS, null, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = right;
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  @Test
  public void testShouldNotReportThisInRightSideOfAssignmentToPrototypeSubProperty() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, "foo.prototype.bar", null);
    Node right = new Node(Token.THIS, null, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = right;
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  @Test
  public void testShouldNotReportThisInRightSideOfAssignment() {
    Node root = new Node(Token.ASSIGN, null, null);
    Node left = new Node(Token.NAME, "foo", null);
    Node right = new Node(Token.THIS, null, null);
    root.setFirstChild(left);
    left.setNext(right);

    NodeTraversal t = NodeTraversal.create(checker);
    t.traverse(root);
    Node thisNode = right;
    when(compiler.report(t.makeError(thisNode, checker.level, checker.GLOBAL_THIS)))
        .thenReturn(null);

    checker.visit(t, root, null);
    assertThat(compiler.reportCount()).isEqualTo(0);
  }

  private Node createFunctionNode(String code) {
    return NodeUtil.createFromCode(code, null, null, null, null);
  }
}