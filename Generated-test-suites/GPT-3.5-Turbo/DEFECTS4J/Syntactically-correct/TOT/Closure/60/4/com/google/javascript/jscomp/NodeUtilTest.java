import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.NAME));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node1));

    // Edge case: node not representing a FOR-IN loop
    Node node2 = new Node(Token.WHILE);
    assertFalse(NodeUtil.isForIn(node2));

    // Error scenario: null node
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.NAME);
    node1.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isStatement(node1));

    // Edge case: node not used as a statement
    Node node2 = new Node(Token.EMPTY);
    node2.setParent(new Node(Token.ROOT));
    assertFalse(NodeUtil.isStatement(node2));

    // Error scenario: null node
    try {
      NodeUtil.isStatement(null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.setType(Token.FUNCTION);
    node1.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Edge case: function is part of an expression
    Node node2 = new Node(Token.FUNCTION);
    node2.setType(Token.FUNCTION);
    node2.setParent(new Node(Token.NAME));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario: null node
    assertFalse(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.setType(Token.FUNCTION);
    node1.setParent(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Edge case: function declaration in scope root
    Node node2 = new Node(Token.FUNCTION);
    node2.setType(Token.FUNCTION);
    node2.setParent(new Node(Token.SCRIPT));
    node2.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Error scenario: null node
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
  }

}