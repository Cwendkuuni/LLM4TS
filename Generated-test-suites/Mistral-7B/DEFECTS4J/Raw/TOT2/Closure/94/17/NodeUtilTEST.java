import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    // Basic test case
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    // Edge cases
    Node nodeFor = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(nodeFor)); // For statement with incorrect child count

    Node nodeIf = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeIf)); // If statement

    Node nodeVar = new Node(Token.VAR);
    assertFalse(NodeUtil.isForIn(nodeVar)); // Variable declaration

    Node nodeFunction = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(nodeFunction)); // Function declaration

    Node nodeArray = new Node(Token.ARRAY_LITERAL);
    assertFalse(NodeUtil.isForIn(nodeArray)); // Array literal

    // Exception handling scenarios
    Node nodeNull = null;
    try {
      NodeUtil.isForIn(nodeNull);
    } catch (NullPointerException e) {
      // Expected
    }
  }

  // Repeat for each method, adding test cases for edge cases and exceptions as necessary

  @org.junit.Test
  public void testIsStatement() {
    // Test cases and edge cases for isStatement method go here
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    // Test cases and edge cases for isFunctionDeclaration method go here
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    // Test cases and edge cases for isHoistedFunctionDeclaration method go here
  }

  // Merge all individual test cases into a complete test file
}