import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    
    boolean result = NodeUtil.isForIn(n);
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.BLOCK);
    parent1.addChildToFront(parent2);
    parent2.addChildToBack(n);
    n.setParent(parent2);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertFalse(result);
  }

}