import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    // Test typical case where the node represents a FOR-IN loop
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.IN));
    n.addChildToFront(new Node(Token.STRING));
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_notForIn() {
    // Test case where the node does not represent a FOR-IN loop
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.PARAM_LIST));
    n.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_notEnoughChildren() {
    // Test case where the node does not have enough children
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.IN));
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement_typical() {
    // Test typical case where the node is used as a statement
    Node n = new Node(Token.SCRIPT);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.EXPR_RESULT));
    n.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_notStatement() {
    // Test case where the node is not used as a statement
    Node n = new Node(Token.NAME);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_typical() {
    // Test typical case where the node is a function declaration
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.PARAM_LIST));
    n.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    // Test case where the node is not a function declaration
    Node n = new Node(Token.SCRIPT);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.EXPR_RESULT));
    n.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    // Test typical case where the node is a hoisted function declaration
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.PARAM_LIST));
    n.addChildToFront(new Node(Token.BLOCK));
    n = new Node(Token.SCRIPT, n);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    // Test case where the node is not a hoisted function declaration
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.PARAM_LIST));
    n.addChildToFront(new Node(Token.BLOCK));
    Node parent = new Node(Token.FUNCTION, n);
    n = new Node(Token.SCRIPT, parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}