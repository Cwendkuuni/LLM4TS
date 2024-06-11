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
import com.google.javascript.jscomp.RemoveConstantExpressions.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.test.base.ContextActionTestCase;

public class RemoveConstantExpressionsTest extends ContextActionTestCase {

  @Override
  protected CompilerOptions getOptions() {
    return new CompilerOptions(true, null);
  }

  public void testRemoveConstantExpressions() throws Exception {
    String input = "function foo() { return 1; }; " +
        "function bar() { return 2; }; " +
        "function baz() { return 3; } " +
        "function test() { " +
        "  return 1 + foo() + bar() + baz(); " +
        "}";

    Compiler compiler = new Compiler();
    ParallelCompiler parallelCompiler = new ParallelCompiler();
    parallelCompiler.addPass(new RemoveConstantExpressions(compiler));
    Node root = parse(input);
    Result result = parallelCompiler.run(root, getOptions());

    Node testFunction = findFunctionByName("test", root);
    Node testExpression = testFunction.getFirstChild();

    assertTrue(testExpression.getNumChildren() > 3);
    List<Node> sideEffectNodes = Lists.newArrayList();
    NodeTraversal.traverse(null, testExpression, new GatherSideEffectNodesCallback(sideEffectNodes));

    assertTrue(sideEffectNodes.size() == 3);
    for (int i = 0; i < sideEffectNodes.size(); i++) {
      Node sideEffectNode = sideEffectNodes.get(i);
      Node expectedNode = findFunctionByName("foo", root).getFirstChild();
      if (i == 0) {
        assertTrue(sideEffectNode.equals(expectedNode));
      } else if (i == 1) {
        expectedNode = findFunctionByName("bar", root).getFirstChild();
        assertTrue(sideEffectNode.equals(expectedNode));
      } else {
        expectedNode = findFunctionByName("baz", root).getFirstChild();
        assertTrue(sideEffectNode.equals(expectedNode));
      }
    }

    Node simplifiedTestExpression = testExpression.getLastChild();
    assertTrue(simplifiedTestExpression.getNumChildren() == 3);
    for (int i = 0; i < simplifiedTestExpression.getNumChildren(); i++) {
      Node child = simplifiedTestExpression.getChild(i);
      assertTrue(child.getType() == Token.CALL_EXPR);
    }
  }

  public void testRemoveConstantExpressionsNoSideEffects() throws Exception {
    String input = "function foo() { return 1; }; " +
        "function test() { " +
        "  return 1 + foo(); " +
        "}";

    Compiler compiler = new Compiler();
    ParallelCompiler parallelCompiler = new ParallelCompiler();
    parallelCompiler.addPass(new RemoveConstantExpressions(compiler));
    Node root = parse(input);
    Result result = parallelCompiler.run(root, getOptions());

    Node testFunction = findFunctionByName("test", root);
    Node testExpression = testFunction.getFirstChild();

    assertFalse(testExpression.getLastChild().getNumChildren() > 1);
  }

  private static class GatherSideEffectNodesCallback extends NodeTraversal.AbstractCallback {
    private final List<Node> sideEffectNodes;

    public GatherSideEffectNodesCallback(List<Node> sideEffectNodes) {
      this.sideEffectNodes = sideEffectNodes;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
        sideEffectNodes.add(node);
      }
    }
  }

  private static class GatherSideEffectSubexpressionsCallback extends NodeTraversal.AbstractCallback {
    private final List<Node> subexpressions;
    private final Node currentNode;

    public GatherSideEffectSubexpressionsCallback(Node currentNode, List<Node> subexpressions) {
      this.currentNode = currentNode;
      this.subexpressions = subexpressions;
    }

    public void copySideEffects(Node newNode) {
      NodeTraversal.traverse(this, newNode, new CopySideEffectSubexpressionsCallback(subexpressions));
    }

    private static class CopySideEffectSubexpressionsCallback extends NodeTraversal.AbstractCallback {
      private final List<Node> subexpressions;

      public CopySideEffectSubexpressionsCallback(List<Node> subexpressions) {
        this.subexpressions = subexpressions;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
          subexpressions.add(NodeUtil.cloneTree(node));
        }
      }
    }
  }
}