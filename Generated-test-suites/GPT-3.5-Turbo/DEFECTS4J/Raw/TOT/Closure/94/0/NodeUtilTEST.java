import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.VAR));
    node1.addChildToBack(new Node(Token.FALSE));
    node1.addChildToBack(new Node(Token.TRUE));
    assertTrue(NodeUtil.isForIn(node1));

    // Edge case
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.FALSE));
    node2.addChildToBack(new Node(Token.TRUE));
    assertFalse(NodeUtil.isForIn(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.EMPTY);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    assertTrue(NodeUtil.isStatement(node1));

    // Edge case
    Node node2 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(node2));

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
    Node node2 = new Node(Token.VOID);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Edge case
    Node node2 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Error scenario
    Node node3 = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}