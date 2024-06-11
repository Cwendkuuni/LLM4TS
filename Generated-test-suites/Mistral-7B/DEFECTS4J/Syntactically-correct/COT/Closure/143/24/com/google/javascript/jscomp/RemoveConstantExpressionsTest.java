package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private RemoveConstantExpressions removeConstantExpressions;

  @org.junit.Before
  public void setUp() {
    this.compiler = new Compiler();
    this.nodeFactory = this.compiler.getFactory();
    this.removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @org.junit.Test
  public void testProcess_EmptyCode() {
    Result result = new Result();
    Node root = nodeFactory.createProgram();
    removeConstantExpressions.process(null, root);
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testProcess_SimpleExpression() {
    Result result = new Result();
    Node root = createNode("1 + 2");
    removeConstantExpressions.process(null, root);
    List<Node> actual = result.getSideEffectNodes();
    List<Node> expected = Lists.newArrayList(createNode("2"));
    assertTrue(result.changed);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess_ComplexExpression() {
    Result result = new Result();
    Node root = createNode("1 + foo() + bar()");
    removeConstantExpressions.process(null, root);
    List<Node> actual = result.getSideEffectNodes();
    List<Node> expected = Lists.newArrayList(createNode("foo()"), createNode("bar()"));
    assertTrue(result.changed);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess_ExpressionWithSideEffects() {
    Result result = new Result();
    Node root = createNode("console.log(1 + 2)");
    removeConstantExpressions.process(null, root);
    List<Node> actual = result.getSideEffectNodes();
    List<Node> expected = Lists.newArrayList(createNode("console.log(2)"));
    assertTrue(result.changed);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess_MultipleExpressions() {
    Result result = new Result();
    Node root = createNode("1 + foo() + bar() + 2 + baz()");
    removeConstantExpressions.process(null, root);
    List<Node> actual = result.getSideEffectNodes();
    List<Node> expected = Lists.newArrayList(createNode("foo()"), createNode("bar()"), createNode("baz()"));
    assertTrue(result.changed);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess_NestedExpressions() {
    Result result = new Result();
    Node root = createNode("(1 + foo()) * (2 + bar())");
    removeConstantExpressions.process(null, root);
    List<Node> actual = result.getSideEffectNodes();
    List<Node> expected = Lists.newArrayList(createNode("foo()"), createNode("bar()"));
    assertTrue(result.changed);
    assertEquals(expected, actual);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testProcess_NonExpressionNode() {
    Result result = new Result();
    Node root = createNode(Token.KEYWORD, "function");
    removeConstantExpressions.process(null, root);
  }

  private Node createNode(int type) {
    return createNode(type, null);
  }

  private Node createNode(int type, Node... children) {
    Node node = nodeFactory.createNode(type);
    if (children != null) {
      for (Node child : children) {
        node.appendChild(child);
      }
    }
    return node;
  }

  private void assertEquals(List<Node> expected, List<Node> actual) {
    for (int i = 0; i < expected.size(); i++) {
      Node expectedNode = expected.get(i);
      Node actualNode = actual.get(i);
      assertEquals(expectedNode, actualNode);
    }
  }

  private void assertEquals(Node expected, Node actual) {
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getFirstChild(), actual.getFirstChild());
    assertEquals(expected.getLastChild(), actual.getLastChild());
  }
}