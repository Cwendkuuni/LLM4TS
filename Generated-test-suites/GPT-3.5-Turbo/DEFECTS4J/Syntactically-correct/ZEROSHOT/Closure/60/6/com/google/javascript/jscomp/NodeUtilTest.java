import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node());
    n.addChildToFront(new Node());
    
    boolean result = NodeUtil.isForIn(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node());
    n.addChildToFront(new Node());
    n.addChildToFront(new Node());
    
    boolean result = NodeUtil.isForIn(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node();
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node();
    Node parent = new Node(Token.NOT_SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_NotDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NOT_SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    grandParent.addChildToFront(n);
    parent.addChildToFront(grandParent);
    n.setParent(parent);
    grandParent.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assertFalse(result);
  }
}