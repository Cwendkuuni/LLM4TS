import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    // Test when the node represents a FOR-IN loop
    Node node1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node1));
    
    // Test when the node does not represent a FOR-IN loop
    Node node2 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    // Test when the node is used as a statement
    Node parent1 = new Node(Token.SCRIPT);
    Node node1 = new Node(Token.EMPTY);
    parent1.addChildToFront(node1);
    assertTrue(NodeUtil.isStatement(node1));
    
    Node parent2 = new Node(Token.BLOCK);
    Node node2 = new Node(Token.EMPTY);
    parent2.addChildToFront(node2);
    assertTrue(NodeUtil.isStatement(node2));
    
    Node parent3 = new Node(Token.LABEL);
    Node node3 = new Node(Token.EMPTY);
    parent3.addChildToFront(node3);
    assertTrue(NodeUtil.isStatement(node3));
    
    // Test when the node is not used as a statement
    Node parent4 = new Node(Token.EMPTY);
    Node node4 = new Node(Token.EMPTY);
    parent4.addChildToFront(node4);
    assertFalse(NodeUtil.isStatement(node4));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    // Test when the node is a function declaration
    Node parent1 = new Node(Token.SCRIPT);
    Node node1 = new Node(Token.FUNCTION);
    parent1.addChildToFront(node1);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    
    // Test when the node is not a function declaration
    Node parent2 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.EMPTY);
    parent2.addChildToFront(node2);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test when the node is a hoisted function declaration
    Node grandParent1 = new Node(Token.SCRIPT);
    Node parent1 = new Node(Token.FUNCTION);
    grandParent1.addChildToFront(parent1);
    Node node1 = new Node(Token.FUNCTION);
    parent1.addChildToFront(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    
    // Test when the node is not a hoisted function declaration
    Node parent2 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(node2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    
    Node parent3 = new Node(Token.SCRIPT);
    Node node3 = new Node(Token.EMPTY);
    parent3.addChildToFront(node3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}