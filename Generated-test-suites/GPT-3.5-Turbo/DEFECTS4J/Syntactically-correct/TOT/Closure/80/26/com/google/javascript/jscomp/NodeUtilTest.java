import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.EMPTY).srcref(0));
    node.addChildToFront(new Node(Token.ITER).srcref(0));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsForIn_emptyNode() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.VAR));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsStatement_typicalCase() {
    Node node = new Node(Token.MATCH);
    node.addChildToFront(new Node(Token.SCRIPT));
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsStatement_nonStatementNode() {
    Node node = new Node(Token.NAME);
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_nonFunctionNode() {
    Node node = new Node(Token.STRING);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.addChildToFront(new Node(Token.NAME));
    scriptNode.addChildToFront(parentFunctionNode);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME));
    parentFunctionNode.addChildToFront(functionNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_nonHoistedFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME));
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_nonFunctionNode() {
    Node node = new Node(Token.SCRIPT);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }
}