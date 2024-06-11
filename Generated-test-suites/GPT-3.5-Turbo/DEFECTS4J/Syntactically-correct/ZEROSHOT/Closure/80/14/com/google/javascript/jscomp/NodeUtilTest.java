import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node();
    n.setType(Token.FOR);
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());
    
    boolean result = NodeUtil.isForIn(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node();
    n.setType(Token.WHILE);
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());
    
    boolean result = NodeUtil.isForIn(n);
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node();
    Node parent = new Node();
    parent.setType(Token.SCRIPT);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node();
    Node parent = new Node();
    parent.setType(Token.EXPR_RESULT);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    n.addChildToBack(new Node());
    
    Node parent = new Node();
    parent.setType(Token.SCRIPT);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_NotDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    n.addChildToBack(new Node());
    
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    n.addChildToBack(new Node());
    
    Node parent = new Node();
    parent.setType(Token.SCRIPT);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    n.addChildToBack(new Node());
    
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertFalse(result);
  }
  
}