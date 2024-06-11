package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test.testType.TestCompiler;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Test class for RemoveConstantExpressions.
 */
public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  /**
   * Test method for RemoveConstantExpressions#process().
   */
  @Test
  public void testProcess() {
    Node node = new Node("1 + foo() + bar()");
    removeConstantExpressions.process(null, node);
    List<Node> actual = NodeUtil.getChildrenList(node);
    List<Node> expected = Lists.newArrayList(new Node("foo()"), new Node("bar()"));
    assertTrue(NodeUtil.areNodesEquivalent(actual, expected));
  }

  /**
   * Test method for RemoveConstantExpressions#trySimplify() with no side effect node.
   */
  @Test
  public void testTrySimplifyNoSideEffect() {
    Node parent = new Node("1 +");
    Node node = new Node("1");
    removeConstantExpressions.trySimplify(parent, node);
    Node actual = parent.getFirstChild();
    Node expected = node;
    assertTrue(NodeUtil.areNodesEquivalent(actual, expected));
  }

  /**
   * Test method for RemoveConstantExpressions#trySimplify() with side effect node.
   */
  @Test
  public void testTrySimplifySideEffect() {
    Node parent = new Node("1 +");
    Node node = new Node("console.log('Hello')");
    removeConstantExpressions.trySimplify(parent, node);
    Node actual = parent.getFirstChild();
    // Since we cannot create a Node representing side effect, we cannot compare
    // them directly here. Instead, we test the result of process method.
    Node root = new Node("console.log('Hello') + 1");
    removeConstantExpressions.process(null, root);
    List<Node> actualProcessResult = NodeUtil.getChildrenList(root);
    List<Node> expectedProcessResult = Lists.newArrayList(new Node("console.log('Hello')"), new Node("1"));
    assertTrue(NodeUtil.areNodesEquivalent(actualProcessResult, expectedProcessResult));
  }

  /**
   * Test method for RemoveConstantExpressions#getSideEffectNodes() with no side effect subexpressions.
   */
  @Test
  public void testGetSideEffectNodesNoSideEffect() {
    Node node = new Node("1 + 2");
    List<Node> actual = removeConstantExpressions.getSideEffectNodes(node).getResult();
    List<Node> expected = Lists.newArrayList();
    assertTrue(NodeUtil.areNodesEquivalent(actual, expected));
  }

  /**
   * Test method for RemoveConstantExpressions#getSideEffectNodes() with side effect subexpressions.
   */
  @Test
  public void testGetSideEffectNodesSideEffect() {
    Node node = new Node("console.log('Hello') + 1");
    List<Node> actual = removeConstantExpressions.getSideEffectNodes(node).getResult();
    List<Node> expected = Lists.newArrayList(new Node("console.log('Hello')"));
    assertTrue(NodeUtil.areNodesEquivalent(actual, expected));
  }
}

class NodeUtil {
  public static List<Node> getChildrenList(Node node) {
    List<Node> list = Lists.newArrayList();
    Node current = node;
    while (current != null) {
      list.add(current);
      current = current.getNextSibling();
    }
    return list;
  }

  public static boolean areNodesEquivalent(List<Node> first, List<Node> second) {
    if (first.size() != second.size()) {
      return false;
    }
    for (int i = 0; i < first.size(); i++) {
      if (!NodeUtil.areNodesEquivalent(first.get(i), second.get(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean areNodesEquivalent(Node first, Node second) {
    if (first.getType() != second.getType()) {
      return false;
    }
    if (first.getFirstChild() != null && second.getFirstChild() != null) {
      return areNodesEquivalent(first.getFirstChild(), second.getFirstChild());
    }
    return first.getText().equals(second.getText());
  }
}