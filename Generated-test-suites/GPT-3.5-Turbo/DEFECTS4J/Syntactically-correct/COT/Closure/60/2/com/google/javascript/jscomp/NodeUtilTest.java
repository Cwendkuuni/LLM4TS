import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  // Test isForIn method
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.IF));
    node.addChildToFront(new Node(Token.IF));
    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);

    node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.IF));
    node.addChildToBack(new Node(Token.IF));
    node.addChildToBack(new Node(Token.IF));
    result = NodeUtil.isForIn(node);
    assertTrue(result);
  }

  // Test isStatement method
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.IF);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);

    node = new Node(Token.IF);
    parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    result = NodeUtil.isStatement(node);
    assertTrue(result);

    node = new Node(Token.IF);
    parent = new Node(Token.LABEL);
    parent.addChildToFront(node);
    result = NodeUtil.isStatement(node);
    assertTrue(result);

    node = new Node(Token.IF);
    parent = null;
    try {
      result = NodeUtil.isStatement(node);
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  // Test isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);

    node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);

    node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    parent = new Node(Token.LABEL);
    parent.addChildToFront(node);
    result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);

    node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    parent = null;
    try {
      result = NodeUtil.isFunctionDeclaration(node);
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  // Test isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);

    node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);

    node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    parent = new Node(Token.FUNCTION);
    parent.addChildToFront(new Node(Token.SCRIPT));
    parent.addChildToBack(node);
    result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);

    node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    parent = null;
    try {
      result = NodeUtil.isHoistedFunctionDeclaration(node);
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }
}