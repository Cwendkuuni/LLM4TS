import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_true() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.NAME);
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_true() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    node.setParent(parent);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_false() {
    Node parent = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.NAME);
    node.setParent(parent);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_true() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node parent = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    function.addChildToFront(script);
    script.addChildToFront(function);
    Node node = new Node(Token.FUNCTION);
    node.setParent(function);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node parent = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
}