import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_WhenNodeIsForIn_ReturnTrue() {
    Node node = createForInNode();
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnFalse() {
    Node node = createNonForInNode();
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsStatement_ReturnTrue() {
    Node node = createStatementNode();
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsNotStatement_ReturnFalse() {
    Node node = createNonStatementNode();
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnTrue() {
    Node node = createFunctionDeclarationNode();
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnFalse() {
    Node node = createNonFunctionDeclarationNode();
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnTrue() {
    Node node = createHoistedFunctionDeclarationNode();
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnFalse() {
    Node node = createNonHoistedFunctionDeclarationNode();
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }
  
  // Helper methods for test cases
  
  private Node createForInNode() {
    Node forNode = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    Node child3 = new Node(Token.NAME);
    forNode.addChildToBack(child1);
    forNode.addChildToBack(child2);
    forNode.addChildToBack(child3);
    return forNode;
  }
  
  private Node createNonForInNode() {
    Node node = new Node(Token.WHILE);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    return node;
  }
  
  private Node createStatementNode() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node statementNode = new Node(Token.EXPR_RESULT);
    blockNode.addChildToBack(statementNode);
    scriptNode.addChildToBack(blockNode);
    return statementNode;
  }
  
  private Node createNonStatementNode() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node exprNode = new Node(Token.NAME);
    blockNode.addChildToBack(exprNode);
    scriptNode.addChildToBack(blockNode);
    return exprNode;
  }
  
  private Node createFunctionDeclarationNode() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node functionNode = new Node(Token.FUNCTION);
    blockNode.addChildToBack(functionNode);
    scriptNode.addChildToBack(blockNode);
    return functionNode;
  }
  
  private Node createNonFunctionDeclarationNode() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node exprNode = new Node(Token.EXPR_RESULT);
    blockNode.addChildToBack(exprNode);
    scriptNode.addChildToBack(blockNode);
    return exprNode;
  }
  
  private Node createHoistedFunctionDeclarationNode() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    return functionNode;
  }
  
  private Node createNonHoistedFunctionDeclarationNode() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node functionNode = new Node(Token.FUNCTION);
    blockNode.addChildToBack(functionNode);
    scriptNode.addChildToBack(blockNode);
    return functionNode;
  }

}