import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.makeNode;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = makeNode(createToken(Token.FOR, "for"), createToken(Token.IDENT, "i"), createToken(Token.KEYWORD, "in"), makeNode(createToken(Token.IDENT, "arr"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RBRACE, "")));
    assertTrue(NodeUtil.isForIn(node));

    node = makeNode(createToken(Token.IF, "if"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RBRACE, ""));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = makeNode(createToken(Token.PRINT, "print"), createToken(Token.LPAREN, ""), createToken(Token.STRING_LITERAL, "\'Hello World\'"), createToken(Token.RPAREN, ""), createToken(Token.SEMICOLON, ""));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = makeNode(createToken(Token.LPAREN, ""), makeNode(createToken(Token.IDENT, "x"), createToken(Token.PLUS, "+"), createToken(Token.IDENT, "y")), createToken(Token.RPAREN, ""));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = makeNode(createToken(Token.FUNCTION, "func"), createToken(Token.IDENT, "func"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RBRACE, ""));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = makeNode(createToken(Token.FUNCTION, "func"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RETURN, "return"), createToken(Token.RPAREN, ""), createToken(Token.RBRACE, ""));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = makeNode(createToken(Token.SCRIPT, ""), createToken(Token.FUNCTION, "func"), createToken(Token.IDENT, "func"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RETURN, "return"), createToken(Token.RPAREN, ""), createToken(Token.RBRACE, ""), createToken(Token.LPAREN, ""));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = makeNode(createToken(Token.FUNCTION, "func"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RETURN, "return"), createToken(Token.RPAREN, ""), createToken(Token.RBRACE, ""), makeNode(createToken(Token.IF, "if"), createToken(Token.LPAREN, ""), createToken(Token.RPAREN, ""), createToken(Token.LBRACE, ""), createToken(Token.RETURN, "return"), createToken(Token.RPAREN, ""), createToken(Token.RBRACE, "")));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}