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
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.EMPTY);
    n.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.EMPTY);
    n.setParent(new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.EMPTY);
    n.setParent(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.setParent(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}