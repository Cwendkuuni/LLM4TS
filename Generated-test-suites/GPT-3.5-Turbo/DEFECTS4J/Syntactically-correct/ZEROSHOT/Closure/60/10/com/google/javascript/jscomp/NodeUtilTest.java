import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.SCRIPT));
    n.addChildToFront(new Node(Token.BLOCK));
    n.addChildToFront(new Node(Token.LABEL));
    boolean result = NodeUtil.isForIn(n);
    assertTrue(result);
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.FOR);
    boolean result = NodeUtil.isForIn(n);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    boolean result = NodeUtil.isStatement(n);
    assertTrue(result);
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node(Token.NAME);
    n.addChildToFront(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isStatement(n);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.NAME);
    n.addChildToFront(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.SCRIPT));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.SCRIPT));
    parent.addChildToFront(n);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.SCRIPT));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.BLOCK));
    parent.addChildToFront(n);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertFalse(result);
  }

}