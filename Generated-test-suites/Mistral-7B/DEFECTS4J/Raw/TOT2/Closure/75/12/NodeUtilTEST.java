import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class isForInTest {
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENT), new Node(Token.ASSIGN), new Node(Token.IDENT)});

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(new Node(Token.IF)));
  }
}