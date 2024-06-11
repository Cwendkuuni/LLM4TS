package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    compiler = new CompilerPassManager.DefaultCompilerPassManager()
        .getFactory().createCompiler();
    safeNameIdSupplier = () -> "testId";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testSimpleFunctionMutation() {
    Node fnNode = NodeFactory.createFunctionNode(
        "testFunction", Token.FUNCTION, null, Lists.newArrayList(),
        NodeFactory.createBlock(NodeFactory.createVarNode("x", null),
            NodeFactory.createAssignStatementNode("x", NodeFactory.createNumberNode(5))),
        NodeFactory.createBlock(NodeFactory.createReturnStatementNode(NodeFactory.createNumberNode(2))));

    Node callNode = NodeFactory.createCallNode(
        NodeFactory.createIdentifierNode("testFunction"),
        Lists.newArrayList(NodeFactory.createNumberNode(3)));

    Node resultNode = functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false);

    assertEquals(NodeUtil.getFunctionBody(resultNode).toString(), "x = 5; break;");
  }

  @Test
  public void testFunctionWithArgumentsMutation() {
    Node fnNode = NodeFactory.createFunctionNode(
        "testFunction", Token.FUNCTION, null, Lists.newArrayList(),
        NodeFactory.createBlock(NodeFactory.createVarNode("x", null),
            NodeFactory.createAssignStatementNode("x", NodeFactory.createIdentifierNode("arg1"))),
        NodeFactory.createBlock(NodeFactory.createReturnStatementNode(NodeFactory.createIdentifierNode("x"))));

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeFactory.createNumberNode(3));

    Node callNode = NodeFactory.createCallNode(
        NodeFactory.createIdentifierNode("testFunction"),
        argMap.entrySet().stream().map(entry -> NodeFactory.createQuotedStringNode(entry.getKey()))
            .collect(Collectors.toList()));

    Node resultNode = functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false);

    assertEquals(NodeUtil.getFunctionBody(resultNode).toString(), "x = arg1; break;");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionWithInvalidCallNode() {
    Node fnNode = NodeFactory.createFunctionNode(
        "testFunction", Token.FUNCTION, null, Lists.newArrayList(),
        NodeFactory.createBlock(NodeFactory.createVarNode("x", null),
            NodeFactory.createAssignStatementNode("x", NodeFactory.createNumberNode(5))),
        NodeFactory.createBlock(NodeFactory.createReturnStatementNode(NodeFactory.createNumberNode(2))));

    Node callNode = NodeFactory.createCallNode(
        NodeFactory.createIdentifierNode("testFunction"));

    functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false);
  }

  @Test
  public void testFunctionInLoopMutation() {
    Node fnNode = NodeFactory.createFunctionNode(
        "testFunction", Token.FUNCTION, null, Lists.newArrayList(),
        NodeFactory.createBlock(NodeFactory.createVarNode("x", null),
            NodeFactory.createAssignStatementNode("x", NodeFactory.createNumberNode(5))),
        NodeFactory.createBlock(NodeFactory.createReturnStatementNode(NodeFactory.createNumberNode(2))));

    Node loopNode = NodeFactory.createLoopNode(
        NodeFactory.createLabelNode("loopLabel"),
        NodeFactory.createBlock(
            NodeFactory.createAssignStatementNode("x", NodeFactory.createNumberNode(1)),
            NodeFactory.createIfStatementNode(
                NodeFactory.createRelationalOperatorNode(Token.LT, NodeFactory.createNumberNode(5), NodeFactory.createIdentifierNode("x")),
                NodeFactory.createBlock(NodeFactory.createBreakStatementNode("loopLabel")),
                NodeFactory.createBlock(), NodeFactory.createEmptyStatementNode())),
        NodeFactory.createLabelNode("loopLabel"));

    Node callNode = NodeFactory.createCallNode(
        NodeFactory.createIdentifierNode("testFunction"),
        Lists.newArrayList());

    Node resultNode = functionToBlockMutator.mutate(null, fnNode, callNode, null, true, false);

    assertEquals(NodeUtil.getFunctionBody(resultNode).toString(), "x = 5; break loopLabel;");
  }

  @Test
  public void testFunctionWithNoReturnMutation() {
    Node fnNode = NodeFactory.createFunctionNode(
        "testFunction", Token.FUNCTION, null, Lists.newArrayList(),
        NodeFactory.createBlock(NodeFactory.createVarNode("x", null),
            NodeFactory.createAssignStatementNode("x", NodeFactory.createNumberNode(5))),
        NodeFactory.createBlock());

    Node callNode = NodeFactory.createCallNode(
        NodeFactory.createIdentifierNode("testFunction"),
        Lists.newArrayList());

    Node resultNode = functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false);

    assertEquals(NodeUtil.getFunctionBody(resultNode).toString(), "x = 5; break;");
  }
}