package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node arg1Node = Node.newNumber(Token.INT_LITERAL, 1);
    Node arg2Node = Node.newNumber(Token.INT_LITERAL, 2);
    Node callNode = Node.newCall(Token.CALL, fnNode, arg1Node, arg2Node);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @Test
  public void testMutateFunctionWithLabel() {
    String fnName = "testFunction";
    String labelName = "testLabel";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    mutator = new FunctionToBlockMutator(null, new FunctionToBlockMutator.LabelNameSupplier(null));
    mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    String mutatedLabelName = (String) mutatedNode.getLastChild().getLastChild()
        .getFirstChild().getFirstChild().getValue();
    assertNotNull(mutatedLabelName);
    assertNotNull(labelName);
    assertTrue(mutatedLabelName.startsWith(labelName + "_"));
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullFnNode() {
    String fnName = "testFunction";
    Node callNode = Node.newCall(Token.CALL, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node mutatedNode = mutator.mutate(fnName, null, callNode, resultName,
        needsDefaultResult, isCallInLoop);
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullCallNode() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node mutatedNode = mutator.mutate(fnName, fnNode, null, resultName,
        needsDefaultResult, isCallInLoop);
  }

  @Test
  public void testMutateFunctionWithArgsAndLabels() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node arg1Node = Node.newNumber(Token.INT_LITERAL, 1);
    Node arg2Node = Node.newNumber(Token.INT_LITERAL, 2);
    Node arg3Node = Node.newNumber(Token.INT_LITERAL, 3);
    Node callNode = Node.newCall(Token.CALL, fnNode, arg1Node, arg2Node, arg3Node);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    Map<String, Node> argMap = Maps.newHashMap();
    argMap.put("arg1", arg1Node);
    argMap.put("arg2", arg2Node);
    argMap.put("arg3", arg3Node);

    Node expectedMutatedNode = NodeUtil.getFunctionBody(fnNode).cloneTree();
    List<Node> newVars = Lists.newLinkedList();

    for (Entry<String, Node> entry : argMap.entrySet()) {
      String name = entry.getKey();
      Node value = entry.getValue();
      Node newValue = value.cloneTree();
      Node newNode = NodeUtil.newVarNode(name, newValue)
          .copyInformationFromForTree(value);
      newVars.add(0, newNode);
      // Remove the parameter from the list to replace.
      argMap.remove(name);
      expectedMutatedNode.replaceChildren(value.getFirstChild(), newValue);
    }

    FunctionArgumentInjector.inject(expectedMutatedNode, null, argMap);

    fixUnitializedVarDeclarations(expectedMutatedNode);

    Node labelNameNode = Node.newString(Token.LABEL_NAME, "testLabel");
    Node labelNode = new Node(Token.LABEL).copyInformationFrom(expectedMutatedNode);
    labelNode.addChildToFront(labelNameNode);
    labelNode.addChildToBack(expectedMutatedNode);

    Node injectableBlock = replaceReturns(expectedMutatedNode, resultName,
        "testLabel", needsDefaultResult);

    assertNotNull(injectableBlock);
    assertNotNull(injectableBlock.getLastChild());
    assertEquals(expectedMutatedNode, injectableBlock.getLastChild());
  }

  @Test
  public void testReplaceReturns() {
    Node block = Node.newBlock(Token.BLOCK, null);
    block.addChildToBack(Node.newReturn(Token.RETURN, null));
    String resultName = "result";
    String labelName = "testLabel";

    Node result = replaceReturns(block, resultName, labelName, false);

    assertNotNull(result);
    assertTrue(result.getType() == Token.BLOCK);
    assertNotNull(result.getLastChild());
    assertNotNull(result.getLastChild().getLastChild());
    assertFalse(result.getLastChild().getLastChild().getType() == Token.RETURN);

    result = replaceReturns(block, resultName, labelName, true);

    assertNotNull(result);
    assertTrue(result.getType() == Token.BLOCK);
    assertNotNull(result.getLastChild());
    assertNotNull(result.getLastChild().getLastChild());
    assertFalse(result.getLastChild().getLastChild().getType() == Token.RETURN);
    assertEquals(NodeUtil.newExpr(NodeUtil.newUndefinedNode(null)),
        result.getLastChild().getLastChild());
  }
}