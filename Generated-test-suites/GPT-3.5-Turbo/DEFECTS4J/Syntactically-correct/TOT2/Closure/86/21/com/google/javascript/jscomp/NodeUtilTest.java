import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
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
    parent.addChildToFront(n);
    grandparent.addChildToFront(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}