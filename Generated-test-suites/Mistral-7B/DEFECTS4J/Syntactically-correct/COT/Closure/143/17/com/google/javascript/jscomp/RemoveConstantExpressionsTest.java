package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantExpressions.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  @org.junit.Before
  public void setUp() {
    // Initialize your test objects here, if necessary
  }

  @org.junit.Test
  public void testProcess() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);

    Node root = NodeFactory.createProgram();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();

    NodeTraversal.traverse(null, root, cb);

    Result result = cb.getResult();
    assertTrue(result.changed);
  }

  @org.junit.Test
  public void testTrySimplifyWithNoSideEffectsExpression() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);

    Node root = NodeFactory.createProgram();
    Node exprBody = NodeFactory.createIntLit(1);
    Node node = NodeFactory.createExprStatement(
        NodeFactory.createBinaryOp(
            Token.OP_PLUS,
            NodeFactory.createUnaryOp(Token.OP_PLUS, exprBody),
            NodeFactory.createIdentifier("foo"),
            null
        ));
    root.addChild(node);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();

    NodeTraversal.traverse(null, root, cb);

    Result result = cb.getResult();
    List<Node> sideEffectNodes = result.getSideEffectNodes();
    assertEquals(0, sideEffectNodes.size());
  }

  @org.junit.Test
  public void testTrySimplifyWithSideEffectsExpression() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);

    Node root = NodeFactory.createProgram();
    Node exprBody = NodeFactory.createIdentifier("foo");
    Node node = NodeFactory.createExprStatement(
        NodeFactory.createBinaryOp(
            Token.OP_PLUS,
            NodeFactory.createUnaryOp(Token.OP_PLUS, NodeFactory.createIntLit(1)),
            exprBody,
            null
        ));
    root.addChild(node);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();

    NodeTraversal.traverse(null, root, cb);

    Result result = cb.getResult();
    List<Node> sideEffectNodes = result.getSideEffectNodes();
    assertEquals(1, sideEffectNodes.size());
    assertEquals(exprBody, sideEffectNodes.get(0));
  }

  @org.junit.Test
  public void testGetSideEffectNodesWithNoSubExpressions() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);

    Node node = NodeFactory.createIntLit(1);

    List<Node> sideEffectNodes = new RemoveConstantRValuesCallback().getSideEffectNodes(node);
    assertEquals(0, sideEffectNodes.size());
  }

  @org.junit.Test
  public void testGetSideEffectNodesWithSubExpressions() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);

    Node exprBody = NodeFactory.createIdentifier("foo");
    Node node = NodeFactory.createBinaryOp(Token.OP_PLUS, NodeFactory.createIntLit(1), exprBody);

    List<Node> sideEffectNodes = new RemoveConstantRValuesCallback().getSideEffectNodes(node);
    assertEquals(1, sideEffectNodes.size());
    assertEquals(exprBody, sideEffectNodes.get(0));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testVisitWithNonExpressionResultNode() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);

    Node root = NodeFactory.createProgram();
    Node node = NodeFactory.createIdentifier("foo");
    root.addChild(node);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();

    NodeTraversal.traverse(null, root, cb);

    try {
      cb.trySimplify(null, node);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}