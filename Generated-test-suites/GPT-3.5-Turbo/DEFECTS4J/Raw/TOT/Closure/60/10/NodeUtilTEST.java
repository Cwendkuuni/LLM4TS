import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_notForInLoop() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsForIn_invalidNode() {
    Node node = null;
    
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
    Node node = new Node(Token.STRING);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_invalidNode() {
    Node node = null;
    
    boolean result = NodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.IF);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_invalidNode() {
    Node node = null;
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToFront(functionNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    Node grandparentNode = new Node(Token.IF);
    grandparentNode.addChildToBack(functionNode);
    parentNode.addChildToFront(grandparentNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_invalidNode() {
    Node node = null;
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
}