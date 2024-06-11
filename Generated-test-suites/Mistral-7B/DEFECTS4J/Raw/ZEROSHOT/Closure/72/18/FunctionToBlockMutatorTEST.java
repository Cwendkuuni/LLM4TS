package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

/**
 * Test class for FunctionToBlockMutator.
 */
public class FunctionToBlockMutatorTest {

  @Test
  public void testFunctionToBlockMutator() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    // Test case 1: Simple function with no arguments and no return value.
    String fnName = "testFunction1";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, fnName, null, null, null, null, null, null);
    Node callNode = NodeUtil.newCallExpressionNode(Token.CALL, fnNode, null);
    String resultName = null;
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK, null);

    Node actualResult = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertEquals(expectedResult, actualResult);

    // Test case 2: Function with arguments and no return value.
    fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, fnName, null, null, null, null, null, null);
    List<Node> args = Lists.newArrayList(
        NodeUtil.newVarNode("arg1", null),
        NodeUtil.newVarNode("arg2", null)
    );
    Node argMap = NodeUtil.newObjectLiteralNode(Token.OBJECT, NodeUtil.newArrayLiteralNode(Token.ARRAY_LITERAL, args));
    callNode = NodeUtil.newCallExpressionNode(Token.CALL, fnNode, argMap);
    resultName = null;
    needsDefaultResult = false;
    isCallInLoop = false;
    expectedResult = NodeUtil.newBlockNode(Token.BLOCK, NodeUtil.newArrayLiteralNode(Token.ARRAY_LITERAL, args));

    actualResult = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertEquals(expectedResult, actualResult);

    // Test case 3: Function with arguments and return value.
    resultName = "result";
    needsDefaultResult = true;
    expectedResult = NodeUtil.newBlockNode(Token.BLOCK, NodeUtil.newAssignStatementNode(Token.ASSIGN, NodeUtil.newVarNode(resultName, null), NodeUtil.newCallExpressionNode(Token.CALL, fnNode, argMap)));

    actualResult = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertEquals(expectedResult, actualResult);

    // Test case 4: Function with arguments and return value, in a loop.
    isCallInLoop = true;
    expectedResult = NodeUtil.newBlockNode(Token.BLOCK, NodeUtil.newAssignStatementNode(Token.ASSIGN, NodeUtil.newVarNode(resultName, null), NodeUtil.newCallExpressionNode(Token.CALL, fnNode, argMap)), NodeUtil.newBreakStatementNode());

    actualResult = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertEquals(expectedResult, actualResult);
  }
}