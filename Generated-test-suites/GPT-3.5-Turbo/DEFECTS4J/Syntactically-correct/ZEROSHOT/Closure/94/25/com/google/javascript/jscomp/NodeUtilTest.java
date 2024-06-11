import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.FOR);
    Node child2 = new Node(Token.FOR);
    Node child3 = new Node(Token.FOR);
    node.addChildToFront(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    boolean result = NodeUtil.isForIn(node);
    assertEquals(true, result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);
    node.addChildToBack(parent);

    boolean result = NodeUtil.isStatement(node);
    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    node.addChildToBack(parent);

    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    node.addChildToBack(parent);
    parent.addChildToBack(grandparent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertEquals(true, result);
  }
}