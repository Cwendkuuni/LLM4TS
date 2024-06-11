import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_typicalCase() {
    Node node = new Node(Token.SCRIPT);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.NAME);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(grandparent);
    grandparent.addChildToFront(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
}