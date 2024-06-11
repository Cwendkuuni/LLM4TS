import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {

  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
  }

  @Test
  public void testIsForIn() {
    Node forNode = nodeFactory.makeNode(
        new Object[] {Token.FOR, Token.IDENT, Token.SEMICOLON, Token.ASSIGN,
            Token.IDENT, Token.KEYWORD, Token.SEMICOLON, Token.SEMICOLON,
            Token.RIGHTBRACE});
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = nodeFactory.makeNode(
        new Object[] {Token.IF, Token.LPAREN, Token.IDENT, Token.RPAREN,
            Token.LBRACE, Token.IDENT, Token.SEMICOLON, Token.IDENT,
            Token.SEMICOLON, Token.RIGHTBRACE});
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = nodeFactory.makeNode(
        new Object[] {Token.IDENT, Token.SEMICOLON});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = nodeFactory.makeNode(
        new Object[] {Token.IDENT, Token.PLUS, Token.NUMBER, Token.SEMICOLON});
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = nodeFactory.makeNode(
        new Object[] {Token.FUNCTION, Token.IDENT, Token.LPAREN, Token.RPAREN,
            Token.LBRACE, Token.RETURN, Token.NUMBER, Token.SEMICOLON,
            Token.RIGHTBRACE});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = nodeFactory.makeNode(
        new Object[] {Token.IDENT, Token.LPAREN, Token.RPAREN, Token.LBRACE,
            Token.RETURN, Token.NUMBER, Token.SEMICOLON, Token.RIGHTBRACE});
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = nodeFactory.makeNode(
        new Object[] {Token.FUNCTION, Token.IDENT, Token.LPAREN, Token.RPAREN,
            Token.LBRACE, Token.RETURN, Token.NUMBER, Token.SEMICOLON,
            Token.RIGHTBRACE, Token.SEMICOLON});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = nodeFactory.makeNode(
        new Object[] {Token.FUNCTION, Token.IDENT, Token.LPAREN, Token.RPAREN,
            Token.LBRACE, Token.RETURN, Token.NUMBER, Token.SEMICOLON,
            Token.IDENT, Token.SEMICOLON, Token.RIGHTBRACE});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}