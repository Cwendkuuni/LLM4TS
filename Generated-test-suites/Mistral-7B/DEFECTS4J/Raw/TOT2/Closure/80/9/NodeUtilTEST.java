import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, Token.IN), new Node(Token.IDENTIFIER, "array")});

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, Token.IF)})));
  }
}