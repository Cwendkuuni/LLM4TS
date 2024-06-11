import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.EXPR_RESULT);
    Node child3 = new Node(Token.SCRIPT);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    
    boolean result = NodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForNotIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.EXPR_RESULT);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    
    boolean result = NodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.EXPR_RESULT);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.ADD);
    
    boolean result = NodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.VAR);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    node.addChildToBack(parent1);
    parent1.addChildToBack(parent2);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    node.addChildToBack(parent1);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
}