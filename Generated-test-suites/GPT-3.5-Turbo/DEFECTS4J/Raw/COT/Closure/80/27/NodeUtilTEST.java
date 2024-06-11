import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    parent.setParent(grandparent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node parent = new Node(Token.EMPTY);
    Node grandparent = new Node(Token.EMPTY);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    parent.setParent(grandparent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}