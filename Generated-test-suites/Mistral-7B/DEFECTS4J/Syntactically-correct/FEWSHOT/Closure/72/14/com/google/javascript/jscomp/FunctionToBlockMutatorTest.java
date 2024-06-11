package org.apache.commons.cli.jscomp; // Assuming the test class is in the same package as the FunctionToBlockMutator class

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

  private static final String TEST_FUNCTION_NAME = "testFunction";
  private static final String TEST_FUNCTION_CALL_NAME = "callTestFunction";
  private static final String TEST_LABEL_NAME = "testLabel";

  private FunctionToBlockMutator functionToBlockMutator;
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = new Supplier<String>() {
      private int id = 0;

      @Override
      public String get() {
        return String.valueOf(++id);
      }
    };
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_NoArguments() {
    Node testFunctionNode = createTestFunctionNode();
    Node callTestFunctionNode = createTestCallNode();

    Node result = functionToBlockMutator.mutate(
        TEST_FUNCTION_NAME, testFunctionNode, callTestFunctionNode, null, false, false);

    Node expectedResult = NodeUtil.getFunctionBody(createTestFunctionBlockNode());
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testMutate_WithArguments() {
    Node testFunctionNode = createTestFunctionNode();
    Node callTestFunctionNode = createTestCallNodeWithArguments();

    LinkedHashMap<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(
        testFunctionNode, callTestFunctionNode, safeNameIdSupplier);

    Node result = functionToBlockMutator.mutate(
        TEST_FUNCTION_NAME, testFunctionNode, callTestFunctionNode, null, false, false);

    Node expectedResult = aliasAndInlineArguments(testFunctionNode, argMap);
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testMutate_WithLabel() {
    Node testFunctionNode = createTestFunctionNodeWithLabel();
    Node callTestFunctionNode = createTestCallNode();

    String labelName = functionToBlockMutator.getLabelNameForFunction(TEST_FUNCTION_NAME);

    Node result = functionToBlockMutator.mutate(
        TEST_FUNCTION_NAME, testFunctionNode, callTestFunctionNode, null, false, false);

    Node expectedResult = replaceReturns(result, null, labelName, false);
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testMutate_WithResultName() {
    Node testFunctionNode = createTestFunctionNode();
    Node callTestFunctionNode = createTestCallNode();

    Node result = functionToBlockMutator.mutate(
        TEST_FUNCTION_NAME, testFunctionNode, callTestFunctionNode, "resultName", true, false);

    Node expectedResult = replaceReturns(result, "resultName", null, true);
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testMutate_WithIsCallInLoop() {
    Node testFunctionNode = createTestFunctionNode();
    Node callTestFunctionNode = createTestCallNode();

    Node result = functionToBlockMutator.mutate(
        TEST_FUNCTION_NAME, testFunctionNode, callTestFunctionNode, null, false, true);

    Node expectedResult = replaceReturns(result, null, null, false);
    fixUnitializedVarDeclarations(expectedResult);
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testAliasAndInlineArguments() {
    Node testFunctionNode = createTestFunctionNodeWithArguments();
    LinkedHashMap<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(
        testFunctionNode, null, safeNameIdSupplier);
    Set<String> namesToAlias = FunctionArgumentInjector.findModifiedParameters(testFunctionNode);

    Node result = functionToBlockMutator.aliasAndInlineArguments(
        NodeUtil.getFunctionBody(testFunctionNode), argMap, namesToAlias);

    Node expectedResult = NodeUtil.getFunctionBody(createTestFunctionBlockNodeWithAliases());
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testReplaceReturns() {
    Node testFunctionBlockNode = createTestFunctionBlockNode();

    Node result = replaceReturns(testFunctionBlockNode, null, null, false);
    Node expectedResult = createTestFunctionBlockNodeWithBreak();
    assertThat(result).isEqualTo(expectedResult);

    result = replaceReturns(testFunctionBlockNode, "resultName", null, true);
    expectedResult = createTestFunctionBlockNodeWithAssignment();
    assertThat(result).isEqualTo(expectedResult);
  }

  private Node createTestFunctionNode() {
    String functionName = TEST_FUNCTION_NAME;
    Node testFunctionNode = new Node(Token.FUNCTION, null, null, null, functionName);
    testFunctionNode.setIsStrict();
    testFunctionNode.setIsVar();
    Node body = NodeUtil.newBlockNode(NodeUtil.newReturnNode(NodeUtil.newIntLiteral(1)));
    testFunctionNode.setBody(body);
    return testFunctionNode;
  }

  private Node createTestFunctionNodeWithLabel() {
    String functionName = TEST_FUNCTION_NAME;
    Node testFunctionNode = new Node(Token.FUNCTION, null, null, null, functionName);
    testFunctionNode.setIsStrict();
    testFunctionNode.setIsVar();
    Node body = NodeUtil.newBlockNode(
        NodeUtil.newLabelNode(TEST_LABEL_NAME),
        NodeUtil.newReturnNode(NodeUtil.newIntLiteral(1))
    );
    testFunctionNode.setBody(body);
    return testFunctionNode;
  }

  private Node createTestFunctionNodeWithArguments() {
    String functionName = TEST_FUNCTION_NAME;
    Node testFunctionNode = new Node(Token.FUNCTION, null, null, null, functionName);
    testFunctionNode.setIsStrict();
    testFunctionNode.setIsVar();
    Node params = NodeUtil.newListNode();
    testFunctionNode.setParams(params);
    Node body = NodeUtil.newBlockNode();
    testFunctionNode.setBody(body);

    params.addChild(NodeUtil.newParamNode("arg1"));
    params.addChild(NodeUtil.newParamNode("arg2"));

    Node arg1 = NodeUtil.newIntLiteral(1);
    Node arg2 = NodeUtil.newIntLiteral(2);

    body.addChildrenToBack(NodeUtil.newAssignStatementNode("arg1", arg1));
    body.addChildrenToBack(NodeUtil.newAssignStatementNode("arg2", arg2));
    body.addChildrenToBack(NodeUtil.newReturnNode(NodeUtil.newNameNode("arg1")));

    return testFunctionNode;
  }

  private Node createTestCallNode() {
    String functionCallName = TEST_FUNCTION_CALL_NAME;
    Node callTestFunctionNode = new Node(Token.CALL, null, null, null, functionCallName);
    callTestFunctionNode.setIsCall();
    Node args = NodeUtil.newListNode();
    callTestFunctionNode.setArgs(args);
    return callTestFunctionNode;
  }

  private Node createTestCallNodeWithArguments() {
    Node callTestFunctionNode = createTestCallNode();

    Node arg1 = NodeUtil.newIntLiteral(3);
    Node arg2 = NodeUtil.newIntLiteral(4);

    args.addChildrenToBack(arg1);
    args.addChildrenToBack(arg2);

    return callTestFunctionNode;
  }

  private Node createTestFunctionBlockNode() {
    Node testFunctionBlockNode = new Node(Token.BLOCK);
    Node returnNode1 = NodeUtil.newReturnNode(NodeUtil.newIntLiteral(1));
    testFunctionBlockNode.addChild(returnNode1);
    return testFunctionBlockNode;
  }

  private Node createTestFunctionBlockNodeWithAliases() {
    Node testFunctionBlockNode = new Node(Token.BLOCK);
    Node alias1Node = NodeUtil.newVarNode("alias1", NodeUtil.newIntLiteral(5));
    Node alias2Node = NodeUtil.newVarNode("alias2", NodeUtil.newIntLiteral(6));

    Node assign1Node = NodeUtil.newAssignStatementNode("alias1", NodeUtil.newIntLiteral(5));
    Node assign2Node = NodeUtil.newAssignStatementNode("alias2", NodeUtil.newIntLiteral(6));

    testFunctionBlockNode.addChildrenToBack(assign1Node);
    testFunctionBlockNode.addChildrenToBack(assign2Node);
    testFunctionBlockNode.addChildrenToBack(returnNode1);

    return testFunctionBlockNode;
  }

  private Node createTestFunctionBlockNodeWithBreak() {
    Node testFunctionBlockNode = new Node(Token.BLOCK);
    Node returnNode1 = NodeUtil.newReturnNode(NodeUtil.newIntLiteral(1));
    Node breakNode = NodeUtil.newBreakNode(null);

    testFunctionBlockNode.addChildrenToBack(returnNode1);
    testFunctionBlockNode.addChildrenToBack(breakNode);

    return testFunctionBlockNode;
  }

  private Node createTestFunctionBlockNodeWithAssignment() {
    Node testFunctionBlockNode = new Node(Token.BLOCK);
    Node resultNameNode = NodeUtil.newNameNode("resultName");
    Node assignNode = NodeUtil.newAssignStatementNode("resultName", NodeUtil.newIntLiteral(1));

    testFunctionBlockNode.addChildrenToBack(assignNode);
    Node returnNode1 = NodeUtil.newReturnNode(resultNameNode);

    testFunctionBlockNode.addChildrenToBack(returnNode1);

    return testFunctionBlockNode;
  }

  private Node aliasAndInlineArguments(Node fnTemplateRoot, LinkedHashMap<String, Node> argMap) {
    return functionToBlockMutator.aliasAndInlineArguments(
        fnTemplateRoot, argMap, FunctionArgumentInjector.findModifiedParameters(fnTemplateRoot));
  }
}