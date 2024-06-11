package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMakeLocalNamesUnique() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode(
        "test",
        Lists.newArrayList(
            NodeUtil.newParamNode("x", NodeUtil.newIntLit(1)),
            NodeUtil.newParamNode("y", NodeUtil.newIntLit(2))),
        NodeUtil.newBlock(
            NodeUtil.newVarNode("a", NodeUtil.newIntLit(3)),
            NodeUtil.newReturnNode(NodeUtil.newIntLit(4))));

    mutator.makeLocalNamesUnique(fnNode, false);

    assertEquals(2, fnNode.getChildCount());
    Node body = fnNode.getChild(1);
    assertEquals(1, body.getChildCount());
    Node statement = body.getChild(0);
    assertEquals("a", statement.getFirstChild().getString());
  }

  @Test
  public void testReplaceReturns() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode(
        "test",
        Lists.newArrayList(),
        NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newIntLit(4))));

    String labelName = "testLabel";
    Node resultNode = mutator.replaceReturns(fnNode, "result", labelName, false);

    assertEquals(3, fnNode.getChildCount());
    Node body = fnNode.getChild(1);
    assertEquals(2, body.getChildCount());
    Node statement = body.getChild(0);
    assertEquals("result", statement.getFirstChild().getString());
    Node breakNode = body.getChild(1);
    assertEquals(Token.BREAK, breakNode.getType());
  }

  @Test
  public void testReplaceReturnsWithResult() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode(
        "test",
        Lists.newArrayList(),
        NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newIntLit(4))));

    String labelName = "testLabel";
    String resultName = "result";
    Node resultNode = mutator.replaceReturns(fnNode, resultName, labelName, true);

    assertEquals(3, fnNode.getChildCount());
    Node body = fnNode.getChild(1);
    assertEquals(3, body.getChildCount());
    Node statement1 = body.getChild(0);
    assertEquals("result", statement1.getFirstChild().getString());
    Node statement2 = body.getChild(1);
    assertEquals("result", statement2.getFirstChild().getString());
    Node breakNode = body.getChild(2);
    assertEquals(Token.BREAK, breakNode.getType());
    assertEquals("testLabel", breakNode.getLastChild().getString());
  }

  @Test
  public void testReplaceReturnsNull() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode(
        "test",
        Lists.newArrayList(),
        NodeUtil.newBlock(NodeUtil.newReturnNode(null)));

    String labelName = "testLabel";
    Node resultNode = mutator.replaceReturns(fnNode, null, labelName, false);

    assertEquals(2, fnNode.getChildCount());
    Node body = fnNode.getChild(1);
    assertEquals(1, body.getChildCount());
    Node statement = body.getChild(0);
    assertEquals("testLabel", statement.getFirstChild().getString());
  }
}