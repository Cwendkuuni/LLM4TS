import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR);
    node1.addChildToFront(new Node(Token.FOR));
    node1.addChildToFront(new Node(Token.BLOCK));
    Assert.assertTrue(NodeUtil.isForIn(node1));

    // Edge case: n is null
    Assert.assertFalse(NodeUtil.isForIn(null));

    // Error scenario: n is not a FOR node
    Node node2 = new Node(Token.IF);
    node2.addChildToFront(new Node(Token.BLOCK));
    node2.addChildToFront(new Node(Token.BLOCK));
    Assert.assertFalse(NodeUtil.isForIn(node2));

    // Error scenario: n has less than 3 children
    Node node3 = new Node(Token.FOR);
    node3.addChildToFront(new Node(Token.FOR));
    Assert.assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.BLOCK);
    Assert.assertTrue(NodeUtil.isStatement(node1));

    // Edge case: n is null
    Assert.assertFalse(NodeUtil.isStatement(null));

    // Error scenario: n is not a child of a valid parent
    Node node2 = new Node(Token.ASSIGN);
    Assert.assertFalse(NodeUtil.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.BLOCK));
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));

    // Edge case: n is null
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(null));

    // Error scenario: n is not a function
    Node node2 = new Node(Token.BLOCK);
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));

    // Error scenario: n is a function but not a statement
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToFront(new Node(Token.BLOCK));
    node3.getParent().setType(Token.EXPR_RESULT);
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.BLOCK));
    node1.getParent().setType(Token.SCRIPT);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    // Edge case: n is null
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));

    // Error scenario: n is not a hoisted function declaration
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.BLOCK));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

    // Error scenario: n is a hoisted function declaration but not in the scope root
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToFront(new Node(Token.BLOCK));
    node3.getParent().setType(Token.BLOCK);
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}