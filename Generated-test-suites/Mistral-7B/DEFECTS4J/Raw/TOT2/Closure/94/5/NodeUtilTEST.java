import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    assertTrue(NodeUtil.isForIn(forNode));

    Node invalidNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(invalidNode));
  }
}