import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(NodeUtil.isForIn(node));
    
    node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.STRING));
    assertFalse(NodeUtil.isForIn(node));
    
    node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.BLOCK);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.STRING);
    node.addChildToBack(new Node(Token.STRING));
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.SCRIPT);
    node.addChildToBack(new Node(Token.FUNCTION));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.BLOCK);
    node.addChildToBack(new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.STRING));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node grandParent = new Node(Token.FUNCTION);
    grandParent.addChildToBack(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent));
  }
  
}