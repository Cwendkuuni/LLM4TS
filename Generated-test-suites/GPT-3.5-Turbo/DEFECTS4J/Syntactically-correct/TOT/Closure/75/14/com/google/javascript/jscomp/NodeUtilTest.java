import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING, "x"));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsForIn_emptyNode() {
    Node node = new Node();

    boolean result = NodeUtil.isForIn(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsStatement_typical() {
    Node node = new Node();
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isStatement(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node();
    Node parent = new Node(Token.OTHER);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isStatement(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.OTHER);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertEquals(false, result);
  }
}