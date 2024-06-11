import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.EMPTY));
    n.addChildToFront(new Node(Token.EMPTY));
    n.addChildToFront(new Node(Token.EMPTY));

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
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.setParent(new Node(Token.SCRIPT));

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertFalse(result);
  }
}