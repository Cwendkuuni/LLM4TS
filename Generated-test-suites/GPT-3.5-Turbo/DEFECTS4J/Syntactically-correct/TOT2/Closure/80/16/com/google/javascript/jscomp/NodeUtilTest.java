import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));

    Node n2 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n));

    Node n2 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    Node n2 = new Node(Token.VAR, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Add test cases for isHoistedFunctionDeclaration method here
  }
}