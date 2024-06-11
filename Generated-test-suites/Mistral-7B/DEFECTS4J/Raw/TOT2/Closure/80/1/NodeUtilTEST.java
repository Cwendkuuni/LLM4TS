import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }
}