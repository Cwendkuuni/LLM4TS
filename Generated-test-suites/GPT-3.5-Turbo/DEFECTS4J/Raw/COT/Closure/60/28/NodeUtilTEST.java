
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
  // Test for isForIn method
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertTrue(result);
  }
  
  // Test for isStatement method
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertTrue(result);
  }
  
  // Test for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }
  
  // Test for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToFront(functionNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
    
    Assert.assertTrue(result);
  }
  
  // Additional test for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToFront(parentFunctionNode);
    Node childFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.addChildToFront(childFunctionNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(childFunctionNode);
    
    Assert.assertTrue(result);
  }
}
