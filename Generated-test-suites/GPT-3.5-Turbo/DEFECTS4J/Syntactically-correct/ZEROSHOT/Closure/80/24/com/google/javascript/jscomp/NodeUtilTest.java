import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    boolean result = NodeUtil.isForIn(n);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.NAME, "test");
    boolean result = NodeUtil.isStatement(n);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "test"));
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "test"));
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertFalse(result);
  }
}