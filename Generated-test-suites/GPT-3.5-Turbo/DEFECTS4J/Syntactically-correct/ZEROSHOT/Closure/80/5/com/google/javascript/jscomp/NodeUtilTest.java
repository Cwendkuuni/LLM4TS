import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_ReturnsFalse() {
    Node node = new Node(Token.IF);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_IsScript_ReturnsTrue() {
    Node node = new Node(Token.SCRIPT);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_IsBlock_ReturnsTrue() {
    Node node = new Node(Token.BLOCK);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_IsLabel_ReturnsTrue() {
    Node node = new Node(Token.LABEL);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_ReturnsFalse() {
    Node node = new Node(Token.IF);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.IF);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_IsScript_ReturnsTrue() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    scriptNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_IsFunction_ReturnsTrue() {
    Node functionNode1 = new Node(Token.FUNCTION);
    Node functionNode2 = new Node(Token.FUNCTION);
    functionNode1.addChildToBack(new Node(Token.NAME));
    functionNode2.addChildToBack(functionNode1);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.IF);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}