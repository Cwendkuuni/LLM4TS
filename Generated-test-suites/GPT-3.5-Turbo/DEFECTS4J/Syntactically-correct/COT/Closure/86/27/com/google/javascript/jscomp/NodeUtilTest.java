
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.EMPTY, new Node(Token.FOR)));
    n.addChildToFront(new Node(Token.EMPTY, new Node(Token.FOR)));
    n.addChildToFront(new Node(Token.EMPTY, new Node(Token.FOR)));

    boolean result = NodeUtil.isForIn(n);

    // Test case: node represents a FOR-IN loop
    assertTrue(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);

    boolean result = NodeUtil.isStatement(n);

    // Test case: node is used as a statement
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));

    boolean result = NodeUtil.isFunctionDeclaration(n);

    // Test case: node is a function declaration
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(new Node(Token.SCRIPT));

    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.setParent(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    // Test case: node is a hoisted function declaration
    assertTrue(result);
  }
}
