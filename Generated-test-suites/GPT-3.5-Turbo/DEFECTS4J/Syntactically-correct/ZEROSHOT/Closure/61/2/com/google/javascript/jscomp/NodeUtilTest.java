import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
    
    Node n2 = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n2));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(n));
    
    Node n2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    parent.addChildToFront(n2);
    assertFalse(NodeUtil.isStatement(n2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    Node n2 = new Node(Token.VAR, new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    parent.addChildToFront(n2);
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    parent.addChildToFront(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    
    Node n2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent2 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    parent2.addChildToFront(n2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
  
}