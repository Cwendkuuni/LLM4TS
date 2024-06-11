
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  //Test isForIn method
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, null);
    Node child1 = new Node(Token.EMPTY, null);
    Node child2 = new Node(Token.EMPTY, null);
    Node child3 = new Node(Token.EMPTY, null);
    n.addChildToFront(child1);
    n.addChildToFront(child2);
    n.addChildToFront(child3);
    
    assertTrue(NodeUtil.isForIn(n));
    
    child1 = new Node(Token.EMPTY, null);
    child2 = new Node(Token.EMPTY, null);
    n = new Node(Token.EMPTY, null);
    n.addChildToFront(child1);
    n.addChildToFront(child2);
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  //Test isStatement method
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT, null);
    Node parent = new Node(Token.EMPTY, null);
    parent.addChildToFront(n);
    
    assertTrue(NodeUtil.isStatement(n));
    
    n = new Node(Token.FUNCTION, null);
    parent = new Node(Token.EMPTY, null);
    parent.addChildToFront(n);
    
    assertTrue(NodeUtil.isStatement(n));
    
    n = new Node(Token.EMPTY, null);
    parent.addChildToFront(n);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  //Test isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, null);
    Node parent = new Node(Token.SCRIPT, null);
    parent.addChildToFront(n);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    parent = new Node(Token.BLOCK, null);
    parent.addChildToFront(n);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    parent = new Node(Token.EMPTY, null);
    parent.addChildToFront(n);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  //Test isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, null);
    Node parent = new Node(Token.SCRIPT, null);
    parent.addChildToFront(n);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    
    parent = new Node(Token.FUNCTION, null);
    parent.addChildToFront(n);
    parent = new Node(Token.SCRIPT, null);
    parent.addChildToFront(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    
    parent = new Node(Token.EMPTY, null);
    parent.addChildToFront(n);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}
