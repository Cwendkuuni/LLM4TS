package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = new NoOpCompiler();
    functionToBlockMutator = new FunctionToBlockMutator(compiler, new MyNameIdSupplier());
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", null);
    Node resultNode = NodeUtil.newUndefinedNode(null);
    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, "result", true, false);

    NodeUtil.assertNodeEquals(NodeUtil.getFunctionBody(fnNode), mutatedFunction);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", Lists.newArrayList(
        NodeUtil.newStringNode("arg1"), NodeUtil.newNumberNode(42)));
    Node resultNode = NodeUtil.newUndefinedNode(null);
    Map<String, Node> arguments = new LinkedHashMap<>();
    arguments.put("arg1", NodeUtil.newStringNode("arg1Value"));
    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, "result", true, false);

    NodeUtil.assertNodeEquals(NodeUtil.getFunctionBody(fnNode), mutatedFunction);

    // Check that arguments were properly inserted
    Node newBlock = NodeUtil.getFunctionBody(mutatedFunction);
    Node argumentNode = NodeUtil.getFirstChildThatMatches(newBlock, NodeUtil.IS_VAR);
    assertThat(argumentNode.getString("name")).isEqualTo("arg1");
    assertThat(argumentNode.getFirstChild().getString("value")).isEqualTo("arg1Value");
  }

  @Test
  public void testReplaceReturns() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", null);
    String resultName = "result";
    String labelName = "label";

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, false).cloneTree();

    Node replacedFunction = replaceReturns(mutatedFunction, resultName, labelName, true);

    NodeUtil.assertNodeEquals(
        NodeUtil.newLabelNode(labelName).copyInformationFrom(fnNode).cloneTree(),
        replacedFunction.getFirstChild());
  }

  @Test
  public void testReplaceReturnsWhenResultNotSet() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode("testFunction", null);
    String resultName = "result";
    String labelName = "label";

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false).cloneTree();

    Node replacedFunction = replaceReturns(mutatedFunction, resultName, labelName, false);

    assertNull(replacedFunction);
  }

  @Test
  public void testFixUnitializedVarDeclarations() {
    String nodeType = Token.VAR;

    Node block = NodeUtil.newVarNode(null, null, nodeType)
        .addChild(NodeUtil.newVarNode(null, null, nodeType))
        .addChild(NodeUtil.newBlockNode(null, NodeUtil.newLabelNode("loop"), null))
        .cloneTree();

    functionToBlockMutator.fixUnitializedVarDeclarations(block);

    Node varNode = NodeUtil.getFirstChildThatMatches(block, NodeUtil.IS_VAR);
    Node child = NodeUtil.getFirstChildThatMatches(varNode, NodeUtil.IS_EXPR);

    Node undefinedNode = NodeUtil.newUndefinedNode(null);
    Node expectedNode = NodeUtil.newAssignStatementNode("", undefinedNode);
    NodeUtil.assertNodeEquals(expectedNode, child);
  }

  @Test
  public void testMakeLocalNamesUnique() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null)
        .addChild(NodeUtil.newVarNode(null, null, Token.VAR, "var1"))
        .addChild(NodeUtil.newVarNode(null, null, Token.VAR, "var2"))
        .cloneTree();

    functionToBlockMutator.makeLocalNamesUnique(fnNode, false);

    Node newFnNode = fnNode.getFirstChild();
    Node newVar1 = NodeUtil.getFirstChildThatMatches(newFnNode, NodeUtil.IS_VAR);
    Node newVar2 = NodeUtil.getFirstChildThatMatches(newFnNode, NodeUtil.IS_VAR, 1);

    String uniqueVar1 = "var1_1";
    String uniqueVar2 = "var2_1";

    String expectedName1 = "testFunction_var1_" + uniqueVar1;
    String expectedName2 = "testFunction_var2_" + uniqueVar2;

    NodeUtil.assertNodeEquals(NodeUtil.newNameNode(expectedName1), newVar1.getFirstChild());
    NodeUtil.assertNodeEquals(NodeUtil.newNameNode(expectedName2), newVar2.getFirstChild());
  }

  @Test
  public void testMakeLabelNamesUnique() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null)
        .addChild(NodeUtil.newLabelNode("label1"))
        .addChild(NodeUtil.newLabelNode("label2"))
        .cloneTree();

    functionToBlockMutator.makeLocalNamesUnique(fnNode, false);

    Node newFnNode = fnNode.getFirstChild();
    Node newLabel1 = NodeUtil.getFirstChildThatMatches(newFnNode, NodeUtil.IS_LABEL);
    Node newLabel2 = NodeUtil.getFirstChildThatMatches(newFnNode, NodeUtil.IS_LABEL, 1);

    String uniqueLabel1 = "testFunction_label1_" + functionToBlockMutator.getLabelNameForFunction(fnName);
    String uniqueLabel2 = "testFunction_label2_" + functionToBlockMutator.getLabelNameForFunction(fnName);

    NodeUtil.assertNodeEquals(NodeUtil.newLabelNode(uniqueLabel1), newLabel1);
    NodeUtil.assertNodeEquals(NodeUtil.newLabelNode(uniqueLabel2), newLabel2);
  }

  @Test
  public void testAliasAndInlineArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null)
        .addChild(NodeUtil.newVarNode(null, null, Token.VAR, "arg1"))
        .addChild(NodeUtil.newCallNode("add", Lists.newArrayList(
            NodeUtil.newNumberNode(2), NodeUtil.newCallNode("mul", Lists.newArrayList(
                NodeUtil.newNumberNode(3), NodeUtil.newNumberNode(5))))))
        .cloneTree();

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberNode(1));
    Set<String> namesToAlias = Set.of("add");

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, null, null, false, false).cloneTree();

    Node mutatedBlock = NodeUtil.getFunctionBody(mutatedFunction);

    Node argsResult = aliasAndInlineArguments(mutatedBlock, argMap, namesToAlias);

    NodeUtil.assertNodeEquals(argsResult, mutatedBlock);
  }

  private static Node aliasAndInlineArguments(Node fnTemplateRoot,
      Map<String, Node> argMap, Set<String> namesToAlias) {
    return functionToBlockMutator.aliasAndInlineArguments(
        fnTemplateRoot, argMap, namesToAlias);
  }

  private static Node replaceReturns(Node block, String resultName, String labelName, boolean resultMustBeSet) {
    return functionToBlockMutator.replaceReturns(
        block, resultName, labelName, resultMustBeSet);
  }

  private static class NoOpCompiler extends AbstractCompiler {
    @Override
    public Node compile(Node tree, CompilationLevel level, boolean typeCheckOnly,
        boolean throwOnError, boolean printStack, boolean printFileNames,
        ErrorReporter errorReporter, Node container) {
      return tree;
    }
  }

  private static class MyNameIdSupplier implements Supplier<String> {
    @Override
    public String get() {
      return "id";
    }
  }
}