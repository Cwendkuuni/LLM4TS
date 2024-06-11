import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createMockNode(Token.FOR, 3);
    assertTrue(NodeUtil.isForIn(node));

    node = createMockNode(Token.IF, 2);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createMockNode(Token.SCRIPT, 0);
    assertTrue(NodeUtil.isStatement(node));

    node = createMockNode(Token.IF, 2);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createMockNode(Token.FUNCTION, 0);
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = createMockNode(Token.VAR, 1);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createMockNode(Token.FUNCTION, 0);
    node.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = createMockNode(Token.VAR, 1);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  // Additional test cases specifically for edge cases and exceptions

  @Test
  public void testIsForIn_NullNode() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatement_NullNode() {
    assertFalse(NodeUtil.isStatement(null));
  }

  @Test
  public void testIsFunctionDeclaration_NullNode() {
    assertFalse(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullNode() {
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  private Node createMockNode(int type, int childCount) {
    Node node = new Node();
    node.setType(type);
    for (int i = 0; i < childCount; i++) {
      Node child = new Node();
      node.addChildToBack(child);
    }
    return node;
  }
}