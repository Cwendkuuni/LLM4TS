import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_RegularCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.LP));
    node.addChildToBack(new Node(Token.RP));
    node.addChildToBack(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_MissingChild() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.LP));
    // Missing one child, should not be considered as FOR-IN loop
    node.addChildToBack(new Node(Token.RP));
    node.addChildToBack(new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_InScript() {
    Node node = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_InBlock() {
    Node node = new Node(Token.VAR);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_InLabel() {
    Node node = new Node(Token.BREAK);
    Node labelNode = new Node(Token.LABEL);
    labelNode.addChildToBack(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_NotInStatement() {
    Node node = new Node(Token.IF);
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.FOR);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToBack(node);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(grandparent);
    grandparent.addChildToBack(node);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}