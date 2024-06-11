import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    // Typical use case: Node represents a FOR-IN loop
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.VAR));
    n.addChildToBack(new Node(Token.FALSE));
    n.addChildToBack(new Node(Token.TRUE));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_edge() {
    // Edge case: Node does not represent a FOR-IN loop
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.VAR));
    n.addChildToBack(new Node(Token.FALSE));

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement_typical() {
    // Typical use case: Node is used as a statement
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.VAR);
    parent.addChildToBack(n);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_edge() {
    // Edge case: Node is not used as a statement
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.VAR);
    parent.addChildToFront(n);

    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    // Typical use case: Node is a function declaration
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_edge() {
    // Edge case: Node is not a function declaration
    Node n = new Node(Token.EXPR_RESULT);
    n.addChildToBack(new Node(Token.FUNCTION));

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    // Typical use case: Node is a hoisted function declaration
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    script.addChildToBack(function);
    Node n = new Node(Token.NAME);
    function.addChildToBack(n);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_edge() {
    // Edge case: Node is not a hoisted function declaration
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    script.addChildToBack(function);
    Node n = new Node(Token.DEFAULT);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}