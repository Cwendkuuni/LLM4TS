import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForInTrue() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.VAR));
    node.addChildToBack(new Node(Token.TRUE));
    node.addChildToBack(new Node(Token.FALSE));
    
    boolean result = NodeUtil.isForIn(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsForInFalse() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.VAR));
    
    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
  }
  
  @Test
  public void testIsStatementTrue() {
    Node node = new Node(Token.EXPR_RESULT);
    
    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsStatementFalse() {
    Node node = new Node(Token.AND);
    
    boolean result = NodeUtil.isStatement(node);
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclarationTrue() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = new Node(Token.VAR);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclarationTrue() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    node.setParent(new Node(Token.SCRIPT));
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
  
}