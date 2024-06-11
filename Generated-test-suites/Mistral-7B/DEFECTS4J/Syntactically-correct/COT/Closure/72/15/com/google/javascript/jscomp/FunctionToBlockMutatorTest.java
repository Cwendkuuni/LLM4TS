package com.google.javascript.jscomp; // Assuming the FunctionToBlockMutator is in this package

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FunctionToBlockMutator.class)
public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    safeNameIdSupplier = Mockito.mock(Supplier.class);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, null);
    String resultName = "testResult";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testMutate_FunctionWithoutArguments() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = null;
    String resultName = null;
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testReplaceReturns_FunctionWithResult() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String resultName = "testResult";

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturns(
        block, resultName, "testLabel", false);

    assertNotNull(replacedBlock);
  }

  @Test
  public void testReplaceReturns_FunctionWithoutResult() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String resultName = null;

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturns(
        block, resultName, "testLabel", false);

    assertNull(replacedBlock);
  }

  @Test
  public void testReplaceReturns_FunctionWithLabel() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String labelName = "testLabel";

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturns(
        block, null, labelName, false);

    assertNotNull(replacedBlock);
  }

  @Test(expected = NullPointerException.class)
  public void testReplaceReturns_NullBlock() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node block = null;
    String resultName = null;
    String labelName = null;
    boolean needsDefaultResult = false;

    FunctionToBlockMutator.replaceReturns(block, resultName, labelName, needsDefaultResult);
  }

  @Test
  public void testReplaceReturns_FunctionWithDefaultResult() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String resultName = "testResult";
    boolean needsDefaultResult = true;

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturns(
        block, resultName, "testLabel", needsDefaultResult);

    assertNotNull(replacedBlock);
  }

  @Test
  public void testReplaceReturnWithBreak_FunctionWithResult() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String labelName = "testLabel";
    String resultName = "testResult";

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturnWithBreak(
        block, lastReturnNode, resultName, labelName);

    assertNotNull(replacedBlock);
  }

  @Test
  public void testReplaceReturnWithBreak_FunctionWithoutResult() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String labelName = "testLabel";

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturnWithBreak(
        block, lastReturnNode, null, labelName);

    assertNotNull(replacedBlock);
  }

  @Test
  public void testReplaceReturnWithBreak_FunctionWithLabelAndResult() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String labelName = "testLabel";
    String resultName = "testResult";

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturnWithBreak(
        block, lastReturnNode, resultName, labelName);

    assertNotNull(replacedBlock);
  }

  @Test
  public void testReplaceReturnWithBreak_FunctionWithoutLabel() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node block = NodeUtil.newBlockNode();
    Node lastReturnNode = NodeUtil.newReturnNode(null);

    Node replacedBlock = FunctionToBlockMutator.replaceReturnWithBreak(
        block, lastReturnNode, null, null);

    assertNotNull(replacedBlock);
  }

  @Test
  public void testMakeLocalNamesUnique() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    boolean isCallInLoop = false;

    functionToBlockMutator.makeLocalNamesUnique(fnNode, isCallInLoop);
  }
}