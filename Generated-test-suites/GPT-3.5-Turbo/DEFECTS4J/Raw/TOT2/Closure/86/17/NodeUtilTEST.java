import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(script));
  }

  @Test
  public void testIsNotStatement() {
    Node expression = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(expression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDecl = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.EMPTY)), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDecl));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node expression = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.EMPTY)), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(expression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.EMPTY)), new Node(Token.EMPTY)));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(script.getFirstChild()));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.EMPTY)), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }

}