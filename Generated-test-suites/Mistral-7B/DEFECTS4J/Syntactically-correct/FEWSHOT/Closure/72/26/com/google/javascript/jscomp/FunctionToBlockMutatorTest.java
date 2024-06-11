package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FunctionToBlockMutatorTest {
  @Test
  public void testMutateFunctionWithNoArgs() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.createFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.createCallNode(fnName, null);
    Node resultName = NodeUtil.newString("result");
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertEquals(fnNode, mutatedNode);
  }

  @Test
  public void testMutateFunctionWithOneArg() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.createFunctionNode(fnName, null, null);
    Node argNode = NodeUtil.newNumberNode(1);
    Node callNode = NodeUtil.createCallNode(fnName, argNode);
    Node resultName = NodeUtil.newString("result");
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The original function node should be mutated and returned.
    assertEquals(fnNode, mutatedNode);

    // Check that the argument has been passed as a parameter to the inline function.
    Node expectedArgNode = NodeUtil.createVarNode("arg0", argNode);
    Node argMap = Maps.newLinkedHashMap();
    argMap.put("arg0", argNode);

    Node expectedMutatedNode = FunctionArgumentInjector.inject(fnNode, null, argMap);

    Node argInlineNode = FunctionArgumentInjector.findInjectedArgument(expectedMutatedNode, "arg0");
    assertEquals(argNode, argInlineNode.getFirstChild());
  }

  @Test
  public void testMutateFunctionWithMultipleArgs() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.createFunctionNode(fnName, null, null);
    Node arg1Node = NodeUtil.newNumberNode(1);
    Node arg2Node = NodeUtil.newNumberNode(2);
    Node callNode = NodeUtil.createCallNode(fnName, arg1Node, arg2Node);
    Node resultName = NodeUtil.newString("result");
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The original function node should be mutated and returned.
    assertEquals(fnNode, mutatedNode);

    // Check that the arguments have been passed as parameters to the inline function.
    Node expectedArgMap = Maps.newLinkedHashMap();
    expectedArgMap.put("arg0", arg1Node);
    expectedArgMap.put("arg1", arg2Node);

    Node expectedMutatedNode = FunctionArgumentInjector.inject(fnNode, null, expectedArgMap);

    List<Node> argInlineNodes = Lists.newArrayList();

    for (Map.Entry<String, Node> entry : expectedArgMap.entrySet()) {
      String argName = entry.getKey();
      Node argNode = entry.getValue();
      Node argInlineNode = FunctionArgumentInjector.findInjectedArgument(expectedMutatedNode, argName);
      argInlineNodes.add(argInlineNode.getFirstChild());
    }

    Node[] args = argInlineNodes.toArray(new Node[0]);

    Node expectedArgNodes = NodeUtil.newExpr(NodeUtil.newVarDeclList(args));

    Node functionBody = NodeUtil.getFunctionBody(expectedMutatedNode);

    Node expectedFunctionBody = NodeUtil.newCallNode(
        "testFunction", expectedArgNodes);

    assertEquals(expectedFunctionBody, functionBody);
  }

  @Test
  public void testMutateFunctionWithLabel() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.createFunctionNode(fnName, null, null);
    Node labelName = Node.newString(Token.LABEL_NAME, "testLabel");
    Node callNode = NodeUtil.createCallNode(fnName);
    Node resultName = NodeUtil.newString("result");
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The original function node should be mutated and returned.
    assertEquals(fnNode, mutatedNode);

    // Check that the label has been replaced with a break statement.
    Node labelNode = mutator.getLabelNode(fnNode, labelName);
    Node expectedBreakNode = NodeUtil.newExpr(new Node(Token.BREAK, labelName));

    assertEquals(expectedBreakNode, labelNode);
  }

  @Test
  public void testMutateFunctionWithLabelAndResult() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.createFunctionNode(fnName, null, null);
    Node labelName = Node.newString(Token.LABEL_NAME, "testLabel");
    Node resultName = NodeUtil.newString("result");
    Node callNode = NodeUtil.createCallNode(fnName);
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The original function node should be mutated and returned.
    assertEquals(fnNode, mutatedNode);

    // Check that the label has been replaced with a break statement.
    Node labelNode = mutator.getLabelNode(fnNode, labelName);
    Node expectedBreakNode = NodeUtil.newExpr(new Node(Token.BREAK, labelName));

    assertEquals(expectedBreakNode, labelNode);

    // Check that the result has been assigned the value returned by the inline function.
    Node functionBody = NodeUtil.getFunctionBody(mutatedNode);
    Node expectedFunctionBody = NodeUtil.newExpr(new Node(Token.ASSIGN, resultName, NodeUtil.createCallNode(fnName)));

    assertEquals(expectedFunctionBody, functionBody);
  }

  @Test
  public void testMutateFunctionInLoop() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.createFunctionNode(fnName, null, null);
    Node labelName = Node.newString(Token.LABEL_NAME, "testLabel");
    Node callNode = NodeUtil.createCallNode(fnName);
    Node resultName = NodeUtil.newString("result");
    boolean needsDefaultResult = true;
    boolean isCallInLoop = true;

    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The original function node should be mutated and returned.
    assertEquals(fnNode, mutatedNode);

    // Check that the label has been replaced with a break statement.
    Node labelNode = mutator.getLabelNode(fnNode, labelName);
    Node expectedBreakNode = NodeUtil.newExpr(new Node(Token.BREAK, labelName));

    assertEquals(expectedBreakNode, labelNode);

    // Check that the result has been assigned the value returned by the inline function, or void 0 if no return value.
    Node functionBody = NodeUtil.getFunctionBody(mutatedNode);
    Node expectedFunctionBody;

    if (fnNode.getLastChild().getType() == Token.RETURN) {
      Node expectedReturnNode = fnNode.getLastChild();
      Node argMap = Maps.newLinkedHashMap();
      argMap.put("arg0", NodeUtil.newUndefinedNode());
      Node expectedMutatedNode = FunctionArgumentInjector.inject(fnNode, null, argMap);
      expectedFunctionBody = NodeUtil.newExpr(expectedMutatedNode);
      expectedFunctionBody = NodeUtil.newExpr(new Node(Token.ASSIGN, resultName, expectedFunctionBody));
    } else {
      expectedFunctionBody = NodeUtil.newExpr(new Node(Token.ASSIGN, resultName, NodeUtil.newUndefinedNode()));
    }

    assertEquals(expectedFunctionBody, functionBody);
  }
}

class NoOpCompiler implements AbstractCompiler {}