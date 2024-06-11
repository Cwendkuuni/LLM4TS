package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private LabelNameSupplier labelNameSupplier;

  @Before
  public void setup() {
    AbstractCompiler compiler = new NoOpCompiler();
    this.functionToBlockMutator = new FunctionToBlockMutator(compiler, new SimpleNameIdSupplier());
    this.labelNameSupplier = new LabelNameSupplier(new SimpleNameIdSupplier());
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);
    Node callNode = NodeUtil.newCallNode(fnName, null, null);
    Node resultNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, "result", false, false);
    Node expectedResult = NodeUtil.newAssignStatementNode("result", NodeUtil.newNumberNode(42));
    Node actualResult = NodeUtil.getFunctionBody(resultNode);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    String arg1 = "arg1";
    String arg2 = "arg2";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);
    Node callNode = NodeUtil.newCallNode(fnName, null, new Node[] {
        NodeUtil.newArgumentNode(arg1, NodeUtil.newNumberNode(1)),
        NodeUtil.newArgumentNode(arg2, NodeUtil.newNumberNode(2))
    });
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put(arg1, NodeUtil.newNumberNode(3));
    argMap.put(arg2, NodeUtil.newNumberNode(5));
    Node resultNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, "result", false, false);
    Node expectedResult = NodeUtil.newAssignStatementNode("result", NodeUtil.newNumberNode(8));
    Node actualResult = NodeUtil.getFunctionBody(resultNode);
    assertEquals(expectedResult, actualResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithIsCallInLoopAndHasArguments() {
    String fnName = "testFunction";
    String arg1 = "arg1";
    String arg2 = "arg2";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);
    Node callNode = NodeUtil.newCallNode(fnName, null, new Node[] {
        NodeUtil.newArgumentNode(arg1, NodeUtil.newNumberNode(1)),
        NodeUtil.newArgumentNode(arg2, NodeUtil.newNumberNode(2))
    });
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put(arg1, NodeUtil.newNumberNode(3));
    argMap.put(arg2, NodeUtil.newNumberNode(5));
    functionToBlockMutator.mutate(
        fnName, fnNode, callNode, "result", false, true);
  }

  @Test
  public void testReplaceReturns() {
    String fnName = "testFunction";
    String resultName = "result";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newBlockNode(), null);
    Node block = NodeUtil.newBlockNode();
    block.addChildToBack(NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)));
    fnNode.setLastChild(block);
    Node result = functionToBlockMutator.replaceReturns(
        fnNode, resultName, "testLabel", false);
    Node expectedResult = NodeUtil.newAssignStatementNode(resultName, NodeUtil.newNumberNode(42));
    Node actualResult = NodeUtil.getFunctionBody(result);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturnsWithResultName() {
    String fnName = "testFunction";
    String resultName = "result";
    String labelName = "testLabel";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newBlockNode(), null);
    Node block = NodeUtil.newBlockNode();
    block.addChildrenToBack(
        NodeUtil.newAssignStatementNode("result", NodeUtil.newNumberNode(42)),
        NodeUtil.newReturnNode(NodeUtil.newUndefinedNode()));
    fnNode.setLastChild(block);
    Node result = functionToBlockMutator.replaceReturns(
        fnNode, resultName, labelName, true);
    Node expectedResult = NodeUtil.newLabelNode(labelName);
    Node actualResult = NodeUtil.getFunctionBody(result);
    assertEquals(expectedResult, actualResult);
  }
}

class NoOpCompiler implements AbstractCompiler {

  @Override
  public Node compile(List<Node> nodes, Node parent, SourceFile sourceFile) {
    return null;
  }

  @Override
  public Node compileExpression(Node expression, Node parent, SourceFile sourceFile) {
    return null;
  }

  @Override
  public Node compileStatement(Node statement, Node parent, SourceFile sourceFile) {
    return null;
  }

  @Override
  public Node compileBlock(Node block, Node parent, SourceFile sourceFile) {
    return null;
  }

  @Override
  public Node compileTopLevelDeclaration(Node decl, Node parent, SourceFile sourceFile) {
    return null;
  }

  @Override
  public void reportCode(Node node, int message, String location) {
    // Do nothing
  }

  @Override
  public void reportWarning(String message, String location) {
    // Do nothing
  }

  @Override
  public void reportError(String message, String location) {
    // Do nothing
  }

  @Override
  public Node getUniqueNameIdSupplier() {
    return new SimpleNameIdSupplier();
  }

  @Override
  public Node getCodingConvention() {
    return null;
  }
}

class SimpleNameIdSupplier implements Supplier<String> {

  private int idCounter = 0;

  @Override
  public String get() {
    return "simpleNameId_" + (++idCounter);
  }
}