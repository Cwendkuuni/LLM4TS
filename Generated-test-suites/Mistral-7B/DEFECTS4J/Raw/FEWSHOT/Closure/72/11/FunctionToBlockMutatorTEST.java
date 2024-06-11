package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private LabelNameSupplier labelNameSupplier;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    functionToBlockMutator = new FunctionToBlockMutator(compiler, labelNameSupplier = new LabelNameSupplier(compiler.getNameIdSupplier()));
  }

  @Test
  public void testMutate_noArgs() {
    String fnName = "foo";
    Node fnNode = Node.newFunctionNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)))));
    Node callNode = NodeUtil.newCallNode(NodeUtil.newNameNode("foo"), Lists.newArrayList());
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)));
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testMutate_oneArg() {
    String fnName = "foo";
    Node fnNode = Node.newFunctionNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(NodeUtil.newVarNode("arg", NodeUtil.newNumberNode(42))), NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNameNode("arg"))));
    Node callNode = NodeUtil.newCallNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(NodeUtil.newNumberNode(42)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVarNode("arg", NodeUtil.newNameNode("arg")),
        NodeUtil.newReturnNode(NodeUtil.newNameNode("arg"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testMutate_multipleArgs() {
    String fnName = "foo";
    Node fnNode = Node.newFunctionNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(
        NodeUtil.newVarNode("arg1", NodeUtil.newNumberNode(42)),
        NodeUtil.newVarNode("arg2", NodeUtil.newNumberNode(43))
    ), NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNameNode("arg1"))));
    Node callNode = NodeUtil.newCallNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(
        NodeUtil.newNumberNode(42),
        NodeUtil.newNumberNode(43)
    ));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVarNode("arg1", NodeUtil.newNameNode("arg1")),
        NodeUtil.newVarNode("arg2", NodeUtil.newNameNode("arg2")),
        NodeUtil.newReturnNode(NodeUtil.newNameNode("arg1"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testMutate_isCallInLoop() {
    String fnName = "foo";
    Node fnNode = Node.newFunctionNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(), NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))));
    Node callNode = NodeUtil.newCallNode(NodeUtil.newNameNode("foo"), Lists.newArrayList());
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVar("result", NodeUtil.newNameNode("result")),
        NodeUtil.newReturn(NodeUtil.newNameNode("result"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testMutate_needsDefaultResult() {
    String fnName = "foo";
    Node fnNode = Node.newFunctionNode(NodeUtil.newNameNode("foo"), Lists.newArrayList(), NodeUtil.newBlock(NodeUtil.newReturnNode(null)));
    Node callNode = NodeUtil.newCallNode(NodeUtil.newNameNode("foo"), Lists.newArrayList());
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVar("result", NodeUtil.newNameNode("result")),
        NodeUtil.newReturn(NodeUtil.newUndefinedNode())
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testReplaceReturns() {
    Node block = NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)));
    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, true);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVar("result", NodeUtil.newNameNode("result")),
        NodeUtil.newLabel(NodeUtil.newIdNode("label")),
        NodeUtil.newBreak(NodeUtil.newLabelNameNode("label"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testReplaceReturns_noResult() {
    Node block = NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)));
    String labelName = "label";

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(block, null, labelName, false);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newLabel(NodeUtil.newLabelNameNode("label")),
        NodeUtil.newBreak(NodeUtil.newLabelNameNode("label"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testReplaceReturns_noLabel() {
    Node block = NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)));
    String resultName = "result";

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(block, resultName, null, false);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVar("result", NodeUtil.newNameNode("result"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testReplaceReturns_noResultOrLabel() {
    Node block = NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)));

    Node mutatedNode = FunctionToBlockMutator.replaceReturns(block, null, null, false);

    Node expectedNode = NodeUtil.newBlock();
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testMakeLocalNamesUnique() {
    Node fnNode = NodeUtil.newFunctionNode(
        NodeUtil.newNameNode("foo"),
        Lists.newArrayList(NodeUtil.newVarNode("a", NodeUtil.newNumberNode(1))),
        NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNameNode("a")))
    );
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.makeLocalNamesUnique(fnNode, isCallInLoop);

    Node expectedNode = NodeUtil.newFunctionNode(
        NodeUtil.newNameNode("foo_1"),
        Lists.newArrayList(NodeUtil.newVarNode("a_1", NodeUtil.newNumberNode(1))),
        NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNameNode("a_1")))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testMakeLocalNamesUnique_isCallInLoop() {
    Node fnNode = NodeUtil.newFunctionNode(
        NodeUtil.newNameNode("foo"),
        Lists.newArrayList(NodeUtil.newVarNode("a", NodeUtil.newNumberNode(1))),
        NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNameNode("a")))
    );
    boolean isCallInLoop = true;

    Node mutatedNode = functionToBlockMutator.makeLocalNamesUnique(fnNode, isCallInLoop);

    Node expectedNode = NodeUtil.newFunctionNode(
        NodeUtil.newNameNode("foo__inline_"),
        Lists.newArrayList(NodeUtil.newVarNode("a__inline_", NodeUtil.newNumberNode(1))),
        NodeUtil.newBlock(NodeUtil.newReturnNode(NodeUtil.newNameNode("a__inline_")))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }

  @Test
  public void testAliasAndInlineArguments() {
    Node fnTemplateRoot = NodeUtil.newBlock(
        NodeUtil.newVarNode("arg1", NodeUtil.newNameNode("arg1")),
        NodeUtil.newVarNode("arg2", NodeUtil.newNameNode("arg2")),
        NodeUtil.newReturnNode(NodeUtil.newNameNode("arg1"))
    );
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberNode(42));
    argMap.put("arg2", NodeUtil.newNumberNode(43));
    Set<String> namesToAlias = Sets.newHashSet("arg1");

    Node mutatedNode = functionToBlockMutator.aliasAndInlineArguments(fnTemplateRoot, argMap, namesToAlias);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVarNode("arg2", NodeUtil.newNameNode("arg2")),
        NodeUtil.newReturnNode(NodeUtil.newNameNode("arg2"))
    );
    Node aliasNode = expectedNode.getLastChild();
    Node aliasVarNode = aliasNode.getFirstChild();
    Node aliasNameNode = aliasVarNode.getFirstChild();
    Node replacedArg1Node = fnTemplateRoot.getChildAt(0);
    String aliasName = aliasNameNode.getString();

    assertThat(mutatedNode).isEqualTo(expectedNode);
    assertThat(aliasName).isEqualTo("arg1__inline_");
    assertThat(replacedArg1Node.getFirstChild().getString()).isEqualTo(aliasName);
  }

  @Test
  public void testAliasAndInlineArguments_noNamesToAlias() {
    Node fnTemplateRoot = NodeUtil.newBlock(
        NodeUtil.newVarNode("arg1", NodeUtil.newNameNode("arg1")),
        NodeUtil.newVarNode("arg2", NodeUtil.newNameNode("arg2")),
        NodeUtil.newReturnNode(NodeUtil.newNameNode("arg1"))
    );
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    Set<String> namesToAlias = Sets.newHashSet();

    Node mutatedNode = functionToBlockMutator.aliasAndInlineArguments(fnTemplateRoot, argMap, namesToAlias);

    Node expectedNode = NodeUtil.newBlock(
        NodeUtil.newVarNode("arg1", NodeUtil.newNameNode("arg1")),
        NodeUtil.newVarNode("arg2", NodeUtil.newNameNode("arg2")),
        NodeUtil.newReturnNode(NodeUtil.newNameNode("arg1"))
    );
    assertThat(mutatedNode).isEqualTo(expectedNode);
  }
}