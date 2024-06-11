package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionToBlockMutator;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setup() {
    // Initialize the test environment based on your requirement
    compiler = new AbstractCompiler() {};
    safeNameIdSupplier = () -> "test";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_NullFnNode() {
    assertNull(functionToBlockMutator.mutate(null, null, null, null, false, false));
  }

  @Test
  public void testMutate_SimpleFunction() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, fnName);
    Node callNode = NodeUtil.newCallNode(Token.CALL, fnNode);

    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, fnName);
    Node callNode = NodeUtil.newCallNode(Token.CALL, fnNode);

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberNode(1));
    argMap.put("arg2", NodeUtil.newStringNode("arg2"));

    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testReplaceReturns_NoReturnStatement() {
    Node block = NodeUtil.newBlockNode();
    String resultName = "result";

    Node resultNode = functionToBlockMutator.replaceReturns(block, resultName, null, false);
    assertNull(resultNode);
  }

  @Test
  public void testReplaceReturns_ReturnStatement() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(1);
    block.addChildToBack(returnNode);
    String resultName = "result";

    Node resultNode = functionToBlockMutator.replaceReturns(block, resultName, labelName, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testReplaceReturns_ReturnStatementWithResult() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(NodeUtil.newNumberNode(1));
    block.addChildToBack(returnNode);
    String resultName = "result";

    Node resultNode = functionToBlockMutator.replaceReturns(block, resultName, labelName, true);
    assertNotNull(resultNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithoutReturnStatement() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    block.addChildToBack(NodeUtil.newVarNode("x", NodeUtil.newNumberNode(1)));

    String resultName = "result";

    Node resultNode = functionToBlockMutator.replaceReturns(block, resultName, labelName, false);
    assertNull(resultNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithReturnStatementAndResult() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(NodeUtil.newNumberNode(1));
    block.addChildToBack(returnNode);
    String resultName = "result";

    Node resultNode = functionToBlockMutator.replaceReturns(block, resultName, labelName, true);
    assertNotNull(resultNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithLoopStructure() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    block.addChildToBack(NodeUtil.newForInStatementNode(NodeUtil.newNameNode("i"), NodeUtil.newNumberNode(0), NodeUtil.newNumberNode(10), NodeUtil.newCallNode(Token.CALL, NodeUtil.newFunctionNode(Token.FUNCTION, NodeUtil.newNameNode("f")), null)));
    block.addChildToBack(NodeUtil.newReturnNode(NodeUtil.newNumberNode(1)));

    String resultName = "result";

    Node resultNode = functionToBlockMutator.replaceReturns(block, resultName, labelName, true);
    assertNotNull(resultNode);
  }
}