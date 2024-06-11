package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.AbstractCompiler.CompilationLevel.ADVANCED_OPTIMIZATIONS;
import static com.google.javascript.rhino.NodeTreeMatcher.assertTreeEquals;
import static org.junit.Assert.assertThat;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionArgumentInjector.FunctionCallParameterMap;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = new CompilerOptions()
        .setCompilationLevel(ADVANCED_OPTIMIZATIONS)
        .buildCompiler();
    functionToBlockMutator = new FunctionToBlockMutator(
        compiler, compiler.getUniqueNameIdSupplier());
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "foo";
    String resultName = "result";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newList(
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
        NodeUtil.newVarNode("b", NodeUtil.newIntNode(2))),
        NodeUtil.newBlockNode(
            NodeUtil.newExprNode(Token.NAME, "return", NodeUtil.newIntNode(3))));

    Node callNode = NodeUtil.newCallNode(
        NodeUtil.newIdentifier("foo"), NodeUtil.newList(
            NodeUtil.newIntNode(4), NodeUtil.newIntNode(5)));

    Node expectedResult = NodeUtil.newBlockNode(
        NodeUtil.newExprNode(Token.ASSIGN, NodeUtil.newVarNode(resultName),
            NodeUtil.newCallNode(
                NodeUtil.newIdentifier("foo"),
                NodeUtil.newList(
                    NodeUtil.newCallNode(NodeUtil.newIdentifier("makeTemp"),
                        NodeUtil.newList(
                            NodeUtil.newIdentifier("arg0"),
                            NodeUtil.newIdentifier("arg1"))),
                    NodeUtil.newIntNode(4),
                    NodeUtil.newIntNode(5)))));

    Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, true, false);
    assertTreeEquals(expectedResult, result);
  }

  @Test
  public void testMutateFunctionWithoutArguments() {
    String fnName = "foo";
    String resultName = "result";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newList(),
        NodeUtil.newBlockNode(NodeUtil.newExprNode(Token.NAME, "return",
            NodeUtil.newIntNode(3))));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier("foo"));

    Node expectedResult = NodeUtil.newBlockNode(
        NodeUtil.newExprNode(Token.ASSIGN, NodeUtil.newVarNode(resultName),
            NodeUtil.newCallNode(NodeUtil.newIdentifier("foo"))));

    Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, true, false);
    assertTreeEquals(expectedResult, result);
  }

  @Test
  public void testMutateFunctionWithInlineArguments() {
    String fnName = "foo";
    String resultName = "result";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newList(),
        NodeUtil.newBlockNode(
            NodeUtil.newExprNode(Token.NAME, "return", NodeUtil.newIntNode(3))));

    Node callNode = NodeUtil.newCallNode(
        NodeUtil.newIdentifier("foo"), NodeUtil.newList(
            NodeUtil.newIntNode(4), NodeUtil.newIntNode(5)));

    // Arguments are inline, no need to create new locals.
    FunctionCallParameterMap argMap = Maps.newHashMap();
    Node expectedResult = NodeUtil.newBlockNode(
        NodeUtil.newExprNode(Token.ASSIGN, NodeUtil.newVarNode(resultName),
            NodeUtil.newIntNode(4)));

    Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, true, true);
    assertTreeEquals(expectedResult, result);
  }

  @Test
  public void testMutateFunctionWithLabel() {
    String fnName = "foo";
    String resultName = "result";
    String labelName = "label";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newList(),
        NodeUtil.newBlockNode(
            NodeUtil.newExprNode(Token.NAME, "return", NodeUtil.newIntNode(3))));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier("foo"));

    Node expectedResult = NodeUtil.newBlockNode(new Node(Token.LABEL),
        NodeUtil.newString(Token.LABEL_NAME, labelName),
        NodeUtil.newVarNode(resultName),
        NodeUtil.newBreakNode(NodeUtil.newString(Token.LABEL_NAME, labelName)));

    Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, true, false);
    assertTreeEquals(expectedResult, result);
  }

  @Test
  public void testMutateFunctionWithResultAssignment() {
    String fnName = "foo";
    String resultName = "result";
    String labelName = "label";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newList(),
        NodeUtil.newBlockNode(
            NodeUtil.newExprNode(Token.NAME, "result", NodeUtil.newIntNode(3)),
            NodeUtil.newExprNode(Token.NAME, "return", NodeUtil.newIntNode(3))));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier("foo"));

    Node expectedResult = NodeUtil.newBlockNode(new Node(Token.LABEL),
        NodeUtil.newString(Token.LABEL_NAME, labelName),
        NodeUtil.newVarNode(resultName),
        NodeUtil.newAssignNode(NodeUtil.newVarNode(resultName),
            NodeUtil.newIntNode(3)),
        NodeUtil.newBreakNode(NodeUtil.newString(Token.LABEL_NAME, labelName)));

    Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, false, false);
    assertTreeEquals(expectedResult, result);
  }

  @Test
  public void testMutateFunctionWithResultAssignmentAndLabel() {
    String fnName = "foo";
    String resultName = "result";
    String labelName = "label";
    Node fnNode = NodeUtil.newFunctionNode(fnName, NodeUtil.newList(),
        NodeUtil.newBlockNode(
            NodeUtil.newAssignNode(NodeUtil.newVarNode(resultName),
                NodeUtil.newIntNode(3)),
            NodeUtil.newExprNode(Token.NAME, "return", NodeUtil.newIntNode(3))));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier("foo"));

    Node expectedResult = NodeUtil.newBlockNode(new Node(Token.LABEL),
        NodeUtil.newString(Token.LABEL_NAME, labelName),
        NodeUtil.newVarNode(resultName),
        NodeUtil.newAssignNode(NodeUtil.newVarNode(resultName),
            NodeUtil.newIntNode(3)),
        NodeUtil.newBreakNode(NodeUtil.newString(Token.LABEL_NAME, labelName)));

    Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, false, false);
    assertTreeEquals(expectedResult, result);
  }
}