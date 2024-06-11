import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    assertEquals(true, NodeUtil.isForIn(forNode));

    Node forNodeWithTwoChildren = new Node(Token.FOR);
    forNodeWithTwoChildren.setChildCount(2);
    assertEquals(false, NodeUtil.isForIn(forNodeWithTwoChildren));

    // Add more edge cases as needed
  }

  // Test the remaining methods with similar test cases and edge cases

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT); // a statement node
    assertEquals(true, NodeUtil.isStatement(statementNode));

    Node statementNodeWithChildren = new Node(Token.BLOCK); // a statement node with children
    statementNodeWithChildren.setChildCount(2);
    assertEquals(true, NodeUtil.isStatement(statementNodeWithChildren));

    Node expressionNode = new Node(Token.ADD); // not a statement node
    assertEquals(false, NodeUtil.isStatement(expressionNode));

    // Add more edge cases as needed
  }

  // Test the remaining methods with similar test cases and edge cases

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nullNode = null; // null node
    NodeUtil.isFunctionDeclaration(nullNode);
  }

  // Test the remaining methods with similar test cases, edge cases, and error scenarios

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    // Test cases for isHoistedFunctionDeclaration
  }

  // Test the remaining methods with similar test cases, edge cases, and error scenarios
}