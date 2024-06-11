import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    Node child3 = new Node(Token.NAME);
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_ReturnsFalse() {
    Node n = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    Node child = new Node(Token.NAME);
    n.addChildToBack(child);
    
    assertTrue(NodeUtil.isStatement(child));
  }
  
  @Test
  public void testIsStatement_ReturnsFalse() {
    Node n = new Node(Token.NAME);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_ReturnsFalse() {
    Node n = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_ReturnsFalse() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}