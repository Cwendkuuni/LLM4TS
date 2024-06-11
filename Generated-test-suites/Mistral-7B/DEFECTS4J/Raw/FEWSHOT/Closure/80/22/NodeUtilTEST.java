package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, Token.IN, 2, 3});
    Node nodeIf = Node.newNode(Token.IF, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null);
    Node nodeBlock = Node.newNode(Token.BLOCK, null);
    Node nodeFunction = Node.newExpressionFunction(Node.newName("f"), new int[0], null, null);
    Node nodeIf = Node.newNode(Token.IF, null);
    Node nodeFunctionCall = Node.newExpressionFunctionCall(Node.newName("f"), null);
    Node nodeExpression = Node.newName("x");

    // Script node is a statement
    assertTrue(NodeUtil.isStatement(nodeScript));

    // Block node is a statement
    assertTrue(NodeUtil.isStatement(nodeBlock));

    // Function declaration node is a statement
    assertTrue(NodeUtil.isStatement(nodeFunction));

    // Function expression node is not a statement (it's part of an expression)
    assertFalse(NodeUtil.isStatement(nodeFunctionCall));

    // If statement node is a statement
    assertTrue(NodeUtil.isStatement(nodeIf));

    // Expression node is not a statement
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newExpressionFunction(Node.newName("f"), new int[0], null, null);
    Node nodeFunctionExpression = Node.newFunctionExpression(Node.newName("g"), Node.newNameList(), null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newExpressionFunction(Node.newName("f"), new int[0], null, null);
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION,
        new int[] {
          Token.FUNCTION,
          Token.IDENT,
          Token.LEFT_BRACE,
          Token.RIGHT_BRACE
        },
        null,
        new Node[]{nodeFunction}
    );
    Node nodeScript = Node.newNode(Token.SCRIPT, null);
    Node nodeFunctionInScript = Node.newNode(
        Token.FUNCTION,
        new int[] {
          Token.FUNCTION,
          Token.IDENT,
          Token.LEFT_BRACE,
          Token.RIGHT_BRACE
        },
        null,
        new Node[]{nodeFunction}
    );
    Node nodeFunctionInFunction = Node.newNode(
        Token.FUNCTION,
        new int[] {
          Token.FUNCTION,
          Token.IDENT,
          Token.LEFT_BRACE,
          Token.RIGHT_BRACE
        },
        null,
        new Node[]{nodeFunctionDeclaration}
    );

    // Hoisted function declaration (function in script)
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    // Function declaration (function in function)
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));

    // Function expression
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    // Script node
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
  }

  private Node newFunctionExpression(String name, Node[] arguments, Node body, Node generator, Node prototype) {
    return Node.newFunctionExpression(
        Node.newName(name),
        arguments == null ? Node.EMPTY_NODE_LIST : Node.newNodeList(arguments),
        body,
        generator,
        prototype
    );
  }

  private Node newExpressionFunction(String name, int[] arguments, Node body, Node generator, Node prototype) {
    return Node.newExpressionFunction(
        Node.newName(name),
        arguments == null ? new int[0] : arguments,
        body,
        generator,
        prototype
    );
  }

  private Node newFunctionDeclaration(String name, int[] arguments, Node body, Node generator, Node prototype) {
    checkArgument(arguments != null, "arguments cannot be null");
    return Node.newNode(Token.FUNCTION, new int[] {
        Token.FUNCTION,
        Token.IDENT,
        Token.LEFT_BRACE,
        Token.RIGHT_BRACE
    }, null, new Node[]{
      NodeUtil.newName(name),
      Node.newNameList(arguments),
      Node.newName("arguments"),
      Node.newList(
          NodeUtil.newName("__defineGetter__"),
          NodeUtil.newName("arguments"),
          NodeUtil.newName("arguments")
      ),
      new Node[]{
        NodeUtil.newName(name),
        Node.newList(
            Node.newName("__defineGetter__"),
            NodeUtil.newName("prototype"),
            NodeUtil.newName("prototype")
        )
      },
      body
    });
  }

  private Node newName(String name) {
    return Node.newName(name);
  }

  private Node newNodeList(Node... nodes) {
    return Node.newNodeList(nodes);
  }
}