package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RemoveConstantExpressionsTest {
  private static final NodeFactory nodeFactory = new NodeFactory();
  private static final Node root = new Node(Token.ROOT, null);
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setUp() {
    removeConstantExpressions = new RemoveConstantExpressions(new NoOpCompiler());
  }

  @Test
  public void testProcess_EmptyRootNode() {
    Result result = new Result();
    removeConstantExpressions.process(null, root);
    assertTrue(result.changed);
    assertFalse(result.errors.isEmpty());
  }

  @Test
  public void testProcess_ConstantExpression() {
    Node constantExpression = new Node(Token.EXPR_CONST, 5);
    root.addChild(constantExpression);

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    assertTrue(result.changed);
    assertFalse(result.errors.isEmpty());
    Node simplifiedNode = result.changes.get(0).getReplaced();
    assertTrue(simplifiedNode instanceof Node);
    assertTrue(simplifiedNode.getFirstChild() instanceof Node);
    assertEquals(constantExpression.getFirstChild(),
        simplifiedNode.getFirstChild());
  }

  @Test
  public void testProcess_ExpressionWithSideEffects() {
    Node expressionWithSideEffects = new Node(Token.EXPR_ASSIGN, null);
    expressionWithSideEffects.addChild(new Node(Token.NAME, "foo"));
    expressionWithSideEffects.addChild(new Node(Token.EXPR_ASSIGN, null));
    expressionWithSideEffects.addChild(new Node(Token.NAME, "bar"));
    root.addChild(expressionWithSideEffects);

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    assertTrue(result.changed);
    assertFalse(result.errors.isEmpty());
    Node simplifiedNode = result.changes.get(0).getReplaced();
    assertTrue(simplifiedNode instanceof Node);
    assertTrue(simplifiedNode.getFirstChild() instanceof Node);
    assertTrue(simplifiedNode.getFirstChild().getType() == Token.EXPR_NAME);
    assertEquals("foo", simplifiedNode.getFirstChild().getString());
    Node nextSibling = simplifiedNode.getNextSibling();
    assertTrue(nextSibling instanceof Node);
    assertTrue(nextSibling.getType() == Token.EXPR_ASSIGN);
    assertTrue(nextSibling.getFirstChild() instanceof Node);
    assertTrue(nextSibling.getFirstChild().getType() == Token.EXPR_NAME);
    assertEquals("bar", nextSibling.getFirstChild().getString());
  }

  @Test
  public void testProcess_ComplexExpression() {
    Node complexExpression = new Node(Token.EXPR_ADD, null);
    complexExpression.addChild(new Node(Token.EXPR_CONST, 1));
    complexExpression.addChild(new Node(Token.EXPR_NAME, "foo"));
    complexExpression.addChild(new Node(Token.EXPR_ASSIGN, null));
    complexExpression.addChild(new Node(Token.EXPR_NAME, "bar"));
    complexExpression.addChild(new Node(Token.EXPR_SUB, null));
    complexExpression.addChild(new Node(Token.EXPR_CONST, 2));
    root.addChild(complexExpression);

    Result result = new Result();
    removeConstantExpressions.process(null, root);
    assertTrue(result.changed);
    assertFalse(result.errors.isEmpty());
    Node simplifiedNode = result.changes.get(0).getReplaced();
    assertTrue(simplifiedNode instanceof Node);
    assertTrue(simplifiedNode.getFirstChild() instanceof Node);
    assertTrue(simplifiedNode.getFirstChild().getType() == Token.EXPR_NAME);
    assertEquals("foo", simplifiedNode.getFirstChild().getString());
    Node nextSibling = simplifiedNode.getNextSibling();
    assertTrue(nextSibling instanceof Node);
    assertTrue(nextSibling.getType() == Token.EXPR_ASSIGN);
    assertTrue(nextSibling.getNextSibling() instanceof Node);
    assertTrue(nextSibling.getNextSibling().getType() == Token.EXPR_NAME);
    assertEquals("bar", nextSibling.getNextSibling().getString());
    Node nextNextSibling = nextSibling.getNextSibling();
    assertTrue(nextNextSibling instanceof Node);
    assertTrue(nextNextSibling.getType() == Token.EXPR_SUB);
    assertTrue(nextNextSibling.getFirstChild() instanceof Node);
    assertTrue(nextNextSibling.getFirstChild().getType() == Token.EXPR_CONST);
    assertEquals(2, nextNextSibling.getFirstChild().getInt());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_NullExterns() {
    removeConstantExpressions.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_NullRoot() {
    removeConstantExpressions.process(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullCompiler() {
    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(null);
    removeConstantExpressions.process(null, root);
  }

  @Test
  public void testGetSideEffectNodes_EmptyNode() {
    Node emptyNode = new Node(Token.EXPR_RESULT, null);
    List<Node> sideEffectNodes = new RemoveConstantRValuesCallback().
        getSideEffectNodes(emptyNode);
    assertTrue(sideEffectNodes.isEmpty());
  }

  @Test
  public void testGetSideEffectNodes_ConstantNode() {
    Node constantNode = new Node(Token.EXPR_CONST, 5);
    List<Node> sideEffectNodes = new RemoveConstantRValuesCallback().
        getSideEffectNodes(constantNode);
    assertTrue(sideEffectNodes.isEmpty());
  }

  @Test
  public void testGetSideEffectNodes_AssignNode() {
    Node assignNode = new Node(Token.EXPR_ASSIGN, null);
    List<Node> sideEffectNodes = new RemoveConstantRValuesCallback().
        getSideEffectNodes(assignNode);
    assertTrue(sideEffectNodes.isEmpty());
  }

  @Test
  public void testGetSideEffectNodes_ComplexNode() {
    Node complexNode = new Node(Token.EXPR_ADD, null);
    complexNode.addChild(new Node(Token.EXPR_CONST, 1));
    complexNode.addChild(new Node(Token.EXPR_NAME, "foo"));
    complexNode.addChild(new Node(Token.EXPR_ASSIGN, null));
    complexNode.addChild(new Node(Token.EXPR_NAME, "bar"));
    complexNode.addChild(new Node(Token.EXPR_SUB, null));
    complexNode.addChild(new Node(Token.EXPR_CONST, 2));
    List<Node> sideEffectNodes = new RemoveConstantRValuesCallback().
        getSideEffectNodes(complexNode);
    List<Node> expectedSideEffectNodes = Lists.newArrayList(
        new Node(Token.EXPR_NAME, "foo"),
        new Node(Token.EXPR_NAME, "bar")
    );
    assertEquals(expectedSideEffectNodes, sideEffectNodes);
  }
}