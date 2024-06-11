import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.STRING));
    n.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_edge() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement_typical() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_edge() {
    Node n = new Node(Token.NAME);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_typical() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_edge() {
    Node n = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent.setParent(grandparent);
    grandparent.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_edge() {
    Node n = new Node(Token.FUNCTION);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}