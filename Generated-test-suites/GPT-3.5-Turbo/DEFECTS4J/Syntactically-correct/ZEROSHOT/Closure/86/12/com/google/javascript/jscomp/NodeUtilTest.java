import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR);
    n1.addChildToFront(new Node(Token.NAME));
    n1.addChildToFront(new Node(Token.OBJECTLIT));
    n1.addChildToFront(new Node(Token.BLOCK));
    
    Node n2 = new Node(Token.IF);
    n2.addChildToFront(new Node(Token.NAME));
    n2.addChildToFront(new Node(Token.STRING));
    n2.addChildToFront(new Node(Token.TRUE));
    
    assertTrue(NodeUtil.isForIn(n1));
    assertFalse(NodeUtil.isForIn(n2));
  }
  
  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.SCRIPT);
    n1.addChildToFront(new Node(Token.NAME));
    n1.addChildToFront(new Node(Token.STRING));
    
    Node n2 = new Node(Token.FUNCTION);
    n2.addChildToFront(new Node(Token.NAME));
    n2.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isStatement(n1));
    assertFalse(NodeUtil.isStatement(n2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToFront(new Node(Token.NAME));
    n1.addChildToFront(new Node(Token.BLOCK));
    
    Node n2 = new Node(Token.IF);
    n2.addChildToFront(new Node(Token.NAME));
    n2.addChildToFront(new Node(Token.STRING));
    
    assertTrue(NodeUtil.isFunctionDeclaration(n1));
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToFront(new Node(Token.NAME));
    n1.addChildToFront(new Node(Token.BLOCK));
    n1.getParent().setType(Token.SCRIPT);
    
    Node n2 = new Node(Token.IF);
    n2.addChildToFront(new Node(Token.NAME));
    n2.addChildToFront(new Node(Token.STRING));
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
  
}