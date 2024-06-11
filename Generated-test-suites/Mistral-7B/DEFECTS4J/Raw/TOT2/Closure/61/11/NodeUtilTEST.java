import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in [1, 2, 3]) {}", Token.FOR);
    assertTrue(NodeUtil.isForIn(node));

    Node node2 = new Node("if (true) {} for (var i in [1, 2, 3]) {}", Token.IF);
    assertFalse(NodeUtil.isForIn(node2));
  }
}