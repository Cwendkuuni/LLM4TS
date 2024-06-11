import org.junit.Test;
import org.junit.Assert;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, createNode(), createNode(), createNode());
    boolean result = NodeUtil.isForIn(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_false() {
    Node node = createNode(Token.FOR, createNode(), createNode());
    boolean result = NodeUtil.isForIn(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(Token.SCRIPT, createNode());
    boolean result = NodeUtil.isStatement(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_false() {
    Node node = createNode(Token.NAME);
    boolean result = NodeUtil.isStatement(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, createNode(), createNode());
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = createNode(Token.NAME);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, createNode(), createNode());
    node.getParent().setType(Token.SCRIPT);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = createNode(Token.NAME);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  // Additional test cases for edge cases and exceptions

  @Test
  public void testIsForIn_emptyNode() {
    Node node = createNode(Token.FOR);
    boolean result = NodeUtil.isForIn(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsForIn_invalidNodeType() {
    Node node = createNode(Token.WHILE, createNode(), createNode(), createNode());
    boolean result = NodeUtil.isForIn(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_noParent() {
    Node node = createNode(Token.FUNCTION, createNode(), createNode());
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_noParent() {
    Node node = createNode(Token.SCRIPT, createNode());
    boolean result = NodeUtil.isStatement(node);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_invalidParentType() {
    Node node = createNode(Token.FUNCTION, createNode(), createNode());
    node.getParent().setType(Token.WHILE);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertFalse(result);
  }

  // Helper methods for creating test nodes

  private Node createNode() {
    return new Node();
  }

  private Node createNode(int type, Node... children) {
    Node node = new Node(type);
    for (Node child : children) {
      node.addChildToBack(child);
    }
    return node;
  }
}