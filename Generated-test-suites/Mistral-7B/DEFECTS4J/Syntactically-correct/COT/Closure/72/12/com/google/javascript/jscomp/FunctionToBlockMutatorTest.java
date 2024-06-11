package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {

  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    safeNameIdSupplier = () -> "test_id";
    functionToBlockMutator = new FunctionToBlockMutator(null, safeNameIdSupplier);
  }

  @Test
  public void testMutate_FunctionWithNoArguments() {
    String fnName = "test_function";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCallNode(Token.CALL, Node.newNameNode(fnName), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    String fnName = "test_function";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCallNode(Token.CALL, Node.newNameNode(fnName),
        Node.newArrayNode(Node.newNameNode("arg1"), Node.newNameNode("arg2")));
    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newNumberNode(1));
    argMap.put("arg2", Node.newNumberNode(2));
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, false);
    assertNotNull(resultNode);
  }

  @Test
  public void testReplaceReturns_WithResult() {
    String fnName = "test_function";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCallNode(Token.CALL, Node.newNameNode(fnName), null);
    String resultName = "test_result";
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, true, false);
    Node expectedResultNode = createExpectedResultNode(resultName, "test_result");
    assertNodeEquals(expectedResultNode, resultNode);
  }

  @Test
  public void testReplaceReturns_WithoutResult() {
    String fnName = "test_function";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCallNode(Token.CALL, Node.newNameNode(fnName), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, false);
    assertNull(resultNode.getFirstChild());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_CallInLoopWithArguments() {
    String fnName = "test_function";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCallNode(Token.CALL, Node.newNameNode(fnName),
        Node.newArrayNode(Node.newNameNode("arg1"), Node.newNameNode("arg2")));
    functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_CallInLoopWithoutArguments() {
    String fnName = "test_function";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = Node.newCallNode(Token.CALL, Node.newNameNode(fnName), null);
    functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, true);
  }

  private Node createExpectedResultNode(String resultName, String labelName) {
    Node labelNode = new Node(Token.LABEL).copyInformationFrom(Node.newNameNode(labelName));
    Node nameNode = Node.newString(Token.LABEL_NAME, labelName);
    labelNode.addChildToFront(nameNode);

    Node resultNode = NodeUtil.newVarNode(resultName, NodeUtil.newUndefinedNode(null));
    Node assignNode = NodeUtil.newAssignStatementNode(resultName, Node.newNameNode("result"));
    Node blockNode = Node.newBlockNode();
    blockNode.addChildrenToBack(resultNode, assignNode, labelNode);

    return NodeUtil.newExpr(blockNode);
  }

  private void assertNodeEquals(Node expectedNode, Node actualNode) {
    assertEquals("Expected and actual nodes are not equal.", expectedNode, actualNode);
  }
}