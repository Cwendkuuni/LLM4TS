import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(node);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(node);

    assertFalse(result);
  }
}