package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = () -> "test";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_SimpleFunction() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Node.NEW_EXPR, null, null, null, null, null);
    Node callNode = Node.newCall(Node.IDENT, fnName, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedMutatedNode = NodeUtil.getFunctionBody(fnNode);
    expectedMutatedNode.detachFromParent();

    NodeUtil.assertNodeEquals(expectedMutatedNode, mutatedNode);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Node.NEW_EXPR, null, null, null, null, null);
    Node callNode = Node.newCall(Node.IDENT, fnName, Lists.newArrayList(
        Node.newString("arg1"), Node.newNumber(1)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedMutatedNode = NodeUtil.getFunctionBody(fnNode);
    expectedMutatedNode.detachFromParent();

    FunctionArgumentInjector.injectArgumentsToFunction(expectedMutatedNode, callNode,
        safeNameIdSupplier);

    NodeUtil.assertNodeEquals(expectedMutatedNode, mutatedNode);
  }

  @Test
  public void testMutate_FunctionWithInlineResult() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Node.NEW_EXPR, null, null, null, null, null);
    Node callNode = Node.newCall(Node.IDENT, fnName, null);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedMutatedNode = NodeUtil.getFunctionBody(fnNode);
    expectedMutatedNode.detachFromParent();

    Node inlineResult = NodeUtil.newAssign(resultName, NodeUtil.newUndefinedNode(null));
    Node expectedInlineResult = NodeUtil.newExpr(inlineResult);

    Node expectedMutatedNodeWithResult = new Node(Token.BLOCK);
    expectedMutatedNodeWithResult.addChildrenToBack(expectedMutatedNode);
    expectedMutatedNodeWithResult.addChildrenToBack(expectedInlineResult);

    NodeUtil.assertNodeEquals(expectedMutatedNodeWithResult, mutatedNode);
  }

  @Test
  public void testMutate_FunctionCallInLoop() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Node.NEW_EXPR, null, null, null, null, null);
    Node callNode = Node.newCall(Node.IDENT, fnName, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    // The mutated node for function call in loop shouldn't be inlineable.
    assertNull(mutatedNode);
  }

  @Test
  public void testReplaceReturns_SimpleFunction() {
    String labelName = "label";
    String resultName = "result";
    Node block = Node.newBlock(NodeUtil.newLabel(labelName));
    block.addChildToBack(Node.newReturn(Node.newNumber(1)));

    Node expectedMutatedNode = Node.newBlock(NodeUtil.newLabel(labelName));
    expectedMutatedNode.addChildToBack(NodeUtil.newAssign(resultName, NodeUtil.newNumber(1)));
    expectedMutatedNode.addChildToBack(NodeUtil.newBreak(labelName));

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(
        block, resultName, labelName, false);

    NodeUtil.assertNodeEquals(expectedMutatedNode, mutatedNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithResult() {
    String labelName = "label";
    String resultName = "result";
    Node block = Node.newBlock(NodeUtil.newLabel(labelName));
    block.addChildToBack(Node.newAssign(resultName, Node.newNumber(1)));
    block.addChildToBack(Node.newReturn(NodeUtil.newExpr(Node.newString("result"))));

    Node expectedMutatedNode = Node.newBlock(NodeUtil.newLabel(labelName));
    expectedMutatedNode.addChildToBack(NodeUtil.newAssign(resultName, NodeUtil.newNumber(1)));
    expectedMutatedNode.addChildToBack(NodeUtil.newBreak(labelName));

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(
        block, resultName, labelName, true);

    NodeUtil.assertNodeEquals(expectedMutatedNode, mutatedNode);
  }

  @Test
  public void testReplaceReturns_EmptyFunction() {
    String labelName = "label";
    Node block = Node.newBlock(NodeUtil.newLabel(labelName));
    block.addChildToBack(Node.newReturn(Node.NULL));

    Node expectedMutatedNode = Node.newBlock(NodeUtil.newLabel(labelName));
    expectedMutatedNode.addChildToBack(NodeUtil.newAssign(labelName, NodeUtil.newNumber(1)));
    expectedMutatedNode.addChildToBack(NodeUtil.newBreak(labelName));

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(
        block, null, labelName, false);

    NodeUtil.assertNodeEquals(expectedMutatedNode, mutatedNode);
  }
}