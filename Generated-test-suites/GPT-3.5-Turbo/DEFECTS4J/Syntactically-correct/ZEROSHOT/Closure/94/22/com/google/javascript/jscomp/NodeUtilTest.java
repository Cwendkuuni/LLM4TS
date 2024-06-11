import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_Positive() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.INIT));
    n.addChildToBack(new Node(Token.CONDITION));
    n.addChildToBack(new Node(Token.INCREMENT));
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_Negative() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.INIT));
    n.addChildToBack(new Node(Token.CONDITION));
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement_Positive() {
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_Negative() {
    Node n = new Node(Token.EXPR_RESULT);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_Positive() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_Negative() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_Positive() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_Negative() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}