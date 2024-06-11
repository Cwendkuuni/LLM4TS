import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_empty() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_edge() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_script() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_block() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_label() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.LABEL);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_nullParent() {
    Node node = new Node(Token.NAME);
    
    assertEquals(false, NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_nullParent() {
    Node node = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    grandParent.addChildToFront(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_nullParent() {
    Node node = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}