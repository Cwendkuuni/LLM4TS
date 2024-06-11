
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.EMPTY));
    n.addChildToFront(new Node(Token.EMPTY));
    n.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_notForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.EMPTY));
    n.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.EMPTY));
    n.getParent().setType(Token.SCRIPT);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.EMPTY));
    n.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}