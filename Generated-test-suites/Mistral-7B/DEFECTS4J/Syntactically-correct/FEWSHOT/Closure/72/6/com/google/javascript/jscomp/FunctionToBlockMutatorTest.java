package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMakeLocalNamesUnique() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    Node fnNode = NodeUtil.newFunctionNode("test", Lists.newArrayList(), null, Lists.newArrayList(), NodeUtil.newBlock(
        NodeUtil.newVarNode("x", NodeUtil.newNumberNode(1)),
        NodeUtil.newVarNode("y", NodeUtil.newNumberNode(2)),
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(3))
    ));

    mutator.makeLocalNamesUnique(fnNode, false);

    Node newFnNode = fnNode.getFirstChild();
    assertNotNull(newFnNode);
    assertEquals(2, newFnNode.getNumChildren());

    Node x = newFnNode.getChild(0);
    Node y = newFnNode.getChild(1);

    assertEquals("x", x.getString(Node.NAME));
    assertEquals("y", y.getString(Node.NAME));

    mutator.makeLocalNamesUnique(fnNode, true);

    newFnNode = fnNode.getFirstChild();
    assertNotNull(newFnNode);
    assertEquals(2, newFnNode.getNumChildren());

    x = newFnNode.getChild(0);
    y = newFnNode.getChild(1);

    assertTrue(x.getString(Node.NAME).startsWith("inline_x_"));
    assertTrue(y.getString(Node.NAME).startsWith("inline_y_"));
  }

  @Test
  public void testReplaceReturns() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    String labelName = "test_label";
    String resultName = "test_result";

    Node fnNode = NodeUtil.newFunctionNode("test", Lists.newArrayList(), null, Lists.newArrayList(), NodeUtil.newBlock(
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(1))
    ));

    Node injectableBlock = mutator.replaceReturns(fnNode, resultName, labelName, false);

    assertNull(injectableBlock);

    fnNode = NodeUtil.newFunctionNode("test", Lists.newArrayList(), null, Lists.newArrayList(), NodeUtil.newBlock(
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(1)),
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newBreakNode()
    ));

    injectableBlock = mutator.replaceReturns(fnNode, resultName, labelName, false);

    assertNull(injectableBlock);

    fnNode = NodeUtil.newFunctionNode("test", Lists.newArrayList(), null, Lists.newArrayList(), NodeUtil.newBlock(
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(1)),
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newAssignStatementNode("test_result", NodeUtil.newNumberNode(1)),
        NodeUtil.newBreakNode()
    ));

    injectableBlock = mutator.replaceReturns(fnNode, resultName, labelName, true);

    assertNotNull(injectableBlock);
    assertEquals(4, injectableBlock.getNumChildren());

    Node testResultNode = injectableBlock.getChild(2);
    assertEquals("test_result", testResultNode.getString(Node.NAME));
  }
}