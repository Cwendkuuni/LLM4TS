import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsForIn_withInvalidNode() {
    Node node = new Node(Token.SCRIPT);

    boolean result = NodeUtil.isForIn(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node childNode = new Node(Token.EMPTY);
    node.addChildToBack(childNode);

    boolean result = NodeUtil.isStatement(childNode);

    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_withInvalidNode() {
    Node node = new Node(Token.BLOCK);

    boolean result = NodeUtil.isStatement(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration_withInvalidNode() {
    Node node = new Node(Token.EMPTY);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent.addChildToBack(grandparent);
    grandparent.addChildToBack(node);
    node.setParent(grandparent);
    grandparent.setParent(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withInvalidNode() {
    Node node = new Node(Token.FUNCTION);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertEquals(false, result);
  }
}