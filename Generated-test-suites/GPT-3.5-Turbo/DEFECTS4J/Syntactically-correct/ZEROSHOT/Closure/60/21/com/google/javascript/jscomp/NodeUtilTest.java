import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_false() {
    Node n = new Node(Token.WHILE, new Node(), new Node());
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT, new Node());
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_false() {
    Node n = new Node(Token.NAME, new Node());
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node());
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node n = new Node(Token.NAME, new Node());
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node());
    n.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node n = new Node(Token.FUNCTION, new Node());
    n.getParent().setType(Token.NAME);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}