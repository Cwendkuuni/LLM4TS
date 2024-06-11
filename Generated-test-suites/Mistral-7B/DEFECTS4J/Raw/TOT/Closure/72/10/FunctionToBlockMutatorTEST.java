package com.google.javascript.jscomp.FunctionToBlockMutatorTest;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import junit.framework.TestCase;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest extends TestCase {

  private static final String TEST_CLASS_NAME = "MyTestClass";
  private static final String TEST_FUNCTION_NAME = "testFunction";
  private static final String TEST_FUNCTION_CALL_NAME = "callTestFunction";
  private static final String TEST_VAR_NAME = "testVar";
  private static final String TEST_LABEL_NAME = "testLabel";
  private static final String TEST_UNIQUE_ID = "testId";

  private AbstractCompiler compiler;
  private FunctionToBlockMutator functionToBlockMutator;
  private FunctionArgumentInjector functionArgumentInjector;
  private LabelNameSupplier labelNameSupplier;

  @Override
  protected void setUp() throws Exception {
    compiler = new TestingAbstractCompiler();
    functionToBlockMutator = new FunctionToBlockMutator(compiler, new TestNameIdSupplier());
    functionArgumentInjector = new FunctionArgumentInjector(compiler);
    labelNameSupplier = new LabelNameSupplier(new TestNameIdSupplier());
  }

  @Override
  protected void tearDown() throws Exception {
    // Nothing to do.
  }

  /**
   * Test method for {@link FunctionToBlockMutator#mutate(String, Node, Node, String, boolean, boolean)}
   */
  public void testMutate() {
    Node functionNode = createTestFunctionNode();
    Node callNode = createTestFunctionCallNode();
    Node injectableBlock = functionToBlockMutator.mutate(TEST_CLASS_NAME, functionNode, callNode, TEST_VAR_NAME, true, false);

    assertEquals("Injectable block should not be null.", injectableBlock, testInjectionBlock());
  }

  /**
   * Test method for {@link FunctionToBlockMutator#mutate(String, Node, Node, String, boolean, boolean)}
   * with loop call
   */
  public void testMutateWithLoopCall() {
    Node functionNode = createTestFunctionNodeWithLoopCall();
    Node callNode = createTestFunctionCallNodeWithLoopCall();
    Node injectableBlock = functionToBlockMutator.mutate(TEST_CLASS_NAME, functionNode, callNode, TEST_VAR_NAME, true, true);

    assertEquals("Injectable block should not be null.", injectableBlock, testInjectionBlockWithLoopCall());
  }

  /**
   * Test method for {@link FunctionToBlockMutator#aliasAndInlineArguments(Node, Map, Set)}
   */
  public void testAliasAndInlineArguments() {
    Node functionNode = createTestFunctionWithArgumentsNode();
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    Set<String> namesToAlias = ImmutableList.of("arg1", "arg2").asSet();
    Node aliasBlock = functionToBlockMutator.aliasAndInlineArguments(functionNode, argMap, namesToAlias);

    assertEquals("AliasBlock should not be null.", aliasBlock, testAliasBlock());
  }

  /**
   * Test method for {@link FunctionToBlockMutator#replaceReturns(Node, String, String, boolean)}
   */
  public void testReplaceReturns() {
    Node block = createTestReturnNode();
    String resultName = TEST_VAR_NAME;
    String labelName = TEST_LABEL_NAME;
    boolean resultMustBeSet = true;
    Node injectableBlock = functionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertEquals("Injectable block should not be null.", injectableBlock, testReplaceReturnsBlock());
  }

  /**
   * Test method for {@link FunctionToBlockMutator#makeLocalNamesUnique(Node, boolean)}
   */
  public void testMakeLocalNamesUnique() {
    Node functionNode = createTestFunctionNodeWithLoopCall();
    Node uniqueFunctionNode = functionToBlockMutator.makeLocalNamesUnique(functionNode, true);

    assertEquals("Unique function node should not be null.", uniqueFunctionNode, testUniqueFunctionNode());
  }

  /**
   * Test method for {@link FunctionToBlockMutator#fixUnitializedVarDeclarations(Node)}
   */
  public void testFixUnitializedVarDeclarations() {
    Node block = createTestVarDeclarationsNode();
    functionToBlockMutator.fixUnitializedVarDeclarations(block);

    Node expectedBlock = NodeOperations.deepClone(testFixedVarDeclarationsBlock());
    assertEquals("Fixed block should be equal to expected block.", expectedBlock, block);
  }

  private Node createTestFunctionNode() {
    return createTestFunctionNode(TEST_FUNCTION_NAME);
  }

  private Node createTestFunctionNodeWithLoopCall() {
    return createTestFunctionNodeWithLoopCall(TEST_FUNCTION_NAME, TEST_LABEL_NAME);
  }

  private Node createTestFunctionNodeWithArguments() {
    return createTestFunctionNode(TEST_FUNCTION_NAME, TEST_VAR_NAME);
  }

  private Node createTestFunctionNodeWithArgumentsAndLoopCall() {
    return createTestFunctionNodeWithArgumentsAndLoopCall(TEST_FUNCTION_NAME, TEST_VAR_NAME, TEST_LABEL_NAME);
  }

  private Node createTestFunctionCallNode() {
    return createTestFunctionCallNode(TEST_FUNCTION_CALL_NAME);
  }

  private Node createTestFunctionCallNodeWithLoopCall() {
    return createTestFunctionCallNodeWithLoopCall(TEST_FUNCTION_CALL_NAME, TEST_LABEL_NAME);
  }

  private Node createTestReturnNode() {
    return createTestReturnNode(TEST_FUNCTION_NAME);
  }

  private Node createTestVarDeclarationsNode() {
    return createTestVarDeclarationsNode(TEST_FUNCTION_NAME);
  }

  private Node testInjectionBlock() {
    return NodeOperations.deepClone(testInjectionBlockNode());
  }

  private Node testInjectionBlockWithLoopCall() {
    return NodeOperations.deepClone(testInjectionBlockWithLoopCallNode());
  }

  private Node testAliasBlock() {
    return NodeOperations.deepClone(testAliasBlockNode());
  }

  private Node testReplaceReturnsBlock() {
    return NodeOperations.deepClone(testReplaceReturnsBlockNode());
  }

  private Node testUniqueFunctionNode() {
    return NodeOperations.deepClone(testUniqueFunctionNodeNode());
  }

  private Node testFixedVarDeclarationsBlock() {
    return NodeOperations.deepClone(testFixedVarDeclarationsBlockNode());
  }

  private Node createTestFunctionNode(String name) {
    return createTestFunctionNode(name, null);
  }

  private Node createTestFunctionNode(String name, String resultName) {
    Node functionNode = createTestFunctionExpressionNode(name);
    Node resultNode = createTestVariableDeclarationNode(resultName);
    Node functionBody = NodeUtil.newBlock(functionNode, resultNode);
    return functionBody;
  }

  private Node createTestFunctionNodeWithArguments(String name, String resultName) {
    Node functionNode = createTestFunctionExpressionNode(name);
    Node arg1 = createTestVariableDeclarationNode("arg1");
    Node arg2 = createTestVariableDeclarationNode("arg2");
    Node arguments = NodeUtil.newBlock(arg1, arg2);
    Node functionBody = NodeUtil.newBlock(functionNode, arguments);
    functionBody.addChildToBack(resultNode);
    return functionBody;
  }

  private Node createTestFunctionNodeWithArgumentsAndLoopCall(String name, String resultName, String labelName) {
    Node functionNode = createTestFunctionExpressionNode(name);
    Node arg1 = createTestVariableDeclarationNode("arg1");
    Node arg2 = createTestVariableDeclarationNode("arg2");
    Node arguments = NodeUtil.newBlock(arg1, arg2);
    Node functionBody = NodeUtil.newBlock(functionNode, arguments);
    Node loop = createTestLoopNode(labelName);
    Node block = NodeUtil.newBlock(functionBody, loop);
    functionBody.addChildToBack(resultNode);
    return block;
  }

  private Node createTestFunctionCallNode(String name) {
    Node callNode = createTestCallExpressionNode(name);
    return NodeUtil.newBlock(callNode);
  }

  private Node createTestFunctionCallNodeWithLoopCall(String name, String labelName) {
    Node callNode = createTestCallExpressionNodeWithLoopCall(name, labelName);
    return NodeUtil.newBlock(callNode);
  }

  private Node createTestReturnNode(String name) {
    Node functionBody = createTestFunctionNode(name);
    Node returnNode = NodeUtil.newReturnStatementNode();
    Node block = NodeUtil.newBlock(functionBody, returnNode);
    return block;
  }

  private Node createTestVarDeclarationsNode(String name) {
    Node var1 = NodeUtil.newVarDeclarationNode(TEST_VAR_NAME, null);
    Node var2 = NodeUtil.newVarDeclarationNode("var2", null);
    Node block = NodeUtil.newBlock(var1, var2);
    Node functionBody = NodeUtil.newBlock(Node.newFunction(name, NodeUtil.newList(Node.EMPTY_LIST), null), block);
    return functionBody;
  }

  private Node createTestFunctionExpressionNode(String name) {
    return Node.newFunction(name, NodeUtil.newList(Node.EMPTY_LIST), null);
  }

  private Node createTestCallExpressionNode(String name) {
    Node callNode = NodeUtil.newCallExpressionNode(NodeUtil.newIdentifierNode(TEST_CLASS_NAME), name, NodeUtil.newList(Node.EMPTY_LIST));
    return callNode;
  }

  private Node createTestCallExpressionNodeWithLoopCall(String name, String labelName) {
    Node callNode = NodeUtil.newCallExpressionNode(NodeUtil.newIdentifierNode(TEST_CLASS_NAME), name, NodeUtil.newList(NodeUtil.newIdentifierNode(labelName)));
    return callNode;
  }

  private Node createTestLoopNode(String labelName) {
    Node loopNode = Node.newLabel(labelName);
    Node loopBody = NodeUtil.newBlock(NodeUtil.newEmptyStatementNode(), NodeUtil.newBreakStatementNode(labelName));
    Node block = NodeUtil.newBlock(loopNode, loopBody);
    return block;
  }

  private Node testInjectionBlockNode() {
    return NodeOperations.deepClone(testInjectionBlockNodeFromFile());
  }

  private Node testInjectionBlockWithLoopCallNode() {
    return NodeOperations.deepClone(testInjectionBlockWithLoopCallNodeFromFile());
  }

  private Node testAliasBlockNode() {
    return NodeOperations.deepClone(testAliasBlockNodeFromFile());
  }

  private Node testReplaceReturnsBlockNode() {
    return NodeOperations.deepClone(testReplaceReturnsBlockNodeFromFile());
  }

  private Node testUniqueFunctionNodeNode() {
    return NodeOperations.deepClone(testUniqueFunctionNodeNodeFromFile());
  }

  private Node testFixedVarDeclarationsBlockNode() {
    return NodeOperations.deepClone(testFixedVarDeclarationsBlockNodeFromFile());
  }

  private Node testFunctionToBlockMutatorNodeFromFile() {
    String classPath = getClass().getResource("/TestFunctionToBlockMutator.js").getFile();
    return NodeUtil.parseAndTraverse(classPath, new TestingCompilerOptions()).getRootNode();
  }

  private static class TestNameIdSupplier implements Supplier<String> {
    private int nextId = 0;

    @Override
    public String get() {
      return TEST_UNIQUE_ID + nextId++;
    }
  }
}