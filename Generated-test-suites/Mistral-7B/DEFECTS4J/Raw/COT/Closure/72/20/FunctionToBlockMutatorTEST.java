package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node expectedResult = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(2))
    );

    Node actualResult = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberLiteral(1));
    argMap.put("arg2", NodeUtil.newNumberLiteral(2));

    Node expectedResult = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newVarNode("arg1", argMap.get("arg1")),
        NodeUtil.newVarNode("arg2", argMap.get("arg2")),
        NodeUtil.newReturnNode(NodeUtil.newBinaryOperator(Token.SUB,
            NodeUtil.newVarNode("local1"),
            NodeUtil.newNumberLiteral(1)))
    );

    Node actualResult = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop, argMap);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testAliasAndInlineArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Set.of("arg1", "arg2");
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberLiteral(1));
    argMap.put("arg2", NodeUtil.newNumberLiteral(2));

    Node expectedResult = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newVarNode("localArg1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newVarNode("localArg2", NodeUtil.newNumberLiteral(2)),
        NodeUtil.newReturnNode(NodeUtil.newBinaryOperator(Token.SUB,
            NodeUtil.newVarNode("local1"),
            NodeUtil.newNumberLiteral(1)))
    );

    Node actualResult = functionToBlockMutator.aliasAndInlineArguments(
        NodeUtil.newBlock(
            NodeUtil.newVarNode("arg1", argMap.get("arg1")),
            NodeUtil.newVarNode("arg2", argMap.get("arg2")),
            NodeUtil.newReturnNode(NodeUtil.newBinaryOperator(Token.MINUS,
                NodeUtil.newVarNode("arg1"),
                NodeUtil.newVarNode("arg2")))
        ), argMap, namesToAlias
    );

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node block = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(2))
    );

    String resultName = "result";
    String labelName = "testLabel";
    boolean resultMustBeSet = true;

    Node expectedResult = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newVarNode(resultName, NodeUtil.newUndefinedNode(null)),
        NodeUtil.newBreakNode(labelName),
        NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(2))
    );

    Node actualResult = functionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturnsWithNoResult() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node block = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(2))
    );

    String labelName = "testLabel";

    Node expectedResult = NodeUtil.newBlock(
        NodeUtil.newVarNode("local1", NodeUtil.newNumberLiteral(1)),
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newBreakNode(labelName)
    );

    Node actualResult = functionToBlockMutator.replaceReturns(block, null, labelName, false);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturnsWithEmptyFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node block = NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(2)));

    String resultName = "result";
    String labelName = "testLabel";
    boolean resultMustBeSet = true;

    Node expectedResult = NodeUtil.newBlock(
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newVarNode(resultName, NodeUtil.newUndefinedNode(null)),
        NodeUtil.newBreakNode(labelName)
    );

    Node actualResult = functionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertEquals(expectedResult, actualResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionNullFnNode() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node callNode = Node.newCall(Token.CALL, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, null, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionNullFnName() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = null;
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionNullCallNode() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = null;
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionNullResultName() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = null;
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionNegativeNeedsDefaultResult() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false; // should be true
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionIsCallInLoopNegative() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false; // should be true

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }
}