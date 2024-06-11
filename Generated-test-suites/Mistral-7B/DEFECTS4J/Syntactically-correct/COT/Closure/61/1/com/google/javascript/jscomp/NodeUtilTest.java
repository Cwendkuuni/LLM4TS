import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = Node.newNode(Token.IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] {1, 2});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.IDENT, new int[] {"expr"});
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(
        Token.FUNCTION, new int[] {Token.IDENT, "funcName", Token.LBRACE, Token.RBRACE},
        null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newNode(
        Token.FUNCTION, new int[] {Token.IDENT, "funcName", Token.LBRACE, Token.RBRACE},
        Node.newNode(Token.ASSIGN, new int[] {Token.IDENT, "expr"}));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(
        Token.FUNCTION, new int[] {Token.IDENT, "funcName", Token.LBRACE, Token.RBRACE},
        Node.newNode(Token.SCRIPT, new int[] {}));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = Node.newNode(
        Token.FUNCTION, new int[] {Token.IDENT, "funcName", Token.LBRACE, Token.RBRACE},
        Node.newNode(Token.BLOCK, new int[] {}, Node.newNode(Token.IDENT, new int[] {"expr"})));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}