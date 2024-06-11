import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.setChildCount(3);
    assertTrue(NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsNotForIn() {
    Node notForInNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(notForInNode));
  }
}