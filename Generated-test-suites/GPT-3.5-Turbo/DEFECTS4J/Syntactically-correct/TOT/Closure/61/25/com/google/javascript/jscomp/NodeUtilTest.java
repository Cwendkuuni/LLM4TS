import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalInput() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_emptyInput() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_inScriptParent() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_inBlockParent() {
    Node parentNode = new Node(Token.BLOCK);
    Node node = new Node(Token.NAME);
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_inLabelParent() {
    Node parentNode = new Node(Token.LABEL);
    Node node = new Node(Token.NAME);
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_notInAST() {
    Node node = new Node(Token.NAME);
    
    try {
      NodeUtil.isStatement(node);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }
  
  @Test
  public void testIsStatement_otherParent() {
    Node parentNode = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.NAME);
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalFunction() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_notInStatement() {
    Node parentNode = new Node(Token.BLOCK);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_inScriptParent() {
    Node grandparentNode = new Node(Token.SCRIPT);
    Node parentNode = new Node(Token.FUNCTION);
    parentNode.setParent(grandparentNode);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_inFunctionParent() {
    Node grandgrandparentNode = new Node(Token.FUNCTION);
    Node grandparentNode = new Node(Token.SCRIPT);
    grandparentNode.setParent(grandgrandparentNode);
    Node parentNode = new Node(Token.FUNCTION);
    parentNode.setParent(grandparentNode);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notInScriptOrFunctionParent() {
    Node parentNode = new Node(Token.BLOCK);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}