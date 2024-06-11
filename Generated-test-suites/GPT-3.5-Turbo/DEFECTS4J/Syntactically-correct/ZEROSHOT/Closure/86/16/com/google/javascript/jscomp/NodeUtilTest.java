import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.EMPTY);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);
    
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);
    
    boolean result = nodeUtil.isForIn(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotForIn() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.EMPTY);
    
    boolean result = nodeUtil.isForIn(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.SCRIPT);
    
    boolean result = nodeUtil.isStatement(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotStatement() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.EMPTY);
    
    boolean result = nodeUtil.isStatement(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    
    boolean result = nodeUtil.isFunctionDeclaration(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);
    
    boolean result = nodeUtil.isFunctionDeclaration(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    grandParent.addChildToFront(n);
    parent.addChildToFront(grandParent);
    
    boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    NodeUtil nodeUtil = new NodeUtil();
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    
    boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
    
    assertFalse(result);
  }
}