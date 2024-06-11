import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.STRING));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_emptyNode() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_incorrectType() {
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.STRING));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_scriptNode() {
    Node node = new Node(Token.SCRIPT);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_blockNode() {
    Node node = new Node(Token.BLOCK);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_labelNode() {
    Node node = new Node(Token.LABEL);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_ifNode() {
    Node parentNode = new Node(Token.IF);
    Node node = new Node(Token.NAME);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_ifNoParentNode() {
    Node node = new Node(Token.IF);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_assignmentNode() {
    Node parentNode = new Node(Token.ASSIGN);
    Node node = new Node(Token.NAME);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_functionExpression() {
    Node parentNode = new Node(Token.NAME);
    Node node = new Node(Token.FUNCTION);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_assignmentNode() {
    Node parentNode = new Node(Token.ASSIGN);
    Node node = new Node(Token.FUNCTION);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node grandParentNode = new Node(Token.SCRIPT);
    Node parentNode = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    grandParentNode.addChildToBack(parentNode);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    parentNode.setParent(grandParentNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_outsideScope() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_insideFunction() {
    Node grandParentNode = new Node(Token.SCRIPT);
    Node parentNode = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    grandParentNode.addChildToBack(parentNode);
    parentNode.addChildToBack(node);
    
    node.setParent(parentNode);
    parentNode.setParent(grandParentNode);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentNode));
  }

}