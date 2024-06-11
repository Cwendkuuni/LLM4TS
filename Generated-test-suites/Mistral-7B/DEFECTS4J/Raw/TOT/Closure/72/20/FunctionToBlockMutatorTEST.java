import static com.google.javascript.jscomp.FunctionToBlockMutator.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = () -> "test_id";
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, Token.VAR_PARAMS, null,
        NodeUtil.newBlock(Token.BLOCK, Node.newPrintStatement(Token.PRINT,
            NodeUtil.newStringLiteral(Token.STRING, "Hello World"))));
    Node callNode = Node.newCallExpression(Token.CALL, Node.newIdentifier(
        Token.NAME, fnName), NodeUtil.newList(NodeUtil.newNullNode()));
    Node resultName = Node.newIdentifier(Token.NAME, "result");
    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, false, false);

    // Check that the function body was cloned and unique local names were assigned.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(fnNode.getLastChild().getLastChild().toString(),
        newBlock.toString());
    // Check that local variables were assigned unique names.
    assertEquals(2, getNumberOfNodesWithName(newBlock, "temp"));

    // Check that the result was assigned to the expected name.
    Node resultNode = getNodeWithName(newBlock, resultName);
    assertNotNull(resultNode);

    // Check that the print statement was replaced with an assignment to the result.
    Node expectedPrintStatement = NodeUtil.newPrintStatement(Token.PRINT,
        NodeUtil.newExpr(resultNode));
    Node actualPrintStatement = getNodeWithType(newBlock, Token.PRINT);
    assertEquals(expectedPrintStatement.toString(), actualPrintStatement.toString());
  }

  @Test
  public void testMutateFunctionWithOneArgument() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION,
        Node.newList(Node.newIdentifier(Token.NAME, "arg")), null,
        NodeUtil.newBlock(Token.BLOCK, Node.newPrintStatement(Token.PRINT,
            NodeUtil.newExpr(NodeUtil.newIdentifier(Token.NAME, "arg")))));
    Node callNode = Node.newCallExpression(Token.CALL, Node.newIdentifier(
        Token.NAME, fnName), NodeUtil.newList(NodeUtil.newNumberLiteral(Token.INT, 1)));
    String resultName = "result";
    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, false, false);

    // Check that the function body was cloned and unique local names were assigned.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(fnNode.getLastChild().getLastChild().toString(),
        newBlock.toString());
    // Check that local variables were assigned unique names.
    assertEquals(2, getNumberOfNodesWithName(newBlock, "temp"));

    // Check that the argument was assigned to a local variable.
    Node argNode = getNodeWithName(newBlock, "arg");
    assertNotNull(argNode);
    assertEquals(argNode.getType(), Token.VAR);

    // Check that the print statement was replaced with an assignment to the result.
    Node expectedPrintStatement = NodeUtil.newPrintStatement(Token.PRINT,
        NodeUtil.newExpr(resultNode));
    Node actualPrintStatement = getNodeWithType(newBlock, Token.PRINT);
    assertEquals(expectedPrintStatement.toString(), actualPrintStatement.toString());
  }

  @Test
  public void testMutateFunctionWithMultipleArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION,
        Node.newList(Node.newIdentifier(Token.NAME, "arg1"),
            Node.newIdentifier(Token.NAME, "arg2")), null,
        NodeUtil.newBlock(Token.BLOCK, Node.newPrintStatement(Token.PRINT,
            NodeUtil.newExpr(NodeUtil.newBinaryOperator(
                Token.PLUS, NodeUtil.newIdentifier(Token.NAME, "arg1"),
                NodeUtil.newIdentifier(Token.NAME, "arg2"))))));
    Node callNode = Node.newCallExpression(Token.CALL, Node.newIdentifier(
        Token.NAME, fnName), NodeUtil.newList(
            NodeUtil.newNumberLiteral(Token.INT, 1),
            NodeUtil.newNumberLiteral(Token.INT, 2)));
    String resultName = "result";
    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, false, false);

    // Check that the function body was cloned and unique local names were assigned.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(fnNode.getLastChild().getLastChild().toString(),
        newBlock.toString());
    // Check that local variables were assigned unique names.
    assertEquals(4, getNumberOfNodesWithName(newBlock, "temp"));

    // Check that the arguments were assigned to local variables.
    Node arg1Node = getNodeWithName(newBlock, "arg1");
    Node arg2Node = getNodeWithName(newBlock, "arg2");
    assertNotNull(arg1Node);
    assertNotNull(arg2Node);
    assertEquals(arg1Node.getType(), Token.VAR);
    assertEquals(arg2Node.getType(), Token.VAR);

    // Check that the print statement was replaced with an assignment to the result.
    Node expectedPrintStatement = NodeUtil.newPrintStatement(Token.PRINT,
        NodeUtil.newExpr(resultNode));
    Node actualPrintStatement = getNodeWithType(newBlock, Token.PRINT);
    assertEquals(expectedPrintStatement.toString(), actualPrintStatement.toString());
  }

  @Test
  public void testMutateFunctionWithIsCallInLoop() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION,
        Node.newList(), null, NodeUtil.newBlock(Token.BLOCK,
            Node.newLabel(Token.LABEL, "loopLabel"),
            Node.newWhileStatement(Token.WHILE,
                Node.newIdentifier(Token.NAME, "condition"), NodeUtil.newBlock(
                    Token.BLOCK, Node.newLabel(Token.LABEL, "bodyLabel"),
                    Node.newPrintStatement(Token.PRINT, NodeUtil.newNullNode()),
                    Node.newBreakStatement(Token.BREAK, "loopLabel")))));
    Node callNode = Node.newCallExpression(Token.CALL, Node.newIdentifier(
        Token.NAME, fnName), NodeUtil.newList());
    String resultName = "result";
    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, false, true);

    // Check that the function body was cloned and unique local names were assigned.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(fnNode.getLastChild().getLastChild().toString(),
        newBlock.toString());
    // Check that local variables were assigned unique names.
    assertEquals(2, getNumberOfNodesWithName(newBlock, "temp"));

    // Check that the condition was not modified.
    Node condition = getNodeWithName(newBlock, "condition");
    assertEquals(condition.getType(), Token.VAR);
    assertEquals(condition.getFirstChild().getType(), Token.IDENTIFIER);
    assertEquals(condition.getFirstChild().getString(), "condition");

    // Check that the body label was assigned a unique name.
    Node bodyLabel = getNodeWithName(newBlock, "bodyLabel");
    assertNotNull(bodyLabel);
    assertEquals(bodyLabel.getType(), Token.LABEL);

    // Check that the print statement was replaced with an assignment to the result.
    Node expectedPrintStatement = NodeUtil.newPrintStatement(Token.PRINT,
        NodeUtil.newExpr(resultNode));
    Node actualPrintStatement = getNodeWithType(newBlock, Token.PRINT);
    assertEquals(expectedPrintStatement.toString(), actualPrintStatement.toString());
  }

  @Test
  public void testMutateFunctionWithNeedsDefaultResult() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, Node.newList(), null,
        NodeUtil.newBlock(Token.BLOCK, Node.newReturnStatement(Token.RETURN,
            NodeUtil.newNullNode())));
    Node callNode = Node.newCallExpression(Token.CALL, Node.newIdentifier(
        Token.NAME, fnName), NodeUtil.newList());
    String resultName = "result";
    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, true, false);

    // Check that the function body was cloned and unique local names were assigned.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(fnNode.getLastChild().getLastChild().toString(),
        newBlock.toString());
    // Check that local variables were assigned unique names.
    assertEquals(1, getNumberOfNodesWithName(newBlock, "temp"));

    // Check that the return statement was replaced with an assignment to the result.
    Node expectedReturnStatement = NodeUtil.newAssignStatement(Token.ASSIGN,
        resultNode, NodeUtil.newNullNode());
    Node actualReturnStatement = getNodeWithType(newBlock, Token.RETURN);
    assertEquals(expectedReturnStatement.toString(), actualReturnStatement.toString());

    // Check that the result was assigned to the expected name.
    Node resultNode = getNodeWithName(newBlock, resultName);
    assertNotNull(resultNode);

    // Check that the print statement was removed.
    Node printStatement = NodeUtil.getNodeWithType(newBlock, Token.PRINT);
    assertNull(printStatement);
  }

  @Test
  public void testMutateFunctionWithNeedsDefaultResultAndCallInLoop() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, Node.newList(), null,
        NodeUtil.newBlock(Token.BLOCK, Node.newReturnStatement(Token.RETURN,
            NodeUtil.newNullNode())));
    Node callNode = Node.newCallExpression(Token.CALL, Node.newIdentifier(
        Token.NAME, fnName), NodeUtil.newList());
    String resultName = "result";
    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, true, true);

    // Check that the function body was cloned and unique local names were assigned.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertEquals(fnNode.getLastChild().getLastChild().toString(),
        newBlock.toString());
    // Check that local variables were assigned unique names.
    assertEquals(1, getNumberOfNodesWithName(newBlock, "temp"));

    // Check that the return statement was replaced with an assignment to the result.
    Node expectedReturnStatement = NodeUtil.newAssignStatement(Token.ASSIGN,
        resultNode, NodeUtil.newNullNode());
    Node actualReturnStatement = getNodeWithType(newBlock, Token.RETURN);
    assertEquals(expectedReturnStatement.toString(), actualReturnStatement.toString());

    // Check that the result was assigned to the expected name.
    Node resultNode = getNodeWithName(newBlock, resultName);
    assertNotNull(resultNode);

    // Check that the print statement was removed.
    Node printStatement = NodeUtil.getNodeWithType(newBlock, Token.PRINT);
    assertNull(printStatement);

    // Check that the body was initialized
    Node body = NodeUtil.getFunctionBody(newFnNode);
    Node initializer = getNodeWithType(body, Token.INITIALIZER);
    assertNotNull(initializer);
    assertEquals(initializer.getType(), Token.EXPR_STATEMENT);
    Node bodyStatement = initializer.getFirstChild();
    assertEquals(bodyStatement.getType(), Token.ASSIGN);
    assertEquals(bodyStatement.getLastChild().getType(), Token.UNDEFINED);
  }
}