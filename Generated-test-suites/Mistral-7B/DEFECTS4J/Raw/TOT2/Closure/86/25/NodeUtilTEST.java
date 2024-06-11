import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node("for", new int[]{Token.FOR, Token.IDENT, Token.ASSIGN, Token.KEYWORD, Token.LPAREN, Token.IDENT, Token.RPAREN, Token.LPAREN, Token.RPAREN, Token.SEMICOLON, Token.LPAREN, Token.RPAREN, Token.SEMICOLON, Token.SEMICOLON});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node("if", new int[]{Token.IF, Token.LPAREN, Token.RPAREN, Token.LPAREN, Token.RPAREN});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }
}