import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.VAR));
    n.addChildToFront(new Node(Token.STRING));
    n.addChildToFront(new Node(Token.NUMBER));
    
    boolean result = NodeUtil.isForIn(n);
    
    assertEquals(false, result);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assertEquals(false, result);
  }
}