package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNode();
    Node callNode = createTestCallNode();
    String resultName = "testResult";

    Node transformedBlock = functionToBlockMutator.mutate(
        "testFunction", functionNode, callNode, resultName, false, false);

    assertNotNull(transformedBlock);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNodeWithArguments();
    Node callNode = createTestCallNodeWithArguments();
    String resultName = "testResult";

    Node transformedBlock = functionToBlockMutator.mutate(
        "testFunction", functionNode, callNode, resultName, false, false);

    assertNotNull(transformedBlock);
  }

  @Test
  public void testMutateFunctionWithCallInLoop() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNode();
    Node callNode = createTestCallNode();
    String resultName = "testResult";

    Node transformedBlock = functionToBlockMutator.mutate(
        "testFunction", functionNode, callNode, resultName, false, true);

    assertNotNull(transformedBlock);
  }

  @Test
  public void testReplaceReturns() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNodeWithReturn();
    String resultName = "testResult";

    Node transformedBlock = functionToBlockMutator.replaceReturns(
        functionNode, resultName, null, false);

    assertNotNull(transformedBlock);
  }

  @Test
  public void testReplaceReturnsWithResultName() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNodeWithReturn();
    String resultName = "testResult";

    Node transformedBlock = functionToBlockMutator.replaceReturns(
        functionNode, resultName, null, true);

    assertNotNull(transformedBlock);
  }

  @Test
  public void testReplaceReturnsWithLabelName() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNodeWithReturn();
    String labelName = "testLabel";

    Node transformedBlock = functionToBlockMutator.replaceReturns(
        functionNode, null, labelName, false);

    assertNotNull(transformedBlock);
  }

  @Test
  public void testReplaceReturnsWithLabelNameAndResultName() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, (Supplier<String>) () -> "test");

    Node functionNode = createTestFunctionNodeWithReturn();
    String labelName = "testLabel";
    String resultName = "testResult";

    Node transformedBlock = functionToBlockMutator.replaceReturns(
        functionNode, resultName, labelName, true);

    assertNotNull(transformedBlock);
  }

  private Node createTestFunctionNode() {
    Node functionNode = NodeUtil.newFunctionDeclaration(
        "testFunction", new Node[0], NodeUtil.newBlock(new Node[0]));

    return functionNode;
  }

  private Node createTestFunctionNodeWithArguments() {
    Node functionNode = NodeUtil.newFunctionDeclaration(
        "testFunction", new Node[] {
            NodeUtil.newIdent("arg1"),
            NodeUtil.newIdent("arg2")
        }, NodeUtil.newBlock(new Node[0]));

    return functionNode;
  }

  private Node createTestFunctionNodeWithReturn() {
    Node functionNode = NodeUtil.newFunctionDeclaration(
        "testFunction", new Node[0], new Node[] {
            NodeUtil.newReturn(NodeUtil.newNumberLiteral(1))
        });

    return functionNode;
  }

  private Node createTestCallNode() {
    Node functionCall = NodeUtil.newCall(
        NodeUtil.newIdent("testFunction"), new Node[0]);

    return functionCall;
  }

  private Node createTestCallNodeWithArguments() {
    Node arg1 = NodeUtil.newNumberLiteral(1);
    Node arg2 = NodeUtil.newNumberLiteral(2);
    Node functionCall = NodeUtil.newCall(
        NodeUtil.newIdent("testFunction"), new Node[] {arg1, arg2});

    return functionCall;
  }
}