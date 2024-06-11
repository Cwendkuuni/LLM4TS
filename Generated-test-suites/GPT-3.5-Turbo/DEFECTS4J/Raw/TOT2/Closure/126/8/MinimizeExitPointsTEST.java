import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {
  
  @Test
  public void testTryMinimizeExits() {
    // Test if a label with a break is removed
    // Given
    MinimizeExitPoints testObj = new MinimizeExitPoints();
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.BREAK));
    
    // When
    testObj.tryMinimizeExits(node, Token.BREAK, "");
    
    // Then
    assertEquals(0, node.getChildCount());
  }
  
  @Test
  public void testMatchingExitNode() {
    // Test if matching exit node works for return type
    // Given
    Node returnNode = new Node(Token.RETURN);
    
    // When & Then
    assertTrue(matchingExitNode(returnNode, Token.RETURN, null));
    
    // Given
    Node returnNodeWithValue = new Node(Token.RETURN, new Node(Token.NAME, "value"));
    
    // When & Then
    assertFalse(matchingExitNode(returnNodeWithValue, Token.RETURN, null));
  }
  
  @Test
  public void testMoveAllFollowing() {
    // Test if all following nodes are correctly moved
    // Given
    Node srcParent = new Node(Token.BLOCK);
    Node destParent = new Node(Token.BLOCK);
    Node start = new Node(Token.LABEL);
    Node node1 = new Node(Token.RETURN);
    Node node2 = new Node(Token.BREAK);
    
    srcParent.addChildToBack(start);
    srcParent.addChildToBack(node1);
    srcParent.addChildToBack(node2);
    
    // When
    moveAllFollowing(start, srcParent, destParent);
    
    // Then
    assertEquals(1, srcParent.getChildCount());
    assertEquals(2, destParent.getChildCount());
  }
}