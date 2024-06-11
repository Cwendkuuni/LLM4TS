import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.IDENT, "foo"));
    nodeForIn.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "i"), new Node(Token.NUM, 0)));
    nodeForIn.setChild(2, new Node(Token.BLOCK, null));

    assertTrue(NodeUtil.isForIn(nodeForIn));
  }
}