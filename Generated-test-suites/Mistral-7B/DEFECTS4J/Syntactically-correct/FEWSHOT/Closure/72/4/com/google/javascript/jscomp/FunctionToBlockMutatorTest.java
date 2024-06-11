package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node fnNode = Node.newFunction("test", Lists.newArrayList("a"), null);
    Node callNode = Node.newCall("test", Lists.newArrayList(Node.newNumber(1)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedBlock);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    Node fnNode = Node.newFunction("test", Lists.newArrayList("a", "b"), null);
    Node callNode = Node.newCall("test", Lists.newArrayList(Node.newNumber(1), Node.newNumber(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedBlock);
  }

  @Test
  public void testMutateFunctionWithLabel() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    String labelName = "label";
    Node fnNode = Node.newFunction("test", Lists.newArrayList(), null);
    Node callNode = null;
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNotNull(mutatedBlock);

    Node label = mutatedBlock.getLastChild();
    assertNotNull(label);
    String labelNameFromMutatedBlock = label.getFirstChild().getString();
    assertEquals(labelName, labelNameFromMutatedBlock);
  }

  @Test
  public void testMutateFunctionWithLabelAndResult() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    String labelName = "label";
    String resultName = "result";
    Node fnNode = Node.newFunction("test", Lists.newArrayList(), null);
    Node callNode = null;

    Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, true, false);
    assertNotNull(mutatedBlock);

    Node label = mutatedBlock.getLastChild();
    assertNotNull(label);
    String labelNameFromMutatedBlock = label.getFirstChild().getString();
    assertEquals(labelName, labelNameFromMutatedBlock);

    Node resultNode = mutatedBlock.getChildBefore(label);
    assertNotNull(resultNode);
    String resultNameFromMutatedBlock = resultNode.getFirstChild().getString();
    assertEquals(resultName, resultNameFromMutatedBlock);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndLabel() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    String labelName = "label";
    Node fnNode = Node.newFunction("test", Lists.newArrayList("a", "b"), null);
    Node callNode = Node.newCall("test", Lists.newArrayList(Node.newNumber(1), Node.newNumber(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNotNull(mutatedBlock);

    Node label = mutatedBlock.getLastChild();
    assertNotNull(label);
    String labelNameFromMutatedBlock = label.getFirstChild().getString();
    assertEquals(labelName, labelNameFromMutatedBlock);

    Node argNodeA = mutatedBlock.getChildBefore(label);
    assertNotNull(argNodeA);
    String argNameA = argNodeA.getFirstChild().getString();
    assertEquals("a", argNameA);

    Node argNodeB = mutatedBlock.getChildBefore(argNodeA);
    assertNotNull(argNodeB);
    String argNameB = argNodeB.getFirstChild().getString();
    assertEquals("b", argNameB);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndLabelAndResult() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    String labelName = "label";
    String resultName = "result";
    Node fnNode = Node.newFunction("test", Lists.newArrayList("a", "b"), null);
    Node callNode = Node.newCall("test", Lists.newArrayList(Node.newNumber(1), Node.newNumber(2)));

    Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, true, false);
    assertNotNull(mutatedBlock);

    Node label = mutatedBlock.getLastChild();
    assertNotNull(label);
    String labelNameFromMutatedBlock = label.getFirstChild().getString();
    assertEquals(labelName, labelNameFromMutatedBlock);

    Node resultNode = mutatedBlock.getChildBefore(label);
    assertNotNull(resultNode);
    String resultNameFromMutatedBlock = resultNode.getFirstChild().getString();
    assertEquals(resultName, resultNameFromMutatedBlock);

    Node argNodeA = mutatedBlock.getChildBefore(resultNode);
    assertNotNull(argNodeA);
    String argNameA = argNodeA.getFirstChild().getString();
    assertEquals("a", argNameA);

    Node argNodeB = mutatedBlock.getChildBefore(argNodeA);
    assertNotNull(argNodeB);
    String argNameB = argNodeB.getFirstChild().getString();
    assertEquals("b", argNameB);
  }

  @Test
  public void testReplaceReturns() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
    String labelName = "label";
    String resultName = "result";
    Node fnNode = Node.newFunction("test", Lists.newArrayList(), null);
    Node callNode = null;

    Node block = NodeUtil.newBlock(Node.newReturn(Node.newNumber(1)));
    Node mutatedBlock = mutator.replaceReturns(block, resultName, labelName, true);

    assertNull(mutatedBlock);

    block = NodeUtil.newBlock(Node.newReturn(Node.newNumber(1)));
    mutatedBlock = mutator.replaceReturns(block, resultName, labelName, false);
    assertNotNull(mutatedBlock);

    Node label = mutatedBlock.getLastChild();
    assertNotNull(label);
    String labelNameFromMutatedBlock = label.getFirstChild().getString();
    assertEquals(labelName, labelNameFromMutatedBlock);

    Node resultNode = mutatedBlock.getChildBefore(label);
    assertNotNull(resultNode);
    String resultNameFromMutatedBlock = resultNode.getFirstChild().getString();
    assertEquals(resultName, resultNameFromMutatedBlock);

    Node statement = mutatedBlock.getChildBefore(resultNode);
    assertNotNull(statement);
    assertEquals(Node.ASSIGN, statement.getType());

    Node valueNode = statement.getLastChild();
    assertNotNull(valueNode);
    assertEquals(Node.UNDEFINED, valueNode.getType());

    Node argNodeA = mutatedBlock.getChildBefore(statement);
    assertNotNull(argNodeA);
    String argNameA = argNodeA.getFirstChild().getString();
    assertEquals("result", argNameA);

    Node argNodeB = mutatedBlock.getChildBefore(argNodeA);
    assertNotNull(argNodeB);
    String argNameB = argNodeB.getString();
    assertEquals("JSCompiler_inline_label_test_" + labelName + "_" + mutator.safeNameIdSupplier.get(), argNameB);
  }
}