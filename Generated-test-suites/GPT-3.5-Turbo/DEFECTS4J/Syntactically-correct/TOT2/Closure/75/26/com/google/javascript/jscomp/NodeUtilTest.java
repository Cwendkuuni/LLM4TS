import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.IF, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test(expected = NullPointerException.class)
  public void testIsForIn_nullNode() {
    Node n = null;
    NodeUtil.isForIn(n);
  }

  @Test
  public void testIsStatement_parentNull() {
    Node n = new Node(Token.IF);
    n.detachFromParent();
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsFunctionDeclaration_functionNotStatement() {
    Node n = new Node(Token.FUNCTION);
    NodeUtil.isFunctionDeclaration(n);
  }

  @Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclaration_noParent() {
    Node n = new Node(Token.FUNCTION);
    n.detachFromParent();
    NodeUtil.isHoistedFunctionDeclaration(n);
  }
}