import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.setChild(0, new Node(Token.IDENT, "for"));
    forInNode.setChild(1, new Node(Token.ASSIGN, new Node(Token.IDENT, "i"), new Node(Token.NUMBER, 0)));
    forInNode.setChild(2, new Node(Token.KEYWORD, "in"));
    Node listNode = new Node(Token.IDENT, "list");
    forInNode.setParent(listNode);

    assertTrue(NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsForIn_False() {
    Node notForInNode = new Node(Token.IF);
    Node listNode = new Node(Token.IDENT, "list");
    notForInNode.setParent(listNode);

    assertFalse(NodeUtil.isForIn(notForInNode));
  }
}