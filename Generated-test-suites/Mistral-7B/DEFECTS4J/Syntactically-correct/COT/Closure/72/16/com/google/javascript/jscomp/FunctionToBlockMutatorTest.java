package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeUtil.*;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate_NullFunctionNode() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    assertNull(mutator.mutate(null, null, null, null, false, false));
  }

  @Test
  public void testMutate_EmptyFunctionNode() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node fnNode = NodeUtil.newBlockNode();
    Node result = mutator.mutate(null, fnNode, null, null, false, false);
    assertEquals("{}", result.toPrintableTree());
  }

  @Test
  public void testMutate_FunctionWithCall() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    // Given: a simple function call
    Node fnNode = NodeUtil.newFunctionNode(
        "testFunction", null, NodeUtil.newBlockNode(), null);
    Node callNode = NodeUtil.newCallNode(fnNode, NodeUtil.newNullNode());
    String resultName = "result";

    Node mutatedNode = mutator.mutate(null, fnNode, callNode, resultName, false, false);

    // Then: the function call should be replaced with its result
    Node expected = NodeUtil.newAssignStatementNode(resultName, callNode);
    assertEquals(expected.toPrintableTree(), mutatedNode.toPrintableTree());
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    // Given: a function with arguments
    Node fnNode = NodeUtil.newFunctionNode(
        "testFunction", NodeUtil.newListNode("arg1", "arg2"),
        NodeUtil.newBlockNode(), null);
    Node callNode = NodeUtil.newCallNode(fnNode, NodeUtil.newListNode(
        NodeUtil.newNumberLiteralNode(1), NodeUtil.newNumberLiteralNode(2)));
    String resultName = "result";

    Node mutatedNode = mutator.mutate(null, fnNode, callNode, resultName, false, false);

    // Then: the arguments should be aliased and inlined
    Map<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", NodeUtil.newNumberLiteralNode(3));
    argMap.put("arg2", NodeUtil.newNumberLiteralNode(4));
    Node expected = FunctionArgumentInjector.inject(
        mutatedNode, null, argMap);

    // Check that the argument values were updated
    List<Node> arguments = mutatedNode.getFirstChild().getChildren();
    assertEquals(2, arguments.size());
    assertEquals(NodeUtil.newNumberLiteralNode(3).toPrintableTree(),
        arguments.get(0).toPrintableTree());
    assertEquals(NodeUtil.newNumberLiteralNode(4).toPrintableTree(),
        arguments.get(1).toPrintableTree());

    assertEquals(expected.toPrintableTree(), mutatedNode.toPrintableTree());
  }

  @Test
  public void testMutate_FunctionWithLoop() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    // Given: a function with a loop
    Node fnNode = NodeUtil.newFunctionNode(
        "testFunction", null, NodeUtil.newBlockNode(), null);
    Node loopStart = NodeUtil.newLabelNode("loopStart");
    Node loopEnd = NodeUtil.newLabelNode("loopEnd");
    Node loopHead = NodeUtil.newForInStatementNode(
        NodeUtil.newIdentifierNode("i"), NodeUtil.newArrayLiteralNode(),
        loopStart, loopEnd, NodeUtil.newBlockNode());
    Node loopBody = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(
        NodeUtil.newIdentifierNode("i"));
    loopBody.addChildToBack(returnNode);
    Node loop = NodeUtil.newLabeledStatementNode(loopStart, loopHead);
    fnNode.addChildToBack(loop);

    Node callNode = NodeUtil.newCallNode(fnNode, NodeUtil.newNullNode());
    String resultName = "result";

    Node mutatedNode = mutator.mutate(null, fnNode, callNode, resultName, false, true);

    // Then: the loop should be transformed into a continuation statement
    // and the return statement should be replaced with a break statement
    Node expected = NodeUtil.newBlockNode();
    expected.addChildToBack(loopStart);
    expected.addChildToBack(NodeUtil.newLabeledStatementNode(
        loopStart, loopHead));
    expected.addChildToBack(NodeUtil.newLabeledStatementNode(
        "loopEnd", NodeUtil.newBreakStatementNode(loopStart)));
    expected.addChildToBack(loopBody);
    expected.addChildToBack(NodeUtil.newAssignStatementNode(
        resultName, NodeUtil.newReturnNode(NodeUtil.newIdentifierNode("i"))));

    assertEquals(expected.toPrintableTree(), mutatedNode.toPrintableTree());
  }

  @Test
  public void testReplaceReturns_EmptyFunctionNode() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node fnNode = NodeUtil.newBlockNode();
    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = mutator.replaceReturns(fnNode, resultName, labelName, false);

    assertEquals("{}", mutatedNode.toPrintableTree());
  }

  @Test
  public void testReplaceReturns_FunctionWithReturnStatement() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    // Given: a function with a return statement
    Node fnNode = NodeUtil.newFunctionNode(
        "testFunction", null, NodeUtil.newBlockNode(), null);
    Node returnNode = NodeUtil.newReturnNode(
        NodeUtil.newIdentifierNode("result"));
    fnNode.addChildToBack(returnNode);
    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = mutator.replaceReturns(fnNode, resultName, labelName, true);

    // Then: the return statement should be replaced with an assignment
    // and a break statement
    Node expected = NodeUtil.newBlockNode();
    expected.addChildToBack(labelName);
    expected.addChildToBack(NodeUtil.newLabeledStatementNode(
        labelName, NodeUtil.newAssignStatementNode(
            resultName, NodeUtil.newReturnNode(NodeUtil.newIdentifierNode("result")))));
    expected.addChildToBack(NodeUtil.newBreakStatementNode(labelName));

    assertEquals(expected.toPrintableTree(), mutatedNode.toPrintableTree());
  }

  @Test
  public void testReplaceReturns_FunctionWithEmptyBlock() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    // Given: a function with an empty block
    Node fnNode = NodeUtil.newFunctionNode(
        "testFunction", null, NodeUtil.newBlockNode(), null);
    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = mutator.replaceReturns(fnNode, resultName, labelName, true);

    // Then: the empty block should be replaced with a break statement
    Node expected = NodeUtil.newBlockNode();
    expected.addChildToBack(labelName);
    expected.addChildToBack(NodeUtil.newBreakStatementNode(labelName));

    assertEquals(expected.toPrintableTree(), mutatedNode.toPrintableTree());
  }

  @Test(expected = AssertionError.class)
  public void testReplaceReturns_FunctionWithMultipleReturnStatements() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

    // Given: a function with multiple return statements
    Node fnNode = NodeUtil.newFunctionNode(
        "testFunction", null, NodeUtil.newBlockNode(), null);
    Node returnNode1 = NodeUtil.newReturnNode(
        NodeUtil.newIdentifierNode("result1"));
    Node returnNode2 = NodeUtil.newReturnNode(
        NodeUtil.newIdentifierNode("result2"));
    fnNode.addChildToBack(returnNode1);
    fnNode.addChildToBack(returnNode2);
    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = mutator.replaceReturns(fnNode, resultName, labelName, true);

    // Then: an assertion error should be thrown
  }
}