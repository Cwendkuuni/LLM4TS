import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
    
    node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(!NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.LABEL));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.FUNCTION));
    assertTrue(!NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.LABEL));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.FUNCTION));
    assertTrue(!NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.BLOCK));
    node.getParent().setParent(new Node(Token.FUNCTION));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.LABEL));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.FUNCTION));
    assertTrue(!NodeUtil.isHoistedFunctionDeclaration(node));
  }
}