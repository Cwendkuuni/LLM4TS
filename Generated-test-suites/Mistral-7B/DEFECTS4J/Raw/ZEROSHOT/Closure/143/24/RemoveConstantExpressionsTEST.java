package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.test.CompilerTestHelper;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class RemoveConstantExpressionsTest {
  @org.junit.Test
  public void testRemoveConstantExpressions() {
    String sourceCode = "function foo() { return 1; }"
        + "function bar() { return 2; }"
        + "function baz() { return 3; }"
        + "function main() {"
        + " 1 + foo() + bar() + baz();"
        + "}"
        + "main();";

    Compiler compiler = CompilerTestHelper.createCompiler();
    ParallelCompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);

    Node ast = compiler.parseTree(sourceCode);
    NodeTraversal.traverse(null, ast, removeConstantExpressions);

    List<Node> expected = NodeFactory.createList(
        NodeFactory.createName("foo"),
        NodeFactory.createName("bar"),
        NodeFactory.createName("baz")
    );

    Node actual = getExpressionList(ast.getFirstChild());

    assertEquals(expected, actual);
  }

  private List<Node> getExpressionList(Node node) {
    List<Node> result = Lists.newArrayList();
    NodeTraversal.traverse(null, node, new ExpressionCollectorCallback(result));
    return result;
  }

  /**
   * Used to collect expressions into a list.
   */
  static class ExpressionCollectorCallback extends NodeTraversal.AbstractCallback {
    private final List<Node> expressions;

    public ExpressionCollectorCallback(List<Node> expressions) {
      this.expressions = expressions;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (node.getType() == Token.EXPR_STATEMENT) {
        expressions.add(node.getFirstChild());
      }
    }
  }
}