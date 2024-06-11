import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENT, "var"));
    forNode.setChild(1, new Node(Token.IDENT, "i"));
    forNode.setChild(2, new Node(Token.LEFT_PAREN, null));
    forNode.setChild(3, new Node(Token.IDENT, "arr"));
    forNode.setChild(4, new Node(Token.SEMICOLON, null));
    forNode.setChild(5, new Node(Token.KEYWORD, "in"));
    Node trueNode = new Node(Token.BOOLEAN, true);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(new Node(Token.FUNCTION, null)));

    // Test edge case: For loop with no 'in' keyword
    Node falseNode = new Node(Token.BOOLEAN, false);
    forNode.setChild(5, falseNode);
    assertFalse(NodeUtil.isForIn(forNode));
  }

  // Test cases for other methods can be added here
}