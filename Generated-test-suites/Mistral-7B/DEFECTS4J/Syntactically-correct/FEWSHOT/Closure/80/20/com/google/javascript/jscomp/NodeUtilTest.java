package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[] {0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = new Node(Token.FUNCTION, new int[] {0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = new Node(Token.IF, new int[] {0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeVariable = new Node(Token.VAR, new int[] {0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeVariable));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {0, 0, 0});
    Node functionName = new Node(Token.IDENT, new int[] {1, 0, 0});
    nodeFunction.setFirstChild(functionName);

    Node nodeFunctionWithArguments = new Node(Token.FUNCTION, new int[] {0, 1, 0});
    Node functionName2 = new Node(Token.IDENT, new int[] {1, 0, 0});
    Node argument = new Node(Token.IDENT, new int[] {1, 0, 1});
    nodeFunctionWithArguments.setFirstChild(functionName2);
    nodeFunctionWithArguments.setLastChild(argument);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionWithArguments));

    Node nodeFunctionWithoutName = new Node(Token.FUNCTION, new int[] {0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionWithoutName));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[] {0, 0, 0});
    Node functionDeclaration = new Node(Token.FUNCTION, new int[] {0, 0, 0});
    Node functionName = new Node(Token.IDENT, new int[] {1, 0, 0});
    functionDeclaration.setFirstChild(functionName);
    nodeScript.setFirstChild(functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node nodeFunction = new Node(Token.FUNCTION, new int[] {0, 0, 0});
    Node functionName2 = new Node(Token.IDENT, new int[] {1, 0, 0});
    Node argument = new Node(Token.IDENT, new int[] {1, 0, 1});
    nodeFunction.setFirstChild(functionName2);
    nodeFunction.setLastChild(argument);
    Node block = new Node(Token.BLOCK, new int[] {0, 0, 0});
    nodeFunction.setParent(block);
    nodeScript.setFirstChild(block);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }

  private void assertFalse(boolean condition) {
    // Do nothing
  }

  private void checkFunctionNode(Node node, int type, int numChildren) {
    Preconditions.checkState(node.getType() == type);
    Preconditions.checkState(node.getChildCount() == numChildren);
  }

  private void checkFunctionNode(Node node, int type, int numChildren, Node... children) {
    Preconditions.checkState(node.getType() == type);
    Preconditions.checkState(node.getChildCount() == numChildren);
    for (int i = 0; i < numChildren; ++i) {
      Preconditions.checkState(node.getChild(i).equals(children[i]));
    }
  }

  @Test
  public void testCreateFunctionNode() {
    Node functionDeclaration = NodeUtil.createFunctionNode("foo", new Node[0]);
    checkFunctionNode(functionDeclaration, Token.FUNCTION, 2);

    Node functionDeclarationWithArguments = NodeUtil.createFunctionNode("foo", new Node[] {"arg1", "arg2"});
    checkFunctionNode(functionDeclarationWithArguments, Token.FUNCTION, 2);
    Node functionName = functionDeclarationWithArguments.getFirstChild();
    Node arguments = functionDeclarationWithArguments.getLastChild();
    Preconditions.checkState(functionName.getType() == Token.IDENT);
    Preconditions.checkState(functionName.toString().equals("foo"));
    Preconditions.checkState(arguments.getType() == Token.LIST);
    checkFunctionNode(arguments, Token.LIST, 2);
    Node arg1 = arguments.getFirstChild();
    Node arg2 = arguments.getLastChild();
    Preconditions.checkState(arg1.getType() == Token.IDENT);
    Preconditions.checkState(arg1.toString().equals("arg1"));
    Preconditions.checkState(arg2.getType() == Token.IDENT);
    Preconditions.checkState(arg2.toString().equals("arg2"));
  }
}