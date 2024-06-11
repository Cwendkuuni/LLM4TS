package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompiler;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Test class for RemoveConstantExpressions.
 */
public class RemoveConstantExpressionsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  /**
   * Test method for RemoveConstantExpressions constructor.
   */
  @Test
  public void testRemoveConstantExpressionsConstructor() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  /**
   * Test method for process method.
   */
  @Test
  public void testProcess() {
    String sourceCode = "function foo() { return 1; } function bar() { return 2; }"
        + "function test() { return 1 + foo() + bar(); }";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = AbstractCompiler.parseTree(sourceCode);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(externs, root);

    Node testNode = getTestNode(root);
    List<Node> expectedSideEffectNodes = Lists.newArrayList(
        new Node(Token.IDENT, "foo"),
        new Node(Token.IDENT, "bar")
    );

    List<Node> actualSideEffectNodes = getSideEffectNodes(testNode);
    assertTrue(expectedSideEffectNodes.equals(actualSideEffectNodes));
  }

  /**
   * Test method for trySimplify when no side effects.
   */
  @Test
  public void testTrySimplifyNoSideEffects() {
    String sourceCode = "function foo() { return 1; } function bar() { return 2; }"
        + "function test() { return 1 + foo() * bar(); }";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = AbstractCompiler.parseTree(sourceCode);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(null, root, removeConstantExpressions.new RemoveConstantRValuesCallback());

    Node testNode = getTestNode(root);
    List<Node> actualSideEffectNodes = getSideEffectNodes(testNode);
    assertFalse(actualSideEffectNodes.isEmpty());
  }

  /**
   * Test method for trySimplify when side effects.
   */
  @Test
  public void testTrySimplifySideEffects() {
    String sourceCode = "function foo() { print('foo'); return 1; } function bar() { print('bar'); return 2; }"
        + "function test() { return 1 + foo() + bar(); }";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = AbstractCompiler.parseTree(sourceCode);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(null, root, removeConstantExpressions.new RemoveConstantRValuesCallback());

    Node testNode = getTestNode(root);
    List<Node> expectedSideEffectNodes = Lists.newArrayList(
        new Node(Token.IDENT, "foo"),
        new Node(Token.IDENT, "bar")
    );

    List<Node> actualSideEffectNodes = getSideEffectNodes(testNode);
    assertTrue(expectedSideEffectNodes.equals(actualSideEffectNodes));
  }

  /**
   * Test method for getSideEffectNodes when no side effects.
   */
  @Test
  public void testGetSideEffectNodesNoSideEffects() {
    String sourceCode = "function foo() { return 1; } function test() { return 1 + foo(); }";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = AbstractCompiler.parseTree(sourceCode);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(null, root, removeConstantExpressions.new RemoveConstantRValuesCallback());

    Node testNode = getTestNode(root);
    List<Node> actualSideEffectNodes = getSideEffectNodes(testNode);
    assertFalse(actualSideEffectNodes.isEmpty());
  }

  /**
   * Test method for getSideEffectNodes when side effects.
   */
  @Test
  public void testGetSideEffectNodesSideEffects() {
    String sourceCode = "function foo() { print('foo'); } function test() { return 1 + foo(); }";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = AbstractCompiler.parseTree(sourceCode);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(null, root, removeConstantExpressions.new RemoveConstantRValuesCallback());

    Node testNode = getTestNode(root);
    List<Node> expectedSideEffectNodes = Lists.newArrayList(
        new Node(Token.IDENT, "foo")
    );

    List<Node> actualSideEffectNodes = getSideEffectNodes(testNode);
    assertTrue(expectedSideEffectNodes.equals(actualSideEffectNodes));
  }

  /**
   * Test method for ReportCodeHasChangedListener.
   */
  @Test (expected = AssertionError.class)
  public void testReportCodeHasChangedListener() {
    String sourceCode = "function foo() { return 1; } function test() { return 1 + foo(); }";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = AbstractCompiler.parseTree(sourceCode);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(null, root, removeConstantExpressions.new RemoveConstantRValuesCallback());

    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
  }

  private Node getTestNode(Node node) {
    while (node.getType() != Token.FUNCTION_DECL) {
      node = node.getParent();
    }
    return node;
  }

  private List<Node> getSideEffectNodes(Node node) {
    List<Node> sideEffectNodes = Lists.newArrayList();
    NodeTraversal.traverse(null, node, new SideEffectFinderCallback(sideEffectNodes));
    return sideEffectNodes;
  }

  /**
   * Callback to find nodes with side effects.
   */
  private static class SideEffectFinderCallback extends AbstractPostOrderCallback {
    private final List<Node> sideEffectNodes;

    private SideEffectFinderCallback(List<Node> sideEffectNodes) {
      this.sideEffectNodes = sideEffectNodes;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
        sideEffectNodes.add(node);
      }
    }
  }

  /**
   * Copies nodes from the given list.
   */
  private static class CopySideEffectSubexpressions implements GatherSideEffectSubexpressionsCallback {
    private final List<Node> sideEffectNodes;
    private final List<Node> copySideEffectNodes;

    private CopySideEffectSubexpressions(List<Node> sideEffectNodes, List<Node> copySideEffectNodes) {
      this.sideEffectNodes = sideEffectNodes;
      this.copySideEffectNodes = copySideEffectNodes;
    }

    @Override
    public void gatherSubexpressions(List<Node> subexpressions, Node node) {
      for (Node subexpression : subexpressions) {
        copySideEffectNodes.add(new Node(Token.EXPR_RESULT, subexpression));
      }
    }
  }
}