import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    String code = "function foo() {}";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_emptyFn"), null);
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testIdentityReducer() {
    String code = "function foo(x) { return x }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_identityFn"),
        Node.newString("x"));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testIdentityReducerWithWrongNumberOfArguments() {
    String code = "function foo(x, y) { return x }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertNull(actual);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    String code = "function foo() { return 10 }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_returnArg"),
        Node.newInt(10));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testReturnConstantReducerWithListOfArguments() {
    String code = "function foo(x, y) { return [x, y] }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_returnArg"),
        Node.newArray(
            Node.newInt(1),
            Node.newInt(2)));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testReturnConstantReducerWithSingleArgument() {
    String code = "function foo() { return [1] }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_returnArg"),
        Node.newInt(1));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGetterReducer() {
    String code = "function foo() { return this.x }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_get"),
        Node.newString("x"));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testSetterReducer() {
    String code = "function foo(x) { this.x = x }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_set"),
        Node.newString("x"));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testSetterReducerWithWrongNumberOfArguments() {
    String code = "function foo(x, y) { this.x = y }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertNull(actual);
  }

  @org.junit.Test
  public void testNoReduction() {
    String code = "function foo() { return 1 + 1 }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node actual = rootNode.getFirstChild();
    assertEquals(NodeUtil.getCodeSize(actual), 2);
  }

  @org.junit.Test
  public void testNoReductionWithHelperCode() {
    String code = "function foo() { return JSCompiler_emptyFn() }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_emptyFn"));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testReductionWithHelperCodeAndCodeChangeReported() {
    String code = "function foo() { return JSCompiler_emptyFn() }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_emptyFn"));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);

    compiler.reportCodeChange();
    List<Node> traversedNodes = NodeTraversal.traverse(
        compiler, rootNode, new NodeCounter());
    assertEquals(NodeUtil.getCodeSize(expected), traversedNodes.get(0).getCodeSize());
  }

  @org.junit.Test
  public void testReductionWithHelperCodeAndCodeChangeNotReported() {
    String code = "function foo() { return JSCompiler_emptyFn() }";
    Node rootNode = compiler.parse(code);
    functionRewriter.process(null, rootNode);

    Node expected = Node.newCall(
        Node.newString("JSCompiler_emptyFn"));
    Node actual = (Node) rootNode.getFirstChild().getLastChild();
    assertEquals(expected, actual);

    NodeTraversal.traverse(compiler, rootNode, new NodeCounter());
    List<Node> traversedNodes = NodeTraversal.traverse(
        compiler, rootNode, new NodeCounter());
    assertEquals(NodeUtil.getCodeSize(expected), traversedNodes.get(0).getCodeSize());
  }

  private static class NodeCounter implements NodeTraversal.Callback {
    int nodesTraversed = 0;

    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
      nodesTraversed++;
      return true;
    }

    @Override
    public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
    }

    int getNodesTraversed() {
      return nodesTraversed;
    }
  }
}