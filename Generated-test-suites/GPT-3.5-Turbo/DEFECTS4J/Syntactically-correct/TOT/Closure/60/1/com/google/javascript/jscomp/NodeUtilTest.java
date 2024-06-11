import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case: a node represents a FOR-IN loop
    Node node1 = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(node1));

    // Edge case: a node does not represent a FOR-IN loop
    Node node2 = new Node(Token.WHILE, new Node(), new Node(), new Node());
    assertFalse(NodeUtil.isForIn(node2));

    // Error scenario: null node
    Node node3 = null;
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case: a node is used as a statement
    Node node1 = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isStatement(node1));

    // Edge case: a node is not used as a statement
    Node node2 = new Node(Token.NAME, new Node(), new Node(), new Node());
    assertFalse(NodeUtil.isStatement(node2));

    // Error scenario: null node
    Node node3 = null;
    assertFalse(NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: a node is a function declaration
    Node node1 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Edge case: a node is not a function declaration
    Node node2 = new Node(Token.IF, new Node(), new Node(), new Node());
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario: null node
    Node node3 = null;
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: a node is a hoisted function declaration
    Node node1 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
    Node parentNode1 = new Node(Token.SCRIPT);
    parentNode1.addChildToFront(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Edge case: a node is not a hoisted function declaration
    Node node2 = new Node(Token.BOOLEAN, new Node(), new Node(), new Node());
    Node parentNode2 = new Node(Token.MODULE_BODY);
    parentNode2.addChildToFront(node2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Error scenario: null node
    Node node3 = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}