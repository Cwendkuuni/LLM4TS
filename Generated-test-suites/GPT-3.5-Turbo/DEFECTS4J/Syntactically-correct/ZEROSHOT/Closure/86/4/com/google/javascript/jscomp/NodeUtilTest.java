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
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SOMETHING);
    Node parent = new Node(Token.SCRIPT, n);
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.SOMETHING);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT, n);
    n.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT, n);
    Node grandParent = new Node(Token.FUNCTION, parent);
    n.setParent(parent);
    parent.setParent(grandParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK, n);
    Node grandParent = new Node(Token.FUNCTION, parent);
    n.setParent(parent);
    parent.setParent(grandParent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}