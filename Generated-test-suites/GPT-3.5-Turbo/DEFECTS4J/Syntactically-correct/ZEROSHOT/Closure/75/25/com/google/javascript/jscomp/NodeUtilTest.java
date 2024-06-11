import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, null);
    n.addChildToBack(new Node(Token.BLOCK, null));
    n.addChildToBack(new Node(Token.BLOCK, null));
    n.addChildToBack(new Node(Token.BLOCK, null));
    
    assertFalse(NodeUtil.isForIn(n));
    
    n.setType(Token.FOR);
    n.setChildCount(3);
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.STRING, null);
    Node parent = new Node(Token.SCRIPT, null);
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));
    
    parent.setType(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(n));
    
    parent.setType(Token.LABEL);
    assertTrue(NodeUtil.isStatement(n));
    
    parent.setType(Token.IF);
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, null);
    n.setType(Token.SCRIPT);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    n.setType(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, null);
    n.setType(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT, null);
    parent.addChildToBack(new Node(Token.FUNCTION, null));
    parent.addChildToBack(n);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    
    parent.getParent().setType(Token.FUNCTION);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}