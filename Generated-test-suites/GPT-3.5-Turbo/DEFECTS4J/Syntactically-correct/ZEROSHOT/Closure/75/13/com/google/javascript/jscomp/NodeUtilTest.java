import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_true() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.NUMBER));
    node.addChildToBack(new Node(Token.TRUE));

    boolean result = NodeUtil.isForIn(node);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.NUMBER));

    boolean result = NodeUtil.isForIn(node);

    assertFalse(result);
  }

  @Test
  public void testIsStatement_true() {
    Node node = new Node(Token.SCRIPT);
    Node childNode = new Node(Token.STRING);
    node.addChildToBack(childNode);

    boolean result = NodeUtil.isStatement(childNode);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.STRING);

    boolean result = NodeUtil.isStatement(node);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertFalse(result);
  }
}