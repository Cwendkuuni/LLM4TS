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
import com.google.javascript.jscomp.test.TestCompiler;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for RemoveConstantExpressions.
 */
public class RemoveConstantExpressionsTest {

  private Compiler compiler;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(null);
    this.compiler = new TestCompiler(options);
  }

  @Test
  public void testExpressionStatements() {
    String jsCode = "function foo() {} function bar() {} "
        + "1 + foo() + bar();";

    Node root = compiler.parseTree(jsCode);
    ParallelCompiler.runPasses(root, new RemoveConstantExpressions(compiler));

    Node expectedRoot = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.FUNCTION, Lists.newArrayList(
            new Node(Token.FUNCTION_DECLARATION, null, "foo"),
            null,
            new Node(Token.LEFT_BRACE, Lists.newArrayList()),
            null,
            new Node(Token.RIGHT_BRACE, Lists.newArrayList())
        )),
        new Node(Token.FUNCTION, Lists.newArrayList(
            new Node(Token.FUNCTION_DECLARATION, null, "bar"),
            null,
            new Node(Token.LEFT_BRACE, Lists.newArrayList()),
            null,
            new Node(Token.RIGHT_BRACE, Lists.newArrayList())
        )),
        new Node(Token.EXPR_STATEMENT, null, null,
            new Node(Token.IDENTIFIER, null, "foo"))));

    NodeTraversal.traverse(null, root, new NodeAssert(expectedRoot));
  }

  @Test
  public void testNoSideEffectExpression() {
    String jsCode = "function foo() {} "
        + "1 + foo();";

    Node root = compiler.parseTree(jsCode);
    ParallelCompiler.runPasses(root, new RemoveConstantExpressions(compiler));

    Node expectedRoot = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.FUNCTION_DECLARATION, null, "foo"),
        null,
        new Node(Token.EXPR_STATEMENT, null, null,
            new Node(Token.IDENTIFIER, null, "foo"))));

    NodeTraversal.traverse(null, root, new NodeAssert(expectedRoot));
  }

  @Test
  public void testSideEffectExpression() {
    String jsCode = "function foo() { document.write('foo'); } "
        + "1 + foo();";

    Node root = compiler.parseTree(jsCode);
    ParallelCompiler.runPasses(root, new RemoveConstantExpressions(compiler));

    Node expectedRoot = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.FUNCTION_DECLARATION, null, "foo"),
        null,
        new Node(Token.EXPR_STATEMENT, null, null,
            new Node(Token.IDENTIFIER, null, "foo"))));

    NodeTraversal.traverse(null, root, new NodeAssert(expectedRoot));

    assertTrue(Compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testNoSideEffectSubexpressions() {
    String jsCode = "function foo() {} "
        + "1 + foo().length;";

    Node root = compiler.parseTree(jsCode);
    ParallelCompiler.runPasses(root, new RemoveConstantExpressions(compiler));

    Node expectedRoot = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.FUNCTION_DECLARATION, null, "foo"),
        null,
        new Node(Token.EXPR_STATEMENT, null, null,
            new Node(Token.IDENTIFIER, null, "foo"),
            new Node(Token.MEMBER, null, null, "length")),
            null));

    NodeTraversal.traverse(null, root, new NodeAssert(expectedRoot));
  }

  @Test
  public void testNoChange() {
    String jsCode = "function foo() {} "
        + "1;";

    Node root = compiler.parseTree(jsCode);
    ParallelCompiler.runPasses(root, new RemoveConstantExpressions(compiler));

    Node expectedRoot = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.FUNCTION_DECLARATION, null, "foo"),
        null,
        new Node(Token.EXPR_STATEMENT, null, null,
            new Node(Token.NUMBER_LITERAL, null, "1"))));

    NodeTraversal.traverse(null, root, new NodeAssert(expectedRoot));
  }

  private static class NodeAssert implements NodeTraversal.Callback {

    private final Node expectedRoot;

    NodeAssert(Node expectedRoot) {
      this.expectedRoot = expectedRoot;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (node.equals(expectedRoot)) {
        assertTrue("Expected root node to be equal to the given root node.",
            true);
        NodeTraversal.traverse(null, node, this::assertNode);
      }
    }

    private void assertNode(NodeTraversal traversal, Node node, Node parent) {
      Node expectedNode = null;
      if (node.getType() != Token.NULL) {
        expectedNode = expectedRoot.getChild(
            expectedRoot.getChildIndex(node));
      }

      if (expectedNode == null) {
        assertTrue("Unexpected node encountered.", false);
        return;
      }

      NodeUtil.assertNodeEquals(node, expectedNode);
    }
  }
}