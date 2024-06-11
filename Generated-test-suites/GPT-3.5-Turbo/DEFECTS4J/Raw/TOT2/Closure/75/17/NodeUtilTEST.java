import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    // Typical use case
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node));
    
    // Edge case
    node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    // Typical use case
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME, parent);
    assertTrue(NodeUtil.isStatement(node));
    
    // Exception handling
    node = new Node(Token.FUNCTION);
    assertThrows(IllegalStateException.class, () -> NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    // Edge case
    parent = new Node(Token.BLOCK);
    node = new Node(Token.FUNCTION, parent);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node grandParent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION, grandParent);
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    // Edge case
    parent = new Node(Token.SCRIPT);
    node = new Node(Token.FUNCTION, parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}