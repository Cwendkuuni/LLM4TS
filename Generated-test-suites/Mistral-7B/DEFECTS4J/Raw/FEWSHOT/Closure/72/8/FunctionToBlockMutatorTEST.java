package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Supplier;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestUtils;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test cases for the FunctionToBlockMutator class.
 */
public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    compiler = new TestUtils.TestCompiler();
    safeNameIdSupplier = compiler.getUniqueNameIdSupplier();
  }

  @Test
  public void testMutateFunction() {
    String fnName = "testFn";
    Node fnNode = TestUtils.createTestFunctionNode(fnName, "a", "b");
    Node callNode = TestUtils.createTestCallNode(fnName, "1", "2");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = new FunctionToBlockMutator(compiler, safeNameIdSupplier)
        .mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Check that local names are unique
    Node body = NodeUtil.getFunctionBody(fnNode);
    TestUtils.assertUniqueLocalNames(body);

    // Check that arguments are inlined and aliased if needed
    List<Node> args = NodeUtil.getCallArguments(callNode);
    Map<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(
        fnNode, callNode, safeNameIdSupplier);
    Set<String> namesToAlias = FunctionArgumentInjector.findModifiedParameters(fnNode);
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.aliasAndInlineArguments(body, argMap, namesToAlias);

    // Check that variables are initialized in for-loops
    Node loopBody = TestUtils.createTestLoopBodyNode();
    mutator.fixUnitializedVarDeclarations(loopBody);
    TestUtils.assertVariablesInitialized(loopBody);

    // Check that return statements are converted to assignments and breaks
    String labelName = mutator.getLabelNameForFunction(fnName);
    Node block = NodeUtil.newBlockNode();
    block.addChildToBack(new Node(Token.RETURN, null));
    Node injectableBlock = mutator.replaceReturns(block, resultName, labelName, needsDefaultResult);
    TestUtils.assertInjectableBlock(injectableBlock);

    // Check that the mutated node has the same shape as the original function body
    Node expected = NodeUtil.getFunctionBody(fnNode);
    Node actual = NodeUtil.getFunctionBody(mutatedNode);
    NodeUtil.assertTreeEquals(expected, actual);
  }

  @Test
  public void testMutateFunctionWithModifiedArguments() {
    String fnName = "testFn";
    Node fnNode = TestUtils.createTestFunctionNode(fnName, "a", "b");
    Node callNode = TestUtils.createTestCallNode(fnName, "1", "2");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Sets.newHashSet("a", "b");
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("a", TestUtils.createTestNumberNode(1));
    argMap.put("b", TestUtils.createTestNumberNode(2));

    Node mutatedNode = new FunctionToBlockMutator(compiler, safeNameIdSupplier)
        .mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Check that arguments are inlined and aliased
    Node body = NodeUtil.getFunctionBody(fnNode);
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.aliasAndInlineArguments(body, argMap, namesToAlias);

    // Check that variables are initialized in for-loops
    Node loopBody = TestUtils.createTestLoopBodyNode();
    mutator.fixUnitizedVarDeclarations(loopBody);
    TestUtils.assertVariablesInitialized(loopBody);

    // Check that return statements are converted to assignments and breaks
    String labelName = mutator.getLabelNameForFunction(fnName);
    Node block = NodeUtil.newBlockNode();
    block.addChildToBack(new Node(Token.RETURN, null));
    Node injectableBlock = mutator.replaceReturns(block, resultName, labelName, needsDefaultResult);
    TestUtils.assertInjectableBlock(injectableBlock);

    // Check that the mutated node has the same shape as the original function body
    Node expected = NodeUtil.getFunctionBody(fnNode);
    Node actual = NodeUtil.getFunctionBody(mutatedNode);
    NodeUtil.assertTreeEquals(expected, actual);
  }
}