import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node1));

    // Edge case
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node1));

    // Edge case
    Node node2 = new Node(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Edge case
    Node node2 = new Node(Token.STRING);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario
    Node node3 = new Node(Token.FUNCTION);
    node3.setParent(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Edge case
    Node node2 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }

}