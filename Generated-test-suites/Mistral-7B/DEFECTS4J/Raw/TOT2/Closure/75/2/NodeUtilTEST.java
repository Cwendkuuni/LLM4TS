import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENTIFIER, "name"));
    forNode.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENTIFIER, "array"), new Node(Token.LITERAL, "[]")));
    forNode.setChild(2, new Node(Token.LEFT_PAREN, null));
    forNode.setChild(3, new Node(Token.RIGHT_PAREN, null));

    Node nonForNode = new Node(Token.IF);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(nonForNode));
  }
}