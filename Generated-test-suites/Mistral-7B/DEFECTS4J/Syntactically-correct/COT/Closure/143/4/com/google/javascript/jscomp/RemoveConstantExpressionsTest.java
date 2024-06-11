package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantExpressions.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.test.TestingCompiler;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    TestingCompiler compiler = new TestingCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    String input = "function foo() { return 1; } function bar() { return 2; }"
        + " function test() { return 1 + foo() + bar(); }";
    Result result = compiler.compile(input, pass);

    assertFalse(result.hadErrors());
    Node root = result.getAST();
    Node testFunction = findNodeByUniqueName(root, "test");
    assertNull(testFunction.getFirstChild());
    assertTrue(testFunction.getLastChild().isNodeType(Token.PUNCTUATION)
        && testFunction.getLastChild().getNodeType() == Token.SEMICOLON);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffects() {
    TestingCompiler compiler = new TestingCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    String input = "function foo() { console.log('Foo'); return 1; } function bar() {"
        + " console.log('Bar'); return 2; } function test() { return 1 + foo() + bar(); }";
    Result result = compiler.compile(input, pass);

    assertFalse(result.hadErrors());
    Node root = result.getAST();
    Node testFunction = findNodeByUniqueName(root, "test");
    List<Node> expectedSideEffects = Lists.newArrayList(
        findNodeByUniqueName(root, "foo"),
        findNodeByUniqueName(root, "bar")
    );
    assertTrue(testFunction.getChildren().containsAll(expectedSideEffects));
  }

  @Test(expected = AssertionError.class)
  public void testRemoveConstantExpressionsWithErrors() {
    TestingCompiler compiler = new TestingCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    String input = "function foo() { return 1; } function bar() {"
        + " return 1 + 2; // Syntax error } function test() { return 1 + foo() + bar(); }";
    Result result = compiler.compile(input, pass);

    assertTrue(result.hadErrors());
  }

  private Node findNodeByUniqueName(Node node, String name) {
    if (node.getUniqueName().equals(name)) {
      return node;
    }
    for (Node child : node.getChildren()) {
      Node result = findNodeByUniqueName(child, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static class GatherSideEffectSubexpressionsCallback
      extends AbstractPostOrderCallback {
    private Node parent;
    private Node subexpressionsContainer;

    private GatherSideEffectSubexpressionsCallback(Node parent,
        Node subexpressionsContainer) {
      this.parent = parent;
      this.subexpressionsContainer = subexpressionsContainer;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
        subexpressionsContainer.addChild(node);
      }
    }
  }

  private static class CopySideEffectSubexpressions
      extends GatherSideEffectSubexpressionsCallback {
    private List<Node> sideEffects;

    private CopySideEffectSubexpressions(Node parent, List<Node> sideEffects) {
      super(parent, null);
      this.sideEffects = sideEffects;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      super.visit(traversal, node, parent);
      sideEffects.add(node);
    }
  }
}