package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestUtils;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class FunctionRewriterTest {

  private NodeFactory nodeFactory = ParserTestUtils.newNodeFactory();
  private Node root;

  @Test
  public void testEmptyFunctionReduction() {
    root = (Node) ParserTestUtils.parseJs(
        nodeFactory,
        "function foo() {}",
        0,
        0);

    FunctionRewriter rewriter = new FunctionRewriter(new TestCompiler());
    rewriter.process(null, root);

    Node expected = nodeFactory.ensureProperty(root, "foo");
    Node actual = findNode(expected);
    assertThat(actual).isInstanceOf(Node.Call.class);
    Node helper = ((Node.Call) actual).getFirstChild();
    assertThat(helper.getString()).isEqualTo("JSCompiler_emptyFn");
  }

  @Test
  public void testIdentityFunctionReduction() {
    root = (Node) ParserTestUtils.parseJs(
        nodeFactory,
        "function foo(x) { return x; }",
        0,
        0);

    FunctionRewriter rewriter = new FunctionRewriter(new TestCompiler());
    rewriter.process(null, root);

    Node expected = findNode(root, "foo");
    Node actual = findNode(expected);
    assertThat(actual).isInstanceOf(Node.Call.class);
    Node helper = ((Node.Call) actual).getFirstChild();
    assertThat(helper.getString()).isEqualTo("JSCompiler_identityFn");
  }

  @Test
  public void testGetPropertyReduction() {
    root = (Node) ParserTestUtils.parseJs(
        nodeFactory,
        "function foo() { return this.a; }",
        0,
        0);

    FunctionRewriter rewriter = new FunctionRewriter(new TestCompiler());
    rewriter.process(null, root);

    Node expected = findNode(root, "foo");
    Node actual = findNode(expected);
    assertThat(actual).isInstanceOf(Node.Call.class);
    Node helper = ((Node.Call) actual).getFirstChild();
    assertThat(helper.getString()).isEqualTo("JSCompiler_get");
    Node prop = ((Node.Call) helper.getLastChild()).getFirstChild();
    assertThat(prop.getString()).isEqualTo("a");
  }

  @Test
  public void testSetPropertyReduction() {
    root = (Node) ParserTestUtils.parseJs(
        nodeFactory,
        "function foo(x) { this.a = x; }",
        0,
        0);

    FunctionRewriter rewriter = new FunctionRewriter(new TestCompiler());
    rewriter.process(null, root);

    Node expected = findNode(root, "foo");
    Node actual = findNode(expected);
    assertThat(actual).isInstanceOf(Node.Call.class);
    Node helper = ((Node.Call) actual).getFirstChild();
    assertThat(helper.getString()).isEqualTo("JSCompiler_set");
    Node prop = ((Node.Call) helper.getLastChild()).getFirstChild();
    assertThat(prop.getString()).isEqualTo("a");
  }

  @Test
  public void testReturnConstantReduction() {
    root = (Node) ParserTestUtils.parseJs(
        nodeFactory,
        "function foo() { return 10; }",
        0,
        0);

    FunctionRewriter rewriter = new FunctionRewriter(new TestCompiler());
    rewriter.process(null, root);

    Node expected = findNode(root, "foo");
    Node actual = findNode(expected);
    assertThat(actual).isInstanceOf(Node.Call.class);
    Node helper = ((Node.Call) actual).getFirstChild();
    assertThat(helper.getString()).isEqualTo("JSCompiler_returnArg");
    Node value = ((Node.Call) helper.getLastChild()).getFirstChild();
    assertThat(value.getDouble().intValue()).isEqualTo(10);
  }

  @Test
  public void testNoReduction() {
    root = (Node) ParserTestUtils.parseJs(
        nodeFactory,
        "function foo() { return Math.random(); }",
        0,
        0);

    FunctionRewriter rewriter = new FunctionRewriter(new TestCompiler());
    rewriter.process(null, root);

    Node expected = findNode(root, "foo");
    Node actual = findNode(expected);
    assertThat(actual).isEqualTo(expected);
  }

  private Node findNode(Node node, String name) {
    return NodeTraversal.findNode(node, new NodeNameMatcher(name));
  }

  private Node findNode(Node node) {
    return findNode(node, node.getQualifiedName());
  }

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public boolean isOptimizeFunctionCallChains() {
      return false;
    }

    @Override
    public boolean isOptimizeSquashing() {
      return false;
    }

    @Override
    public boolean isOptimizeFunctionCalls() {
      return false;
    }

    @Override
    public boolean isOptimizeInlining() {
      return false;
    }

    @Override
    public boolean isOptimizeSideEffectFree() {
      return false;
    }

    @Override
    public boolean isOptimizeRewritingTypeOf() {
      return false;
    }

    @Override
    public boolean isOptimizeCoercion() {
      return false;
    }

    @Override
    public boolean isOptimizeExpressionConsolidation() {
      return false;
    }

    @Override
    public boolean isOptimizeAst() {
      return false;
    }

    @Override
    public boolean isOptimizeDeadCodeRemoval() {
      return false;
    }

    @Override
    public boolean isOptimizeAlpha() {
      return false;
    }

    @Override
    public boolean isOptimizeRho() {
      return false;
    }

    @Override
    public boolean isOptimizeClosure() {
      return false;
    }

    @Override
    public boolean isOptimizeNameMangling() {
      return false;
    }

    @Override
    public boolean isOptimizeTypeInference() {
      return false;
    }

    @Override
    public boolean isOptimizeTypeChecking() {
      return false;
    }

    @Override
    public boolean isOptimizeTypeEquality() {
      return false;
    }

    @Override
    public boolean isOptimizeTypeCoercion() {
      return false;
    }

    @Override
    public boolean isOptimizeTypePreservation() {
      return false;
    }

    @Override
    public boolean isOptimizeTypeAnnotations() {
      return false;
    }

    @Override
    public void process(Node externs, Node root) {
      // Empty implementation.
    }
  }

  private static class NodeNameMatcher implements Callback<Node> {
    private final String name;

    NodeNameMatcher(String name) {
      this.name = name;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
      return node.getQualifiedName().equals(name);
    }
  }
}