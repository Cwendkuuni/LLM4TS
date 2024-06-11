import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode1 = new Node(Token.IF, new int[] {1});
    assertFalse(NodeUtil.isForIn(invalidNode1));

    Node invalidNode2 = new Node(Token.VAR, new int[] {1, 1});
    assertFalse(NodeUtil.isForIn(invalidNode2));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IDENT, new int[] {1, 1});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.ADD, new int[] {1, 2});
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[] {1, 1, 1}, 0);
    functionDeclarationNode.setProperty("name", "functionName");
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[] {1, 1, 1}, 0);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {1, 1, 1}, 0);
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[] {1}));
    hoistedFunctionDeclarationNode.setProperty("name", "hoistedFunctionDeclaration");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {1, 1, 1}, 0);
    notHoistedFunctionDeclarationNode.setParent(new Node(Token.BLOCK, new int[] {1}));
    notHoistedFunctionDeclarationNode.setProperty("name", "notHoistedFunctionDeclaration");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}